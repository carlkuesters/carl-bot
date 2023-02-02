package carlbot.commands.other;

import carlbot.Bot;
import carlbot.commands.audio.AudioUtility;
import carlbot.commands.audio.MessageAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlayCircusOnFirstBloodCommand extends MessageAudioCommand {

    public PlayCircusOnFirstBloodCommand(Bot bot) {
        super(bot, null);
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadFile("./data/other/circus.wav");
    }

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        return event.getAuthor().getName().equals("First Blood");
    }

    @Override
    protected AudioChannel getAudioChannelToJoin(MessageReceivedEvent event) {
        return AudioUtility.getCurrentOrBestAudioChannel(event.getGuild());
    }

    @Override
    protected void play(MessageReceivedEvent event, AudioPlayer audioPlayer) {
        AudioTrack audioTrack = audioLibrary.getTrack("circus");
        audioPlayer.playTrack(audioTrack.makeClone());
    }
}
