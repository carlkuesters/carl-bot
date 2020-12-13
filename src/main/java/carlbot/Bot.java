package carlbot;

import carlbot.commands.face.FaceImageCommand;
import carlbot.commands.other.*;
import carlbot.commands.tts.TextToSpeechCommand;
import carlbot.database.Database;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.HashMap;

public class Bot extends ListenerAdapter {

    private Database database;
    private Command[] guildMessageCommands;
    private Command[] guildVoiceCommands;
    private HashMap<Guild, Boolean> isPlayingAudioInGuilds = new HashMap<>();

    void connect() throws SQLException, LoginException {
        String[] databaseSecrets = FileManager.getFileLines("./database.ini");
        database = new Database("mysql", "//localhost/" + databaseSecrets[2], databaseSecrets[0], databaseSecrets[1]);
        database.connect();
        initializeCommands();
        String discordBotToken = FileManager.getFileContent("./discord.ini");
        JDA jda = JDABuilder.createDefault(discordBotToken).build();
        jda.addEventListener(this);
    }

    private void initializeCommands() {
        guildMessageCommands = new Command[] {
            new FaceImageCommand(this),
            new QuestionCommand(this),
            new TextToSpeechCommand(this),
            new SaltCommand(this),
            new PlayCommand(this),
            new MaxiWritesHiCommand(this),
            new HeyCarlCommand(this),
            new ReactOnWaveCommand(this),
            new EchoCommand(this),
            new ChooseCommand(this),
            new CommandSaveCommand(this),
            // Has to be last since it will match every message starting with !
            new CommandExecuteCommand(this),
        };
        guildVoiceCommands = new Command[] {
            new PlayMaxiHiOnJoinCommand(this)
        };
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        handleGuildMessageReceivedEvent(event, null);
    }

    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent event) {
        handleGenericGuildVoiceEvent(event);
    }

    public void handleEvent(Event event, String forcedContent) {
        if (event instanceof GuildMessageReceivedEvent) {
            handleGuildMessageReceivedEvent((GuildMessageReceivedEvent) event, forcedContent);
        } else if (event instanceof GenericGuildVoiceEvent) {
            handleGenericGuildVoiceEvent((GenericGuildVoiceEvent) event);
        }
    }

    private void handleGuildMessageReceivedEvent(GuildMessageReceivedEvent event, String forcedContent) {
        if (event.getAuthor() != event.getJDA().getSelfUser()) {
            String content = ((forcedContent != null) ? forcedContent : event.getMessage().getContentRaw());
            try {
                handleEventCommands(guildMessageCommands, event, content);
            } catch (Exception ex) {
                ex.printStackTrace();
                event.getChannel().sendMessage("Da ging was richtig schief...").queue();
            }
        }
    }

    private void handleGenericGuildVoiceEvent(GenericGuildVoiceEvent event) {
        boolean isHuman = (!event.getMember().getUser().isBot());
        boolean isSelf = (event.getMember().getUser() == event.getJDA().getSelfUser());
        if (isHuman || isSelf) {
            try {
                handleEventCommands(guildVoiceCommands, event, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void handleEventCommands(Command[] commands, Event event, String content) {
        for (Command command : commands) {
            if (command.isMatching(event, content)) {
                command.parse(event, content);
                command.execute(event);
                break;
            }
        }
    }

    public Database getDatabase() {
        return database;
    }

    public void setPlayingAudioInGuild(Guild guild, boolean isPlayingAudio) {
        isPlayingAudioInGuilds.put(guild, isPlayingAudio);
    }

    public boolean isPlayingAudioInGuild(Guild guild) {
        return isPlayingAudioInGuilds.getOrDefault(guild, false);
    }
}
