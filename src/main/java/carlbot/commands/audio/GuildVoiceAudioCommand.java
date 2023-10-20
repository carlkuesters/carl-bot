package carlbot.commands.audio;

import carlbot.Bot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;

public abstract class GuildVoiceAudioCommand extends AudioCommand<GenericGuildVoiceEvent> {

    protected GuildVoiceAudioCommand(Bot bot, String commandPrefix) {
        super(bot, commandPrefix);
    }

    @Override
    protected Guild getGuild(GenericGuildVoiceEvent event) {
        return event.getGuild();
    }

    @Override
    protected AudioChannel getAudioChannelToJoin(GenericGuildVoiceEvent event) {
        return event.getVoiceState().getChannel();
    }
}
