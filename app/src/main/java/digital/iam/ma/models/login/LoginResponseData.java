package digital.iam.ma.models.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseData {

    @Expose
    private String token;
    @Expose
    private String firstname;
    @Expose
    private String lastname;
    @Expose
    private String email;
    @SerializedName("shipping_addresse")
    private String shippingAddress;
    @SerializedName("shipping_telephone")
    private String shippingPhone;
    @SerializedName("shipping_postcode")
    private String shippingPostcode;
    @SerializedName("shipping_city")
    private String shippingCity;
    @Expose
    private String cin;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("line_is_activated")
    private String lineIsActivated;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getShippingPostcode() {
        return shippingPostcode;
    }

    public void setShippingPostcode(String shippingPostcode) {
        this.shippingPostcode = shippingPostcode;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLineIsActivated() {
        return lineIsActivated;
    }

    public void setLineIsActivated(String lineIsActivated) {
        this.lineIsActivated = lineIsActivated;
    }
}
