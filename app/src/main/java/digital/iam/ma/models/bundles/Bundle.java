package digital.iam.ma.models.bundles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bundle {
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
    @SerializedName("forfait_voix")
    private String voice;
    @SerializedName("forfait_data")
    private String data;

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

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
