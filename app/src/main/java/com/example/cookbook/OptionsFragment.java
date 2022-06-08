package com.example.cookbook;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import com.example.cookbook.databinding.FragmentOptionsBinding;


public class OptionsFragment extends Fragment {

    private Context context;
    private FragmentOptionsBinding binding;
    private Dialog dialog;
    private EditText dialogText;

    private TextView name, email, password, dialogTitle, changedField;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.change_information_window);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        setTextField(root);
        setButtons(root);
        return root;
    }

    private void setTextField(View root) {
        name = binding.name;
        email = binding.email;
        password = binding.password;
        dialogText = dialog.findViewById(R.id.dialog_text);
        dialogTitle = dialog.findViewById(R.id.data_change_title);
    }

    private void setButtons(View root) {
        ImageButton editName = root.findViewById(R.id.edit_name);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTitle.setText("Введите новое имя");
                dialogText.setText(name.getText());
                changedField = name;
                dialogText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                dialog.show();
            }
        });
        ImageButton editEmail = root.findViewById(R.id.edit_email);
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTitle.setText("Введите новый e-mail");
                dialogText.setText(email.getText());
                changedField = email;
                dialogText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                dialog.show();
            }
        });
        ImageButton editPassword = root.findViewById(R.id.edit_password);
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTitle.setText("Введите новый пароль");
                dialogText.setText("");
                changedField = password;
                dialogText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                dialog.show();
            }
        });

        ImageButton closeButton = dialog.findViewById(R.id.close_change_field);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        Button save = dialog.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedField.setText(dialogText.getText());
                dialog.hide();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }
}