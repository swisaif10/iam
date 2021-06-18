package digital.iam.ma.views.dashboard.payment;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import digital.iam.ma.R;
import digital.iam.ma.databinding.PaymentDetailsItemLayoutBinding;
import digital.iam.ma.listener.OnItemSelectedListener;
import digital.iam.ma.models.orders.Order;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {

    private final Context context;
    private final List<Order> orders;
    private final OnItemSelectedListener onItemSelectedListener;

    public PaymentsAdapter(Context context, List<Order> orders, OnItemSelectedListener onItemSelectedListener) {
        this.context = context;
        this.orders = orders;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(PaymentDetailsItemLayoutBinding.inflate(
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

        PaymentDetailsItemLayoutBinding itemBinding;

        ViewHolder(PaymentDetailsItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind(Order order) {
            itemBinding.name.setText(order.getOrderTitle());
            itemBinding.price.setText(order.getOrderTotal());
            itemBinding.status.setText(order.getOrderStatus());

            if (order.getOrderStatus().equalsIgnoreCase("Impayé")) {
                itemBinding.showDetailsBtn.setText(context.getString(R.string.pay_btn_text));
                itemBinding.showDetailsBtn.setPaintFlags(itemBinding.showDetailsBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                itemBinding.showDetailsBtn.setOnClickListener(v -> onItemSelectedListener.onItemSelected(order, true));
            } else
                itemBinding.showDetailsBtn.setOnClickListener(v -> onItemSelectedListener.onItemSelected(order, false));
        }
    }
}