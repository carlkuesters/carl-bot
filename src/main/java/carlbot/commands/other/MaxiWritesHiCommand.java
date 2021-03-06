package carlbot.commands.other;

import carlbot.Bot;
import carlbot.commands.audio.AudioUtility;
import carlbot.commands.audio.GuildMessageAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MaxiWritesHiCommand extends GuildMessageAudioCommand {

    public MaxiWritesHiCommand(Bot bot) {
        super(bot, null);
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadFile("./data/other/maxi_hi.wav");
    }

    @Override
    public boolean isMatching(GuildMessageReceivedEvent event, String content) {
        if (event.getAuthor().getName().equals("GameUser21")) {
            String contentLowerCase = content.toLowerCase();
            if (contentLowerCase.equals("hi") || (contentLowerCase.startsWith("hi") && contentLowerCase.contains("^^"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected VoiceChannel getVoiceChannelToJoin(GuildMessageReceivedEvent event) {
        return AudioUtility.getCurrentOrBestVoiceChannel(event.getGuild());
    }

    @Override
    protected void play(GuildMessageReceivedEvent event, AudioPlayer audioPlayer) {
        AudioTrack audioTrack = audioLibrary.getTrack("maxi_hi");
        audioPlayer.playTrack(audioTrack.makeClone());
    }
}
