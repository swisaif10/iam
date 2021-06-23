package digital.iam.ma.views.dashboard.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

import digital.iam.ma.databinding.FragmentServicesBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnServiceUpdatedListener;
import digital.iam.ma.models.contract.SuspendContractData;
import digital.iam.ma.models.services.get.Response;
import digital.iam.ma.models.services.get.Service;
import digital.iam.ma.models.services.get.ServicesListData;
import digital.iam.ma.models.services.update.Item;
import digital.iam.ma.models.services.update.UpdateServicesData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.ServicesViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.dashboard.DashboardActivity;

public class ServicesFragment extends Fragment implements OnServiceUpdatedListener {

    private FragmentServicesBinding fragmentBinding;
    private PreferenceManager preferenceManager;
    private ServicesViewModel viewModel;
    private UpdateServicesData updateServicesData;
    private List<Item> affectedServices;
    private List<Item> availableServices;

    public ServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        viewModel = ViewModelProviders.of(this).get(ServicesViewModel.class);
        viewModel.getServicesLiveData().observe(this, this::handleServicesData);
        viewModel.getUpdateServicesLiveData().observe(this, this::handleUpdateServicesData);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentServicesBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getServices();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) requireActivity()).showHideTabLayout(View.VISIBLE);
    }

    @Override
    public void onServiceSelected(Service service, String type) {
        if (type.equalsIgnoreCase("affected"))
            addService(new Item(service.getId(), true), affectedServices);
        else
            addService(new Item(service.getId(), true), availableServices);
    }

    @Override
    public void onServiceUnSelected(Service service, String type) {
        if (type.equalsIgnoreCase("affected"))
            addService(new Item(service.getId(), false), affectedServices);
        else
            addService(new Item(service.getId(), false), availableServices);
    }

    private void addService(Item item, List<Item> items) {
        fragmentBinding.saveBtn.setEnabled(true);
        int index = searchITemById(items, item.getId());
        if (index == -1)
            items.add(item);
        else
            items.set(index, item);
    }

    private void getServices() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.getServices(preferenceManager.getValue(Constants.TOKEN, ""), preferenceManager.getValue(Constants.MSISDN, ""), preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleServicesData(Resource<ServicesListData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                init(responseData.data.getResponse());
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(requireContext(), responseData.data.getHeader().getMessage(), v -> {
                    preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                    startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
                    requireActivity().finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }

    private void init(Response response) {

        fragmentBinding.affectedServicesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentBinding.affectedServicesRecycler.setAdapter(new ServicesAdapter(requireContext(), response.getAffectedServices().getServices(), this, "affected"));
        fragmentBinding.affectedServicesRecycler.setNestedScrollingEnabled(false);

        fragmentBinding.availableServicesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentBinding.availableServicesRecycler.setAdapter(new ServicesAdapter(requireContext(), response.getAvailableServices().getServices(), this, "available"));
        fragmentBinding.availableServicesRecycler.setNestedScrollingEnabled(false);

        updateServicesData = new UpdateServicesData();
        updateServicesData.setToken(preferenceManager.getValue(Constants.TOKEN, ""));
        affectedServices = new ArrayList<>();
        availableServices = new ArrayList<>();

        fragmentBinding.saveBtn.setOnClickListener(v -> {
            if (!affectedServices.isEmpty() && !availableServices.isEmpty()) {
                updateServicesData.setAffectedServices(affectedServices);
                updateServicesData.setAvailableServices(availableServices);
                updateServices();
            }
        });

        fragmentBinding.body.setVisibility(View.VISIBLE);
    }

    private int searchITemById(List<Item> services, String id) {
        final Predicate<Item> itemPredicate = item -> item.getId().equals(id);
        return Iterables.indexOf(services, itemPredicate);
    }

    private void updateServices() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.updateServices(updateServicesData, preferenceManager.getValue(Constants.LANGUAGE, "fr"));
    }

    private void handleUpdateServicesData(Resource<SuspendContractData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(requireContext(), responseData.data.getHeader().getMessage(), v -> {
                    preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                    startActivity(new Intent(requireActivity(), AuthenticationActivity.class));
                    requireActivity().finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }
}