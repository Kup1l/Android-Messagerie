package com.example.le4tp2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompteActivity extends AppCompatActivity implements View.OnClickListener {

    APIInterface apiService;
    String hash;
    String login;
    TextView labelLogin;
    EditText edtPasse;
    Button btnOK;
    GlobalState gs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gs = (GlobalState) getApplication();
        setContentView(R.layout.activity_compte);
        labelLogin = findViewById(R.id.compte_labelLogin);
        edtPasse = findViewById(R.id.compte_edtPasse);
        btnOK = findViewById(R.id.compte_btnOK);
        btnOK.setOnClickListener(this);

        Bundle bdl = this.getIntent().getExtras();
        hash = bdl.getString("hash");
        login = bdl.getString("login");
        if(hash == "" || hash == null){

            Intent change2Login = new Intent(this,LoginActivity.class);
            startActivity(change2Login);
        }
        labelLogin.setText("Compte "+login);
        apiService = APIClient.getClient(this).create(APIInterface.class);
    }

    @Override
    public void onClick(View view) {
        String msg = edtPasse.getText().toString();
        if(msg != ""){
            Call<ResponseBody> call1 = apiService.doChangePassword(msg,hash);
            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(gs.TAG,response.toString());
                    Log.i(gs.TAG,call.request().toString());
                    gs.alerter("Mot de passe changé");
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }
}
