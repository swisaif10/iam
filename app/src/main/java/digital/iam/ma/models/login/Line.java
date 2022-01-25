package digital.iam.ma.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Line {
    @SerializedName("sku_forfait")
    private String sKu;
    @SerializedName("public_id")
    private String publicId;
    @Expose
    private String price;
    @Expose
    private String state;
    @SerializedName("customer_id")
    private Integer customerId;
    @Expose
    private String msisdn;
    @SerializedName("order_id")
    private Integer orderId;
    @Expose
    private String status;
    @SerializedName("forfait")
    private String bundleName;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("exp_date")
    private String expireDate;

    public String getSKu() {
        return sKu;
    }

    public void setSKu(String sKu) {
        this.sKu = sKu;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

}
