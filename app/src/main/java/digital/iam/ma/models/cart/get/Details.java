package digital.iam.ma.models.cart.get;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import digital.iam.ma.models.cart.Item;

public class Details {

    @SerializedName("billing_address")
    private BillingAddress billingAddress;
    @SerializedName("created_at")
    private String createdAt;
    @Expose
    private Currency currency;
    @Expose
    private Customer customer;
    @SerializedName("customer_is_guest")
    private Boolean customerIsGuest;
    @SerializedName("customer_note_notify")
    private Boolean customerNoteNotify;
    @SerializedName("customer_tax_class_id")
    private int customerTaxClassId;
    @SerializedName("extension_attributes")
    private ExtensionAttributes extensionAttributes;
    @Expose
    private int id;
    @SerializedName("is_active")
    private Boolean isActive;
    @SerializedName("is_virtual")
    private Boolean isVirtual;
    @Expose
    private List<Item> items;
    @SerializedName("items_count")
    private int itemsCount;
    @SerializedName("items_qty")
    private int itemsQty;
    @SerializedName("orig_order_id")
    private int origOrderId;
    @SerializedName("store_id")
    private int storeId;
    @SerializedName("updated_at")
    private String updatedAt;

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Boolean getCustomerIsGuest() {
        return customerIsGuest;
    }

    public void setCustomerIsGuest(Boolean customerIsGuest) {
        this.customerIsGuest = customerIsGuest;
    }

    public Boolean getCustomerNoteNotify() {
        return customerNoteNotify;
    }

    public void setCustomerNoteNotify(Boolean customerNoteNotify) {
        this.customerNoteNotify = customerNoteNotify;
    }

    public int getCustomerTaxClassId() {
        return customerTaxClassId;
    }

    public void setCustomerTaxClassId(int customerTaxClassId) {
        this.customerTaxClassId = customerTaxClassId;
    }

    public ExtensionAttributes getExtensionAttributes() {
        return extensionAttributes;
    }

    public void setExtensionAttributes(ExtensionAttributes extensionAttributes) {
        this.extensionAttributes = extensionAttributes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    public int getItemsQty() {
        return itemsQty;
    }

    public void setItemsQty(int itemsQty) {
        this.itemsQty = itemsQty;
    }

    public int getOrigOrderId() {
        return origOrderId;
    }

    public void setOrigOrderId(int origOrderId) {
        this.origOrderId = origOrderId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
