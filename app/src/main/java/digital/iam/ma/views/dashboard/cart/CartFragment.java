package digital.iam.ma.views.dashboard.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import digital.iam.ma.databinding.FragmentCartBinding;
import digital.iam.ma.utilities.Utilities;

public class CartFragment extends Fragment {

    private FragmentCartBinding fragmentBinding;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    private void init() {

        fragmentBinding.backBtn.setOnClickListener(v -> requireActivity().onBackPressed());
        fragmentBinding.purchaseBtn.setOnClickListener(v -> Utilities.showPurchaseDialog(requireContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
    }
}