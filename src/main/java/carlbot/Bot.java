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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.sql.SQLException;
import java.util.HashMap;

public class Bot extends ListenerAdapter {

    private Database database;
    private Command[] guildMessageCommands;
    private Command[] guildVoiceCommands;
    private HashMap<Guild, Boolean> isPlayingAudioInGuilds = new HashMap<>();

    void connect() throws SQLException {
        String[] databaseSecrets = FileManager.getFileLines("./database.ini");
        database = new Database("mysql", databaseSecrets[0], databaseSecrets[1], databaseSecrets[2]);
        database.connect();
        initializeCommands();
        String discordBotToken = FileManager.getFileContent("./discord.ini");
        JDA jda = JDABuilder.createDefault(discordBotToken).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
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
            new GambleCommand(this),
            new CountCommand(this),
            new PlaySoundOnLeagueMessageCommand(this),
            // Has to be last since it will match every message starting with !
            new CommandExecuteCommand(this),
        };
        guildVoiceCommands = new Command[] {
            new PlayMaxiHiOnJoinCommand(this)
        };
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        handleGenericMessageReceivedEvent(event, null);
    }

    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent event) {
        super.onGenericGuildVoice(event);
        handleGenericGuildVoiceEvent(event);
    }

    public void handleEvent(Event event, String forcedContent) {
        if (event instanceof MessageReceivedEvent) {
            handleGenericMessageReceivedEvent((MessageReceivedEvent) event, forcedContent);
        } else if (event instanceof GenericGuildVoiceEvent) {
            handleGenericGuildVoiceEvent((GenericGuildVoiceEvent) event);
        }
    }

    private void handleGenericMessageReceivedEvent(MessageReceivedEvent event, String forcedContent) {
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
