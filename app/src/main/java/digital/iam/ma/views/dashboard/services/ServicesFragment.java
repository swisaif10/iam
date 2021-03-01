package digital.iam.ma.views.dashboard.services;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import digital.iam.ma.databinding.FragmentServicesBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.services.Service;
import digital.iam.ma.models.services.ServicesData;
import digital.iam.ma.models.services.ServicesResponseData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.ServicesViewModel;

public class ServicesFragment extends Fragment {

    private FragmentServicesBinding fragmentBinding;
    private PreferenceManager preferenceManager;
    private ServicesViewModel viewModel;
    private ServicesAdapter affectedServicesAdapter;
    private List<Service> affectedServices;
    private List<Service> availableServices;
    private ServicesAdapter availableServicesAdapter;

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

    private void init(ServicesResponseData responseData) {

        fragmentBinding.affectedServicesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        affectedServices = new ArrayList<>();
        affectedServices = responseData.getServices();
        affectedServicesAdapter = new ServicesAdapter(affectedServices);
        fragmentBinding.affectedServicesRecycler.setAdapter(affectedServicesAdapter);
        fragmentBinding.affectedServicesRecycler.setNestedScrollingEnabled(false);

        fragmentBinding.availableServicesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        availableServices = new ArrayList<>();
        availableServicesAdapter = new ServicesAdapter(availableServices);
        fragmentBinding.availableServicesRecycler.setAdapter(availableServicesAdapter);
        fragmentBinding.availableServicesRecycler.setNestedScrollingEnabled(false);
    }

    private void getServices() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.getServices(preferenceManager.getValue(Constants.TOKEN, ""), "fr");
    }

    private void handleServicesData(Resource<ServicesData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                assert responseData.data != null;
                init(responseData.data.getResponse());
                break;
            case LOADING:
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }

}