package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Optional;

public class HeyCarlCommand extends Command<GuildMessageReceivedEvent> {

    public HeyCarlCommand(Bot bot) {
        super(bot);
    }
    private static final int PAUSE_DURATION = 60000;
    private static final String EMOTE_NAME_HEY_CARL_W = "heycarlW";
    private static final String EMOTE_NAME_HEY_CARL_E = "heycarlE";
    private static final String EMOTE_MENTION_HEY_CARL_W = "<:heycarlW:421005325118734337>";
    private static final String EMOTE_MENTION_HEY_CARL_E = "<:heycarlE:422459785871360000>";

    private long lastResponseTimestamp;
    private String receivedEmoteName;
    private String responseMessage;

    @Override
    public boolean isMatching(GuildMessageReceivedEvent event) {
        if ((System.currentTimeMillis() - lastResponseTimestamp) > PAUSE_DURATION) {
            Optional<Emote> heyCarlEmote = event.getMessage().getEmotes().stream()
                    .filter(emote -> EMOTE_NAME_HEY_CARL_W.equals(emote.getName()) || EMOTE_NAME_HEY_CARL_E.equals(emote.getName()))
                    .findFirst();
            if (heyCarlEmote.isPresent()) {
                receivedEmoteName = heyCarlEmote.get().getName();
                return true;
            }
        }
        return false;
    }

    @Override
    public void parse(GuildMessageReceivedEvent event) {
        responseMessage = (EMOTE_NAME_HEY_CARL_W.equals(receivedEmoteName) ? EMOTE_MENTION_HEY_CARL_E : EMOTE_MENTION_HEY_CARL_W);
    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(responseMessage).queue();
        lastResponseTimestamp = System.currentTimeMillis();
    }
}
