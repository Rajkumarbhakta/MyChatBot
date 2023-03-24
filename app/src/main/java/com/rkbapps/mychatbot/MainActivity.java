package com.rkbapps.mychatbot;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rkbapps.mychatbot.adapters.ViewAdapter;
import com.rkbapps.mychatbot.databinding.ActivityMainBinding;
import com.rkbapps.mychatbot.models.BodyModel;
import com.rkbapps.mychatbot.models.RecyclerViewModelClass;
import com.rkbapps.mychatbot.models.ResponseModel;
import com.rkbapps.mychatbot.services.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewAdapter viewAdapter;
    private List<RecyclerViewModelClass> messageList = new ArrayList<>();
    private BodyModel bodyModel = new BodyModel();
    private ActivityMainBinding activityMainBinding;

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
                    activityMainBinding.recyclerView.smoothScrollToPosition(messageList.size() - 1);
                    bodyModel.setQuery(message);
                    loadBotResponse(bodyModel);
                }
            }
        });
    }


    private void loadBotResponse(BodyModel body) {

        RecyclerViewModelClass model = new RecyclerViewModelClass("Typing..", RecyclerViewModelClass.SEND_BY_BOT);
        messageList.add(model);
        viewAdapter.notifyDataSetChanged();
        activityMainBinding.recyclerView.smoothScrollToPosition(messageList.size() - 1);
        RetrofitInstance.getChatApi().getBotResponse(body).enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    if (bodyModel.getConversationId() != response.body().conversationId)
                        bodyModel.setConversationId(response.body().getConversationId());
                    messageList.remove(viewAdapter.getItemCount() - 1);
                    messageList.add(new RecyclerViewModelClass(response.body().getResponse(), RecyclerViewModelClass.SEND_BY_BOT));
                } else {
                    messageList.remove(viewAdapter.getItemCount() - 1);
                    messageList.add(new RecyclerViewModelClass(response.message(), RecyclerViewModelClass.SEND_BY_BOT));
                }
                viewAdapter.notifyDataSetChanged();
                activityMainBinding.recyclerView.smoothScrollToPosition(messageList.size() - 1);

            }

            @Override
            public void onFailure(retrofit2.Call<ResponseModel> call, Throwable t) {
                messageList.remove(viewAdapter.getItemCount() - 1);
                messageList.add(new RecyclerViewModelClass(t.getMessage(), RecyclerViewModelClass.SEND_BY_BOT));
                viewAdapter.notifyDataSetChanged();
                activityMainBinding.recyclerView.smoothScrollToPosition(messageList.size() - 1);
            }
        });
    }

}