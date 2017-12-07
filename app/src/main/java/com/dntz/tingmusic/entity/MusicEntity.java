package com.dntz.tingmusic.entity;

/**
 * Created by dntz on 2017/11/6.
 */

public class MusicEntity {
    private int musicid;
    private String musicmd5;
    private String musicname;
    private String musicauthor;
    private String musiclyric;
    private String musickeywards;
    private String musicimage;
    private String musicalbum;
    private String musicurl;

    public int getMusicid() {
        return musicid;
    }

    public void setMusicid(int musicid) {
        this.musicid = musicid;
    }

    public String getMusicmd5() {
        return musicmd5;
    }

    public void setMusicmd5(String musicmd5) {
        this.musicmd5 = musicmd5;
    }

    public String getMusicname() {
        return musicname;
    }

    public void setMusicname(String musicname) {
        this.musicname = musicname;
    }

    public String getMusicauthor() {
        return musicauthor;
    }

    public void setMusicauthor(String musicauthor) {
        this.musicauthor = musicauthor;
    }

    public String getMusiclyric() {
        return musiclyric;
    }

    public void setMusiclyric(String musiclyric) {
        this.musiclyric = musiclyric;
    }

    public String getMusickeywards() {
        return musickeywards;
    }

    public void setMusickeywards(String musickeywards) {
        this.musickeywards = musickeywards;
    }

    public String getMusicimage() {
        return musicimage;
    }

    public void setMusicimage(String musicimage) {
        this.musicimage = musicimage;
    }

    public String getMusicalbum() {
        return musicalbum;
    }

    public void setMusicalbum(String musicalbum) {
        this.musicalbum = musicalbum;
    }

    public String getMusicurl() {
        return musicurl;
    }

    public void setMusicurl(String musicurl) {
        this.musicurl = musicurl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MusicEntity that = (MusicEntity) o;

        if (musicid != that.musicid) return false;
        if (musicmd5 != null ? !musicmd5.equals(that.musicmd5) : that.musicmd5 != null) return false;
        if (musicname != null ? !musicname.equals(that.musicname) : that.musicname != null) return false;
        if (musicauthor != null ? !musicauthor.equals(that.musicauthor) : that.musicauthor != null) return false;
        if (musiclyric != null ? !musiclyric.equals(that.musiclyric) : that.musiclyric != null) return false;
        if (musickeywards != null ? !musickeywards.equals(that.musickeywards) : that.musickeywards != null)
            return false;
        if (musicimage != null ? !musicimage.equals(that.musicimage) : that.musicimage != null) return false;
        if (musicalbum != null ? !musicalbum.equals(that.musicalbum) : that.musicalbum != null) return false;
        if (musicurl != null ? !musicurl.equals(that.musicurl) : that.musicurl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = musicid;
        result = 31 * result + (musicmd5 != null ? musicmd5.hashCode() : 0);
        result = 31 * result + (musicname != null ? musicname.hashCode() : 0);
        result = 31 * result + (musicauthor != null ? musicauthor.hashCode() : 0);
        result = 31 * result + (musiclyric != null ? musiclyric.hashCode() : 0);
        result = 31 * result + (musickeywards != null ? musickeywards.hashCode() : 0);
        result = 31 * result + (musicimage != null ? musicimage.hashCode() : 0);
        result = 31 * result + (musicalbum != null ? musicalbum.hashCode() : 0);
        result = 31 * result + (musicurl != null ? musicurl.hashCode() : 0);
        return result;
    }
}

