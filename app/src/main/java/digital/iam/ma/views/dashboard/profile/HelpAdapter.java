package digital.iam.ma.views.dashboard.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import digital.iam.ma.databinding.HelpItemLayoutBinding;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {

    public HelpAdapter() {
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
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 6;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        HelpItemLayoutBinding itemBinding;

        ViewHolder(HelpItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind() {

            switch (getAdapterPosition()) {
                case 0:
                case 3:
                    itemBinding.title.setText("Urgence & dépannage");
                    break;
                case 1:
                case 4:
                    itemBinding.title.setText("Offre & options");
                    break;
                case 2:
                case 5:
                    itemBinding.title.setText("Taris & communication");
                    break;
            }

        }
    }
}