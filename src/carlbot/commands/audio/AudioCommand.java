package carlbot.commands.audio;

import carlbot.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public abstract class AudioCommand<T extends GenericGuildEvent> extends Command<T> {

    AudioCommand(String commandPrefix) {
        this.commandPrefix = commandPrefix;
        audioPlayerManager = new DefaultAudioPlayerManager();
        audioLibrary = new AudioLibrary(audioPlayerManager);
    }
    protected String commandPrefix;
    protected AudioPlayerManager audioPlayerManager;
    protected AudioLibrary audioLibrary;

    @Override
    public void parse(GenericGuildEvent event) {

    }

    @Override
    public void execute(T event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
        VoiceChannel myChannel = getVoiceChannelToJoin(event);
        if (myChannel != null) {
            play(event, audioPlayer);
            audioManager.openAudioConnection(myChannel);
        }
    }

    protected abstract VoiceChannel getVoiceChannelToJoin(T event);

    protected abstract void play(T event, AudioPlayer audioPlayer);
}
