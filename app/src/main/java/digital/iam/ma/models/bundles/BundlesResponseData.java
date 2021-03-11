package digital.iam.ma.models.bundles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BundlesResponseData {
    @SerializedName("forfaits")
    private List<Bundle> bundles;

    public List<Bundle> getBundles() {
        return bundles;
    }

    public void setBundles(List<Bundle> bundles) {
        this.bundles = bundles;
    }
}
