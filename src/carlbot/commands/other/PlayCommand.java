package carlbot.commands.other;

import carlbot.commands.audio.AudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class PlayCommand extends AudioCommand {

    public PlayCommand() {
        super("!play");
        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager(true));
        audioPlayerManager.registerSourceManager(new SoundCloudAudioSourceManager());
        audioPlayerManager.registerSourceManager(new BandcampAudioSourceManager());
        audioPlayerManager.registerSourceManager(new VimeoAudioSourceManager());
        audioPlayerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        audioPlayerManager.registerSourceManager(new HttpAudioSourceManager());
    }
    private String audioIdentifier;

    @Override
    public void parse(GuildMessageReceivedEvent event) {
        audioIdentifier = event.getMessage().getContentRaw().substring(commandPrefix.length()).trim();
        if (!isURL(audioIdentifier)) {
            audioIdentifier = "ytsearch:" + audioIdentifier;
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
        audioLibrary.loadTrack(audioIdentifier, audioPlayer::playTrack);
    }
}
