package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;

public class CommandExecuteCommand extends Command<MessageReceivedEvent> {

    public CommandExecuteCommand(Bot bot) {
        super(bot);
    }
    private String commandPrefix = "!";
    private String commandContent;

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        return content.startsWith(commandPrefix);
    }

    @Override
    public void parse(MessageReceivedEvent event, String content) {
        long guildId = event.getGuild().getIdLong();
        String commandName = content.substring(commandPrefix.length()).trim();
        try {
            commandContent = bot.getDatabase().getQueryResult("SELECT content FROM commands WHERE guild_id = " + guildId + " AND name = '" + bot.getDatabase().escape(commandName) + "' LIMIT 1").nextString_Close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        if (commandContent != null) {
            bot.handleEvent(event, commandContent);
        }
    }
}
