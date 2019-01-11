package cuesheet;

import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.ID3v23Frame;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused"})
public class AudioInformation {
    private String filePath;
    private Map<FieldKey, List<TagField>> nameTagMap;
    private int minute;
    private int second;
    public AudioInformation(String filePath, Map<FieldKey, List<TagField>> nameTagMap, int minute, int second) {
        this.filePath = filePath;
        this.nameTagMap = nameTagMap;
        this.minute = minute;
        this.second = second;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Map<FieldKey, List<TagField>> getNameTagMap() {
        return nameTagMap;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setNameTagMap(Map<FieldKey, List<TagField>> nameTagMap) {
        this.nameTagMap = nameTagMap;
    }

    public String getTag(FieldKey key) {
        List<TagField> field = nameTagMap.get(key);
        if (field == null || field.size() == 0) return "";
        return field.get(0) instanceof ID3v23Frame ? ((ID3v23Frame) field.get(0)).getContent() : field.get(0).toString();
    }
}