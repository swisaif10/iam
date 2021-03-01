package digital.iam.ma.views.dashboard.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import digital.iam.ma.databinding.FragmentPaymentBinding;
import digital.iam.ma.views.dashboard.DashboardActivity;
import digital.iam.ma.views.dashboard.cart.CartFragment;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding fragmentBinding;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentPaymentBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {

        fragmentBinding.payBtn.setOnClickListener(v -> ((DashboardActivity) requireActivity()).addFragment(new CartFragment()));

        fragmentBinding.paymentsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        fragmentBinding.paymentsRecycler.setAdapter(new PaymentsAdapter(requireContext()));
        fragmentBinding.paymentsRecycler.setNestedScrollingEnabled(false);
    }
}