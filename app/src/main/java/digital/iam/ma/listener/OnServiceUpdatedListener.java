package digital.iam.ma.listener;

import digital.iam.ma.models.services.get.Service;

public interface OnServiceUpdatedListener {
    void onServiceSelected(Service service, String type);

    void onServiceUnSelected(Service service, String type);
}
