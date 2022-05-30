package org.techtown.today;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    String sqlexe;

    public static String taskDB = "task.db";
    public static int version = 1;
    String taskTable = "tasktable"; // 테이블 이름

    List<Integer> id_list = new ArrayList<>();
    List<Integer> check_list = new ArrayList<>();
    List<String> tasks_list = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase;


    public DBHelper(@Nullable Context context) {

        super(context, taskDB, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("DBHelper", "--------------------DB onCreate ----------------------");

        sqLiteDatabase.execSQL("CREATE TABLE if not exists " + taskTable + "("
                + "_id integer PRIMARY KEY autoincrement,"
                + "startdate int,"
                + "enddate int,"
                + "day int,"
                + "task String,"
                + "exe int)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    // Data 저장 - String startdate, String enddate, String day, String tasks, int exe
    public void insertTask(int startdate, int enddate, int day, String tasks, int exe) {
        //SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        Log.d("DBHelper", "--------sqliteDatabase writable 전-------" + sqLiteDatabase);
        sqLiteDatabase = getWritableDatabase();
        Log.d("DBHelper", "--------sqliteDatabase writable 후-------" + sqLiteDatabase);
        Log.d("DBHelper", "--------------------DB insertTask()----------------------");

        try {
            if (taskTable == null) {
                Log.d("DBHelper", "--------------------DB insert faild----------------------");
                return;
            }

            sqlexe = "insert into taskTable"
                    + "(startdate, enddate, day, task, exe)"
                    + "values"
                    + "('" + startdate + "', '" + enddate + "', '" + day + "', '" + tasks + "', " + exe + ")";

            Log.d("DBHelper", sqlexe);
            sqLiteDatabase.execSQL(sqlexe);

            Log.d("DBHelper", "--------------------DB insert 성공----------------------");


        } catch (Exception e) {
            Log.d("DBHelper", "--------------------DB Exception----------------------");
            e.printStackTrace();
        }

    }



    public void getTaskDB(String date){
        //SQLiteDatabase sqLiteDatabaseR = getReadableDatabase();
        sqLiteDatabase = getReadableDatabase();
        id_list.clear();
        check_list.clear();
        tasks_list.clear();
        int Intdate = Integer.parseInt(date);




       // String sqlread = "SELECT * FROM taskTable WHERE startdate = "+Intdate+" AND "+"enddate = "+Intdate;
        String sqlread = "SELECT * FROM taskTable WHERE startdate <= "+Intdate+" AND "+"enddate >= "+Intdate;
        Log.d("DBHelper", "-----------------"+sqlread);


        Cursor cursor = sqLiteDatabase.rawQuery(sqlread,null);

        while (cursor.moveToNext()){
            Log.d("DBHelper", "-----------------"+cursor.getString(0)+" / "+cursor.getString(4) +"  /  "+cursor.getInt(5));
            id_list.add(cursor.getInt(0));
            tasks_list.add(cursor.getString(4));
            check_list.add(cursor.getInt(5));

        }
    }


    public void updateTable(int check ,int ID){
        Log.d("DBHelper", "--------updateTable 들어옴---------" + check + " / " + ID);
        Log.d("DBHelper", "--------sqliteDatabase-------" + sqLiteDatabase);
        //SQLiteDatabase sqLiteDatabaseW = getWritableDatabase();
        sqLiteDatabase = getWritableDatabase();
        Log.d("DBHelper", "--------getWritableDatabase 실행됨---------");

        if(taskTable != null){
            try{

                String updateString = "UPDATE taskTable"
                        + " SET"
                        + " exe = " + check
                        + " WHERE _id = "+ ID;
                Log.d("DBHelper", "---------"+updateString);
                sqLiteDatabase.execSQL(updateString);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void deleteTask(ArrayList<Integer> delete_list){
        sqLiteDatabase = getReadableDatabase();
        for (int i = 0; i<delete_list.size(); i++){
            String deletestring = "DELETE FROM taskTable"
                    + " WHERE _id = "+ delete_list.get(i);
            Log.d("DBHelper", "---------"+deletestring);
            sqLiteDatabase.execSQL(deletestring);
        }
    }


}
