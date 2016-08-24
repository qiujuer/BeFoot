package net.qiujuer.sample.photowall;

/**
 * Created by JuQiu on 16/7/31.
 */

public class PhotoBean {
    private int id;
    private String path;
    private String thumbPath;
    private long date;

    public PhotoBean(){

    }

    public PhotoBean(int id,String path,String thumbPath,long date){
        this.id = id;
        this.path = path;
        this.thumbPath = thumbPath;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
