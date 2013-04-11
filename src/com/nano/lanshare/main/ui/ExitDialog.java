
package com.nano.lanshare.main.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.nano.lanshare.R;
import com.nano.lanshare.main.BaseActivity;

public class ExitDialog extends DialogFragment {
    public static ExitDialog newInstance() {
        return new ExitDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.notice)
                .setMessage(R.string.exit_warning)
                .setPositiveButton(R.string.exit_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((BaseActivity) getActivity()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton(R.string.exit_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((BaseActivity) getActivity()).doNegativeClick();
                            }
                        }
                )
                .create();
    }
}
