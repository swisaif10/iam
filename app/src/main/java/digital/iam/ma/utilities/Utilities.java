package digital.iam.ma.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import digital.iam.ma.R;
import digital.iam.ma.listener.OnConfirmClickListener;
import digital.iam.ma.listener.OnDialogButtonsClickListener;
import digital.iam.ma.listener.OnRechargeSelectedListener;
import digital.iam.ma.listener.OnRenew;
import digital.iam.ma.listener.OnResetPasswordDialogClickListener;
import digital.iam.ma.listener.OnUpdatePasswordDialogClickListener;
import digital.iam.ma.models.payment.PaymentData;
import digital.iam.ma.models.recharge.RechargeItem;
import digital.iam.ma.models.recharge.RechargeListResponse;
import digital.iam.ma.models.recharge.RechargeSubItem;

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

    static void showUpdateDialog(Context context, String message, String status, String link, OnDialogButtonsClickListener onDialogButtonsClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null, false);
        Button update = view.findViewById(R.id.updateBtn);
        Button cancel = view.findViewById(R.id.cancelBtn);
        TextView msg = view.findViewById(R.id.message);

        msg.setText(Html.fromHtml(message));

        if (link.isEmpty() || link == "null")
            update.setEnabled(false);

        if (status.equalsIgnoreCase("blocked"))
            cancel.setVisibility(View.GONE);

        update.setOnClickListener(v -> {
            dialog.dismiss();
            onDialogButtonsClickListener.firstChoice();
        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            onDialogButtonsClickListener.secondChoice();
        });
        dialog.setContentView(view);
        dialog.show();
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

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ok.setEnabled(isNotEmpty(email));
            }
        });

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

    static void showPaymentDialog(Context context, PaymentData responsePaymentData, OnRenew onPurchase) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_payment_mode, null, false);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        RadioButton radioButton0 = view.findViewById(R.id.radio0);
        RadioButton radioButton1 = view.findViewById(R.id.radio1);
        RadioButton radioButton2 = view.findViewById(R.id.radio2);
        Button confirm = view.findViewById(R.id.confirmBtn);
        RelativeLayout modePaymentLayout = view.findViewById(R.id.layoutModePayment);
        final int[] selectedode = new int[1];

        if (responsePaymentData != null) {
            if (responsePaymentData.getCmi()) {
                radioButton0.setVisibility(View.VISIBLE);
            }
            if (responsePaymentData.getFatourati()) {
                radioButton1.setVisibility(View.VISIBLE);
            }
            if (responsePaymentData.getMtcash()) {
                radioButton2.setVisibility(View.VISIBLE);
            }
        } else {
            modePaymentLayout.setVisibility(View.GONE);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkedRadioId = radioGroup.getCheckedRadioButtonId();

                radioButton0.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_off));
                radioButton1.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_off));
                radioButton2.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_off));

                ColorStateList colorStateList = new ColorStateList(
                        new int[][]
                                {
                                        new int[]{-android.R.attr.state_enabled}, // Disabled
                                        new int[]{android.R.attr.state_enabled}   // Enabled
                                },
                        new int[]
                                {
                                        ContextCompat.getColor(context, R.color.grey), // disabled
                                        ContextCompat.getColor(context, R.color.radio_tint)   // enabled
                                }
                );
                radioButton0.setButtonTintList(colorStateList);
                radioButton1.setButtonTintList(colorStateList);
                radioButton2.setButtonTintList(colorStateList);

                if (checkedRadioId == radioButton0.getId()) {
                    selectedode[0] = 0;
                    radioButton0.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_on));
                } else if (checkedRadioId == radioButton1.getId()) {
                    selectedode[0] = 1;
                    radioButton1.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_on));
                } else if (checkedRadioId == radioButton2.getId()) {
                    selectedode[0] = 2;
                    radioButton2.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_on));
                }
            }
        });
        ConstraintLayout container = view.findViewById(R.id.container);

        confirm.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "BUY";

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (responsePaymentData != null)
                    onPurchase.onPurchase(selectedode[0]);
                else
                    onPurchase.onPurchase(3);
            }
        });

        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    static void showRechargeDialog(Context context, String msisdn, RechargeListResponse response, PaymentData responsePaymentData, OnRechargeSelectedListener onRechargeSelectedListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.buy_recharge_dialog, null, false);
        Button buy = view.findViewById(R.id.buyBtn);
        ConstraintLayout container = view.findViewById(R.id.container);
        TextView msisdnTv = view.findViewById(R.id.msisdn);
        AutoCompleteTextView rechargeDropDown = view.findViewById(R.id.rechargeDropDown);
        AutoCompleteTextView rechargeChildrenDropDown = view.findViewById(R.id.rechargeChildrenDropDown);

        LinearLayout modePaymentLayout = view.findViewById(R.id.modePaymentLayout);
        RadioGroup modePaymentRg = view.findViewById(R.id.radioGroup);
        RadioButton radioButton0 = view.findViewById(R.id.radio0);
        RadioButton radioButton1 = view.findViewById(R.id.radio1);
        RadioButton radioButton2 = view.findViewById(R.id.radio2);

        modePaymentLayout.setVisibility(View.GONE);
        rechargeChildrenDropDown.setVisibility(View.GONE);
        final RechargeItem[] rechargeItem = new RechargeItem[1];
        final RechargeSubItem[] rechargeSubItem = new RechargeSubItem[1];
        final String[] price = new String[1];

        msisdnTv.setText(msisdn);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_dropdown_item_layout, response.getRechargesNames());
        rechargeDropDown.setAdapter(adapter);
        rechargeDropDown.setOnTouchListener((v, event) -> {
            rechargeDropDown.showDropDown();
            return false;
        });

        rechargeDropDown.setOnItemClickListener((parent, view1, position, id) -> {
            rechargeItem[0] = response.getRecharges().get(position);
            ArrayAdapter<String> citiesAdapter = null;
            if (rechargeItem[0].getType().getRechargesNames() != null)
                citiesAdapter = new ArrayAdapter<>(context, R.layout.custom_dropdown_item_layout, rechargeItem[0].getType().getRechargesNames());
            rechargeChildrenDropDown.setAdapter(citiesAdapter);
            rechargeChildrenDropDown.setOnTouchListener((v, event) -> {
                rechargeChildrenDropDown.showDropDown();
                return false;
            });
            rechargeChildrenDropDown.setText("");
            if (citiesAdapter != null && !citiesAdapter.isEmpty()) {
                rechargeSubItem[0] = rechargeItem[0].getType().getChildren().get(0);
                rechargeChildrenDropDown.setText(citiesAdapter.getItem(0));
                citiesAdapter.getFilter().filter(null);
            }

            rechargeChildrenDropDown.setVisibility(rechargeItem[0].getType().getChildren() == null ? View.GONE : View.VISIBLE);
            modePaymentLayout.setVisibility(rechargeItem[0].getType().getChildren() == null ? View.GONE : View.VISIBLE);
        });

        rechargeChildrenDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rechargeSubItem[0] = rechargeItem[0].getType().getChildren().get(position);
                modePaymentRg.setVisibility(View.VISIBLE);
            }
        });

        if (responsePaymentData != null) {
            if (responsePaymentData.getCmi()) {
                radioButton0.setVisibility(View.VISIBLE);
            }
            if (responsePaymentData.getFatourati()) {
                radioButton1.setVisibility(View.VISIBLE);
            }
            if (responsePaymentData.getMtcash()) {
                radioButton2.setVisibility(View.VISIBLE);
            }
        } else {
            modePaymentLayout.setVisibility(View.GONE);
        }


        final int[] selectedode = new int[1];
        modePaymentRg.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            int checkedRadioId = group.getCheckedRadioButtonId();

            radioButton0.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_off));
            radioButton1.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_off));
            radioButton2.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_off));

            ColorStateList colorStateList = new ColorStateList(
                    new int[][]
                            {
                                    new int[]{-android.R.attr.state_enabled}, // Disabled
                                    new int[]{android.R.attr.state_enabled}   // Enabled
                            },
                    new int[]
                            {
                                    ContextCompat.getColor(context, R.color.grey), // disabled
                                    ContextCompat.getColor(context, R.color.radio_tint)   // enabled
                            }
            );
            radioButton0.setButtonTintList(colorStateList);
            radioButton1.setButtonTintList(colorStateList);
            radioButton2.setButtonTintList(colorStateList);

            if (checkedRadioId == radioButton0.getId()) {
                selectedode[0] = 0;
                radioButton0.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_on));
            } else if (checkedRadioId == radioButton1.getId()) {
                selectedode[0] = 1;
                radioButton1.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_on));
            } else if (checkedRadioId == radioButton2.getId()) {
                selectedode[0] = 2;
                radioButton2.setBackground(ContextCompat.getDrawable(context, R.drawable.radio_on));
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "BUY";

            @Override
            public void onClick(View v) {
                if (!rechargeDropDown.getText().toString().isEmpty()) {
                    if (modePaymentRg.getCheckedRadioButtonId() != -1) {
                        dialog.dismiss();
                        if (rechargeSubItem[0] != null) {
                            if (responsePaymentData != null) {
                                Log.d("RECHARGE", "onClick: " + rechargeSubItem[0].getSku());
                                onRechargeSelectedListener.onPurchaseRecharge(rechargeSubItem[0].getSku(), selectedode[0]);
                            } else
                                onRechargeSelectedListener.onPurchaseRecharge(rechargeSubItem[0].getSku(), 3);
                        } else {
                            onRechargeSelectedListener.onPurchaseRecharge(rechargeItem[0].getType().getSku(), selectedode[0]);
                        }
                    } else {
                        showErrorPopup(context, "Veuillez séléctionner une mode de paiment");
                    }
                } else {
                    showErrorPopup(context, "Sélectionner une recharge svp");
                }
            }
        });

        /*

         */

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

    static void showContractDialog(Context context, String title, String message, String type, OnConfirmClickListener onConfirmClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_contract_options, null, false);
        TextView titleTV = view.findViewById(R.id.title);
        TextView messageTV = view.findViewById(R.id.message);
        EditText code = view.findViewById(R.id.code);
        EditText reason = view.findViewById(R.id.motif);
        Button cancel = view.findViewById(R.id.cancelBtn);
        Button confirm = view.findViewById(R.id.confirmBtn);
        TextView close = view.findViewById(R.id.closeBtn);
        ConstraintLayout container = view.findViewById(R.id.container);

        titleTV.setText(title);
        messageTV.setText(message);
        switch (type) {
            case "suspend":
                reason.setVisibility(View.GONE);
                break;
            case "end":
                reason.setVisibility(View.GONE);
                code.setVisibility(View.GONE);
                break;
            case "resend_puk":
                //code.setHint(R.string.phone_number_hint);
                //code.setVisibility(View.VISIBLE);
                confirm.setText(R.string.resend_btn_label);
                //code.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                //code.setTransformationMethod(new NumericKeyBoardTransformationMethod());
                //code.setTypeface(ResourcesCompat.getFont(context, R.font.avenir_book));
                break;
            case "change_sim":
                cancel.setText(context.getResources().getString(R.string.no_hint));
                confirm.setText(context.getResources().getString(R.string.yes_hint));
                break;
        }
        cancel.setOnClickListener(v -> dialog.dismiss());
        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            onConfirmClickListener.onConfirmClick("", "", type);
            /*
            if (reason.getVisibility() == View.GONE && code.getVisibility() == View.GONE) {
                dialog.dismiss();
                onConfirmClickListener.onConfirmClick("", "", type);
            } else if (reason.getVisibility() == View.VISIBLE && isNotEmpty(reason) && code.getVisibility() == View.GONE) {
                dialog.dismiss();
                onConfirmClickListener.onConfirmClick(reason.getText().toString(), "", type);
            } else if (code.getVisibility() == View.VISIBLE && isNotEmpty(code) && reason.getVisibility() == View.GONE) {
                dialog.dismiss();
                onConfirmClickListener.onConfirmClick("", code.getText().toString(), type);
            } else if (reason.getVisibility() == View.VISIBLE && isNotEmpty(reason) && code.getVisibility() == View.VISIBLE && isNotEmpty(code)) {
                dialog.dismiss();
                onConfirmClickListener.onConfirmClick(reason.getText().toString(), code.getText().toString(), type);
            }*/
        });
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

    static void showErrorPopupAndFinnish(Context context, String message, View.OnClickListener view1) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.server_error_dialog, null, false);
        Button ok = view.findViewById(R.id.okBtn);
        TextView msg = view.findViewById(R.id.message);
        ConstraintLayout container = view.findViewById(R.id.container);

        msg.setText(message);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onClick(v);
            }
        });
        //ok.setOnClickListener(v -> dialog.dismiss(););
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    static void showCodePuk(Context context,String title1, String message) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.new_code_puk, null, false);
        Button ok = view.findViewById(R.id.okBtn);
        TextView msg = view.findViewById(R.id.message);
        TextView title = view.findViewById(R.id.title);
        ConstraintLayout container = view.findViewById(R.id.container);

        msg.setText(message);
        title.setText(title1);

        ok.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    static void showErrorPopupWithClick(Context context, String message, View.OnClickListener onClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.server_error_dialog, null, false);
        Button ok = view.findViewById(R.id.okBtn);
        TextView msg = view.findViewById(R.id.message);
        ConstraintLayout container = view.findViewById(R.id.container);

        msg.setText(message);

        ok.setOnClickListener(v -> {
            onClickListener.onClick(v);
            dialog.dismiss();
        });
        container.setOnClickListener(v -> {
            onClickListener.onClick(v);
            dialog.dismiss();
        });
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

    static void showConfirmRenewPopup(Context context, View.OnClickListener onClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.confirm_renew_dialog, null, false);
        Button cancel = view.findViewById(R.id.cancelBtn);
        Button confirm = view.findViewById(R.id.confirmBtn);
        ConstraintLayout container = view.findViewById(R.id.container);

        cancel.setOnClickListener(v -> dialog.dismiss());
        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            onClickListener.onClick(v);
        });
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    static void showConfirmDialog(Context context, String message, View.OnClickListener onClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.confirm_renew_dialog, null, false);
        Button cancel = view.findViewById(R.id.cancelBtn);
        Button confirm = view.findViewById(R.id.confirmBtn);
        TextView message1 = view.findViewById(R.id.message);
        ConstraintLayout container = view.findViewById(R.id.container);
        message1.setText(message);
        cancel.setOnClickListener(v -> dialog.dismiss());
        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            onClickListener.onClick(v);
        });
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    static void showRootDialog(Context context, String message, View.OnClickListener onClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.root_detection_dialog, null, false);
        Button confirm = view.findViewById(R.id.confirmBtn);
        TextView message1 = view.findViewById(R.id.message);
        ConstraintLayout container = view.findViewById(R.id.container);
        message1.setText(message);
        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            onClickListener.onClick(v);
        });
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();
    }

    static void showLogoutDialog(Context context, String message, OnDialogButtonsClickListener onDialogButtonsClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.logout_dialog, null, false);
        Button confirm = view.findViewById(R.id.confirmBtn);
        Button cancel = view.findViewById(R.id.cancelBtn);
        TextView msg = view.findViewById(R.id.message);

        msg.setText(Html.fromHtml(message));

        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            onDialogButtonsClickListener.firstChoice();
        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            onDialogButtonsClickListener.secondChoice();
        });
        dialog.setContentView(view);
        dialog.show();
    }

    static Boolean isNotEmpty(EditText editText) {
        return !editText.getText().toString().equalsIgnoreCase("");
    }
}