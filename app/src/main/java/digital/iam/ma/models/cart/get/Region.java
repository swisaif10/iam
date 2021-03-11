package digital.iam.ma.models.cart.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Region {

    @Expose
    private String region;
    @SerializedName("region_code")
    private Object regionCode;
    @SerializedName("region_id")
    private int regionId;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Object getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Object regionCode) {
        this.regionCode = regionCode;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

}
