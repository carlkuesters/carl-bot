package carlbot.commands.audio;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class AudioUtility {

    public static AudioChannel getCurrentOrBestAudioChannel(Guild guild) {
        AudioManager audioManager = guild.getAudioManager();
        AudioChannelUnion connectedChannel = audioManager.getConnectedChannel();
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
            int members = voiceChannel.getMembers().size();
            if (members > maximumMembers) {
                maximumMembersVoiceChannel = voiceChannel;
                maximumMembers = members;
            }
        }
        return maximumMembersVoiceChannel;
    }
}
