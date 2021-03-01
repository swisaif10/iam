package digital.iam.ma.models.services;

import com.google.gson.annotations.Expose;

public class Service {
    @Expose
    private String title;
    @Expose
    private String url;
    @Expose
    private Boolean active;

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
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
