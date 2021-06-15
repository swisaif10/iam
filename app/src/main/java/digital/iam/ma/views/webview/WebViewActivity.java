package digital.iam.ma.views.webview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.net.URI;
import java.net.URISyntaxException;

import digital.iam.ma.R;
import digital.iam.ma.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {

    private ActivityWebViewBinding activityBinding;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);

        if (getIntent().hasExtra("url"))
            url = getIntent().getStringExtra("url");

        init();
    }

    private void init() {
        activityBinding.closeWebViewBtn.setOnClickListener(v -> finish());
        activityBinding.webView.getSettings().setJavaScriptEnabled(true);
        activityBinding.webView.loadUrl("https://docs.google.com/viewer?embedded=true&url=" + url);

        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            activityBinding.webViewUrl.setText(domain);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}