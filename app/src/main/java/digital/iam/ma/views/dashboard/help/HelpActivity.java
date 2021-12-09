package digital.iam.ma.views.dashboard.help;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import digital.iam.ma.R;
import digital.iam.ma.databinding.ActivityHelpBinding;
import digital.iam.ma.datamanager.sharedpref.PreferenceManager;
import digital.iam.ma.models.help.FAQ;
import digital.iam.ma.models.help.HelpData;
import digital.iam.ma.models.help.Item;
import digital.iam.ma.utilities.Constants;
import digital.iam.ma.utilities.Resource;
import digital.iam.ma.utilities.Utilities;
import digital.iam.ma.viewmodels.AccountViewModel;
import digital.iam.ma.views.base.BaseActivity;

public class HelpActivity extends BaseActivity {

    private ActivityHelpBinding activityBinding;
    private AccountViewModel viewModel;
    private PreferenceManager preferenceManager;
    private ArrayList<Item> itemlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_help);

        viewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        viewModel.getHelpLiveData().observe(this, this::handleHelpData);

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        getHelp();
    }

    private void getHelp() {
        activityBinding.loader.setVisibility(View.VISIBLE);
        viewModel.getHelp();
    }

    private void handleHelpData(Resource<HelpData> responseData) {
        activityBinding.loader.setVisibility(View.GONE);
        switch (responseData.status) {
            case SUCCESS:
                init(responseData.data.getItems());
                break;
            case ERROR:
                Utilities.showErrorPopup(this, responseData.message);
                break;
        }
    }

    private void init(List<Item> items) {
        //initItems();
        if (preferenceManager.getValue(Constants.LANGUAGE, "fr").equalsIgnoreCase("ar"))
            activityBinding.backImage.setRotation(180f);
        activityBinding.backBtn.setOnClickListener(v -> finish());
        activityBinding.helpRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activityBinding.helpRecycler.setAdapter(new HelpAdapter(this, items));
        activityBinding.helpRecycler.setNestedScrollingEnabled(false);
    }



}