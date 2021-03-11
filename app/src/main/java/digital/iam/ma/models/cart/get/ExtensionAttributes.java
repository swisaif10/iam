package digital.iam.ma.models.cart.get;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExtensionAttributes {

    @SerializedName("is_subscribed")
    private Boolean isSubscribed;
    @SerializedName("shipping_assignments")
    private List<Object> shippingAssignments;

    public Boolean getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(Boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    public List<Object> getShippingAssignments() {
        return shippingAssignments;
    }

    public void setShippingAssignments(List<Object> shippingAssignments) {
        this.shippingAssignments = shippingAssignments;
    }

}
