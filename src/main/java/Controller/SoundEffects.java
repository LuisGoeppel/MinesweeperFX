package Controller;

import java.io.File;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;
import javax.swing.*;

public class SoundEffects {

    static Clip music;

    public static void playSound(String soundFile) {
        try {
            File f = new File("src/main/resources/Sounds/" + soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startMusic(String soundFile) {
        try {
            File f = new File("src/main/resources/Sounds/" + soundFile);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
            music = AudioSystem.getClip();
            music.open(audioIn);
            music.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        music.stop();
    }
}
