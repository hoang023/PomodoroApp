package com.example.pomodoro.Task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pomodoro.R;

import java.util.ArrayList;

public class InformationAdapter extends ArrayAdapter<Information> {
    Context context;
    ArrayList<Information> ItemList;

    public InformationAdapter(@NonNull Context context, ArrayList<Information> ItemList) {
        super(context, 0,ItemList);
        this.context = context;
        this.ItemList = ItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){//if view null then create new view
            view= LayoutInflater.from(context).inflate(R.layout.detail_item,parent,false);//for creating view
        }

        Information item = ItemList.get(position);

        //finding listview shape component
        TextView subject = view.findViewById(R.id.detailText);
        //return super.getView(position, convertView, parent);


        //setting listview shape component to arrryList
        subject.setText(item.getSubject());

        return view;
    }
}