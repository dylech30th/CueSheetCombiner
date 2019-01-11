package cuesheet.cue;

public class TRACK {
    private int offset;
    private String TITLE;
    private String PERFORMER;
    private String REM_COMPOSER;
    private String ISRC;
    private Duration INDEX00;
    private Duration INDEX01;

    public TRACK(int offset, String TITLE, String PERFORMER, String REM_COMPOSER, String ISRC, Duration INDEX00, Duration INDEX01) {
        this.offset = offset;
        this.TITLE = TITLE;
        this.PERFORMER = PERFORMER;
        this.REM_COMPOSER = REM_COMPOSER;
        this.ISRC = ISRC;
        this.INDEX00 = INDEX00;
        this.INDEX01 = INDEX01;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getPERFORMER() {
        return PERFORMER;
    }

    public void setPERFORMER(String PERFORMER) {
        this.PERFORMER = PERFORMER;
    }

    public String getREM_COMPOSER() {
        return REM_COMPOSER;
    }

    public void setREM_COMPOSER(String REM_COMPOSER) {
        this.REM_COMPOSER = REM_COMPOSER;
    }

    public String getISRC() {
        return ISRC;
    }

    public void setISRC(String ISRC) {
        this.ISRC = ISRC;
    }

    public Duration getINDEX00() {
        return INDEX00;
    }

    public void setINDEX00(Duration INDEX00) {
        this.INDEX00 = INDEX00;
    }

    public Duration getINDEX01() {
        return INDEX01;
    }

    public void setINDEX01(Duration INDEX01) {
        this.INDEX01 = INDEX01;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "TRACK " + offset + " AUDIO" + '\n' +
                "  TITLE \"" + TITLE + "\"\n" +
                "  PERFORMER \"" + PERFORMER + "\"\n" +
                "  REM COMPOSER \"" + ((REM_COMPOSER == null) ? "" : REM_COMPOSER) + "\"\n" +
                "  ISRC \"" + ISRC + "\"\n" +
                "  INDEX 00 " + INDEX00.getMinute() + ':' + INDEX00.getSecond() + ':' + INDEX00.getFrame() + '\n' +
                "  INDEX 01 " + INDEX01.getMinute() + ':' + INDEX01.getSecond() + ':' + INDEX01.getFrame() + '\n';
    }
}
