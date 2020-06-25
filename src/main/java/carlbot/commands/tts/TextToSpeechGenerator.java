package carlbot.commands.tts;

import carlbot.commands.audio.AudioLibrary;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;

class TextToSpeechGenerator {

    TextToSpeechGenerator(AudioLibrary audioLibrary) {
        this.audioLibrary = audioLibrary;
    }
    private AudioLibrary audioLibrary;

    TextToSpeechResult generate(String text) {
        ArrayList<TextToSpeechResultPart> parts = new ArrayList<>();
        String[] words = text.split(" ");
        for (String word : words) {
            ArrayList<AudioTrack> partAudioTracks = new ArrayList<>();
            long pauseAfter = 0;
            AudioTrack wordAudioTrack = audioLibrary.getTrack(word);
            if (wordAudioTrack != null) {
                partAudioTracks.add(wordAudioTrack);
            } else {
                String[] letters = word.split("");
                for (String letter : letters) {
                    letter = letter.toLowerCase();
                    if (".".equals(letter)) {
                        pauseAfter = 300;
                    } else {
                        AudioTrack letterAudioTrack = audioLibrary.getTrack(letter);
                        if (letterAudioTrack != null) {
                            partAudioTracks.add(letterAudioTrack);
                        }
                    }
                }
            }
            parts.add(new TextToSpeechResultPart(partAudioTracks, pauseAfter));
        }
        return new TextToSpeechResult(parts);
    }
}
