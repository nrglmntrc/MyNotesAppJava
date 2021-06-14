package com.nurgulmantarci.mynotesappjava.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.databinding.ActivityRegisterBinding;
import com.nurgulmantarci.mynotesappjava.loginData.LoginDatabaseAdapter;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding dataBinding;
    LoginDatabaseAdapter loginDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loginDatabaseAdapter = new LoginDatabaseAdapter(this);
        loginDatabaseAdapter=loginDatabaseAdapter.open();

    }

    public void btnSignUpClicked(View view){
        controlValidations();
    }

    private void controlValidations(){

        final String name = dataBinding.editTextName.getText().toString().trim();
        final String surname = dataBinding.editTextSurName.getText().toString().trim();
        final String email = dataBinding.editTextEmail.getText().toString().trim();
        String password = dataBinding.editTextPassword.getText().toString().trim();
        String passwordConfirm = dataBinding.editTextPasswordConfirm.getText().toString().trim();
        final String phone = dataBinding.editTextTel.getText().toString();

        if (name.isEmpty()) {
            dataBinding.editTextName.setError(getString(R.string.fill_message));
            dataBinding.editTextName.requestFocus();
            return;
        } else if (surname.isEmpty()) {
            dataBinding.editTextSurName.setError(getString(R.string.fill_message));
            dataBinding.editTextSurName.requestFocus();
            return;
        }else if (phone.isEmpty()) {
            dataBinding.editTextTel.setError(getString(R.string.fill_message));
            dataBinding.editTextTel.requestFocus();
            return;
        }else if (phone.length()<11) {
            dataBinding.editTextTel.setError(getString(R.string.wrong_format));
            dataBinding.editTextTel.requestFocus();
            return;
        } else if (email.isEmpty()) {
            dataBinding.editTextEmail.setError(getString(R.string.fill_message));
            dataBinding.editTextEmail.requestFocus();
            return;
        }else if(loginDatabaseAdapter.controlEmailIsExist(email)){
              Toast.makeText(this, "Bu mail adresi zaten kayıtlı", Toast.LENGTH_SHORT).show();
              return;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            dataBinding.editTextEmail.setError(getString(R.string.wrong_format));
            dataBinding.editTextEmail.requestFocus();
            return;
        } else if (password.isEmpty()) {
            dataBinding.editTextPassword.setError(getString(R.string.fill_message));
            dataBinding.editTextPassword.requestFocus();
            return;
        }else if (password.length()<6) {
            dataBinding.editTextPassword.setError(getString(R.string.password_length_message));
            dataBinding.editTextPassword.requestFocus();
            return;
        }else if (passwordConfirm.isEmpty()) {
            dataBinding.editTextPasswordConfirm.setError(getString(R.string.fill_message));
            dataBinding.editTextPasswordConfirm.requestFocus();
            return;
        }else if(!password.equals(passwordConfirm)){
            dataBinding.editTextPasswordConfirm.setError(getString(R.string.password_dont_match));
            dataBinding.editTextPasswordConfirm.requestFocus();
            return;
        }  else if(!dataBinding.chkUserAgreement.isChecked()){
            Toast.makeText(this, "Sözleşmeyi onaylayınız", Toast.LENGTH_SHORT).show();
        }else{

            loginDatabaseAdapter.insertEntry(password,email);
            Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("email",email);
            intent.putExtra("password",password);
            startActivity(intent);
            finish();

        }

    }

    public void txtSignInClicked(View view){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginDatabaseAdapter.close();
    }


}