package digital.iam.ma.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import digital.iam.ma.R;
import digital.iam.ma.listener.OnResetPasswordDialogClickListener;
import digital.iam.ma.listener.OnUpdatePasswordDialogClickListener;

public interface Utilities {

    static void hideSoftKeyboard(Context context, View view) {
        if (context == null || view == null) {
            return;
        }
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }

    static void showResetPasswordDialog(Context context, OnResetPasswordDialogClickListener onResetPasswordDialogClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.recover_password_dialog, null, false);
        Button ok = view.findViewById(R.id.okBtn);
        TextView close = view.findViewById(R.id.closeBtn);
        EditText email = view.findViewById(R.id.email);
        LinearLayout container = view.findViewById(R.id.container);

        ok.setOnClickListener(v -> {
            dialog.dismiss();
            onResetPasswordDialogClickListener.onResetClick(email.getText().toString());
        });
        close.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> hideSoftKeyboard(context, view));
        dialog.setContentView(view);
        dialog.show();
    }

    static void showActivateSimDialog(Context context, View.OnClickListener onClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.activate_sim_dialog, null, false);
        Button yes = view.findViewById(R.id.yesBtn);
        Button no = view.findViewById(R.id.noBtn);
        ConstraintLayout container = view.findViewById(R.id.container);

        yes.setOnClickListener(v -> {
            onClickListener.onClick(view);
            dialog.dismiss();
        });
        no.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    static void showRechargeDialog(Context context, View.OnClickListener onClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.buy_recharge_dialog, null, false);
        Button buy = view.findViewById(R.id.buyBtn);
        ConstraintLayout container = view.findViewById(R.id.container);

        buy.setOnClickListener(v -> {
            dialog.dismiss();
            onClickListener.onClick(v);
        });
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    static void showPurchaseDialog(Context context, View.OnClickListener onClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.confirm_purchase_dialog, null, false);
        Button yes = view.findViewById(R.id.confirmBtn);
        Button no = view.findViewById(R.id.cancelBtn);
        TextView close = view.findViewById(R.id.closeBtn);
        ConstraintLayout container = view.findViewById(R.id.container);

        yes.setOnClickListener(v -> {
            dialog.dismiss();
            onClickListener.onClick(v);
        });
        no.setOnClickListener(v -> dialog.dismiss());
        close.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    static void showUpdatePasswordDialog(Context context, OnUpdatePasswordDialogClickListener onUpdatePasswordDialogClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.update_password_dialog, null, false);
        Button cancel = view.findViewById(R.id.cancelBtn);
        Button save = view.findViewById(R.id.saveBtn);
        TextView close = view.findViewById(R.id.closeBtn);
        ImageView showOldPassword = view.findViewById(R.id.showOldPassword);
        ImageView showNewPassword = view.findViewById(R.id.showNewPassword);
        ImageView showConfirmedPassword = view.findViewById(R.id.showConfirmedPassword);
        EditText oldPassword = view.findViewById(R.id.oldPassword);
        EditText newPassword = view.findViewById(R.id.newPassword);
        EditText confirmPassword = view.findViewById(R.id.confirmPassword);
        LinearLayout container = view.findViewById(R.id.container);

        cancel.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {
            dialog.dismiss();
            if (newPassword.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString()))
                onUpdatePasswordDialogClickListener.onUpdateButtonClick(oldPassword.getText().toString(), newPassword.getText().toString());
        });
        close.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> hideSoftKeyboard(context, view));
        showOldPassword.setOnClickListener(v -> {
            if (oldPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                oldPassword.setTransformationMethod(null);
            } else {
                oldPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
            oldPassword.setSelection(oldPassword.getText().length());
        });
        showNewPassword.setOnClickListener(v -> {
            if (newPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                newPassword.setTransformationMethod(null);
            } else {
                newPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
            newPassword.setSelection(newPassword.getText().length());
        });
        showConfirmedPassword.setOnClickListener(v -> {
            if (confirmPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                confirmPassword.setTransformationMethod(null);
            } else {
                confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
            confirmPassword.setSelection(confirmPassword.getText().length());
        });
        dialog.setContentView(view);
        dialog.show();
    }

    static void showSuspendContractDialog(Context context) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.suspend_contract_dialog, null, false);
        Button cancel = view.findViewById(R.id.cancelBtn);
        Button confirm = view.findViewById(R.id.confirmBtn);
        TextView close = view.findViewById(R.id.closeBtn);
        ConstraintLayout container = view.findViewById(R.id.container);

        cancel.setOnClickListener(v -> dialog.dismiss());
        confirm.setOnClickListener(v -> dialog.dismiss());
        close.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    static void showResendCodePUKDialog(Context context) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.resend_code_puk_dialog, null, false);
        Button ok = view.findViewById(R.id.okBtn);
        TextView close = view.findViewById(R.id.closeBtn);
        LinearLayout container = view.findViewById(R.id.container);

        ok.setOnClickListener(v -> dialog.dismiss());
        close.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> hideSoftKeyboard(context, view));
        dialog.setContentView(view);
        dialog.show();
    }

    static void showConfirmRechargeDialog(Context context) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.confirm_recharge_dialog, null, false);
        Button ok = view.findViewById(R.id.okBtn);
        TextView close = view.findViewById(R.id.closeBtn);
        EditText code = view.findViewById(R.id.code);
        LinearLayout container = view.findViewById(R.id.container);

        code.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        code.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        ok.setOnClickListener(v -> dialog.dismiss());
        close.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> hideSoftKeyboard(context, view));
        dialog.setContentView(view);
        dialog.show();
    }

    static void showBundleDetailsDialog(Context context) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.bundle_details_dialog, null, false);
        TextView close = view.findViewById(R.id.closeBtn);
        ConstraintLayout container = view.findViewById(R.id.container);

        close.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    static void showErrorPopup(Context context, String message) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.server_error_dialog, null, false);
        Button ok = view.findViewById(R.id.okBtn);
        TextView msg = view.findViewById(R.id.message);
        ConstraintLayout container = view.findViewById(R.id.container);

        msg.setText(message);

        ok.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    static void showBiometricsPromptPopup(Context context, String message, View.OnClickListener onClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.prompt_biometrics_dialog, null, false);
        Button cancel = view.findViewById(R.id.cancelBtn);
        Button settings = view.findViewById(R.id.settingsBtn);
        TextView msg = view.findViewById(R.id.message);
        ConstraintLayout container = view.findViewById(R.id.container);

        msg.setText(message);

        cancel.setOnClickListener(v -> dialog.dismiss());
        settings.setOnClickListener(onClickListener);
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }
}
