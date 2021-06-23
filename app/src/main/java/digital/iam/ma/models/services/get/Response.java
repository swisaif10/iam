package digital.iam.ma.models.services.get;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("available_services")
    private ServicesCategory availableServices;
    @SerializedName("affected_services")
    private ServicesCategory affectedServices;

    public ServicesCategory getAvailableServices() {
        return availableServices;
    }

    public void setAvailableServices(ServicesCategory availableServices) {
        this.availableServices = availableServices;
    }

    public ServicesCategory getAffectedServices() {
        return affectedServices;
    }

    public void setAffectedServices(ServicesCategory affectedServices) {
        this.affectedServices = affectedServices;
    }
}
