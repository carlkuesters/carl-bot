package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import carlbot.Emojis;
import carlbot.database.Database;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;

public class GambleCommand extends Command<GuildMessageReceivedEvent> {

    public GambleCommand(Bot bot) {
        super(bot);
    }
    private String commandPrefix = "!gamble ";
    private boolean showAmount;
    private boolean betAll;
    private BigInteger betAmount;

    @Override
    public boolean isMatching(GuildMessageReceivedEvent event, String content) {
        return content.startsWith(commandPrefix);
    }

    @Override
    public void parse(GuildMessageReceivedEvent event, String content) {
        String betAmountText = content.substring(commandPrefix.length());
        showAmount = "?".equals(betAmountText);
        betAll = "all".equals(betAmountText);
        betAmount = null;
        if ((!showAmount) && (!betAll)) {
            try {
                betAmount = new BigInteger(betAmountText);
            } catch (NumberFormatException ex) {
                // Will further down be accordingly responded to
            }
        }
    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        String user = event.getAuthor().getName();
        long date = System.currentTimeMillis();

        String message;
        if ((!betAll) && (!showAmount) && (betAmount == null)) {
            message = "?";
        } else if ((!betAll) && (!showAmount) && (betAmount.compareTo(BigInteger.ZERO) <= 0)) {
            message = Emojis.CARLTHINK;
        } else {
            try {
                Database database = bot.getDatabase();
                BigInteger oldAmount = BigInteger.ZERO;
                BigDecimal oldAmountDecimal = database.getQueryResult("SELECT amount FROM gambling WHERE user = '" + database.escape(user) + "' LIMIT 1").nextBigDecimal_Close();
                if (oldAmountDecimal != null) {
                    oldAmount = oldAmountDecimal.toBigInteger();
                }

                if (showAmount) {
                    message = user + ", du hast " + oldAmount.toString() + " Salzstreuer. " + Emojis.KARL;
                } else {
                    if (betAll) {
                        betAmount = oldAmount;
                    }

                    BigInteger newAmount = null;
                    if (oldAmount.equals(BigInteger.ZERO)) {
                        message = user + " hat gar keine Salzstreuer, daher schenke ich ihm 1.";
                        newAmount = BigInteger.ONE;
                    } else if (oldAmount.compareTo(betAmount) >= 0) {
                        boolean win = (Math.random() < 0.5);
                        if (win) {
                            newAmount = oldAmount.add(betAmount);
                        } else {
                            newAmount = oldAmount.subtract(betAmount);
                        }
                        message = user + " " + (betAll ? "geht mit " + betAmount.toString() + " Salzstreuern all-in" : "wettet " + betAmount.toString() + " Salzstreuer")
                                + ", " + (win ? "gewinnt" : "verliert") + " und besitzt jetzt " + newAmount.toString() + " Salzstreuer."
                                + " " + Emojis.KARL + " " + (win ? Emojis.FEELSGOODMAN : Emojis.FEELSBADMANC);
                    } else {
                        message = user + ", du hast nur " + oldAmount.toString() + " Salzstreuer...";
                    }

                    if (newAmount != null) {
                        if (oldAmountDecimal == null) {
                            database.executeQuery("INSERT INTO gambling (user, amount, date) VALUES ('" + database.escape(user) + "', " + newAmount.toString() + ", " + date + ")");
                        } else {
                            database.executeQuery("UPDATE gambling SET amount = " + newAmount + ", date = " + date + " WHERE user = '" + database.escape(user) + "' LIMIT 1");
                        }
                    }
                }
            } catch (SQLException ex) {
                message = "Da ging was mit der Datenbank schief...";
            }
        }

        event.getChannel().sendMessage(message).queue();
    }
}
