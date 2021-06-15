package digital.iam.ma.listener;

import digital.iam.ma.models.orders.Order;

public interface OnItemSelectedListener {
    void onItemSelected(Order order, Boolean payment);
}
