package carlbot.commands.other;

import carlbot.Bot;
import carlbot.commands.audio.AudioUtility;
import carlbot.commands.audio.MessageAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MaxiWritesHiCommand extends MessageAudioCommand {

    public MaxiWritesHiCommand(Bot bot) {
        super(bot, null);
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadFile("./data/other/maxi_hi.wav");
    }

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        if (event.getAuthor().getName().equals("GameUser21")) {
            String contentLowerCase = content.toLowerCase();
            if (contentLowerCase.equals("hi") || (contentLowerCase.startsWith("hi") && contentLowerCase.contains("^^"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected AudioChannel getAudioChannelToJoin(MessageReceivedEvent event) {
        return AudioUtility.getCurrentOrBestAudioChannel(event.getGuild());
    }

    @Override
    protected void play(MessageReceivedEvent event, AudioPlayer audioPlayer) {
        AudioTrack audioTrack = audioLibrary.getTrack("maxi_hi");
        audioPlayer.playTrack(audioTrack.makeClone());
    }
}
