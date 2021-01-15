package com.github.guqt178.dialogs.easy;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.text.TextUtils;

import com.github.guqt178.dialogs.R;


public class DialogMaker {

	private static EasyProgressDialog progressDialog;

    public static EasyProgressDialog showProgressDialog(Context context, String message) {
        return showProgressDialog(context,0.0f, message, true);
    }

	public static EasyProgressDialog showProgressDialog(Context context,
                                                        float amount,
                                                        String message,
                                                        boolean cancelable) {
		return showProgressDialog(context,amount,  message, cancelable, null);
	}
	
	public static EasyProgressDialog showProgressDialog(Context context,
                                                        float amount,
                                                        String message,
                                                        boolean canCancelable,
                                                        OnCancelListener listener) {
		if (progressDialog == null) {
			progressDialog = new EasyProgressDialog(context, message);
		} else if (progressDialog.getContext() != context) {
			// maybe existing dialog is running in a destroyed activity cotext
			// we should recreate one
			dismissProgressDialog();
			progressDialog = new EasyProgressDialog(context, message);
		}

		progressDialog.setCancelable(canCancelable);
		progressDialog.setOnCancelListener(listener);
        progressDialog.setAnimStyle(R.style.IOSDialogInAndOutAnim);
        progressDialog.getWindow().setDimAmount(amount);
		progressDialog.show();

		return progressDialog;
	}
	
	public static void dismissProgressDialog() {
		if (null == progressDialog) {
			return;
		}
		if (progressDialog.isShowing()) {
			try {
				progressDialog.dismiss();
				progressDialog = null;
			} catch (Exception e) {
				// maybe we catch IllegalArgumentException here.
			}

		}

	}
	
	public static void setMessage(String message) {
		if (null != progressDialog && progressDialog.isShowing()
				&& !TextUtils.isEmpty(message)) {
			progressDialog.setMessage(message);
		}
	}

	public static void updateLoadingMessage(String message) {
		if (null != progressDialog && progressDialog.isShowing()
				&& !TextUtils.isEmpty(message)) {
			progressDialog.updateLoadingMessage(message);
		}
	}
	
	public static boolean isShowing() {
		return (progressDialog != null && progressDialog.isShowing());
	}
}
