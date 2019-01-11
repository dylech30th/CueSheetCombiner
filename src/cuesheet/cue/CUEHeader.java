package cuesheet.cue;

public class CUEHeader {
    private String REM_DISCID;
    private String REM_COMMENT;
    private String PERFORMER;
    private String TITLE;
    private String REM_COMPOSER;
    private String FILE;

    public CUEHeader(String REM_DISCID, String REM_COMMENT, String PERFORMER, String TITLE, String REM_COMPOSER, String FILE) {
        this.REM_DISCID = REM_DISCID;
        this.REM_COMMENT = REM_COMMENT;
        this.PERFORMER = PERFORMER;
        this.TITLE = TITLE;
        this.REM_COMPOSER = REM_COMPOSER;
        this.FILE = FILE;
    }

    public CUEHeader(String REM_DISCID, String REM_COMMENT, String PERFORMER, String TITLE, String REM_COMPOSER) {
        this.REM_DISCID = REM_DISCID;
        this.REM_COMMENT = REM_COMMENT;
        this.PERFORMER = PERFORMER;
        this.TITLE = TITLE;
        this.REM_COMPOSER = REM_COMPOSER;
    }

    public String getREM_DISCID() {
        return REM_DISCID;
    }

    public void setREM_DISCID(String REM_DISCID) {
        this.REM_DISCID = REM_DISCID;
    }

    public String getREM_COMMENT() {
        return REM_COMMENT;
    }

    public void setREM_COMMENT(String REM_COMMENT) {
        this.REM_COMMENT = REM_COMMENT;
    }

    public String getPERFORMER() {
        return PERFORMER;
    }

    public void setPERFORMER(String PERFORMER) {
        this.PERFORMER = PERFORMER;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getREM_COMPOSER() {
        return REM_COMPOSER;
    }

    public void setREM_COMPOSER(String REM_COMPOSER) {
        this.REM_COMPOSER = REM_COMPOSER;
    }

    public String getFILE() {
        return FILE;
    }

    public void setFILE(String FILE) {
        this.FILE = FILE;
    }

    @Override
    public String toString() {
        return "REM DISCID " + REM_DISCID + '\n' +
                "REM COMMENT \"" + REM_COMMENT + "\"\n" +
                "PERFORMER \"" + PERFORMER + "\"\n" +
                "TITLE \"" + TITLE + "\"\n" +
                "REM COMPOSER \"" + ((REM_COMPOSER == null) ? "" : REM_COMPOSER) + "\"\n" +
                "FILE \"" + FILE + "\" WAVE\n";
    }
}
