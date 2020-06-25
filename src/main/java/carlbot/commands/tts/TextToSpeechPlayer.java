package carlbot.commands.tts;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

class TextToSpeechPlayer {

    TextToSpeechPlayer(TextToSpeechResult result) {
        this.result = result;
    }
    private TextToSpeechResult result;
    private int partIndex;
    private int partTrackIndex;

    void play(AudioPlayer audioPlayer) {
        tryPlayNewTrack(audioPlayer);
        audioPlayer.addListener(audioEvent -> {
            if (audioEvent instanceof TrackEndEvent) {
                partTrackIndex++;
                if (partTrackIndex >= getCurrentPart().getAudioTracks().size()) {
                    partTrackIndex = 0;
                    partIndex++;
                }
                tryPlayNewTrack(audioPlayer);
            }
        });
    }

    private void tryPlayNewTrack(AudioPlayer audioPlayer) {
        if (partIndex < result.getParts().size()) {
            // TODO: Find a better way to pause in between as freezing the thread can cause the audio to lag
            /*long pauseAfter = getCurrentPart().getPauseAfter();
            if (pauseAfter > 0) {
                try {
                    Thread.sleep(pauseAfter);
                } catch (InterruptedException ex) {
                }
            }*/
            AudioTrack newTrack = getCurrentPart().getAudioTracks().get(partTrackIndex);
            audioPlayer.playTrack(newTrack.makeClone());
        }
    }

    private TextToSpeechResultPart getCurrentPart() {
        return result.getParts().get(partIndex);
    }
}
