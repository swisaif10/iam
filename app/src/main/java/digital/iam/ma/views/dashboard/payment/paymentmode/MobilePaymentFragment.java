package digital.iam.ma.views.dashboard.payment.paymentmode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import digital.iam.ma.databinding.FragmentMobilePaymentBinding;
import digital.iam.ma.views.dashboard.DashboardActivity;
import digital.iam.ma.views.dashboard.home.HomeFragment;

public class MobilePaymentFragment extends Fragment {

    private FragmentMobilePaymentBinding fragmentBinding;
    private int position = 0;
    private String ref = "";

    public MobilePaymentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //assert getArguments() != null;
        if (getArguments() != null){
            position = getArguments().getInt("POSITION");
            ref = getArguments().getString("REF");
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentMobilePaymentBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentBinding.validateBtn.setOnClickListener(v -> {
            //Toast.makeText(getContext(), "Validate Btn", Toast.LENGTH_SHORT).show();
        });

        fragmentBinding.backImage.setOnClickListener(v -> {
            //requireActivity().onBackPressed();
            ((DashboardActivity) requireActivity()).replaceFragment(new HomeFragment(), "HOME");
        });
        fragmentBinding.ref.setText(ref);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardActivity) requireActivity()).showHideTabLayout(View.VISIBLE);
    }
}