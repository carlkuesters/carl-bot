package carlbot.commands.tts;

import carlbot.Bot;
import carlbot.commands.audio.MessageAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TextToSpeechCommand extends MessageAudioCommand {

    public TextToSpeechCommand(Bot bot) {
        super(bot, "!say ");
        audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        audioLibrary.loadDirectory(audioDirectory + "words/");
        audioLibrary.loadDirectory(audioDirectory + "parts/");
        textToSpeechGenerator = new TextToSpeechGenerator(audioLibrary);
    }
    private static final String audioDirectory = "./data/tts/";
    private TextToSpeechGenerator textToSpeechGenerator;
    private String text;

    @Override
    public void parse(MessageReceivedEvent event, String content) {
        text = content.substring(commandPrefix.length());
    }

    @Override
    protected void play(MessageReceivedEvent event, AudioPlayer audioPlayer) {
        TextToSpeechResult textToSpeechResult = textToSpeechGenerator.generate(text);
        TextToSpeechPlayer textToSpeechPlayer = new TextToSpeechPlayer(textToSpeechResult);
        audioPlayer.setVolume(150);
        textToSpeechPlayer.play(audioPlayer);
    }
}
