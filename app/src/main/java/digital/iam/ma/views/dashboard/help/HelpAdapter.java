package digital.iam.ma.views.dashboard.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import digital.iam.ma.databinding.HelpItemLayoutBinding;
import digital.iam.ma.models.help.Item;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {

    private final Context context;
    private final List<Item> items;

    public HelpAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(HelpItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        HelpItemLayoutBinding itemBinding;

        ViewHolder(HelpItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind(int position) {
            Item item = items.get(position);
            itemBinding.title.setText(item.getTitle());
            itemBinding.faqRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            itemBinding.faqRecycler.setAdapter(new HelpFAQAdapter(item.getFaqs(), item.getIdentifier()));
            itemBinding.faqRecycler.setNestedScrollingEnabled(false);
        }
    }
}