package digital.iam.ma.views.dashboard.cart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import digital.iam.ma.databinding.FragmentCartBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.cart.get.GetItemsData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.CartViewModel;

public class CartFragment extends Fragment {

    private FragmentCartBinding fragmentBinding;
    private CartViewModel viewModel;
    private PreferenceManager preferenceManager;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        viewModel.getGetItemsLiveData().observe(this, this::handleGetItemsData);

        preferenceManager = new PreferenceManager.Builder(requireContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentCartBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        getItems();
    }

    private void init() {

        fragmentBinding.backBtn.setOnClickListener(v -> requireActivity().onBackPressed());
        fragmentBinding.purchaseBtn.setOnClickListener(v -> Utilities.showPurchaseDialog(requireContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
    }

    private void getItems() {
        fragmentBinding.loader.setVisibility(View.VISIBLE);
        viewModel.getItems(preferenceManager.getValue(Constants.TOKEN, ""), "fr");
    }

    private void handleGetItemsData(Resource<GetItemsData> responseData) {
        fragmentBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:

                break;
            case LOADING:
                break;
            case ERROR:
                Utilities.showErrorPopup(requireContext(), responseData.message);
                break;
        }
    }
}