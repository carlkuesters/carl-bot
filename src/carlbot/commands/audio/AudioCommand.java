package carlbot.commands.audio;

import carlbot.Command;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public abstract class AudioCommand extends Command<GuildMessageReceivedEvent> {

    protected AudioCommand(String commandPrefix) {
        this.commandPrefix = commandPrefix;
        audioPlayerManager = new DefaultAudioPlayerManager();
        audioLibrary = new AudioLibrary(audioPlayerManager);
    }
    protected String commandPrefix;
    protected AudioPlayerManager audioPlayerManager;
    protected AudioLibrary audioLibrary;

    @Override
    public boolean isMatching(GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(commandPrefix)) {
            if (event.getMember().getVoiceState().inVoiceChannel()) {
                return true;
            } else {
                event.getChannel().sendMessage("Geh in nen Voice Channel, dann join ich dir und sags...").queue();
            }
        }
        return false;
    }

    @Override
    public void execute(GuildMessageReceivedEvent event) {
        AudioManager audioManager = event.getGuild().getAudioManager();
        AudioPlayer audioPlayer = audioPlayerManager.createPlayer();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(audioPlayer));
        VoiceChannel myChannel = event.getMember().getVoiceState().getChannel();
        play(event, audioPlayer);
        audioManager.openAudioConnection(myChannel);
    }

    protected abstract void play(GuildMessageReceivedEvent event, AudioPlayer audioPlayer);
}
