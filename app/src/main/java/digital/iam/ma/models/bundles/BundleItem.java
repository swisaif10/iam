package digital.iam.ma.models.bundles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BundleItem {
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String sku;
    @Expose
    private String currency;
    @Expose
    private int price;
    @SerializedName("label_price")
    private String labelPrice;
    @Expose
    private int status;
    @Expose
    private int visibility;
    @SerializedName("forfait_voix_value")
    private int call;
    @SerializedName("forfait_voix_unit")
    private String callUnit;
    @SerializedName("forfait_data_value")
    private int internet;
    @SerializedName("forfait_data_unit")
    private String internetUnit;
    private Boolean selected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLabelPrice() {
        return labelPrice;
    }

    public void setLabelPrice(String labelPrice) {
        this.labelPrice = labelPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getCall() {
        return call;
    }

    public void setCall(int call) {
        this.call = call;
    }

    public String getCallUnit() {
        return callUnit;
    }

    public void setCallUnit(String callUnit) {
        this.callUnit = callUnit;
    }

    public int getInternet() {
        return internet;
    }

    public void setInternet(int internet) {
        this.internet = internet;
    }

    public String getInternetUnit() {
        return internetUnit;
    }

    public void setInternetUnit(String internetUnit) {
        this.internetUnit = internetUnit;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
