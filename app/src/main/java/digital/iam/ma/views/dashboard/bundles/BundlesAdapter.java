package digital.iam.ma.views.dashboard.bundles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import digital.iam.ma.R;
import digital.iam.ma.databinding.BundlesItemLayoutBinding;
import digital.iam.ma.listener.OnBundleSelectedListener;
import digital.iam.ma.models.bundles.BundleItem;

public class BundlesAdapter extends RecyclerView.Adapter<BundlesAdapter.ViewHolder> {

    private final Context context;
    private final List<BundleItem> bundleItemList;
    private final OnBundleSelectedListener onBundleSelectedListener;

    public BundlesAdapter(Context context, List<BundleItem> bundleItemList, OnBundleSelectedListener onBundleSelectedListener) {
        this.context = context;
        this.bundleItemList = bundleItemList;
        this.onBundleSelectedListener = onBundleSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(BundlesItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(bundleItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return bundleItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        BundlesItemLayoutBinding itemBinding;

        ViewHolder(BundlesItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind(BundleItem bundleItem) {
            itemBinding.title.setText(bundleItem.getName());
            itemBinding.subtitle.setText(bundleItem.getLabelPrice());

            if (getAdapterPosition() == 0) {
                if (getAdapterPosition() == bundleItemList.size() - 2 || getAdapterPosition() == bundleItemList.size() - 1)
                    itemBinding.horizontalSeparator.setVisibility(View.GONE);
                if (getAdapterPosition() == bundleItemList.size() - 1 || getAdapterPosition() % 2 != 0)
                    itemBinding.verticalSeparator.setVisibility(View.GONE);
            }

            if (bundleItem.getSelected()) {
                itemBinding.container.setBackgroundResource(R.drawable.selected_bundle_background);
                itemBinding.title.setTextColor(ContextCompat.getColor(context, R.color.white));
                itemBinding.subtitle.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                itemBinding.container.setBackgroundResource(R.drawable.unselected_bundle_background);
                itemBinding.title.setTextColor(ContextCompat.getColor(context, R.color.grey8));
                itemBinding.subtitle.setTextColor(ContextCompat.getColor(context, R.color.grey8));
            }

            itemBinding.getRoot().setOnClickListener(v -> {
                if (!bundleItem.getSelected()) {
                    for (BundleItem item : bundleItemList) {
                        item.setSelected(false);
                    }
                    bundleItemList.get(getAdapterPosition()).setSelected(true);
                    onBundleSelectedListener.onBundleSelected(bundleItemList.get(getAdapterPosition()));
                }
                notifyDataSetChanged();
            });
        }
    }

}