package digital.iam.ma.views.dashboard.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import digital.iam.ma.R;
import digital.iam.ma.databinding.ServiceItemLayoutBinding;
import digital.iam.ma.listener.OnServiceUpdatedListener;
import digital.iam.ma.models.services.get.Service;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private final Context context;
    private final List<Service> services;
    private final OnServiceUpdatedListener onServiceUpdatedListener;
    private final String serviceType;

    public ServicesAdapter(Context context, List<Service> services, OnServiceUpdatedListener onServiceUpdatedListener, String serviceType) {
        this.context = context;
        this.services = services;
        this.onServiceUpdatedListener = onServiceUpdatedListener;
        this.serviceType = serviceType;
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


    class ViewHolder extends RecyclerView.ViewHolder {

        ServiceItemLayoutBinding itemBinding;

        ViewHolder(ServiceItemLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }

        private void bind(Service service) {
            itemBinding.title.setText(service.getTitle());
            if (service.getActive()) {
                itemBinding.switcher.toggleOn();
                itemBinding.container.setBackgroundColor(ContextCompat.getColor(context, R.color.lightBlue));
            } else {
                itemBinding.switcher.toggleOff();
                itemBinding.container.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }

            itemBinding.switcher.setOnToggleChanged((toggleStatus, booleanToggleStatus, toggleIntValue) -> {
                if (booleanToggleStatus) {
                    itemBinding.container.setBackgroundColor(ContextCompat.getColor(context, R.color.lightBlue));
                    onServiceUpdatedListener.onServiceSelected(service, serviceType);
                } else {
                    itemBinding.container.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    onServiceUpdatedListener.onServiceUnSelected(service, serviceType);
                }
            });
        }
    }
}