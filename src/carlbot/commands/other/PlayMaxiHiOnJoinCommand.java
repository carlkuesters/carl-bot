package carlbot.commands.other;

import carlbot.commands.audio.GuildVoiceAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;

public class PlayMaxiHiOnJoinCommand extends GuildVoiceAudioCommand {

    public PlayMaxiHiOnJoinCommand() {
        super(null);
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadFile("./data/other/maxi_hi.wav");
    }

    @Override
    public boolean isMatching(GenericGuildVoiceEvent event) {
        if ((event instanceof GuildVoiceJoinEvent) || (event instanceof GuildVoiceMoveEvent)) {
            Member botGuildMember = event.getGuild().getMember(event.getJDA().getSelfUser());
            VoiceChannel botChannel = botGuildMember.getVoiceState().getChannel();
            if (event.getVoiceState().getChannel() == botChannel) {
                return true;
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
