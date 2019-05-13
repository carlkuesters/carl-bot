package carlbot.commands.other;

import carlbot.commands.audio.AudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class SaltCommand extends AudioCommand {

    public SaltCommand() {
        super("!salt");
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadDirectory("./data/salt/");
    }
    private String name;

    @Override
    public void parse(GuildMessageReceivedEvent event) {
        name = event.getMessage().getContentRaw().substring(commandPrefix.length()).trim();
    }

    @Override
    protected void play(GuildMessageReceivedEvent event, AudioPlayer audioPlayer) {
        AudioTrack audioTrack = (name.isEmpty() ? audioLibrary.getRandomTrack() : audioLibrary.getTrack(name));
        if (audioTrack != null) {
            audioPlayer.setVolume(15);
            audioPlayer.playTrack(audioTrack.makeClone());
        } else {
            event.getChannel().sendMessage("Da war ich nicht salty...").queue();
        }
    }
}
