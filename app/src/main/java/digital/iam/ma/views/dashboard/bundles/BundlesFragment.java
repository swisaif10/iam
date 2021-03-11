package digital.iam.ma.views.dashboard.bundles;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import digital.iam.ma.databinding.FragmentBundlesBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.mybundle.MyBundleData;
import digital.iam.ma.models.mybundle.MyBundleResponse;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.BundlesViewModel;

public class BundlesFragment extends Fragment {

    private FragmentBundlesBinding fragmentBinding;
    private BundlesViewModel viewModel;
    private PreferenceManager preferenceManager;

    public BundlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(BundlesViewModel.class);
        viewModel.getMyBundleLiveData().observe(this, this::handleMyBundleDetailsData);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentBundlesBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMyBundleDetails();
    }

    private void init(MyBundleResponse myBundleResponse) {
        fragmentBinding.bundleName.setText(String.format("%s\n%s", myBundleResponse.getBundle().getLabel(), myBundleResponse.getBundle().getDetails()));
        fragmentBinding.msisdn.setText(myBundleResponse.getMsisdn());
        fragmentBinding.date.setText(String.format("%s\n%s", myBundleResponse.getDate().getStart(), myBundleResponse.getDate().getExpire()));

    }

    private void getMyBundleDetails() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.getMyBundleDetails(preferenceManager.getValue(Constants.TOKEN, ""), "fr");
    }

    private void handleMyBundleDetailsData(Resource<MyBundleData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                init(responseData.data.getMyBundleResponse());
                break;
            case LOADING:
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }
}