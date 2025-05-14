package main;


import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
  Clip clip;
   URL[] soundUrls = new URL[30];

    public Sound() {
        soundUrls[0] = getClass().getResource("/sounds/WPawn.wav");
        soundUrls[1] = getClass().getResource("/sounds/BPawn.wav");
        soundUrls[2] = getClass().getResource("/sounds/WRook.wav");
        soundUrls[3] = getClass().getResource("/sounds/BRook.wav");
        soundUrls[4] = getClass().getResource("/sounds/WKnight.wav");
        soundUrls[5] = getClass().getResource("/sounds/BKnight.wav");
        soundUrls[6] = getClass().getResource("/sounds/WBishop.wav");
        soundUrls[7] = getClass().getResource("/sounds/BBishop.wav");
        soundUrls[8] = getClass().getResource("/sounds/wQueen.wav");
        soundUrls[9] = getClass().getResource("/sounds/BQueen.wav");
        soundUrls[10] = getClass().getResource("/sounds/wKing.wav");
        soundUrls[11] = getClass().getResource("/sounds/BKing.wav");
        soundUrls[20] = getClass().getResource("/sounds/move-self.wav");
    }

    public void setFile(int index) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrls[index]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); 
            clip.start();
        } else {
            System.err.println("Clip not loaded!");
        }
    }


    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
