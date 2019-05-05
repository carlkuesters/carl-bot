package carlbot.commands.tts;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;

class TextToSpeechResultPart {

    TextToSpeechResultPart(ArrayList<AudioTrack> audioTracks, long pauseAfter) {
        this.audioTracks = audioTracks;
        this.pauseAfter = pauseAfter;
    }
    private ArrayList<AudioTrack> audioTracks;
    private long pauseAfter;

    ArrayList<AudioTrack> getAudioTracks() {
        return audioTracks;
    }

    long getPauseAfter() {
        return pauseAfter;
    }
}
