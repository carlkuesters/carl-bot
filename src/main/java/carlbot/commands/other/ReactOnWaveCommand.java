package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import carlbot.Emojis;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ReactOnWaveCommand extends Command<MessageReceivedEvent> {

    public ReactOnWaveCommand(Bot bot) {
        super(bot);
    }

    private Message receivedMessage;

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        if (Emojis.WAVE.equals(content)) {
            receivedMessage = event.getMessage();
            return true;
        }
        return false;
    }

    @Override
    public void parse(MessageReceivedEvent event, String content) {

    }

    @Override
    public void execute(MessageReceivedEvent event) {
        receivedMessage.addReaction(Emoji.fromUnicode(Emojis.WAVE)).submit();
    }
}
