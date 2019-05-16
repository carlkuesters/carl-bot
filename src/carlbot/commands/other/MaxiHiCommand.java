package carlbot.commands.other;

import carlbot.commands.audio.AudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.entities.impl.VoiceChannelImpl;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

import java.util.List;

public class MaxiHiCommand extends AudioCommand {

    public MaxiHiCommand() {
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
    public void parse(GuildMessageReceivedEvent event) {

    }

    @Override
    protected VoiceChannel getVoiceChannelToJoin(GuildMessageReceivedEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        VoiceChannel connectedChannel = audioManager.getConnectedChannel();
        if (connectedChannel != null) {
            return connectedChannel;
        }
        return getPopulatedVoiceChannelWithMostMembers(event.getGuild());
    }

    private VoiceChannel getPopulatedVoiceChannelWithMostMembers(Guild guild) {
        List<VoiceChannel> voiceChannels = guild.getVoiceChannels();
        VoiceChannel maximumMembersVoiceChannel = null;
        int maximumMembers = 0;
        for (VoiceChannel voiceChannel : voiceChannels) {
            VoiceChannelImpl voiceChannelImpl = (VoiceChannelImpl) voiceChannel;
            int members = voiceChannelImpl.getMembers().size();
            if (members > maximumMembers) {
                maximumMembersVoiceChannel = voiceChannel;
                maximumMembers = members;
            }
        }
        return maximumMembersVoiceChannel;
    }

    @Override
    protected void play(GuildMessageReceivedEvent event, AudioPlayer audioPlayer) {
        AudioTrack audioTrack = audioLibrary.getTrack("maxi_hi");
        audioPlayer.playTrack(audioTrack.makeClone());
    }
}
