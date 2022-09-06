package carlbot.commands.other;

import carlbot.Bot;
import carlbot.commands.audio.MessageAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SaltCommand extends MessageAudioCommand {

    public SaltCommand(Bot bot) {
        super(bot, "!salt");
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadDirectory("./data/salt/");
    }
    private String name;

    @Override
    public void parse(MessageReceivedEvent event, String content) {
        name = content.substring(commandPrefix.length()).trim();
    }

    @Override
    protected void play(MessageReceivedEvent event, AudioPlayer audioPlayer) {
        AudioTrack audioTrack = (name.isEmpty() ? audioLibrary.getRandomTrack() : audioLibrary.getTrack(name));
        if (audioTrack != null) {
            audioPlayer.setVolume(15);
            audioPlayer.playTrack(audioTrack.makeClone());
        } else {
            event.getChannel().sendMessage("Da war ich nicht salty...").queue();
        }
    }
}
