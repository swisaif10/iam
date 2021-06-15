package digital.iam.ma.models.orders;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetOrdersResponse {
    @SerializedName("paid_orders")
    private List<Order> paidOrders;
    @SerializedName("pending_orders")
    private List<Order> pendingOrders;

    public List<Order> getPaidOrders() {
        return paidOrders;
    }

    public void setPaidOrders(List<Order> paidOrders) {
        this.paidOrders = paidOrders;
    }

    public List<Order> getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(List<Order> pendingOrders) {
        this.pendingOrders = pendingOrders;
    }
}
