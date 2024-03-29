package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import carlbot.Emojis;
import carlbot.database.Database;
import carlbot.database.QueryResult;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CountCommand extends Command<MessageReceivedEvent> {

    public CountCommand(Bot bot) {
        super(bot);
    }
    private String commandPrefix = "!count ";
    private SimpleDateFormat lastDateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
    private String name;
    private int amount;

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        return content.startsWith(commandPrefix);
    }

    @Override
    public void parse(MessageReceivedEvent event, String content) {
        String[] commandParts = content.substring(commandPrefix.length()).split(" ");
        name = commandParts[0];
        if (commandParts.length > 1) {
            amount = ("?".equals(commandParts[1]) ? 0 : Integer.parseInt(commandParts[1]));
        } else {
            amount = 1;
        }
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        Database database = bot.getDatabase();
        long currentDate = System.currentTimeMillis();

        String message;
        try {
            QueryResult queryResult = database.getQueryResult("SELECT id, value, date FROM counters WHERE name = '" + database.escape(name) + "' LIMIT 1");

            int oldValue = 0;
            boolean counterExists = queryResult.next();
            if (counterExists) {
                oldValue = queryResult.getInteger("value");
            }
            int newValue = Math.max(oldValue + amount, 0);
            message = name + " Counter = " + newValue + " " + Emojis.YEP;

            if (amount != 0) {
                if (newValue > 0) {
                    for (int i = 1000000; i > 1; i /= 10) {
                        if ((newValue % i) == 0) {
                            message += " " + i + "-ER JUBILÄUM! " + Emojis.POG;
                            break;
                        }
                    }
                }

                if (counterExists) {
                    database.executeQuery("UPDATE counters SET " +
                            "value = " + newValue + ", " +
                            "date = " + currentDate + " " +
                            "WHERE id = " + queryResult.getInteger("id") + " " +
                            "LIMIT 1");
                } else {
                    database.executeQuery("INSERT INTO counters (name, value, date) VALUES (" +
                            "'" + database.escape(name) + "', " +
                            newValue + ", " +
                            currentDate + ")");
                }
            } else {
                Date lastDate = new Date(queryResult.getLong("date"));
                message += " (Letztes Mal am " + lastDateFormat.format(lastDate) + ")";
            }
        } catch (SQLException ex) {
            message = "Da ging was mit der Datenbank schief...";
        }

        event.getChannel().sendMessage(message).queue();
    }
}
