package carlbot.commands.audio;

import carlbot.Bot;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class GuildMessageAudioCommand extends AudioCommand<GuildMessageReceivedEvent> {

    protected GuildMessageAudioCommand(Bot bot, String commandPrefix) {
        super(bot, commandPrefix);
    }
    private VoiceChannel bestVoiceChannel;

    @Override
    public boolean isMatching(GuildMessageReceivedEvent event, String content) {
        if (content.startsWith(commandPrefix)) {
            bestVoiceChannel = findBestVoiceChannel(event);
            if (bestVoiceChannel != null) {
                return true;
            } else {
                event.getChannel().sendMessage("Bringt ja nichts, wenn niemand in einem Voice Channel ist...").queue();
            }
        }
        return false;
    }

    private VoiceChannel findBestVoiceChannel(GuildMessageReceivedEvent event) {
        VoiceChannel userVoiceChannel = event.getMember().getVoiceState().getChannel();
        return ((userVoiceChannel != null) ? userVoiceChannel : AudioUtility.getCurrentOrBestVoiceChannel(event.getGuild()));
    }

    @Override
    protected VoiceChannel getVoiceChannelToJoin(GuildMessageReceivedEvent event) {
        return bestVoiceChannel;
    }

    protected abstract void play(GuildMessageReceivedEvent event, AudioPlayer audioPlayer);
}
