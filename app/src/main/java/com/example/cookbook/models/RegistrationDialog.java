package com.example.cookbook.models;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.cookbook.R;

public class RegistrationDialog {

    private Dialog dialog;
    private EditText name, email, password, confirmPassword;
    private Button registrationButton;

    public RegistrationDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.registration);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        name = dialog.findViewById(R.id.registration_name);
        email = dialog.findViewById(R.id.registration_email);
        password = dialog.findViewById(R.id.registration_password);
        confirmPassword = dialog.findViewById(R.id.registration_confirm_password);
        registrationButton = dialog.findViewById(R.id.register_now);
    }

    public void clear(){
        name.setText("");
        email.setText("");
        password.setText("");
        confirmPassword.setText("");
    }

    public Dialog getDialog() {
        return dialog;
    }

    public EditText getEmail() {
        return email;
    }

    public EditText getPassword() {
        return password;
    }

    public EditText getName() {
        return name;
    }

    public EditText getConfirmPassword() {
        return confirmPassword;
    }

    public Button getRegistrationButton() {
        return registrationButton;
    }
}
