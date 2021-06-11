package com.nurgulmantarci.mynotesappjava.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        controlGetIntent();
    }

    private void controlGetIntent(){
        if(getIntent().getStringExtra("email")!=null){
            if(!getIntent().getStringExtra("email").equals("")){
                dataBinding.editTextEmail.setText(getIntent().getStringExtra("email"));
            }
        }

        if(getIntent().getStringExtra("password")!=null){
            if(!getIntent().getStringExtra("password").equals("")){
                dataBinding.editTextPassword.setText(getIntent().getStringExtra("password"));
            }
        }

    }

    public void btnSignInClick(View view){

        controlValidation();

    }

    private void controlValidation(){
        final String email = dataBinding.editTextEmail.getText().toString().trim();
        String password = dataBinding.editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            dataBinding.editTextEmail.setError(getString(R.string.fill_message));
            dataBinding.editTextEmail.requestFocus();
            return;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            dataBinding.editTextEmail.setError(getString(R.string.wrong_format));
            dataBinding.editTextEmail.requestFocus();
            return;
        } else if (password.isEmpty()) {
            dataBinding.editTextPassword.setError(getString(R.string.fill_message));
            dataBinding.editTextPassword.requestFocus();
            return;
        }else{

        }
    }

    public void txtSignUpClicked(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

}