package digital.iam.ma.views.dashboard.bundles;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import digital.iam.ma.R;
import digital.iam.ma.databinding.FragmentBundlesBinding;
import digital.iam.ma.models.BundleItem;
import digital.iam.ma.utilities.Utilities;

public class BundlesFragment extends Fragment {

    private FragmentBundlesBinding fragmentBinding;

    public BundlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        init();
    }

    private void init() {

        ArrayList<BundleItem> list1 = new ArrayList<BundleItem>() {{
            add(new BundleItem("12GO", "59 dh / mois", false));
            add(new BundleItem("20GO", "99 dh / mois", true));
            add(new BundleItem("30GO", "159 dh / mois", false));
            add(new BundleItem("50GO", "199 dh / mois", false));
        }};

        ArrayList<BundleItem> list2 = new ArrayList<BundleItem>() {{
            add(new BundleItem("30GO", "220 dh / mois", false));
            add(new BundleItem("12GO", "220 dh / mois", true));
            add(new BundleItem("14GO", "220 dh / mois", false));
            add(new BundleItem("30GO", "220 dh / mois", false));
        }};

        ArrayList<BundleItem> list3 = new ArrayList<BundleItem>() {{
            add(new BundleItem("10GO", "", false));
            add(new BundleItem("20GO", "", true));
            add(new BundleItem("30GO", "", false));
            add(new BundleItem("50GO", "", false));
            add(new BundleItem("10GO", "", false));
            add(new BundleItem("20GO", "", false));
            add(new BundleItem("30GO", "", false));
            add(new BundleItem("50GO", "", false));
        }};

        fragmentBinding.bundlesRecycler.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        fragmentBinding.bundlesRecycler.setAdapter(new BundlesAdapter(requireContext(), list1));
        fragmentBinding.bundlesRecycler.setNestedScrollingEnabled(false);

        fragmentBinding.bundle1Btn.setOnClickListener(v -> {
            fragmentBinding.bundle1Btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey1)));
            fragmentBinding.bundle2Btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey5)));
            fragmentBinding.bundle3Btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey5)));


            fragmentBinding.bundlesRecycler.setAdapter(new BundlesAdapter(requireContext(), list1));
        });
        fragmentBinding.bundle2Btn.setOnClickListener(v -> {
            fragmentBinding.bundle1Btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey5)));
            fragmentBinding.bundle2Btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey1)));
            fragmentBinding.bundle3Btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey5)));

            fragmentBinding.bundlesRecycler.setAdapter(new BundlesAdapter(requireContext(), list2));
        });
        fragmentBinding.bundle3Btn.setOnClickListener(v -> {
            fragmentBinding.bundle1Btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey5)));
            fragmentBinding.bundle2Btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey5)));
            fragmentBinding.bundle3Btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey1)));

            fragmentBinding.bundlesRecycler.setAdapter(new BundlesAdapter(requireContext(), list3));
        });

        fragmentBinding.changeBundleBtn.setOnClickListener(v -> Utilities.showPurchaseDialog(requireContext(), v1 -> {
            fragmentBinding.bundle1Btn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.grey1)));
            fragmentBinding.bundlesList.setAlpha(1.0f);
            fragmentBinding.bundlesDetails.setAlpha(1.0f);
        }));

        fragmentBinding.bundleDetailsBtn.setOnClickListener(v -> Utilities.showBundleDetailsDialog(requireContext()));

        /*fragmentBinding.limitedBtn.setOnClickListener(v -> {
            fragmentBinding.limitedBtn.setBackgroundResource(R.drawable.selected_bundle_background);
            fragmentBinding.unlimitedBtn.setBackgroundResource(R.drawable.unselected_bundle_background);
        });

        fragmentBinding.unlimitedBtn.setOnClickListener(v -> {
            fragmentBinding.limitedBtn.setBackgroundResource(R.drawable.unselected_bundle_background);
            fragmentBinding.unlimitedBtn.setBackgroundResource(R.drawable.selected_bundle_background);
        });*/
    }
}