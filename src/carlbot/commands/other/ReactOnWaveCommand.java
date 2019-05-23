package carlbot.commands.other;

import carlbot.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class ReactOnWaveCommand extends Command<GuildMessageReceivedEvent> {

    private static final String EMOTE_WAVE_RAW = "\uD83D\uDC4B";

    private Message receivedMessage;

    @Override
    public boolean isMatching(GuildMessageReceivedEvent event) {
        if (EMOTE_WAVE_RAW.equals(event.getMessage().getContentRaw())) {
            receivedMessage = event.getMessage();
            return true;
        }
        return false;
    }

    @Override
    public void parse(GuildMessageReceivedEvent event) {

    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        receivedMessage.addReaction(EMOTE_WAVE_RAW).submit();
    }
}
