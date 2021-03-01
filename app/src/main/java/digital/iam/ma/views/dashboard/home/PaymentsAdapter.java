package digital.iam.ma.views.dashboard.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import digital.iam.ma.databinding.PaymentItemLayoutBinding;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {

    public PaymentsAdapter() {
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
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 8;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        PaymentItemLayoutBinding itemBinding;

        ViewHolder(PaymentItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind() {
            if (getAdapterPosition() == 7)
                itemBinding.separator.setVisibility(View.GONE);
        }
    }
}