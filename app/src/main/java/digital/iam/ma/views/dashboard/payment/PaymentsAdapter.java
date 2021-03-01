package digital.iam.ma.views.dashboard.payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import digital.iam.ma.databinding.PaymentDetailsItemLayoutBinding;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {

    private Context context;

    public PaymentsAdapter(Context context) {
        this.context = context;
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
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        PaymentDetailsItemLayoutBinding itemBinding;

        ViewHolder(PaymentDetailsItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind() {
            itemBinding.showDetailsBtn.setOnClickListener(v -> ((DashboardActivity) context).addFragment(new ContractDetailsFragment()));

        }
    }
}