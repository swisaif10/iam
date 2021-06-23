package digital.iam.ma.models.services.get;

import com.google.gson.annotations.Expose;

public class Service {
    @Expose
    private int active;
    @Expose
    private String id;
    @Expose
    private String title;

    public Boolean getActive() {
        return active == 1;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
