package com.gipl.notifyme.ui.leaveconfirm;

import android.content.Context;
import android.text.Html;
import android.view.View;

import com.gipl.notifyme.R;
import com.gipl.notifyme.databinding.DialogLeaveConfrimationBinding;
import com.gipl.notifyme.ui.base.BaseDialog;
import com.gipl.notifyme.ui.model.LeaveFor;

import java.text.DecimalFormat;
import java.util.Locale;

public class LeaveConfirmationDialog extends BaseDialog<DialogLeaveConfrimationBinding, LeaveConfirmationViewModel> {
    private LeaveConfirmationViewModel viewModel;
    private Context context;

    public interface LeaveConfirmationListener {
        void onCorrect();
    }

    LeaveConfirmationListener leaveConfirmationListener;

    public LeaveConfirmationDialog(Context context, LeaveConfirmationListener leaveConfirmationListener) {
        this.context = context;
        this.leaveConfirmationListener = leaveConfirmationListener;
        viewModel = new LeaveConfirmationViewModel();
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_leave_confrimation;
    }

    @Override
    public LeaveConfirmationViewModel getDialogViewModel() {
        return viewModel;
    }

    @Override
    public int getDialogBindingVariable() {
        return R.layout.dialog_leave_confrimation;
    }

    public void show(long numberOfLeaveDays,
                     double finalNoOfLeaveDays,
                     String fromDate,
                     String toDate,
                     String fromSession,
                     String reason,
                     String leaveType) {
        init(context);
        setCancelable(false);
        if (numberOfLeaveDays > 1) {
            DecimalFormat decimalFormat = new DecimalFormat("0.#####");
            String displayDays = decimalFormat.format(finalNoOfLeaveDays);
            getViewBinding().tvTitle.setText(context.getString(R.string.title_leave_for, displayDays));
            getViewBinding().tvMsg.setText(context.getString(R.string.msg_multiple_days_leave, displayDays,
                    fromDate, toDate));
            getViewBinding().tvLeaveDays.setText(context.getString(R.string.days,displayDays));
            getViewBinding().tvLeavePeriod.setText(String.format(Locale.US, "%s  %s  %s", fromDate, context.getString(R.string.lbl_to), toDate));
        } else {
            getViewBinding().tvTitle.setText(R.string.title_leave_half_day);
            getViewBinding().tvMsg.setText(context.getString(R.string.msg_single_day_leave,
                    fromDate, fromSession));
            getViewBinding().tvLeaveDays.setText(fromSession);
            getViewBinding().tvLeavePeriod.setText(fromDate);
        }

        getViewBinding().tvReason.setText(reason);
        getViewBinding().tvLeaveType.setText(leaveType);
        getViewBinding().btnCorrect.setOnClickListener(v -> {
            leaveConfirmationListener.onCorrect();
            dismissDialog();
        });
        getViewBinding().btnWrong.setOnClickListener(v -> dismissDialog());
        getViewBinding().ivCancel.setOnClickListener(v -> dismissDialog());
    }
}
