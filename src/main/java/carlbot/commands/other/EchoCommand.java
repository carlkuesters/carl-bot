package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class EchoCommand extends Command<GuildMessageReceivedEvent> {

    public EchoCommand(Bot bot) {
        super(bot);
    }
    private String commandPrefix = "!echo ";
    private String output;

    @Override
    public boolean isMatching(GuildMessageReceivedEvent event, String content) {
        return content.startsWith(commandPrefix);
    }

    @Override
    public void parse(GuildMessageReceivedEvent event, String content) {
        output = content.substring(commandPrefix.length());
    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(output).queue();
    }
}
