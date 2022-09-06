package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EchoCommand extends Command<MessageReceivedEvent> {

    public EchoCommand(Bot bot) {
        super(bot);
    }
    private String commandPrefix = "!echo ";
    private String output;

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        return content.startsWith(commandPrefix);
    }

    @Override
    public void parse(MessageReceivedEvent event, String content) {
        output = content.substring(commandPrefix.length());
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessage(output).queue();
    }
}
