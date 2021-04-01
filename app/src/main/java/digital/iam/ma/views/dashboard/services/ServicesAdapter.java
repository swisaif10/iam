package digital.iam.ma.views.dashboard.services;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import digital.iam.ma.databinding.ServiceItemLayoutBinding;
import digital.iam.ma.models.services.Service;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private final List<Service> services;

    public ServicesAdapter(List<Service> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ServiceItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(services.get(position));
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ServiceItemLayoutBinding itemBinding;

        ViewHolder(ServiceItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind(Service service) {
            itemBinding.title.setText(service.getTitle());
            itemBinding.checkbox.setChecked(service.getActive());

            /*if (count == 8) {
                switch (getAdapterPosition()) {
                    case 0:
                        itemBinding.title.setText("Internet 4G");
                        itemBinding.checkbox.setChecked(true);
                        break;
                    case 1:
                        itemBinding.title.setText("Service internet mobile");
                        itemBinding.checkbox.setChecked(true);
                        break;
                    case 2:
                        itemBinding.title.setText("Roaming postpayé");
                        itemBinding.checkbox.setChecked(false);
                        break;
                    case 3:
                        itemBinding.title.setText("Notification d'appel manqué");
                        itemBinding.checkbox.setChecked(true);
                        break;
                    case 4:
                        itemBinding.title.setText("Mobile zone");
                        itemBinding.checkbox.setChecked(true);
                        break;
                    case 5:
                        itemBinding.title.setText("Recharge complémentaire");
                        itemBinding.checkbox.setChecked(true);
                        break;
                    case 6:
                        itemBinding.title.setText("Boite vocale");
                        itemBinding.checkbox.setChecked(false);
                        break;
                    case 7:
                        itemBinding.title.setText("Service recharge internet mobile");
                        itemBinding.checkbox.setChecked(true);
                        itemBinding.separator.setVisibility(View.GONE);
                        break;
                }
            } else {
                switch (getAdapterPosition()) {
                    case 0:
                        itemBinding.title.setText("SMS MAP");
                        itemBinding.checkbox.setChecked(true);
                        break;
                    case 1:
                        itemBinding.title.setText("Carte SIM Secours");
                        itemBinding.checkbox.setChecked(true);
                        break;
                }
            }*/
        }
    }
}