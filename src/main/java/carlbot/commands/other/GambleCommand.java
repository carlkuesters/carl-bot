package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import carlbot.Emojis;
import carlbot.database.Database;
import carlbot.database.QueryResult;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;

public class GambleCommand extends Command<GuildMessageReceivedEvent> {

    public GambleCommand(Bot bot) {
        super(bot);
    }
    private String commandPrefix = "!gamble ";
    private boolean showRanking;
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
        showRanking = "ranking".equals(betAmountText);
        showAmount = "?".equals(betAmountText);
        betAll = "all".equals(betAmountText);
        betAmount = null;
        if ((!showRanking) && (!showAmount) && (!betAll)) {
            try {
                betAmount = new BigInteger(betAmountText);
            } catch (NumberFormatException ex) {
                // Will further down be accordingly responded to
            }
        }
    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        Database database = bot.getDatabase();
        String user = event.getAuthor().getName();
        long date = System.currentTimeMillis();

        String message;
        try {
            if (showRanking) {
                QueryResult rows = database.getQueryResult("SELECT user, amount FROM gambling ORDER BY amount DESC");
                int rank = 0;
                BigDecimal rankAmount = null;
                message = "Salzstreuer-Bestenliste:";
                while (rows.next()) {
                    String currentUser = rows.getString("user");
                    BigDecimal currentAmount = rows.getBigDecimal("amount");
                    if ((rankAmount == null) || (rankAmount.compareTo(currentAmount) > 0)) {
                        rank++;
                    }
                    message += "\n" + rank + ". " + currentUser + " (" + currentAmount + ")";
                    rankAmount = currentAmount;
                }
            } else if ((!betAll) && (!showAmount) && (betAmount == null)) {
                message = "?";
            } else if ((!betAll) && (!showAmount) && (betAmount.compareTo(BigInteger.ZERO) <= 0)) {
                message = Emojis.CARLTHINK;
            } else {
                BigInteger oldAmount = BigInteger.ZERO;
                BigInteger games = BigInteger.ZERO;
                BigInteger gamesWon = BigInteger.ZERO;
                BigInteger gamesLost = BigInteger.ZERO;
                BigInteger amountWon = BigInteger.ZERO;
                BigInteger amountLost = BigInteger.ZERO;
                BigInteger highscore = BigInteger.ZERO;
                QueryResult queryResult = database.getQueryResult("SELECT amount, games, games_won, games_lost, amount_won, amount_lost, highscore FROM gambling WHERE user = '" + database.escape(user) + "' LIMIT 1");
                boolean userExists = queryResult.next();
                if (userExists) {
                    oldAmount = queryResult.getBigDecimal("amount").toBigInteger();
                    games = queryResult.getBigDecimal("games").toBigInteger();
                    gamesWon = queryResult.getBigDecimal("games_won").toBigInteger();
                    gamesLost = queryResult.getBigDecimal("games_lost").toBigInteger();
                    amountWon = queryResult.getBigDecimal("amount_won").toBigInteger();
                    amountLost = queryResult.getBigDecimal("amount_lost").toBigInteger();
                    highscore = queryResult.getBigDecimal("highscore").toBigInteger();
                }

                if (showAmount) {
                    message = user + ", du hast " + oldAmount + " Salzstreuer. " + Emojis.KARL;
                } else {
                    if (betAll) {
                        betAmount = oldAmount;
                    }

                    BigInteger newAmount = null;
                    if (oldAmount.equals(BigInteger.ZERO)) {
                        message = user + " hat gar keine Salzstreuer, daher schenke ich ihm 1.";
                        newAmount = BigInteger.ONE;
                    } else if (oldAmount.compareTo(betAmount) >= 0) {
                        BigInteger minimumBetAmount = BigInteger.ONE;
                        // Ensures that players can't build up a large enough amount to ensure never going broke
                        int minimumDoublingGamblesToReachZero = 7;
                        if (oldAmount.bitLength() > minimumDoublingGamblesToReachZero) {
                            minimumBetAmount = BigInteger.TWO.pow(oldAmount.bitLength() - minimumDoublingGamblesToReachZero);
                        }
                        if (betAmount.compareTo(minimumBetAmount) >= 0) {
                            boolean win = (Math.random() < 0.5);
                            if (win) {
                                newAmount = oldAmount.add(betAmount);
                                gamesWon = gamesWon.add(BigInteger.ONE);
                                amountWon = amountWon.add(betAmount);
                                if (newAmount.compareTo(highscore) > 0) {
                                    highscore = newAmount;
                                }
                            } else {
                                newAmount = oldAmount.subtract(betAmount);
                                gamesLost = gamesLost.add(BigInteger.ONE);
                                amountLost = amountLost.add(betAmount);
                            }
                            games = games.add(BigInteger.ONE);
                            message = user + " " + (betAll ? "geht mit " + betAmount + " Salzstreuern all-in" : "wettet " + betAmount + " Salzstreuer")
                                    + ", " + (win ? "gewinnt" : "verliert") + " und besitzt jetzt " + newAmount + " Salzstreuer."
                                    + " " + Emojis.KARL + " " + (win ? Emojis.FEELSGOODMAN : Emojis.FEELSBADMANC);
                        } else {
                            message = user + ", dein Mindesteinsatz ist mittlerweile " + minimumBetAmount + " Salzstreuer.";
                        }
                    } else {
                        message = user + ", du hast nur " + oldAmount + " Salzstreuer...";
                    }

                    if (newAmount != null) {
                        if (userExists) {
                            database.executeQuery("UPDATE gambling SET " +
                                    "amount = " + newAmount + ", " +
                                    "games = " + games + ", " +
                                    "games_won = " + gamesWon + ", " +
                                    "games_lost = " + gamesLost + ", " +
                                    "amount_won = " + amountWon + ", " +
                                    "amount_lost = " + amountLost + ", " +
                                    "highscore = " + highscore + ", " +
                                    "date = " + date + " " +
                                    "WHERE user = '" + database.escape(user) + "' LIMIT 1");
                        } else {
                            database.executeQuery("INSERT INTO gambling (user, amount, games, games_won, games_lost, amount_won, amount_lost, highscore, date) VALUES (" +
                                    "'" + database.escape(user) + "', " +
                                    newAmount + ", " +
                                    games + ", " +
                                    gamesWon + ", " +
                                    gamesLost + ", " +
                                    amountWon + ", " +
                                    amountLost + ", " +
                                    highscore + ", " +
                                    date + ")");
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            message = "Da ging was mit der Datenbank schief...";
        }

        event.getChannel().sendMessage(message).queue();
    }
}
