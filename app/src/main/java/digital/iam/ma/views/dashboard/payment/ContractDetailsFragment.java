package digital.iam.ma.views.dashboard.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import digital.iam.ma.databinding.FragmentContractDetailsBinding;
import digital.iam.ma.utilities.Utilities;

public class ContractDetailsFragment extends Fragment {

    private FragmentContractDetailsBinding fragmentBinding;

    public ContractDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentBinding = FragmentContractDetailsBinding.inflate(inflater, container, false);
        return fragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        fragmentBinding.suspendContractBtn.setOnClickListener(v -> Utilities.showSuspendContractDialog(requireContext()));

        fragmentBinding.resendCodePUKBtn.setOnClickListener(v -> Utilities.showResendCodePUKDialog(requireContext()));
    }
}