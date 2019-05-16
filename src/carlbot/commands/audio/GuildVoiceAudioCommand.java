package carlbot.commands.audio;

import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;

public abstract class GuildVoiceAudioCommand extends AudioCommand<GenericGuildVoiceEvent> {

    protected GuildVoiceAudioCommand(String commandPrefix) {
        super(commandPrefix);
    }

    @Override
    protected VoiceChannel getVoiceChannelToJoin(GenericGuildVoiceEvent event) {
        return event.getVoiceState().getChannel();
    }
}
