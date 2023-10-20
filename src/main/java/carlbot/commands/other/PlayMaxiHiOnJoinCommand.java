package carlbot.commands.other;

import carlbot.Bot;
import carlbot.commands.audio.GuildVoiceAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

public class PlayMaxiHiOnJoinCommand extends GuildVoiceAudioCommand {

    public PlayMaxiHiOnJoinCommand(Bot bot) {
        super(bot, null);
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadFile("./data/other/maxi_hi.wav");
    }

    @Override
    public boolean isMatching(GenericGuildVoiceEvent event, String content) {
        if (!bot.isPlayingAudioInGuild(event.getGuild())) {
            if (event instanceof GuildVoiceUpdateEvent guildVoiceUpdateEvent) {
                return guildVoiceUpdateEvent.getChannelJoined() == getBotAudioChannel(event);
            }
        }
        return false;
    }

    private AudioChannelUnion getBotAudioChannel(GenericGuildVoiceEvent event) {
        Member botGuildMember = event.getGuild().getMember(event.getJDA().getSelfUser());
        return botGuildMember.getVoiceState().getChannel();
    }

    @Override
    protected void play(GenericGuildVoiceEvent event, AudioPlayer audioPlayer) {
        AudioTrack audioTrack = audioLibrary.getTrack("maxi_hi");
        audioPlayer.playTrack(audioTrack.makeClone());
    }
}
