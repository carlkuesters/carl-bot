package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import carlbot.Emojis;
import carlbot.database.Database;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;

public class CommandSaveCommand extends Command<MessageReceivedEvent> {

    public CommandSaveCommand(Bot bot) {
        super(bot);
    }
    private String commandPrefix = "!command ";
    private String commandName;
    private String commandContent;

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        return content.startsWith(commandPrefix);
    }

    @Override
    public void parse(MessageReceivedEvent event, String content) {
        String commandNameAndContent = content.substring(commandPrefix.length());
        int commandNameAndContentSeparatorIndex = commandNameAndContent.indexOf(" ");
        commandName = commandNameAndContent.substring(0, commandNameAndContentSeparatorIndex).trim();
        commandContent = commandNameAndContent.substring(commandNameAndContentSeparatorIndex + 1).trim();
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        String message;
        if (commandContent.startsWith(commandPrefix)) {
            message = Emojis.KAPPA;
        } else {
            long guildId = event.getGuild().getIdLong();
            String creator = event.getAuthor().getName();
            long date = System.currentTimeMillis();

            try {
                Database database = bot.getDatabase();
                Integer commandId = database.getQueryResult("SELECT id FROM commands WHERE guild_id = " + guildId + " AND name = '" + database.escape(commandName) + "' LIMIT 1").nextInteger_Close();
                if (commandId == null) {
                    database.executeQuery("INSERT INTO commands (guild_id, name, content, creator, date) VALUES (" + guildId + ", '" + database.escape(commandName) + "', '" + database.escape(commandContent) + "', '" + database.escape(creator) + "', " + date + ")");
                } else {
                    database.executeQuery("UPDATE commands SET content = '" + database.escape(commandContent) + "', creator = '" + database.escape(creator) + "', date = " + date + " WHERE id = " + commandId);
                }
                message = "Okay, ist gespeichert.";
            } catch (SQLException ex) {
                message = "Da ging was mit der Datenbank schief...";
            }
        }
        event.getChannel().sendMessage(message).queue();
    }
}
