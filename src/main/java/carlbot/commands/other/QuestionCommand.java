package carlbot.commands.other;

import carlbot.Bot;
import carlbot.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class QuestionCommand extends Command<MessageReceivedEvent> {

    public QuestionCommand(Bot bot) {
        super(bot);
    }
    private String[] answers = {
            "Ja.", "Nein.", "Vielleicht.", "Mit absoluter Sicherheit.",
            "Niemals.", "Nie im Leben.", "Manchmal.", "Ich denke schon.",
            "Immer.", "Teilweise.", "Jop.", "Wir werden sehen...",
            "Die Legende besagt \"Ja\".", "Nur selten.", "Ab und zu.",
            "Jep.", "Woher soll ich das wissen?", "Gelegentlich.",
            "Ich würde all mein Geld auf \"Ja\" verwetten.",
            "So sicher wie Carl salty wird.", "So sicher wie Lukas feedet.",
            "Nope.", "Auf jeden Fall.", "Es kommt immer drauf an...",
            "So sehr wie Yasuo Skill braucht.", "Aber hallo!",
            "Da kann ich mich nicht entscheiden...", "Glasklar.",
            "Deni-.. Definitiv nein.", "Klaro.", "Das trifft eigentlich immer zu.",
            "Ich denke, das sollte so sein, ja.", "Das weiss ich nicht.",
            "Ähm... Nein.", "Darüber müsste ich sehr genau nachdenken.",
            "So sicher wie Lukas neue Freunde hat.", "Vergiss es.",
            "Das ist unmöglich zu beantworten.", "Wir wissen alle, dass das wahr ist.",
            "Yesssss.", "Nö.", "Früher nein, mittlerweile ja.",
            "So sehr wie Fabi sich einen reinluckt.", "So sehr wie Carl Glück in TFT hat.",
            "Ist Wasser nass?", "Verrat ich nicht.", "Ooooh ja!",
            "So sehr wie meine Bildersuche funktioniert.", "Sichi Siggi.",
            "Klar wie Kloßbrühe", "So sehr wie Anna Frühschicht mag.",
            "So sicher wie Viego untargetable wird.", "Safe.",
            "So sehr wie Riot sich für Balance interessiert."
    };

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        return event.getMessage().getMentions().isMentioned(event.getJDA().getSelfUser()) && content.endsWith("?");
    }

    @Override
    public void parse(MessageReceivedEvent event, String content) {

    }

    @Override
    public void execute(MessageReceivedEvent event) {
        String answer = answers[(int) (Math.random() * answers.length)];
        event.getChannel().sendMessage(answer).queue();
    }
}
