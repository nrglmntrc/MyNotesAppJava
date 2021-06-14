package com.nurgulmantarci.mynotesappjava.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.databinding.ActivityLoginBinding;
import com.nurgulmantarci.mynotesappjava.helper.MySharedPref;
import com.nurgulmantarci.mynotesappjava.helper.UserInformationHelper;
import com.nurgulmantarci.mynotesappjava.loginData.LoginDatabaseAdapter;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding dataBinding;
    LoginDatabaseAdapter loginDatabaseAdapter;
    MySharedPref mySharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loginDatabaseAdapter = new LoginDatabaseAdapter(getApplicationContext());
        loginDatabaseAdapter.open();

        controlGetIntent();

        controlRememberMe();
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

    private void controlRememberMe(){
        mySharedPref=new MySharedPref();
        if (mySharedPref.getValueBoolean(this, "rememberMe")) { //benihatırla işaretlenmiş ise
            dataBinding.editTextEmail.setText(mySharedPref.getValueString(this, "email"));
            dataBinding.editTextPassword.setText(mySharedPref.getValueString(this, "password"));
            dataBinding.chkBeniHatirla.setChecked(mySharedPref.getValueBoolean(this, "rememberMe"));
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
        }else if(!loginDatabaseAdapter.controlEmailIsExist(email)){
            Toast.makeText(this, "Email kayıtlı değil", Toast.LENGTH_LONG).show();
            return;
        }else {
            String storedEmail=loginDatabaseAdapter.getSingleEntry(password);
            if(email.equals(storedEmail)){
                UserInformationHelper.saveUserEmail(this,email);

                setRememberMeInfo();

                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this, "Şifre hatalı", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void txtSignUpClicked(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    private void setRememberMeInfo(){
        if (dataBinding.chkBeniHatirla.isChecked()) {
            mySharedPref.saveString(this, "email", dataBinding.editTextEmail.getText().toString());
            mySharedPref.saveString(this, "password", dataBinding.editTextPassword.getText().toString());

        } else {
            mySharedPref.saveString(this, "email", "");
            mySharedPref.saveString(this,"password","");
        }
        mySharedPref.saveBoolean(this, "rememberMe", dataBinding.chkBeniHatirla.isChecked());
    }

}