package cuesheet.cue;

import cuesheet.AudioInformation;

import java.text.DecimalFormat;

public class Duration {
    private String minute;
    private String second;
    private String frame;

    public Duration(int minute, int second, int frame) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        this.minute = decimalFormat.format(minute);
        this.second = decimalFormat.format(second);
        this.frame = decimalFormat.format(frame);
        if (Integer.valueOf(this.second) >= 60) {
            String temp = this.second;
            int min = Integer.valueOf(this.minute) + Integer.valueOf(temp) / 60;
            int sec = Integer.valueOf(this.minute) * 60 + Integer.valueOf(this.second);
            this.minute = String.valueOf(min);
            this.second = String.valueOf(sec - min * 60);
        }
    }

    public Duration(AudioInformation audioInformation) {
        this.minute = String.valueOf(audioInformation.getMinute());
        this.second = String.valueOf(audioInformation.getSecond());
        this.frame = "00";
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }
}
