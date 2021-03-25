package carlbot.commands.other;

import carlbot.Bot;
import carlbot.FileManager;
import carlbot.commands.audio.GuildMessageAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.TrackMarker;
import com.sedmelluq.discord.lavaplayer.track.TrackMarkerHandler;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.IPlaylistItem;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayCommand extends GuildMessageAudioCommand {

    public PlayCommand(Bot bot) {
        super(bot, "!play");
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
        audioPlayerManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
        audioPlayerManager.registerSourceManager(new BandcampAudioSourceManager());
        audioPlayerManager.registerSourceManager(new VimeoAudioSourceManager());
        audioPlayerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        audioPlayerManager.registerSourceManager(new HttpAudioSourceManager());

        String[] spotifySecrets = FileManager.getFileLines("./spotify.ini");
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(spotifySecrets[0])
                .setClientSecret(spotifySecrets[1])
                .build();
    }
    private Pattern TIME_RANGE_PATTERN = Pattern.compile("^(.+) #((\\d+):)?(\\d+)(-((\\d+):)?(\\d+))?$");
    private SpotifyApi spotifyApi;
    private String audioIdentifier;
    private Long startPosition;
    private Long endPosition;

    @Override
    public void parse(GuildMessageReceivedEvent event, String content) {
        audioIdentifier = content.substring(commandPrefix.length()).trim();
        parseTimeRange();
        if (!isURL(audioIdentifier)) {
            String spotifyTrackIdentifier = getRandomSpotifyPlaylistTrack(audioIdentifier);
            if (spotifyTrackIdentifier != null) {
                audioIdentifier = spotifyTrackIdentifier;
            }
            audioIdentifier = "ytsearch:" + audioIdentifier;
        }
    }

    private String getRandomSpotifyPlaylistTrack(String audioIdentifier) {
        try {
            ClientCredentials clientCredentials = spotifyApi.clientCredentials().build().execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            Playlist playlist = spotifyApi.getPlaylist(audioIdentifier).build().execute();
            PlaylistTrack[] playlistItems = playlist.getTracks().getItems();
            IPlaylistItem playlistItem = playlistItems[(int) (Math.random() * playlistItems.length)].getTrack();
            Track track = spotifyApi.getTrack(playlistItem.getId()).build().execute();
            String trackIdentifier = "";
            for (ArtistSimplified artist : track.getArtists()) {
                trackIdentifier += artist.getName() + " ";
            }
            trackIdentifier += track.getName();
            return trackIdentifier;
        } catch (IOException | SpotifyWebApiException | ParseException ex) {
            return null;
        }
    }

    private void parseTimeRange() {
        startPosition = null;
        endPosition = null;
        Matcher timeRangeMatcher = TIME_RANGE_PATTERN.matcher(audioIdentifier);
        if (timeRangeMatcher.matches()) {
            audioIdentifier = timeRangeMatcher.group(1);
            int startMinutes = 0;
            int startSeconds = 0;
            int endMinutes = 0;
            int endSeconds = 0;
            String startMinutesGroup = timeRangeMatcher.group(3);
            if (startMinutesGroup != null) {
                startMinutes = Integer.parseInt(startMinutesGroup);
            }
            String startSecondsGroup = timeRangeMatcher.group(4);
            if (startSecondsGroup != null) {
                startSeconds = Integer.parseInt(startSecondsGroup);
            }
            String endMinutesGroup = timeRangeMatcher.group(7);
            if (endMinutesGroup != null) {
                endMinutes = Integer.parseInt(endMinutesGroup);
            }
            String endSecondsGroup = timeRangeMatcher.group(8);
            if (endSecondsGroup != null) {
                endSeconds = Integer.parseInt(endSecondsGroup);
            }
            startPosition = (((startMinutes * 60L) + (startSeconds)) * 1000);
            if ((endMinutes != 0) || (endSeconds != 0)) {
                endPosition = (((endMinutes * 60L) + (endSeconds)) * 1000);
            }
        }
    }

    private boolean isURL(String text) {
        try {
            URL url = new URL(text);
            url.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException ex) {
            return false;
        }
    }

    @Override
    protected void play(GuildMessageReceivedEvent event, AudioPlayer audioPlayer) {
        audioPlayer.setVolume(12);
        audioLibrary.loadTrack(audioIdentifier, audioTrack -> {
            if (startPosition != null) {
                audioTrack.setPosition(startPosition);
            }
            if (endPosition != null) {
                audioTrack.setMarker(new TrackMarker(endPosition, markerState -> {
                    if (markerState == TrackMarkerHandler.MarkerState.REACHED) {
                        audioPlayer.stopTrack();
                    }
                }));
            }
            audioPlayer.playTrack(audioTrack);
        });
    }
}
