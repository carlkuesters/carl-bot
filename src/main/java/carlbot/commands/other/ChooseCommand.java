package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChooseCommand extends Command<MessageReceivedEvent> {

    public ChooseCommand(Bot bot) {
        super(bot);
    }
    private String commandPrefix = "!choose ";
    private String[] options;

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        return content.startsWith(commandPrefix);
    }

    @Override
    public void parse(MessageReceivedEvent event, String content) {
        options = content.substring(commandPrefix.length()).split(",");
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        String randomOption = options[(int) (Math.random() * options.length)].trim();
        event.getChannel().sendMessage(randomOption).queue();
    }
}
