package digital.iam.ma.models.cart.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillingAddress {

    @Expose
    private String city;
    @SerializedName("country_id")
    private String countryId;
    @SerializedName("customer_address_id")
    private int customerAddressId;
    @SerializedName("customer_id")
    private int customerId;
    @Expose
    private String email;
    @Expose
    private String firstname;
    @Expose
    private int id;
    @Expose
    private String lastname;
    @Expose
    private String postcode;
    @Expose
    private String region;
    @SerializedName("region_code")
    private int regionCode;
    @SerializedName("region_id")
    private int regionId;
    @SerializedName("same_as_billing")
    private int sameAsBilling;
    @SerializedName("save_in_address_book")
    private int saveInAddressBook;
    @Expose
    private List<String> street;
    @Expose
    private String telephone;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public int getCustomerAddressId() {
        return customerAddressId;
    }

    public void setCustomerAddressId(int customerAddressId) {
        this.customerAddressId = customerAddressId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(int regionCode) {
        this.regionCode = regionCode;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public int getSameAsBilling() {
        return sameAsBilling;
    }

    public void setSameAsBilling(int sameAsBilling) {
        this.sameAsBilling = sameAsBilling;
    }

    public int getSaveInAddressBook() {
        return saveInAddressBook;
    }

    public void setSaveInAddressBook(int saveInAddressBook) {
        this.saveInAddressBook = saveInAddressBook;
    }

    public List<String> getStreet() {
        return street;
    }

    public void setStreet(List<String> street) {
        this.street = street;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}
