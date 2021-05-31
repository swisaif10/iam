package digital.iam.ma.models.bundles;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BundlesResponseData {
    @SerializedName("forfaits")
    private List<BundleItem> bundles;

    public List<BundleItem> getBundles() {
        return bundles;
    }

    public void setBundles(List<BundleItem> bundles) {
        this.bundles = bundles;
    }
}
