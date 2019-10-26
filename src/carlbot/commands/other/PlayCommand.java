package carlbot.commands.other;

import carlbot.Bot;
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
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayCommand extends GuildMessageAudioCommand {

    public PlayCommand(Bot bot) {
        super(bot, "!play");
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
        audioPlayerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        audioPlayerManager.registerSourceManager(new BandcampAudioSourceManager());
        audioPlayerManager.registerSourceManager(new VimeoAudioSourceManager());
        audioPlayerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        audioPlayerManager.registerSourceManager(new HttpAudioSourceManager());
    }
    private Pattern TIME_RANGE_PATTERN = Pattern.compile("^(.+) #((\\d+):)?(\\d+)(-((\\d+):)?(\\d+))?$");
    private String audioIdentifier;
    private Long startPosition;
    private Long endPosition;

    @Override
    public void parse(GuildMessageReceivedEvent event) {
        audioIdentifier = event.getMessage().getContentRaw().substring(commandPrefix.length()).trim();
        parseTimeRange();
        if (!isURL(audioIdentifier)) {
            audioIdentifier = "ytsearch:" + audioIdentifier;
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
