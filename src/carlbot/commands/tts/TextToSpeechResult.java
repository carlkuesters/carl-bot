package carlbot.commands.tts;

import java.util.ArrayList;

class TextToSpeechResult {

    TextToSpeechResult(ArrayList<TextToSpeechResultPart> parts) {
        this.parts = parts;
    }
    private ArrayList<TextToSpeechResultPart> parts;

    ArrayList<TextToSpeechResultPart> getParts() {
        return parts;
    }
}
