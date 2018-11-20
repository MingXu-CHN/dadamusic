package cn.tedu.mediaplayer.entity;

public class MatchPic {
    private int aid;
    private int artist_id;
    private String lrc;
    private int sid;
    private String song;

    public MatchPic() {
    }

    public MatchPic(int aid, int artist_id, String lrc, int sid, String song) {
        this.aid = aid;
        this.artist_id = artist_id;
        this.lrc = lrc;
        this.sid = sid;
        this.song = song;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }
}
