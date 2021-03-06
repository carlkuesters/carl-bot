package carlbot.commands.other;

import carlbot.Bot;
import carlbot.commands.audio.GuildVoiceAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;

public class PlayMaxiHiOnJoinCommand extends GuildVoiceAudioCommand {

    public PlayMaxiHiOnJoinCommand(Bot bot) {
        super(bot, null);
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadFile("./data/other/maxi_hi.wav");
    }

    @Override
    public boolean isMatching(GenericGuildVoiceEvent event, String content) {
        if (!bot.isPlayingAudioInGuild(event.getGuild())) {
            if ((event instanceof GuildVoiceJoinEvent) || (event instanceof GuildVoiceMoveEvent)) {
                Member botGuildMember = event.getGuild().getMember(event.getJDA().getSelfUser());
                VoiceChannel botChannel = botGuildMember.getVoiceState().getChannel();
                if (event.getVoiceState().getChannel() == botChannel) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void play(GenericGuildVoiceEvent event, AudioPlayer audioPlayer) {
        AudioTrack audioTrack = audioLibrary.getTrack("maxi_hi");
        audioPlayer.playTrack(audioTrack.makeClone());
    }
}
