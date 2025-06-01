package finalproject.game.util;

import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioSource implements Cloneable {
    // idk how to set the volume/level
    long currentFrame;
    Clip clip;
    State state;
    AudioInputStream inputStream;
    File file;
    int loop;

    public AudioSource(@NotNull File file, int loop) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        this.file = file;
        this.loop = loop;
        clip = AudioSystem.getClip();
        resetAudio();
    }

    public AudioSource(File file) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        this(file, Clip.LOOP_CONTINUOUSLY);
    }

    public void play() {
        clip.start();
        state = State.PLAYING;
    }

    public void pause() {
        currentFrame = clip.getMicrosecondPosition();
        clip.stop();
        state = State.PAUSED;
    }

    public void resume() {
        if(state.equals(State.PLAYING)) return;

        clip.close();

        try {
            resetAudio();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }

        clip.setMicrosecondPosition(currentFrame);
        play();
    }

    public void stop() {
        currentFrame = 0;
        clip.stop();
        clip.close();
        state = State.PAUSED;
    }

    public void restart() {
        stop();
        resume();
    }

    public void jump(long frames) {
        pause();
        currentFrame += frames;
        resume();
    }

    private void resetAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        inputStream = AudioSystem.getAudioInputStream(new File(file.toURI()).getAbsoluteFile());

        clip.open(inputStream);
        clip.loop(loop);
    }

    public State getState() {
        return state;
    }

    @Override
    public AudioSource clone() {
        try {
            AudioSource clone = (AudioSource) super.clone();
            clone.file = new File(file.toURI()).getAbsoluteFile();
            clone.clip = AudioSystem.getClip();
            clone.resetAudio();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum State {
        PLAYING,
        PAUSED;
    }
}