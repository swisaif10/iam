package digital.iam.ma.models.services;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ServicesResponseData {
    @Expose
    private List<Service> services;

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
