package digital.iam.ma.views.dashboard.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import digital.iam.ma.databinding.PaymentItemLayoutBinding;
import digital.iam.ma.models.orders.Order;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {

    private final List<Order> orders;

    public PaymentsAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(PaymentItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(orders.get(position));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        PaymentItemLayoutBinding itemBinding;

        ViewHolder(PaymentItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind(Order order) {
            itemBinding.title.setText(order.getOrderTitle());
            itemBinding.subtitle.setText(order.getOrderStatus());
            itemBinding.msisdn.setText("0000000000");
            itemBinding.price.setText(order.getOrderTotal());

            if (getAdapterPosition() == (orders.size() - 1))
                itemBinding.separator.setVisibility(View.GONE);
        }
    }
}