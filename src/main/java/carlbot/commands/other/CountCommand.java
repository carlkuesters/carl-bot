package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import carlbot.Emojis;
import carlbot.database.Database;
import carlbot.database.QueryResult;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.SQLException;

public class CountCommand extends Command<GuildMessageReceivedEvent> {

    public CountCommand(Bot bot) {
        super(bot);
    }
    private String commandPrefix = "!count ";
    private String name;
    private int amount;

    @Override
    public boolean isMatching(GuildMessageReceivedEvent event, String content) {
        return content.startsWith(commandPrefix);
    }

    @Override
    public void parse(GuildMessageReceivedEvent event, String content) {
        String[] commandParts = content.substring(commandPrefix.length()).split(" ");
        name = commandParts[0];
        if (commandParts.length > 1) {
            amount = ("?".equals(commandParts[1]) ? 0 : Integer.parseInt(commandParts[1]));
        } else {
            amount = 1;
        }
    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        Database database = bot.getDatabase();
        long date = System.currentTimeMillis();

        String message;
        try {
            QueryResult queryResult = database.getQueryResult("SELECT id, value, date FROM counters WHERE name = '" + database.escape(name) + "' LIMIT 1");

            int oldValue = 0;
            if (queryResult != null) {
                oldValue = queryResult.getInteger("value");
            }
            int newValue = oldValue + amount;
            message = name + " counter = " + newValue + " " + Emojis.YEP;

            if (amount != 0) {
                for (int i = 10; i <= 1000000; i *= 10) {
                    if ((newValue % i) == 0) {
                        message += " JUBILÄUM! " + Emojis.POG;
                    }
                }

                if (queryResult != null) {
                    database.executeQuery("UPDATE counters SET " +
                            "value = " + newValue + ", " +
                            "date = " + date + " " +
                            "WHERE id = " + queryResult.getInteger("id") + " " +
                            "LIMIT 1");
                } else {
                    database.executeQuery("INSERT INTO counters (name, value, date) VALUES (" +
                            "'" + database.escape(name) + "', " +
                            newValue + ", " +
                            date + ")");
                }
            }
        } catch (SQLException ex) {
            message = "Da ging was mit der Datenbank schief...";
        }

        event.getChannel().sendMessage(message).queue();
    }
}