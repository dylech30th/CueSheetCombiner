package cuesheet;

import cuesheet.cue.*;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.audio.wav.WavFileReader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "FieldCanBeLocal", "WeakerAccess", "SpellCheckingInspection"})
public class CueSheetMaker {
    public enum AudioType {
        MP3, FLAC, WAV;
        static AudioType parse(String str) {
            switch (str.toLowerCase()) {
                case "mp3":
                    return MP3;
                case "wav":
                    return WAV;
                case "flac":
                    return FLAC;
            }
            return null;
        }
    }
    private final String ffmpegPath = "ffmpeg\\bin\\ffmpeg.exe";
    private String outputPath;
    private String cueDirectory;
    private String cueFilePath;
    private List<String> toCombineList = new ArrayList<>();
    private int headLength1;
    private int headLength2;
    private AudioType audioType;
    private FieldKey[] fieldKeys = FieldKey.values();
    private Duration index = new Duration(0, 0, 0);

    public void setFieldKeys(FieldKey[] fieldKeys) {
        this.fieldKeys = fieldKeys;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public CueSheetMaker(String filePath, CueConfig cueConfig) {
        File file = new File(filePath);
        this.cueDirectory = cueConfig.getCueDirectory();
        this.cueFilePath = cueConfig.getCueFilePath();
        this.outputPath = cueConfig.getOutputAudioPath();
        this.toCombineList = Collections.singletonList(filePath);
        this.audioType = AudioType.parse(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('.') + 1));
    }

    public CueSheetMaker(List<String> toCombineList, CueConfig cueConfig, FieldKey... fieldKeys) {
        File file = new File(toCombineList.get(0));
        this.toCombineList = toCombineList;
        if (fieldKeys.length != 0) this.fieldKeys = fieldKeys;
        this.cueDirectory = cueConfig.getCueDirectory();
        this.cueFilePath = cueConfig.getCueFilePath();
        this.outputPath = cueConfig.getOutputAudioPath();
        this.audioType = AudioType.parse(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('.') + 1));
    }

    public CueSheetMaker(CueConfig cueConfig, String... toCombineList) {
        File file = new File(toCombineList[0]);
        this.cueDirectory = cueConfig.getCueDirectory();
        this.cueFilePath = cueConfig.getCueFilePath();
        this.outputPath = cueConfig.getOutputAudioPath();
        this.toCombineList.addAll(Arrays.asList(toCombineList));
        this.audioType = AudioType.parse(file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('.') + 1));
    }

    public CueSheetMaker() { }

    private Pair<Integer, Integer> getDuration(AudioHeader header) {
        int minute = header.getTrackLength() / 60;
        int second = header.getTrackLength() - minute * 60;
        return new Pair<>(minute, second);
    }

    private <T extends AudioFileReader> AudioInformation getActualInfo(String filePath, T audioFileReader, FieldKey[] fieldKeys) {
        AudioFile fileInfo = null;
        try {
            fileInfo = audioFileReader.read(new File(filePath));
        } catch (CannotReadException | IOException | ReadOnlyFileException | TagException | InvalidAudioFrameException e) {
            e.printStackTrace();
        }
        Map<FieldKey, List<TagField>> tagListMap = new HashMap<>();
        Pair<Integer, Integer> duration = getDuration(Objects.requireNonNull(fileInfo).getAudioHeader());
        Tag tag = fileInfo.getTag();
        for (FieldKey key : fieldKeys) {
            tagListMap.put(key, tag.getFields(key));
        }
        return new AudioInformation(filePath, tagListMap, duration.getKey(), duration.getValue());
    }

    private AudioInformation getAudioInformation(String filePath) {
        switch (Objects.requireNonNull(AudioType.parse(filePath.substring(filePath.lastIndexOf('.') + 1)))) {
            case MP3:
                return getActualInfo(filePath, new MP3FileReader(), fieldKeys);
            case WAV:
                return getActualInfo(filePath, new WavFileReader(), fieldKeys);
            case FLAC:
                return getActualInfo(filePath, new FlacFileReader(), fieldKeys);
        }
        return null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void merge() throws IOException, InterruptedException {
        StringBuilder stringBuilder = new StringBuilder();
        this.toCombineList.forEach(t -> stringBuilder.append("file").append(" \'").append(t).append("\'\n"));
        File temp = new File("command.txt");
        File out = new File(outputPath);
        if (out.exists()) out.delete();
        if (!temp.exists()) temp.createNewFile();
        try (FileWriter fileWriter = new FileWriter(temp)) {
            fileWriter.write(stringBuilder.toString());
        }
        temp.deleteOnExit();
        String combineCommand = ffmpegPath + " -f concat -safe 0 -i command.txt -acodec copy " + outputPath;
        Process process = Runtime.getRuntime().exec(combineCommand);
        process.waitFor();
        process.destroy();
    }

    public void printInformation(String filePath) {
        AudioInformation information = getAudioInformation(filePath);
        Stream<FieldKey> queryContent = Objects.requireNonNull(information).getNameTagMap().keySet().stream();
        System.out.println("时长: " + information.getMinute() + "分" + information.getSecond() + "秒");
        System.out.println("文件路径: " + information.getFilePath());
        queryContent.filter(t -> !information.getTag(t).isEmpty()).forEach(t -> System.out.println("Tag ID: " + t + " Tag Content: " + information.getTag(t)));
    }

    private CUE parse(CUEHeader cueHeader, Duration gap) {
        cueHeader.setFILE(outputPath.substring(outputPath.lastIndexOf("\\") + 1));
        List<AudioInformation> informational = toCombineList.stream().map(this::getAudioInformation).collect(Collectors.toList());
        List<TRACK> tracks = new ArrayList<>();
        for (AudioInformation information : informational) {
            int offset = informational.indexOf(information);
            String title = information.getTag(FieldKey.TITLE);
            String isrc = information.getTag(FieldKey.ISRC);
            String remComposer = information.getTag(FieldKey.COMPOSER);
            String performer = information.getTag(FieldKey.ARTIST);
            Duration index00 = new Duration(
                    Integer.valueOf(this.index.getMinute()),
                    Integer.valueOf(this.index.getSecond()),
                    Integer.valueOf(this.index.getFrame()));
            Duration index01;
            if (offset == 0) {
                index01 = new Duration(0, 0, 0);
            } else {
                index01 = new Duration(
                        Integer.valueOf(gap.getMinute()) + Integer.valueOf(this.index.getMinute()),
                        Integer.valueOf(gap.getSecond()) + Integer.valueOf(this.index.getSecond()),
                        Integer.valueOf(gap.getFrame())
                );
            }
            this.index = new Duration(
                    information.getMinute() + Integer.valueOf(index00.getMinute()),
                    information.getSecond() + Integer.valueOf(index00.getSecond()),
                    Integer.valueOf(index00.getFrame())
            );
            TRACK track = new TRACK(offset, title, performer, remComposer, isrc, index00, index01);
            tracks.add(track);
        }
        return new CUE(cueHeader, tracks);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public CUE execute(CUEHeader header, Duration gap) throws IOException, InterruptedException {
        File file = new File(cueDirectory); file.mkdirs();
        File cuePath = new File(cueFilePath); if (!cuePath.exists()) cuePath.createNewFile();
        this.merge();
        CUE cue = parse(header, gap);
        try (FileWriter fileWriter = new FileWriter(cuePath)) {
            fileWriter.write(cue.toString());
        }
        return cue;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> file = Stream.of(Objects.requireNonNull(new File("D:\\CueTest").listFiles())).map(File::getAbsolutePath).collect(Collectors.toList());
        CueConfig cueConfig = new CueConfig("testresult", "cuetest.cue", "result.mp3");
        CueSheetMaker cueSheetMaker = new CueSheetMaker(file, cueConfig);
        System.out.println(cueSheetMaker.execute(
                new CUEHeader("0102030405060708", "SIMPLE_REM_COMMENT", "SIMPLE_PERFORMER", "SIMPLE_TITLE", "SIMPLE_COMPOSER"),
                new Duration(0, 2, 0)));
    }
}
