package digital.iam.ma.models.services.update;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateServicesData {
    @SerializedName("available_services")
    private List<Item> availableServices;
    @SerializedName("affected_services")
    private List<Item> affectedServices;
    @Expose
    private String token;

    public List<Item> getAvailableServices() {
        return availableServices;
    }

    public void setAvailableServices(List<Item> availableServices) {
        this.availableServices = availableServices;
    }

    public List<Item> getAffectedServices() {
        return affectedServices;
    }

    public void setAffectedServices(List<Item> affectedServices) {
        this.affectedServices = affectedServices;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}