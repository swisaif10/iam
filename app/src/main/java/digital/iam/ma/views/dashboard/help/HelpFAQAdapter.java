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
import digital.iam.ma.models.help.FAQ;

public class HelpFAQAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<FAQ> items;
    private final String identifier;

    public HelpFAQAdapter(List<FAQ> items, String identifier) {
        this.items = items;
        this.identifier = identifier;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (identifier.equalsIgnoreCase("faq"))
            return new FAQViewHolder(HelpFaqItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false));
        else
            return new CustomerServiceViewHolder(CustomerServiceItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false));
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
            FAQ faq = items.get(position);
            itemBinding.title.setText(faq.getTitle());
            itemBinding.description.setText(Html.fromHtml(faq.getContent()));

            if (faq.getExpandable().equalsIgnoreCase("1"))
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
            FAQ faq = items.get(position);
            itemBinding.title.setText(faq.getContent());
        }
    }
}