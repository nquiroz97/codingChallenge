package com.datechnologies.androidtest.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.datechnologies.androidtest.MainActivity;

public class LoginResponseDialog extends DialogFragment {
    public LoginResponseDialog() {

    }

    public static LoginResponseDialog newInstance(String displayText, boolean isSuccess) {
        LoginResponseDialog dialogFragment = new LoginResponseDialog();

        // Supply message as an argument.
        Bundle args = new Bundle();
        args.putString("message", displayText);
        args.putBoolean("success", isSuccess);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        assert getArguments() != null;
        String displayText = getArguments().getString("message");
        boolean successfulLogin = getArguments().getBoolean("success");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(displayText)
                .setPositiveButton("OK", (dialog, id) -> handleButtonPress(successfulLogin));

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void handleButtonPress(boolean successfulLogin){
        dismiss();
        if(successfulLogin){
            Intent intent = new Intent(this.getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

}
