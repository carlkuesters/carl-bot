package carlbot.commands.other;

import carlbot.Bot;
import carlbot.commands.audio.AudioUtility;
import carlbot.commands.audio.MessageAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PlaySoundOnLeagueMessageCommand extends MessageAudioCommand {

    public PlaySoundOnLeagueMessageCommand(Bot bot) {
        super(bot, null);
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadFile("./data/other/boing.wav");
        audioLibrary.loadFile("./data/other/circus.wav");
        audioLibrary.loadFile("./data/other/faker.wav");
        audioLibrary.loadFile("./data/other/silver_scrapes.wav");
        audioLibrary.loadFile("./data/other/victory.wav");
    }
    private String trackName;

    @Override
    public boolean isMatching(MessageReceivedEvent event, String content) {
        switch (event.getAuthor().getName()) {
            case "Deserved Victory":
                trackName = "victory";
                break;
            case "Execute":
                trackName = "boing";
                break;
            case "First Blood":
                trackName = "circus";
                break;
            case "Pentakill":
                trackName = (content.contains("destroflyer") ? "faker" : "silver_scrapes");
                break;
            default:
                trackName = null;
                break;
        }
        return (trackName != null);
    }

    @Override
    protected AudioChannel getAudioChannelToJoin(MessageReceivedEvent event) {
        return AudioUtility.getCurrentOrBestAudioChannel(event.getGuild());
    }

    @Override
    protected void play(MessageReceivedEvent event, AudioPlayer audioPlayer) {
        AudioTrack audioTrack = audioLibrary.getTrack(trackName);
        audioPlayer.playTrack(audioTrack.makeClone());
    }
}
