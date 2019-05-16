package carlbot.commands.other;

import carlbot.commands.audio.AudioUtility;
import carlbot.commands.audio.GuildMessageAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class MaxiWritesHiCommand extends GuildMessageAudioCommand {

    public MaxiWritesHiCommand() {
        super(null);
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadFile("./data/other/maxi_hi.wav");
    }

    @Override
    public boolean isMatching(GuildMessageReceivedEvent event) {
        if (event.getAuthor().getName().equals("GameUser21")) {
            String messageLowerCase = event.getMessage().getContentRaw().toLowerCase();
            if (messageLowerCase.equals("hi") || (messageLowerCase.startsWith("hi") && messageLowerCase.contains("^^"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected VoiceChannel getVoiceChannelToJoin(GuildMessageReceivedEvent event) {
        return AudioUtility.getCurrentOrBestVoiceChannel(event.getGuild());
    }

    @Override
    protected void play(GuildMessageReceivedEvent event, AudioPlayer audioPlayer) {
        AudioTrack audioTrack = audioLibrary.getTrack("maxi_hi");
        audioPlayer.playTrack(audioTrack.makeClone());
    }
}
