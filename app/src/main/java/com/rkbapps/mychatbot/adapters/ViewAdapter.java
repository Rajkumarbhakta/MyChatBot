package com.rkbapps.mychatbot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rkbapps.mychatbot.databinding.SendByBotChatBinding;
import com.rkbapps.mychatbot.databinding.SendByMeChatBinding;
import com.rkbapps.mychatbot.models.RecyclerViewModelClass;

import java.util.ArrayList;
import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter {

    List<RecyclerViewModelClass> massageList = new ArrayList<>();

    public ViewAdapter(List<RecyclerViewModelClass> massageList) {
        this.massageList = massageList;
    }

    @Override
    public int getItemViewType(int position) {
        if(massageList.get(position).getViewType() == RecyclerViewModelClass.SEND_BY_BOT){
            return 0;
        }else
            return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0){
            return  new BotMassageViewHolder(SendByBotChatBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }else{
            return  new MyMessageViewHolder(SendByMeChatBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if(viewType == 0){
            BotMassageViewHolder botHolder = (BotMassageViewHolder) holder;
            botHolder.sendByBotChatBinding.txtBotMassage.setText(massageList.get(position).getMassage());
        }else {
            MyMessageViewHolder myHolder = (MyMessageViewHolder) holder;
            myHolder.sendByMeChatBinding.txtMyMassage.setText(massageList.get(position).getMassage());
        }
    }

    @Override
    public int getItemCount() {
        return massageList.size();
    }

    public class MyMessageViewHolder extends RecyclerView.ViewHolder{

        SendByMeChatBinding sendByMeChatBinding;
        public MyMessageViewHolder(@NonNull SendByMeChatBinding itemView) {
            super(itemView.getRoot());
            sendByMeChatBinding=itemView;
        }

    }

    public class BotMassageViewHolder extends RecyclerView.ViewHolder{

        SendByBotChatBinding sendByBotChatBinding;
        public BotMassageViewHolder(@NonNull SendByBotChatBinding itemView) {
            super(itemView.getRoot());
            sendByBotChatBinding = itemView;
        }
    }

}
