package digital.iam.ma.views.dashboard.payment.paymentmode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import digital.iam.ma.databinding.FragmentCashPaymentBinding;
import digital.iam.ma.views.dashboard.DashboardActivity;


public class CashPaymentFragment extends Fragment {

    private FragmentCashPaymentBinding cashPaymentBinding;
    private int position = 0;

    public CashPaymentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //assert getArguments() != null;
        if (getArguments() != null)
            position = getArguments().getInt("POSITION");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cashPaymentBinding = FragmentCashPaymentBinding.inflate(inflater, container, false);
        return cashPaymentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cashPaymentBinding.backImage.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}