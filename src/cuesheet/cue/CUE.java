package cuesheet.cue;

import java.util.List;

@SuppressWarnings({"unused"})
public class CUE {
    private CUEHeader cueHeader;
    private List<TRACK> files;

    public CUE(CUEHeader header, List<TRACK> files) {
        this.cueHeader = header;
        this.files = files;
    }

    public CUEHeader getHeader() {
        return cueHeader;
    }

    public void setHeader(CUEHeader header) {
        this.cueHeader = header;
    }

    public List<TRACK> getFiles() {
        return files;
    }

    public void setFiles(List<TRACK> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        String header = cueHeader.toString();
        StringBuilder stringBuilder = new StringBuilder();
        files.forEach(stringBuilder::append);
        return header + stringBuilder.toString();
    }
}
