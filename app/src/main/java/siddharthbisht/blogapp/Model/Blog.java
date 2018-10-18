package siddharthbisht.blogapp.Model;

public class Blog {
    public String title;
    public String desc;
    public String image;
    public String timeStamp;
    public String userId;

    public Blog() {
    }

    public Blog(String title, String desc, String image, String timeStamp, String userId) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.timeStamp = timeStamp;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
