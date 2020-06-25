package carlbot.commands.audio;

import carlbot.Bot;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;

public abstract class GuildVoiceAudioCommand extends AudioCommand<GenericGuildVoiceEvent> {

    protected GuildVoiceAudioCommand(Bot bot, String commandPrefix) {
        super(bot, commandPrefix);
    }

    @Override
    protected VoiceChannel getVoiceChannelToJoin(GenericGuildVoiceEvent event) {
        return event.getVoiceState().getChannel();
    }
}
