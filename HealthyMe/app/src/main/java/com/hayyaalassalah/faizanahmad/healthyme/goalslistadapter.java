package com.hayyaalassalah.faizanahmad.healthyme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

/**
 * Created by IBM on 11/29/2016.
 */
public class goalslistadapter extends BaseAdapter {

    private Cursor result;
    private Context context;
    SQLiteDatabase database;
    MyDbHelper openHelper;
    private String query;
    public goalslistadapter(Context context){
        super();
        this.context=context;
        query= " SELECT * FROM Goal ORDER BY status DESC";
        openHelper = new MyDbHelper(context);

        database = openHelper.getWritableDatabase();
        result=database.rawQuery(query,null);


    }


    @Override
    public int getCount() {
        return result.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public  void update(){

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckBox c;
        result.moveToPosition(position);
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.individualitem,parent,false);
        }
        c= (CheckBox) convertView.findViewById(R.id.checkBox);
        c.setText(result.getString(1)); //getting the text
        c.setTag(result.getInt(0));
        //set tag to the id it has in databasee
        if(result.getInt(2)==0){
            c.setChecked(true);
        }else {
            c.setChecked(false);
        }
        c.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Cursor temp;
                ContentValues values= new ContentValues();

                int i=( int) v.getTag();
                String q="SELECT * FROM Goal WHERE ID=";
                q=q+i;
                String[] query2=new String[1];
                //query2[0]="ID=";
                        //query2="UPDATE Goal SET status = ";


                temp=database.rawQuery(q,null);
                temp.moveToNext();
                int status=temp.getInt(2);
                values.put("goaltext",temp.getString(1));


                if(status==0) {
                        values.put("status","1");
                        //query2=query2+" 1 WHERE ID = ";
                }else {
                    values.put("status","0");
                    //query2=query2+" 0 WHERE ID = ";
                }
                //query2="DELETE FROM Goal WHERE ID = ";
                query2[0]=query2[0]+i;

                database.update("Goal",values,"ID="+i,null);

                result=database.rawQuery(query,null);


                notifyDataSetChanged();

            }
        });




        return convertView;
    }
}
