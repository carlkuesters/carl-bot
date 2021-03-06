package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ReactOnWaveCommand extends Command<GuildMessageReceivedEvent> {

    public ReactOnWaveCommand(Bot bot) {
        super(bot);
    }
    private static final String EMOTE_WAVE_RAW = "\uD83D\uDC4B";

    private Message receivedMessage;

    @Override
    public boolean isMatching(GuildMessageReceivedEvent event, String content) {
        if (EMOTE_WAVE_RAW.equals(content)) {
            receivedMessage = event.getMessage();
            return true;
        }
        return false;
    }

    @Override
    public void parse(GuildMessageReceivedEvent event, String content) {

    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        receivedMessage.addReaction(EMOTE_WAVE_RAW).submit();
    }
}
