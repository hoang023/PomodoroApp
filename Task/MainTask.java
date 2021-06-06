package com.example.pomodoro.Task;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pomodoro.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class MainTask {
    ListView listView;
    SqliteDatabase db;
    ArrayList<Information> arrayList;
    ArrayList<String> selectList = new ArrayList<String>();
    ArrayList<Integer> unDeleteSelect = new ArrayList<Integer>();

    ArrayAdapter arrayAdapter;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new SqliteDatabase(this);
        SQLiteDatabase sqliteDatabase = db.getWritableDatabase();

        listView = findViewById(R.id.ListviewId);

        arrayList=new ArrayList<Information>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // ClickListener for floating action bar
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTask.this,AddDataActivity.class);
                startActivity(intent);
            }

            private void startActivity(Intent intent) {
            }
        });

        view();//calling view method

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainTask.this,UpdateActivity.class);
                intent.putExtra("subject",arrayList.get(i).getSubject());
                intent.putExtra("listId",arrayList.get(i).getId());
                startActivity(intent);
            }

            private void startActivity(Intent intent) {
            }
        });

    }

    public void view() {
        Cursor cursor = db.display();
        while (cursor.moveToNext()) {
            Information information = new Information(cursor.getString(0),cursor.getString(1));
            arrayList.add(information);
        }
        Collections.reverse(arrayList);//reversing arrayList for showing data in a proper way

        arrayAdapter = new InformationAdapter(this, arrayList);//passing context and arrayList to arrayAdapter
        listView.setAdapter(arrayAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);//setting choice mode
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {//method for multiChoice option

            //checking state Item on Click mode or not
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                String id = arrayList.get(i).getId();//for getting database Id
                //if double click Item color will be white
                if(selectList.contains(id) && count>0){
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                    selectList.remove(id);
                    count--;
                }
                //else item color will be gray
                else{
                    selectList.add(arrayList.get(i).getId());
                    listView.getChildAt(i).setBackgroundColor(Color.GRAY);
                    unDeleteSelect.add(i);//item position storing on new arrayList
                    count++;
                }
                actionMode.setTitle(count+" item selected");
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            //this method for taking action like delete,share
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.deleteContextMenuId){
                    for(String i : selectList){
                        db.delete(i);
                        arrayAdapter.remove(i);
                        Toast.makeText(getApplicationContext(),count+" item Deleted",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainTask.this,MainTask.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }
                    arrayAdapter.notifyDataSetChanged();
                    actionMode.finish();
                    count = 0;
                }
                return true;
            }

            //this method for destroying actionMode
            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                for(int i: unDeleteSelect){
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);//reset all selected item with gray color
                }
                count = 0;//reset count here
                unDeleteSelect.clear();
                selectList.clear();
            }
        });
    }
}
