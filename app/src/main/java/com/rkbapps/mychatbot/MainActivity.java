package com.rkbapps.mychatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rkbapps.mychatbot.adapters.ViewAdapter;
import com.rkbapps.mychatbot.databinding.ActivityMainBinding;
import com.rkbapps.mychatbot.models.Message;
import com.rkbapps.mychatbot.models.RecyclerViewModelClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private final String url ="https://api.openai.com/v1/completions";
    //private final String url = "https://chatgpt-ai-chat-bot.p.rapidapi.com/ask";
    private final String KEY = "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";
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    ViewAdapter viewAdapter;
    List<RecyclerViewModelClass> messageList = new ArrayList<>();
    String strMessage;

    RecyclerViewModelClass ans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(activityMainBinding.toolBar);

        viewAdapter = new ViewAdapter(messageList);

        LinearLayoutManager lm = new LinearLayoutManager(this);

        activityMainBinding.recyclerView.setLayoutManager(lm);
        activityMainBinding.recyclerView.setAdapter(viewAdapter);


        activityMainBinding.cardViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = activityMainBinding.edtMassage.getText().toString().trim();
                if (!message.equals("")) {
                    messageList.add(new RecyclerViewModelClass(message, RecyclerViewModelClass.SEND_BY_ME));
                    activityMainBinding.edtMassage.setText("");
                    viewAdapter.notifyItemInserted(messageList.size() - 1);
                    getChatResponse(message);
                }
            }
        });
    }

//    private void getChatResponse(String message) {
//
//        MediaType mediaType = MediaType.parse("application/json");
//        String value = "{\r\"text\": \""+message+"\"\r}";
//        RequestBody body = RequestBody.create(mediaType, value);
//        Request request = new Request.Builder()
//            .url("https://chatgpt-ai-chat-bot.p.rapidapi.com/ask")
//            .post(body)
//            .addHeader("content-type", "application/json")
//            .addHeader("X-RapidAPI-Key", "d3fb9e41a3msh968de6836a5c82dp1a1f93jsne6a7369cf3eb")
//            .addHeader("X-RapidAPI-Host", "chatgpt-ai-chat-bot.p.rapidapi.com")
//            .build();
//
//    client.newCall(request).enqueue(new Callback() {
//        @Override
//        public void onFailure(@NonNull Call call, @NonNull IOException e) {
//            //Log.d("response",e.getMessage());
//            messageList.add(new RecyclerViewModelClass(e.getMessage(),RecyclerViewModelClass.SEND_BY_BOT));
//        }
//
//        @Override
//        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//            //Log.d("response",""+response.body().string());
//            messageList.add(new RecyclerViewModelClass(response.body().string(),RecyclerViewModelClass.SEND_BY_BOT));
//        }
//    });
//    }

    private void getChatResponse(String message)  {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", "text-davinci-003");
            jsonObject.put("prompt", message);
            jsonObject.put("max_tokens", 4000);
            jsonObject.put("temperature", 0);
        }catch (JSONException e){
            e.printStackTrace();
        }

        RequestBody requestBody =RequestBody.create(jsonObject.toString(),JSON);

        Request request  =new Request.Builder()
                .url(url)
                .header("Authorization","Bearer "+KEY)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                strMessage = e.getMessage();
                //messageList.add(ans);
                messageList.add(new RecyclerViewModelClass(strMessage, RecyclerViewModelClass.SEND_BY_BOT));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                Message message1 = gson.fromJson(response.body().string(),Message.class);
                //Log.d("response",response.body().string());
                //messageList.add(ans);
                strMessage = message1.getChoices().get(0).getText();
                messageList.add(new RecyclerViewModelClass(strMessage, RecyclerViewModelClass.SEND_BY_BOT));
            }
        });
        viewAdapter.notifyItemInserted(messageList.size() - 1);
    }

}