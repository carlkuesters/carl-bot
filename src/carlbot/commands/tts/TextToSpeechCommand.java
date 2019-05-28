package carlbot.commands.tts;

import carlbot.Bot;
import carlbot.commands.audio.GuildMessageAudioCommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class TextToSpeechCommand extends GuildMessageAudioCommand {

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
    public void parse(GuildMessageReceivedEvent event) {
        text = event.getMessage().getContentRaw().substring(commandPrefix.length());
    }

    @Override
    protected void play(GuildMessageReceivedEvent event, AudioPlayer audioPlayer) {
        TextToSpeechResult textToSpeechResult = textToSpeechGenerator.generate(text);
        TextToSpeechPlayer textToSpeechPlayer = new TextToSpeechPlayer(textToSpeechResult);
        audioPlayer.setVolume(150);
        textToSpeechPlayer.play(audioPlayer);
    }
}
