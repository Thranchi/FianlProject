package com.example.hudin.fianlproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.RenamingDelegatingContext;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.hudin.fianlproject.R.id.comebackhome;
import static java.security.AccessController.getContext;

public class Statistic extends AppCompatActivity {

    SQLiteDatabase database;
    ListView listview;
    ListViewAdapter adapter;
    MainActivity main;
    int index = 0;
    ArrayList<String> checkbox = new ArrayList<>();

    int ddate = 0;

    int dbreakfast = 0;
    int dlunch = 0;
    int ddinner = 0;
    int dsleep = 0;
    int dwake = 0;
    int dalchol = 0;
    int dculture = 0;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        main = new MainActivity();

        // Adapter 생성
        adapter = new ListViewAdapter(this);
        database = openOrCreateDatabase("sample2.db", MODE_PRIVATE, null);
        //Toast.makeText(this, "테스트중"+"으로 생성성공", Toast.LENGTH_SHORT).show();

        try {
            createSQL();
            todayisnewday();
   //         Toast.makeText(this, "생성은 돼", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
    //        Toast.makeText(this, "생성도안돼", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        getFile();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.mon),
                ++index + " 번째 하루", "20160601");
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.wed),
                ++index + " 번째 하루", "20140601");
        // 세 번째 아이템 추가.
      //  adapter.addItem(ContextCompat.getDrawable(this, R.drawable.thu),
   //            ++index + " 번째 하루", getThisday()+"");

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                String descStr = item.getDesc();

  //              Toast.makeText(context, "테스트중"+Integer.parseInt(descStr), Toast.LENGTH_SHORT).show();

                String breakfast=sqlselect(Integer.parseInt(descStr), "brtime");
                String luntime=sqlselect(Integer.parseInt(descStr), "luntime");
                String dintime=sqlselect(Integer.parseInt(descStr), "dintime");
                String waketime=sqlselect(Integer.parseInt(descStr), "waketime");
                String bedtime=sqlselect(Integer.parseInt(descStr), "bedtime");
                String alchol=sqlselect(Integer.parseInt(descStr), "alchol");
                String exercise=sqlselect(Integer.parseInt(descStr), "exercise");
                String culture=sqlselect(Integer.parseInt(descStr), "culture");

                breakfast=nullize(breakfast);
                luntime=nullize(luntime);
                dintime=nullize(dintime);
                waketime=nullize(waketime);
                bedtime=nullize(bedtime);
                alchol=nullize(alchol);
                exercise=nullize(exercise);
                culture=nullize(culture);


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // 제목셋팅
                alertDialogBuilder.setTitle("하루보기");

                // AlertDialog 셋팅
                alertDialogBuilder
                        .setMessage("아침식사 : "+showwindowing(Integer.parseInt(breakfast))+"\n" +
                                "점심식사 : "+showwindowing(Integer.parseInt(luntime))+"\n" +
                                "저녁식사 : "+showwindowing(Integer.parseInt(dintime))+"\n" +
                                "기상시간 : "+showwindowing(Integer.parseInt(waketime))+"\n" +
                                "취침시간 : "+showwindowing(Integer.parseInt(bedtime))+"\n"+
                                "술 자 리 : "+alchol+"회\n" +
                                "운    동 : "+exercise+"회\n" +
                                "문화생활 : "+culture+"회\n"
                                )
                        .setCancelable(false)
                        .setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // 다이얼로그를 취소한다
                                        dialog.cancel();
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statisticmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //새로고침
        if (item.getItemId() == R.id.refresh) {

        }
        //불러오기
        else if (item.getItemId() == R.id.open) {

        }
        //저장
        else if (item.getItemId() == R.id.save) {

        }
        //초기화
        else if (item.getItemId() == R.id.gotohell) {

        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.comebackhome) {
            Intent intent = new Intent(Statistic.this, MainActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.Showstatistic) {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // 제목셋팅
            alertDialogBuilder.setTitle("통계보기");

            // AlertDialog 셋팅
            alertDialogBuilder
                    .setMessage("아침식사 : "+getavg("brtime")+"\n"+
                            "점심식사 : "+getavg("luntime")+"\n" +
                            "저녁식사 : "+getavg("dintime")+"\n" +
                            "기상시간 : "+getavg("waketime")+"\n" +
                            "취침시간 : "+getavg("bedtime")+"\n"+
                            "술 자 리 : "+getcount("alchol")+"\n" +
                            "운    동 : "+getcount("exercise")+"\n" +
                            "문화생활 : "+getcount("culture")+"\n"
                    )
                    .setCancelable(false)
                    .setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    // 다이얼로그를 취소한다
                                    dialog.cancel();
                                }
                            });

            // 다이얼로그 생성
            AlertDialog alertDialog = alertDialogBuilder.create();

            // 다이얼로그 보여주기
            alertDialog.show();
        }
    }

    public void todayisnewday(){
        String sql1 = "select date from brtime";
        String str = "";
        Cursor recordset = database.rawQuery(sql1, null);
        recordset.moveToFirst();
        if(recordset.getCount()!=0){
            try {
                str = recordset.getInt(0)+"";
                if(str.equals(getThisday()+""))
                {
                    return;
                }
            } catch (SQLiteException e) {
            }}
        recordset.close();

        String a="INSERT INTO brtime VALUES(null,"+getThisday()+",null)";
        String b="INSERT INTO luntime VALUES(null,"+getThisday()+",null)";
        String c="INSERT INTO dintime VALUES(null,"+getThisday()+",null)";
        String d="INSERT INTO waketime VALUES(null,"+getThisday()+",null)";
        String e="INSERT INTO bedtime VALUES(null,"+getThisday()+",null)";
        String f="INSERT INTO alchol VALUES(null,"+getThisday()+",null)";
        String g="INSERT INTO exercise VALUES(null,"+getThisday()+",null)";
        String h="INSERT INTO culture VALUES(null,"+getThisday()+",null)";

        database.execSQL(a);
        database.execSQL(b);
        database.execSQL(c);
        database.execSQL(d);
        database.execSQL(e);
        database.execSQL(f);
        database.execSQL(g);
        database.execSQL(h);

        try {
            adapter.addItem(drawablefinder(sunmontue()),
                    ++index + " 번째 하루", getThisday()+"");
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }

    public void dateupdatedate(int date) {


        try {
            adapter.addItem(drawablefinder(sunmontue()), ++index + " 번째 하루", date + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int sunmontue() throws ParseException {
        int day = 0;

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String showdate = "" + sdf.format(d);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date nDate = dateFormat.parse(showdate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);


        return dayNum;
    }

    public int getThisday() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String showdate = "" + sdf.format(d);
        sdf = new SimpleDateFormat("yyyyMMdd");
        int calculdate = Integer.parseInt(sdf.format(d).toString());

        return calculdate;
    }

    public Drawable drawablefinder(int dayNum) {
        switch (dayNum) {
            case 1:
                return getResources().getDrawable(R.drawable.sun);
            case 2:
                return getResources().getDrawable(R.drawable.mon);

            case 3:
                return getResources().getDrawable(R.drawable.tue);

            case 4:
                return getResources().getDrawable(R.drawable.wed);

            case 5:
                return getResources().getDrawable(R.drawable.thu);

            case 6:
                return getResources().getDrawable(R.drawable.fri);

            case 7:
                return getResources().getDrawable(R.drawable.sat);


        }
        return null;
    }

    public void clearall() {
        try {
            String drop = "DROP TABLE everydayismine";
            database.execSQL(drop);
            createSQL();
           /* BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "db.txt", true));
            String clear = "";
            /* INSERT 문을 사용하여 테이블에 데이터 추가. */
           /* bw.write(clear);
            bw.close();*/
            //Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String sqlselect(int date, String key) {
        String sql1 = "select * from "+key+" where date="+date;
        String str = "";
        Cursor recordset = database.rawQuery(sql1, null);
        recordset.moveToFirst();
        if(recordset.getCount()!=0){
        try {
            str = recordset.getInt(2)+"";
        } catch (SQLiteException e) {
        }}
        else
            str="해당사항없음";
        recordset.close();
        return str;
    }

    public String getcount(String key) {
        String sql1 = "select * from "+key;
        String str = "";
        int total=0;
        int count=0;
        int i=0;
        Cursor recordset = database.rawQuery(sql1, null);
        recordset.moveToFirst();

        if(recordset.getCount()!=0) {
            do {
                if(recordset.getInt(2)!=0) {
                    i++;
                    total = total + inttodate(recordset.getInt(2));
                }
            } while (recordset.moveToNext());
        }
        else
            total=0;

        return  i+"회";
    }

    public String getavg(String key) {
        String sql1 = "select * from "+key;
        String str = "";
        int total=0;
        int count=0;
        int i=0;
        Cursor recordset = database.rawQuery(sql1, null);
        recordset.moveToFirst();

        if(recordset.getCount()!=0) {
            do {
                if(recordset.getInt(2)!=0) {
                    i++;
                    total = total + inttodate(recordset.getInt(2));
                }
            } while (recordset.moveToNext());
        }
        else
            total=0;
//        Toast.makeText(this,"아이는"+i,Toast.LENGTH_SHORT).show();
        int result=total/i;

        return  showwindowing(datetoint(result));
    }

    public void createSQL() throws IOException {
        /*
        BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "db.txt", true));
        String clear = "";
            /* INSERT 문을 사용하여 테이블에 데이터 추가. */
        /*
        bw.write(clear);
        bw.close();*/
        String drop="drop table if exists brtime";
        String drop1="drop table  if exists luntime";
        String drop2="drop table if exists  dintime";
        String drop3="drop table  if exists bedtime";
        String drop4="drop table if exists  waketime";
        String drop5="drop table  if exists dintime";
        String drop6="drop table  if exists bedtime";
        String drop7="drop table  if exists waketime";
        String drop8="drop table  if exists exercise";
        String drop9="drop table  if exists culture";

        String breakfast = "create table if not exists brtime(\n" +
                "id integer primary key autoincrement,\n" +
                "date integer,\n" +
                "brtime integer\n" +
                ")\n";

        String lunch = "create table if not exists luntime(\n" +
                "id integer primary key autoincrement,\n" +
                "date integer,\n" +
                "luntime integer\n" +
                ")\n";

        String dinner = "create table if not exists dintime(\n" +
                "id integer primary key autoincrement,\n" +
                "date integer,\n" +
                "dintime integer\n" +
                ")\n";

        String sleep = "create table if not exists bedtime(\n" +
                "id integer primary key autoincrement,\n" +
                "date integer,\n" +
                "bedtime integer\n" +
                ")\n";

        String wake = "create table if not exists waketime(\n" +
                "id integer primary key autoincrement,\n" +
                "date integer,\n" +
                "waketime integer\n" +
                ")\n";

        String alchol = "create table if not exists alchol(\n" +
                "id integer primary key autoincrement,\n" +
                "date integer,\n" +
                "alchol integer\n" +
                ")\n";

        String exercise = "create table if not exists exercise(\n" +
                "id integer primary key autoincrement,\n" +
                "date integer,\n" +
                "exercise integer\n" +
                ")\n";

        String culture = "create table if not exists culture(\n" +
                "id integer primary key autoincrement,\n" +
                "date integer,\n" +
                "culture integer\n" +
                ")\n";

        String test="INSERT INTO brtime VALUES(null,20160601,0900)";
        String test2="INSERT INTO luntime VALUES(null,20160601,1200)";
        String test3="INSERT INTO dintime VALUES(null,20160601,1800)";
        String test4="INSERT INTO waketime VALUES(null,20160601,0700)";
        String test5="INSERT INTO bedtime VALUES(null,20160601,2300)";
        String test6="INSERT INTO alchol VALUES(null,20160601,1)";
        String test7="INSERT INTO exercise VALUES(null,20160601,1)";
        String test8="INSERT INTO culture VALUES(null,20160601,0)";

        String test1="INSERT INTO brtime VALUES(null,20140601,0700)";
        String test12="INSERT INTO luntime VALUES(null,20140601,1400)";
        String test13="INSERT INTO dintime VALUES(null,20140601,1900)";
        String test14="INSERT INTO waketime VALUES(null,20140601,0500)";
        String test15="INSERT INTO bedtime VALUES(null,20140601,2000)";
        String test16="INSERT INTO alchol VALUES(null,20140601,0)";
        String test17="INSERT INTO exercise VALUES(null,20140601,1)";
        String test18="INSERT INTO culture VALUES(null,20140601,0)";

        String test19="INSERT INTO culture VALUES(null,"+getThisday()+",0)";

        try {

            database.execSQL(drop);
            database.execSQL(drop1);
            database.execSQL(drop2);
            database.execSQL(drop3);
            database.execSQL(drop4);
            database.execSQL(drop5);
            database.execSQL(drop6);
            database.execSQL(drop7);
            database.execSQL(drop8);
            database.execSQL(drop9);

            database.execSQL(breakfast);
            database.execSQL(lunch);
            database.execSQL(dinner);
            database.execSQL(sleep);
            database.execSQL(wake);
            database.execSQL(alchol);
            database.execSQL(exercise);
            database.execSQL(culture);
            database.execSQL(test);
            database.execSQL(test2);
            database.execSQL(test3);
            database.execSQL(test4);
            database.execSQL(test5);
            database.execSQL(test6);
            database.execSQL(test7);
            database.execSQL(test8);

            database.execSQL(test1);
            database.execSQL(test12);
            database.execSQL(test13);
            database.execSQL(test14);
            database.execSQL(test15);
            database.execSQL(test16);
            database.execSQL(test17);
            database.execSQL(test18);
            database.execSQL(test19);

            //database.close();
            //goToast("하루 테이블 생성");
        } catch (SQLiteException e) {
            //goToast("이미 있는겁니다");
        }
    }

    public void inputDataToTable(int date, String table, int dbreakfast) {
        String newsqltext = "INSERT OR REPLACE INTO " + table + " VALUES (null," + date + "," + dbreakfast + ")";
        database.execSQL(newsqltext);
    }

    public void makeFile(int date, int data, String key) {
        /* BufferedReader br = new BufferedReader(new FileReader(getFilesDir() + "db.txt"));
         String readStr = "";
         String str = null;
         while ((str = br.readLine()) != null)
             readStr += str + "\n";
         br.close();

         BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "db.txt", true));*/


        String sql1 = "select date from everydayismine";


        Cursor recordset = database.rawQuery(sql1, null);
        recordset.moveToFirst();
        boolean heishere = false;
        for (int i = 0; ; i++) {
            if (recordset.getInt(i) == date) {
                heishere = true;
                break;
            }
            if (recordset.moveToNext()) {
                recordset.moveToNext();
            } else {
                break;
            }
        }
        recordset.close();
        String newsqltext = "";
        if (heishere) {
            newsqltext = "INSERT OR REPLACE INTO everydayismine (" + key + ") VALUES (" + data + ") where date=" + date;
            dateupdatedate(date);
        } else
            newsqltext = "INSERT OR REPLACE INTO everydayismine (" + date + "," + key + ") VALUES (" + date + "," + data + ")";

            /* INSERT 문을 사용하여 테이블에 데이터 추가. */
            /*readStr = readStr + newsqltext;
            bw.write(readStr);
            bw.close();*/
        //Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();
    }
/*
    public void getFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir() + "db.txt"));
            String readStr = "";
            String str = null;
            while ((str = br.readLine()) != null)
                readStr += str + "\n";
            br.close();
            try {
                database.execSQL(readStr);
                //goToast("하루 테이블 생성");
            } catch (SQLiteException e) {
                //goToast("이미 있는겁니다");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    public int counttime(String key) {
        String sqlSelect = "SELECT COUNT(" + key + ") FROM everydayismine;";
        Cursor cursor = null;
        int val = 0;

        cursor = database.rawQuery(sqlSelect, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            val = cursor.getInt(0);
        }
        cursor.close();
        return val;
    }

    public int avgtime(String key) {
        String sqlSelect = "SELECT " + key + " FROM everydayismine;";
        Cursor cursor = null;
        int i = 0;
        int val = 0;
        int sum = 0;
        cursor = database.rawQuery(sqlSelect, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            val = cursor.getInt(i);
            val = inttodate(val);
            sum = sum + val;
        }
        cursor.close();
        return datetoint(sum / i);
    }


    public int inttodate(int original) {
        String str = original + "";
        int i = str.length();
        int result = 0;
        String temp;
        //String s1 = str.substring(12);
//        Toast.makeText(context,"오리지널"+original,Toast.LENGTH_LONG).show();
        if (i == 3) {
            temp = str.substring(0, 1);
            result = Integer.parseInt(temp) * 60 + Integer.parseInt(str.substring(2));
   //         Toast.makeText(context,"개조"+result,Toast.LENGTH_LONG).show();
            return result;
        } else if (i == 4) {
            temp = str.substring(0, 2);
            return result = Integer.parseInt(temp) * 60 + Integer.parseInt(str.substring(3));
        }
        return 0;
    }

    public String showwindowing(int original) {
     //   Toast.makeText(context,"온 숫자"+original,Toast.LENGTH_SHORT).show();
        String str = original + "";
        int i = str.length();
        String result = "";
        String temp;

        if (i == 3) {
            temp = str.substring(0, 1);
            return result = Integer.parseInt(temp)+"시" + Integer.parseInt(str.substring(2))+"분";
        } else if (i == 4) {
            temp = str.substring(0, 2);
            return result = Integer.parseInt(temp)+"시" + Integer.parseInt(str.substring(3))+"분";

        }
        return "해당사항없음";
    }

    public int datetoint(int original) {
        String tempresult="";
        int temphour = original / 60;
        int tempmin = original % 60;
        if(tempmin<10)
            tempresult= temphour + "0" + tempmin;
        else
            tempresult= temphour + "" + tempmin;


        return Integer.parseInt(tempresult);
    }

    public String nullize(String word){
        if(word.length()==0)
            return word="해당사항 없음";
        else
            return word;
    }
}