package carlbot;

import carlbot.commands.face.FaceImageCommand;
import carlbot.commands.other.*;
import carlbot.commands.tts.TextToSpeechCommand;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.HashMap;

public class Bot extends ListenerAdapter {

    private Command[] defaultCommands = {
        new FaceImageCommand(this),
        new QuestionCommand(this)
    };
    private Command[] guildMessageCommands = {
        new TextToSpeechCommand(this),
        new SaltCommand(this),
        new PlayCommand(this),
        new MaxiWritesHiCommand(this),
        new HeyCarlCommand(this),
        new ReactOnWaveCommand(this)
    };
    private Command[] guildVoiceCommands = {
        new PlayMaxiHiOnJoinCommand(this)
    };
    private HashMap<Guild, Boolean> isPlayingAudioInGuilds = new HashMap<>();

    void connect() throws LoginException {
        JDA jda = new JDABuilder(AccountType.BOT).setToken("YOUR-SECRET-TOKEN").build();
        jda.addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor() != event.getJDA().getSelfUser()) {
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
        if (event.getAuthor() != event.getJDA().getSelfUser()) {
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

    public void setPlayingAudioInGuild(Guild guild, boolean isPlayingAudio) {
        isPlayingAudioInGuilds.put(guild, isPlayingAudio);
    }

    public boolean isPlayingAudioInGuild(Guild guild) {
        return isPlayingAudioInGuilds.getOrDefault(guild, false);
    }
}
