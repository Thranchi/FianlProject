package com.example.hudin.fianlproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.hudin.fianlproject.R.id.comebackhome;
import static java.security.AccessController.getContext;

public class Statistic extends AppCompatActivity {

    SQLiteDatabase database ;
    ListView listview ;
    ListViewAdapter adapter;
    MainActivity main;
    int index=0;
    ArrayList<String> checkbox=new ArrayList<>();

    int ddate=0;

    int dbreakfast=0;
    int dlunch=0;
    int ddinner=0;
    int dsleep=0;
    int dwake=0;
    int dalchol=0;
    int dculture=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        main=new MainActivity();

        // Adapter 생성
        adapter = new ListViewAdapter(this) ;
        database=openOrCreateDatabase("sample.db",MODE_PRIVATE,null);
        //Toast.makeText(this, "테스트중"+"으로 생성성공", Toast.LENGTH_SHORT).show();

        try {
            createSQL();
            Toast.makeText(this,"생성은 돼",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this,"생성도안돼",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        getFile();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.mon),
                ++index+" 번째 하루",0601+"") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.wed),
                ++index+" 번째 하루","0602") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.thu),
                ++index+" 번째 하루",0603+"") ;

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                String titleStr = item.getTitle() ;
                String descStr = item.getDesc() ;
                Drawable iconDrawable = item.getIcon() ;

                // TODO : use item data.
            }
        }) ;
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
            clearall();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        if(v.getId()==R.id.comebackhome) {
            Intent intent = new Intent(Statistic.this, MainActivity.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.Showstatistic) {

        }
    }

    public void updatedate(int date){
        for(int i=0;i<checkbox.size();i++){
            if(checkbox.get(i).equals(getThisday()+""))
                return;
        }

        try {
            adapter.addItem(drawablefinder(sunmontue()),++index+" 번째 하루",date+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int sunmontue() throws ParseException {
        int day=0;

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String showdate = "" + sdf.format(d);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
        Date nDate = dateFormat.parse(showdate) ;

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;


        return dayNum ;
    }

    public int getThisday() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String showdate = "" + sdf.format(d);
        sdf = new SimpleDateFormat("yyyyMMdd");
        int calculdate = Integer.parseInt(sdf.format(d).toString());

        return calculdate;
    }

    public Drawable drawablefinder(int dayNum){
        switch(dayNum){
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
            BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "db.txt", true));
            String clear = "";
            /* INSERT 문을 사용하여 테이블에 데이터 추가. */
            bw.write(clear);
            bw.close();
            //Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String sqlselect() {
        String sql1 = "select name from everydayismine";

        try {
            Cursor recordset = database.rawQuery(sql1, null);
            recordset.moveToFirst();
            String str = "이거 안되네";
            do {
                str += recordset.getInt(1) + "/"+ "\n";
            } while (recordset.moveToNext());
            recordset.close();
            return str;
        } catch (SQLiteException e) {

        }

        return "스큐엘 테스트 실패";
    }

    public void createSQL() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "db.txt", true));
        String clear = "";
            /* INSERT 문을 사용하여 테이블에 데이터 추가. */
        bw.write(clear);
        bw.close();

        String breakfast = "create table if not exists brtime(\n" +
                "date integer primary key,\n" +
                "brtime integer,\n" +
                ")\n";

        String lunch = "create table if not exists luntime(\n" +
                "date integer primary key,\n" +
                "luntime integer\n" +
                ")\n";

        String dinner = "create table if not exists dintime(\n" +
                "date integer primary key,\n" +
                "dintime integer\n" +
                ")\n";

        String sleep = "create table if not exists bedtime(\n" +
                "date integer primary key,\n" +
                "bedtime integer\n" +
                ")\n";

        String wake = "create table if not exists waketime(\n" +
                "date integer primary key,\n" +
                "waketime integer\n" +
                ")\n";

        String alchol = "create table if not exists alchol(\n" +
                "date integer primary key,\n" +
                "alchol integer\n" +
                ")\n";

        String exercise = "create table if not exists exercise(\n" +
                "date integer primary key,\n" +
                "exercise integer\n" +
                ")\n";

        String culture = "create table if not exists culture(\n" +
                "date integer primary key,\n" +
                "culture integer\n" +
                ")\n";

        try {
            database.execSQL(breakfast);
            database.execSQL(lunch);
            database.execSQL(dinner);
            database.execSQL(sleep);
            database.execSQL(wake);
            database.execSQL(alchol);
            database.execSQL(exercise);
            database.execSQL(culture);

            //goToast("하루 테이블 생성");
        } catch (SQLiteException e) {
            //goToast("이미 있는겁니다");
        }
    }

    public void inputDataToTable(int date, String table ,int dbreakfast){
        /*String sql1 = "select date from "+table;
        String newsqltext="";
        try{
            Cursor recordset = database.rawQuery(sql1,null);
            recordset.moveToFirst();
            boolean heishere = false;
            for(int i=0;;i++){
                if(recordset.getInt(i)==date){
                    heishere=true;
                    break;
                }
                if(recordset.moveToNext()){
                    recordset.moveToNext();
                }
                else {
                    break;
                }
            }
            if(heishere)*/
        String newsqltext = "INSERT OR REPLACE INTO "+table+" VALUES (" + date + ","+dbreakfast+")";
          /*  else{
                recordset.close();
                return;}

            recordset.close();
        } catch (SQLiteException e){
        }
*/
        try {
            /* INSERT 문을 사용하여 테이블에 데이터 추가. */
            database.execSQL(newsqltext);
            BufferedWriter bw = null;
            bw = new BufferedWriter(new FileWriter(getFilesDir() + "db.txt", true));
            bw.write(newsqltext);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeFile(int date, int data, String key) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir() + "db.txt"));
            String readStr = "";
            String str = null;
            while ((str = br.readLine()) != null)
                readStr += str + "\n";
            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "db.txt", true));


            String sql1 = "select date from everydayismine";


            Cursor recordset = database.rawQuery(sql1, null);
            recordset.moveToFirst();
            boolean heishere = false;
            for(int i=0;;i++){
                if(recordset.getInt(i)==date){
                    heishere=true;
                    break;
                }
                if(recordset.moveToNext()){
                    recordset.moveToNext();
                }
                else {
                    break;
                }
            }
            recordset.close();
            String newsqltext ="";
            if(heishere) {
                newsqltext = "INSERT OR REPLACE INTO everydayismine (" + key + ") VALUES (" + data + ") where date=" + date;
                updatedate(date);
            }
            else
                newsqltext = "INSERT OR REPLACE INTO everydayismine ("+date +"," + key + ") VALUES ("+date +"," + data + ")";

            /* INSERT 문을 사용하여 테이블에 데이터 추가. */
            readStr = readStr + newsqltext;
            bw.write(readStr);
            bw.close();
            //Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

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

    }

    public int counttime(String key){
        String sqlSelect = "SELECT COUNT("+key+") FROM everydayismine;";
        Cursor cursor = null ;
        int val=0;

        cursor = database.rawQuery(sqlSelect,null) ;
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            val = cursor.getInt(0) ;
        }
        cursor.close();
        return val;
    }

    public int avgtime(String key){
        String sqlSelect = "SELECT "+key+" FROM everydayismine;";
        Cursor cursor = null ;
        int i=0;
        int val=0;
        int sum=0;
        cursor = database.rawQuery(sqlSelect,null) ;
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            val = cursor.getInt(i) ;
            val=inttodate(val);
            sum=sum+val;
        }
        cursor.close();
        return datetoint(sum/i);
    }


    public int inttodate(int original){
        String str=original+"";
        int i=str.length();
        int result=0;
        String temp;
        String s1 = str.substring(12) ;

        if(i==3){
            temp=str.substring(0,1);
            return result=Integer.parseInt(temp)*60+Integer.parseInt(str.substring(2));
        }
        else if(i==4){
            temp=str.substring(0,2);
            return result=Integer.parseInt(temp)*60+Integer.parseInt(str.substring(3));
        }
        return 0;
    }

    public int datetoint(int original){
        int temphour=original/60;
        int tempmin=original%60;
        String tempresult=temphour+""+tempmin;

        return Integer.parseInt(tempresult);
    }

    public int getDdate() {
        return ddate;
    }

    public void setDdate(int ddate) {
        this.ddate = ddate;
    }

    public int getDbreakfast() {
        return dbreakfast;
    }

    public void setDbreakfast(int date, String table ,int dbreakfast) {
        this.dbreakfast = dbreakfast;
    }

    public int getDlunch() {
        return dlunch;
    }

    public void setDlunch(int dlunch) {
        this.dlunch = dlunch;
    }

    public int getDdinner() {
        return ddinner;
    }

    public void setDdinner(int ddinner) {
        this.ddinner = ddinner;
    }

    public int getDsleep() {
        return dsleep;
    }

    public void setDsleep(int dsleep) {
        this.dsleep = dsleep;
    }

    public int getDwake() {
        return dwake;
    }

    public void setDwake(int dwake) {
        this.dwake = dwake;
    }

    public int getDalchol() {
        return dalchol;
    }

    public void setDalchol(int dalchol) {
        this.dalchol = dalchol;
    }

    public int getDculture() {
        return dculture;
    }

    public void setDculture(int dculture) {
        this.dculture = dculture;
    }
}
