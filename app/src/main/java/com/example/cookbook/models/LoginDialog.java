package com.example.cookbook.models;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cookbook.R;

public class LoginDialog {

    private Dialog dialog;
    private EditText name, password;
    private TextView registration;
    private Button enterButton;
    private CheckBox rememberMe;

    public TextView getRegistration() {
        return registration;
    }

    public LoginDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.login);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        name = dialog.findViewById(R.id.login_name);
        password = dialog.findViewById(R.id.login_password);
        enterButton = dialog.findViewById(R.id.enter);
        rememberMe = dialog.findViewById(R.id.remember_me);
        registration = dialog.findViewById(R.id.registration);
    }

    public void clear(){
        name.setText("");
        password.setText("");
    }

    public Dialog getDialog() {
        return dialog;
    }

    public EditText getName () {
        return name;
    }

    public EditText getPassword() {
        return password;
    }

    public Button getEnterButton() {
        return enterButton;
    }

    public CheckBox getRememberMe() {
        return rememberMe;
    }
}
