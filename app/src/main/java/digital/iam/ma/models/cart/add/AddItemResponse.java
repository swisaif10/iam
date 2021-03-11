package digital.iam.ma.models.cart.add;

import com.google.gson.annotations.SerializedName;

import digital.iam.ma.models.cart.Item;

public class AddItemResponse {
    @SerializedName("cart")
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
