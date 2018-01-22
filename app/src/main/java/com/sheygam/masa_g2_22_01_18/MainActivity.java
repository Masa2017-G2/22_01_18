package com.sheygam.masa_g2_22_01_18;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText inputEmail, inputPasword;
    private Button loginBtn, regBtn;
    private ProgressBar myProgress;
    private View inputWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputEmail = findViewById(R.id.input_email);
        inputPasword = findViewById(R.id.input_password);
        loginBtn = findViewById(R.id.login_btn);
        regBtn = findViewById(R.id.reg_btn);
        myProgress = findViewById(R.id.my_progress);
        inputWrapper = findViewById(R.id.input_wrapper);

        loginBtn.setOnClickListener(this);
        regBtn.setOnClickListener(this);

        ArrayList<String> names = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            names.forEach(System.out::println);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_btn){
            showProgress(true);
            String email = inputEmail.getText().toString();
            String password = inputPasword.getText().toString();
            HttpProvider.getInstance().login(new Auth(email,password), callback);
        }else if(v.getId() == R.id.reg_btn){
            new RegTask().execute();
        }
    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            runOnUiThread(() -> {
                showProgress(false);
                showError("Connection error!");
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            runOnUiThread(() -> showProgress(false));

            if(response.isSuccessful()){
                Gson gson = new Gson();
                AuthToken auth = gson.fromJson(response.body().string(),AuthToken.class);
                Log.d("MY_TAG", "onResponse: token: " + auth.getToken());
                runOnUiThread(() -> showSuccess("Login ok!"));
            }else if(response.code() == 401){
                runOnUiThread(() -> showError("Wrong email or password!"));
            }else{
                Log.d("MY_TAG", "onResponse: error: " + response.body().string());
                runOnUiThread(() -> showError("Server error!"));
            }
        }
    };

    private void showProgress(boolean show){
        inputWrapper.setVisibility(show ? View.INVISIBLE:View.VISIBLE);
        myProgress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void showError(String error){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Error!")
                .setMessage(error)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();
        dialog.show();

    }

    private void showSuccess(String msg){
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("Ok",null)
                .create()
                .show();
    }

    private void builderSample(){
        Person p = new Person.Builder()
                .name("Vasya")
                .email("vasya@mail.com")
                .phone("1234567890")
                .build();
    }

    class RegTask extends AsyncTask<Void, Void, String> {
        private boolean isSuccess = true;
        private Auth auth;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String email = inputEmail.getText().toString();
            String password = inputPasword.getText().toString();
            auth = new Auth(email, password);
            showProgress(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "Registration ok";
            try {
                AuthToken authToken = HttpProvider.getInstance().registration(auth);
                Log.d("MY_TAG", "doInBackground: token: " + authToken.getToken());
            }catch (IOException e){
                e.printStackTrace();
                isSuccess = false;
                result = "Connection error! Check your internet!";
            }catch (Exception e) {
                e.printStackTrace();
                isSuccess = false;
                result = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            showProgress(false);
            if(isSuccess){
                showSuccess(s);
            }else {
                showError(s);
            }
        }
    }
}
