package carlbot.commands.audio;

import carlbot.Bot;
import carlbot.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.managers.AudioManager;

public abstract class AudioCommand<T extends Event> extends Command<T> {

    AudioCommand(Bot bot, String commandPrefix) {
        super(bot);
        this.commandPrefix = commandPrefix;
        audioPlayerManager = new DefaultAudioPlayerManager();
        audioLibrary = new AudioLibrary(audioPlayerManager);
    }
    protected String commandPrefix;
    protected AudioPlayerManager audioPlayerManager;
    protected AudioLibrary audioLibrary;

    @Override
    public void parse(T event, String content) {

    }

    @Override
    public void execute(T event) {
        Guild guild = getGuild(event);
        AudioManager audioManager = guild.getAudioManager();
        AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
        audioManager.setSendingHandler(new BotGuildAudioPlayerSendHandler(audioPlayer, bot, guild));
        AudioChannel audioChannel = getAudioChannelToJoin(event);
        if (audioChannel != null) {
            play(event, audioPlayer);
            audioManager.openAudioConnection(audioChannel);
        }
    }

    protected abstract Guild getGuild(T event);

    protected abstract AudioChannel getAudioChannelToJoin(T event);

    protected abstract void play(T event, AudioPlayer audioPlayer);
}
