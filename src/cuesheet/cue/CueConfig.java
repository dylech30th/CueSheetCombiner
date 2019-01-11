package cuesheet.cue;

public class CueConfig {
    private String cueDirectory;
    private String cueFilePath;
    private String outputAudioPath;

    public CueConfig(String cueDirectory, String cueFilePath, String outputAudioPath) {
        this.cueDirectory = cueDirectory;
        this.cueFilePath = cueDirectory + "\\" + cueFilePath;
        this.outputAudioPath = cueDirectory + "\\" + outputAudioPath;
    }

    public String getCueDirectory() {
        return cueDirectory;
    }

    public String getCueFilePath() {
        return cueFilePath;
    }

    public String getOutputAudioPath() {
        return outputAudioPath;
    }
}
