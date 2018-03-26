package com.hayyaalassalah.faizanahmad.healthyme;

import android.app.Fragment;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

/**
 * Created by IBM on 12/3/2016.
 */
public class GoalGridviewFrag extends Fragment {

   private View view;
    private Button button;
    private GridView listView;
    private goalslistadapter myadapter;
    SQLiteDatabase database;
    MyDbHelper openHelper;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.activity_goals_grid, container, false);

        button= (Button) view.findViewById(R.id.buttongrid);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on
                EditText editText= (EditText) view.findViewById(R.id.edittextgrid);
                String goal=editText.getText().toString();
                editText.setText("");



                if(goal!=""){
                    openHelper = new MyDbHelper(getActivity());
                    database = openHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("goaltext",goal);
                    values.put("status","1");
                    database.insertWithOnConflict("Goal",null,values,SQLiteDatabase.CONFLICT_REPLACE);
                    myadapter=new goalslistadapter(getActivity());
                    listView.setAdapter(myadapter);
                    openHelper.close();


                }



            }
        });

        myadapter=new goalslistadapter(getActivity());
        listView= (GridView) view.findViewById(R.id.GridView);
        listView.setAdapter(myadapter);


        return view;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
