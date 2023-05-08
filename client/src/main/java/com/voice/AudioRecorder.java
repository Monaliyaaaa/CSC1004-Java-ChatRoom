package com.voice;

import javax.sound.sampled.*;
import java.io.*;

public class AudioRecorder extends Thread{
    private static TargetDataLine mic;
    private String audioName;

    public AudioRecorder(String audioName){
        this.audioName=audioName;
    }

    @Override
    public void run(){
        startRecording();
        saveRecording();

    }

    private void startRecording(){
        System.out.println("recording...");
        try{
            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

            mic = (TargetDataLine) AudioSystem.getLine(info);
            mic.open();

            System.out.println("pls say something...");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveRecording(){
        try {
            mic.start();
            AudioInputStream audioInputStream = new AudioInputStream(mic);
            File f = new File(audioName);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, f);
            System.out.println("saving...");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void stopRecording() {
        mic.stop();
        mic.close();
        System.out.println("ending...");
    }

    public void play(String file){
        try {
            System.out.println("start playing...");
            FileInputStream input = new FileInputStream(file);
            AudioFormat.Encoding encoding =  new AudioFormat.Encoding("PCM_SIGNED");
            AudioFormat format = new AudioFormat(encoding,44100, 16, 2, 4, 44100 ,false);
            SourceDataLine dataLine = null;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            try {
                dataLine = (SourceDataLine) AudioSystem.getLine(info);
                dataLine.open(format);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }

            dataLine.start();
            byte[] b = new byte[256];
            try {
                while (input.read(b)>0){
                    dataLine.write(b,0,b.length);
                }
                dataLine.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
