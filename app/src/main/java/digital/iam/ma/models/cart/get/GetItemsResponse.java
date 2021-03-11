package digital.iam.ma.models.cart.get;

import com.google.gson.annotations.SerializedName;

public class GetItemsResponse {

    @SerializedName("items")
    private Details details;

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }
}
