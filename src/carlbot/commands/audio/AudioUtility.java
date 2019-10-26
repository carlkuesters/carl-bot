package carlbot.commands.audio;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.internal.entities.VoiceChannelImpl;

import java.util.List;

public class AudioUtility {

    public static VoiceChannel getCurrentOrBestVoiceChannel(Guild guild) {
        AudioManager audioManager = guild.getAudioManager();
        VoiceChannel connectedChannel = audioManager.getConnectedChannel();
        if (connectedChannel != null) {
            return connectedChannel;
        }
        return getPopulatedVoiceChannelWithMostMembers(guild);
    }

    private static VoiceChannel getPopulatedVoiceChannelWithMostMembers(Guild guild) {
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
}
