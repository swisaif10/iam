package digital.iam.ma.models.services.update;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("service_id")
    private String id;
    @SerializedName("service_enabled")
    private Boolean enabled;

    public Item(String id, Boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
