package digital.iam.ma.models.services.get;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ServicesCategory {
    @Expose
    private List<Service> services;
    @Expose
    private String title;

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}