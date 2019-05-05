package carlbot.commands.other;

import carlbot.Command;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class QuestionCommand extends Command<MessageReceivedEvent> {

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
            "Ich denke, das sollte so sein, ja."
    };

    @Override
    public boolean isMatching(MessageReceivedEvent event) {
        Message message = event.getMessage();
        return message.isMentioned(event.getJDA().getSelfUser()) && message.getContentRaw().endsWith("?");
    }

    @Override
    public void parse(MessageReceivedEvent event) {

    }

    @Override
    public void execute(MessageReceivedEvent event) {
        String answer = answers[(int) (Math.random() * answers.length)];
        event.getChannel().sendMessage(answer).queue();
    }
}
