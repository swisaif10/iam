package digital.iam.ma.models.orders;

import com.google.gson.annotations.Expose;

import java.util.List;

public class GetOrdersResponse {
    @Expose
    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
