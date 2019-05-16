package carlbot;

import carlbot.commands.face.FaceImageCommand;
import carlbot.commands.other.*;
import carlbot.commands.tts.TextToSpeechCommand;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter {

    private Command[] defaultCommands = {
        new FaceImageCommand(),
        new QuestionCommand()
    };
    private Command[] guildMessageCommands = {
            new TextToSpeechCommand(),
            new SaltCommand(),
            new PlayCommand(),
            new MaxiWritesHiCommand()
    };
    private Command[] guildVoiceCommands = {
            new PlayMaxiHiOnJoinCommand()
    };

    void connect() throws LoginException {
        JDA jda = new JDABuilder(AccountType.BOT).setToken("YOUR-SECRET-TOKEN").build();
        jda.addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            try {
                handleEventCommands(event, defaultCommands);
            } catch (Exception ex) {
                ex.printStackTrace();
                event.getChannel().sendMessage("Da ging was richtig schief...").queue();
            }
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            try {
                handleEventCommands(event, guildMessageCommands);
            } catch (Exception ex) {
                ex.printStackTrace();
                event.getChannel().sendMessage("Da ging was richtig schief...").queue();
            }
        }
    }

    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent event) {
        boolean isHuman = (!event.getMember().getUser().isBot());
        boolean isSelf = (event.getMember().getUser() == event.getJDA().getSelfUser());
        if (isHuman || isSelf) {
            try {
                handleEventCommands(event, guildVoiceCommands);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleEventCommands(Event event, Command[] commands) {
        for (Command command : commands) {
            if (command.isMatching(event)) {
                command.parse(event);
                command.execute(event);
                break;
            }
        }
    }
}
