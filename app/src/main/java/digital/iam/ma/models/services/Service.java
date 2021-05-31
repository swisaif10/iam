package digital.iam.ma.models.services;

import com.google.gson.annotations.Expose;

public class Service {
    @Expose
    private String title;
    @Expose
    private String url;
    @Expose
    private int active;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActive() {
        return active == 1;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
