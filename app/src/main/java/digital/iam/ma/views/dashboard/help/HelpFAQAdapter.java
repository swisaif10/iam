package digital.iam.ma.views.dashboard.help;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import digital.iam.ma.databinding.CustomerServiceItemLayoutBinding;
import digital.iam.ma.databinding.HelpFaqItemLayoutBinding;
import digital.iam.ma.models.help.Datum;

public class HelpFAQAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Datum> items;

    public HelpFAQAdapter(List<Datum> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //if (identifier.equalsIgnoreCase("faq"))
        return new FAQViewHolder(HelpFaqItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false));
        //else
            /*return new CustomerServiceViewHolder(CustomerServiceItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false));*/
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FAQViewHolder)
            ((FAQViewHolder) holder).bind(position);
        else
            ((CustomerServiceViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class FAQViewHolder extends RecyclerView.ViewHolder {

        HelpFaqItemLayoutBinding itemBinding;

        FAQViewHolder(HelpFaqItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind(int position) {

            Datum faq = items.get(position);
            itemBinding.title.setText(faq.getTopic());
            itemBinding.description.setText(Html.fromHtml(faq.getDescription()));

            itemBinding.getRoot().setOnClickListener(v -> {
                if (itemBinding.arrow.getRotation() == 90) {
                    itemBinding.arrow.setRotation(270);
                    itemBinding.description.setVisibility(View.VISIBLE);
                } else {
                    itemBinding.arrow.setRotation(90);
                    itemBinding.description.setVisibility(View.GONE);
                }
            });
        }
    }

    class CustomerServiceViewHolder extends RecyclerView.ViewHolder {

        CustomerServiceItemLayoutBinding itemBinding;

        CustomerServiceViewHolder(CustomerServiceItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind(int position) {
            Datum faq = items.get(position);
            itemBinding.title.setText(faq.getDescription());
        }
    }
}