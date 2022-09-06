package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import carlbot.Emojis;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Optional;

public class HeyCarlCommand extends Command<MessageReceivedEvent> {

    public HeyCarlCommand(Bot bot) {
        super(bot);
    }
    private static final int PAUSE_DURATION = 60000;

    private long lastResponseTimestamp;
    private String receivedEmojiAsMention;
    private String responseMessage;

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        if ((System.currentTimeMillis() - lastResponseTimestamp) > PAUSE_DURATION) {
            Optional<CustomEmoji> heyCarlEmoji = event.getMessage().getMentions().getCustomEmojis().stream()
                    .filter(customEmoji -> Emojis.HEY_CARL_W.equals(customEmoji.getAsMention()) || Emojis.HEY_CARL_E.equals(customEmoji.getAsMention()))
                    .findFirst();
            if (heyCarlEmoji.isPresent()) {
                receivedEmojiAsMention = heyCarlEmoji.get().getAsMention();
                return true;
            }
        }
        return false;
    }

    @Override
    public void parse(MessageReceivedEvent event, String content) {
        responseMessage = (Emojis.HEY_CARL_W.equals(receivedEmojiAsMention) ? Emojis.HEY_CARL_E : Emojis.HEY_CARL_W);
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage(responseMessage).queue();
        lastResponseTimestamp = System.currentTimeMillis();
    }
}
