package Big2.sound;
import java.io.*;
import javax.sound.sampled.*;

public class Sounds {
    Clip clip;
    AudioInputStream audioStream;
    public void setMusic(String soundFileName) {
        try {
            audioStream = AudioSystem.getAudioInputStream(new File(soundFileName));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMusicContinuously() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopMusic() {
        clip.close();
        clip.stop();
    }
}