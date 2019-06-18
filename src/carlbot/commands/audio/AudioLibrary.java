package carlbot.commands.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class AudioLibrary {

    AudioLibrary(AudioPlayerManager audioPlayerManager) {
        this.audioPlayerManager = audioPlayerManager;
    }
    private AudioPlayerManager audioPlayerManager;
    private HashMap<String, AudioTrack> audioTracks = new HashMap<>();

    public void loadDirectory(String directory) {
        File[] files = new File(directory).listFiles();
        for (File file : files) {
            loadFile(file);
        }
    }

    public void loadFile(String filePath) {
        loadFile(new File(filePath));
    }

    private void loadFile(File file) {
        String key = file.getName().substring(0, file.getName().length() - 4);
        loadAndStoreTrack(key, file.getPath());
    }

    private void loadAndStoreTrack(String key, String identifier) {
        loadTrack(identifier, audioTrack -> audioTracks.put(key, audioTrack));
    }

    public void loadTrack(String identifier, Consumer<AudioTrack> audioTrackConsumer) {
        audioPlayerManager.loadItem(identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                audioTrackConsumer.accept(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // For now, playlists are not supported yet
                // Sources that can return one (like YouTube search) will just play the first track
                audioTrackConsumer.accept(playlist.getTracks().get(0));
            }

            @Override
            public void noMatches() {
                // Notify no one that we've got nothing?
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                // Notify the developer that everything exploded
                throwable.printStackTrace();
            }
        });
    }

    public AudioTrack getTrack(String key) {
        return audioTracks.get(key);
    }

    public AudioTrack getRandomTrack() {
        Set<String> keySet = audioTracks.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        int index = (int) (Math.random() * keySet.size());
        for (int i = 0; i < index; i++) {
            keyIterator.next();
        }
        String key = keyIterator.next();
        return audioTracks.get(key);
    }
}
