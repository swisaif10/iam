package digital.iam.ma.views.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import digital.iam.ma.R;
import digital.iam.ma.databinding.ActivityDashboardBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.listener.OnDialogButtonsClickListener;
import digital.iam.ma.models.logout.LogoutData;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.DashboardViewModel;
import digital.iam.ma.views.authentication.AuthenticationActivity;
import digital.iam.ma.views.authentication.offers.DiscoverOffersFragment;
import digital.iam.ma.views.base.BaseActivity;
import digital.iam.ma.views.dashboard.bundles.BundlesFragment;
import digital.iam.ma.views.dashboard.cart.CartFragment;
import digital.iam.ma.views.dashboard.home.HomeFragment;
import digital.iam.ma.views.dashboard.payment.PaymentFragment;
import digital.iam.ma.views.dashboard.profile.HelpFragment;
import digital.iam.ma.views.dashboard.profile.PersonalInformationFragment;
import digital.iam.ma.views.dashboard.services.ServicesFragment;

public class DashboardActivity extends BaseActivity {

    private ActivityDashboardBinding activityBinding;
    private ArrayList<String> names;
    private ArrayList<Integer> icons;
    private ArrayList<Fragment> fragments;
    private Boolean menuIsVisible = false;
    private PreferenceManager preferenceManager;
    private DashboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        viewModel.getLogoutLiveData().observe(this, this::handleLogoutData);

        init();
    }

    @Override
    public void onBackPressed() {
        if (menuIsVisible)
            closeSideMenu();
        else if (getSupportFragmentManager().getBackStackEntryCount() > 1)
            getSupportFragmentManager().popBackStack();
        else
            finish();
    }

    private void init() {
        names = new ArrayList<String>() {{
            add("Mon forfait");
            add("Mes\npaiements");
            add("Mes services");
            add("Modifier\nmon forfait");
        }};

        icons = new ArrayList<Integer>() {{
            add(R.drawable.ic_home);
            add(R.drawable.ic_paiments);
            add(R.drawable.ic_services);
            add(R.drawable.ic_forfait);
        }};

        fragments = new ArrayList<Fragment>() {{
            add(new HomeFragment());
            add(new PaymentFragment());
            add(new ServicesFragment());
            add(new BundlesFragment());
        }};

        for (int i = 0; i < names.size(); i++) {
            TabLayout.Tab tab = activityBinding.tabLayout.newTab();
            View view = tab.getCustomView() == null ? LayoutInflater.from(activityBinding.tabLayout.getContext()).inflate(R.layout.tab_item_layout, null) : tab.getCustomView();
            if (tab.getCustomView() == null) {
                tab.setCustomView(view);
            }
            TextView tabTextView = view.findViewById(R.id.title);
            tabTextView.setText(names.get(i));
            ImageView tabImageView = view.findViewById(R.id.icon);
            tabImageView.setImageResource(icons.get(i));
            activityBinding.tabLayout.addTab(tab);
        }

        activityBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                replaceFragment(fragments.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                replaceFragment(fragments.get(tab.getPosition()));
            }
        });

        addFragment(HomeFragment.newInstance(getIntent().getBooleanExtra("is_first_login", false)));

        activityBinding.profileBtn.setOnClickListener(v -> {
            activityBinding.closeMenu.setVisibility(View.VISIBLE);
            activityBinding.slideMenu.animate().translationX(0);
            menuIsVisible = true;
        });
        activityBinding.closeBtn.setOnClickListener(v -> closeSideMenu());

        activityBinding.closeMenu.setOnClickListener(v -> {
            if (menuIsVisible)
                closeSideMenu();
            else {
                activityBinding.notificationLayout.setVisibility(View.GONE);
                activityBinding.closeMenu.setVisibility(View.GONE);
            }
        });

        activityBinding.personalInfoBtn.setOnClickListener(v -> {
            closeSideMenu();
            addFragment(new PersonalInformationFragment());
        });

        activityBinding.helpBtn.setOnClickListener(v -> {
            closeSideMenu();
            addFragment(new HelpFragment());
        });

        activityBinding.offersBtn.setOnClickListener(v -> {
            closeSideMenu();
            addFragment(DiscoverOffersFragment.newInstance(true));
        });

        activityBinding.notificationBtn.setOnClickListener(v -> {
            activityBinding.closeMenu.setVisibility(View.VISIBLE);
            activityBinding.notificationLayout.setVisibility(View.VISIBLE);
            activityBinding.notificationLayout.setAlpha(0.0f);

            activityBinding.notificationLayout.animate()
                    .translationX(0)
                    .alpha(1.0f)
                    .setListener(null);
        });

        activityBinding.cartBtn.setOnClickListener(v -> addFragment(new CartFragment()));

        activityBinding.logoutBtn.setOnClickListener(v -> {
            closeSideMenu();
            Utilities.showLogoutDialog(this, "Etes vous sûr de vouloir vous déconnecter ?", new OnDialogButtonsClickListener() {
                @Override
                public void firstChoice() {
                    logout();
                }

                @Override
                public void secondChoice() {

                }
            });
        });
    }

    private void closeSideMenu() {
        activityBinding.slideMenu.animate().translationX(activityBinding.slideMenu.getWidth());
        menuIsVisible = false;
        activityBinding.closeMenu.setVisibility(View.GONE);
    }

    private void logout() {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.logout(preferenceManager.getValue(Constants.TOKEN, ""), "fr");
    }

    private void handleLogoutData(Resource<LogoutData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                preferenceManager.clearValue(Constants.IS_LOGGED_IN);
                startActivity(new Intent(DashboardActivity.this, AuthenticationActivity.class));
                finishAffinity();
                break;
            case INVALID_TOKEN:
                Utilities.showErrorPopupWithClick(this, responseData.data.getHeader().getMessage(), v -> {
                    startActivity(new Intent(this, AuthenticationActivity.class));
                    finishAffinity();
                });
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
    }

    public void selectPaymentsTab() {
        activityBinding.tabLayout.getTabAt(1).select();
    }
}