package com.example.le4tp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowConvActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences settings;
    APIInterface apiService;
    String hash;
    String idConv;
    String login;
    String idLastMessage;
    Bundle bdl;

    private LinearLayout msgLayout;
    private EditText edtMsg;
    Button btnOK;
    GlobalState gs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gs = (GlobalState) getApplication();
        setContentView(R.layout.activity_show_conversation);
        msgLayout = findViewById(R.id.conversation_svLayoutMessages);
        btnOK = findViewById(R.id.conversation_btnOK);
        btnOK.setOnClickListener(this);

        idLastMessage = "-1";

        edtMsg = findViewById(R.id.conversation_edtMessage);

        bdl = this.getIntent().getExtras();
        idConv = bdl.getString("idConv");
        hash = bdl.getString("hash");
        login = bdl.getString("login");
        Log.i(gs.TAG,idConv);
        Log.i(gs.TAG,hash);



        apiService = APIClient.getClient(this).create(APIInterface.class);
        recuperationMessages();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {

            @Override
            public void run() {
                recuperationMessages();
            }

        };
        timer.schedule(doAsynchronousTask, 0, 5000);
    }

    private void recuperationMessages(){
        //gs.alerter("recuperation");
        Call<ListMessage> call1 = apiService.doGetListMessageConversation(idConv,hash);
        call1.enqueue(new Callback<ListMessage>() {
            @Override
            public void onResponse(Call<ListMessage> call, Response<ListMessage> response) {
                ListMessage lm = response.body();
                ArrayList<Message> list = new ArrayList<Message>(lm.getMessages());
                int size = lm.getMessages().size();
                if (idLastMessage != "-1"){
                    String id = "";
                    int i = 0;
                    do{
                        id = list.get(i).id;
                        list.remove(i);
                    }while (!idLastMessage.equals(id) && list.size() != 0);
                }
                for(Message message : list){
                    TextView tv = new TextView(ShowConvActivity.this);
                    tv.setText("[" + message.auteur + "] " + message.contenu);
                    tv.setTextColor(Color.parseColor(message.couleur));

                    msgLayout.addView(tv);
                }
                if(size != 0){
                    idLastMessage = lm.getMessages().get(size-1).id;
                }
            }

            @Override
            public void onFailure(Call<ListMessage> call, Throwable t) {
                call.cancel();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        getSupportFragmentManager().findFragmentById(R.id.menu_fragment).setArguments(bdl); //setting menu fragment argument to notify it that we're logged
    }

    @Override
    public void onClick(View view) {
        String msg = edtMsg.getText().toString();
        if(msg != ""){
            Call<ResponseBody> call1 = apiService.doPostMessage(idConv,msg,hash);
            call1.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.i(gs.TAG,response.toString());
                    Log.i(gs.TAG,response.message());
                    Log.i(gs.TAG,""+response.code());
                    if(response.code() == 201){
                        gs.alerter("Message Envoyé");
                        recuperationMessages();
                    }else{
                        gs.alerter("Erreur lors de l'envoie");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                }
            });
        }else {
            gs.alerter("Le message est vide");
        }

        edtMsg.setText("");
    }
}
