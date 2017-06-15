package com.example.hudin.fianlproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.Calendar;
import java.util.Date;

import static com.example.hudin.fianlproject.R.drawable.fri;
import static com.example.hudin.fianlproject.R.drawable.mon;
import static com.example.hudin.fianlproject.R.drawable.sat;
import static com.example.hudin.fianlproject.R.drawable.thu;
import static com.example.hudin.fianlproject.R.drawable.tue;
import static com.example.hudin.fianlproject.R.drawable.wed;

public class MainActivity extends AppCompatActivity {

    ListViewItem ListViewItem=new ListViewItem();
    SQLiteDatabase database;
    Button btn_meal, btn_bed, btn_act;
    int inputTime = 0, inputDate = 0;
    int breackfast = 0, lunch = 0, dinner = 0;
    int sleepTime = 0, wakeTime = 0;
    int actTime = 0;
    boolean ifitistrueitismorning = true;
    String whatisthis;
    int todaytodayohglorytoday = 0;
    int index=0;
    int actstatus=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_meal = (Button) findViewById(R.id.btn_meal);
        btn_bed = (Button) findViewById(R.id.btn_bed);
        btn_act = (Button) findViewById(R.id.btn_act);

        database=openOrCreateDatabase("테스트중",MODE_PRIVATE,null);
        //Toast.makeText(this, "테스트중"+"으로 생성성공", Toast.LENGTH_SHORT).show();

        try {
            createSQL();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getFile();


        btn_act.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (actstatus==0) {
                    actTime = getThistime();
                    goToast(actTime + "");
                    whatisthis = "alchol";
                    makeFile(todaytodayohglorytoday, actTime, whatisthis);
                } else if (actstatus==1) {
                    actTime = getThistime();
                    goToast(actTime + "");
                    whatisthis = "exercise";
                    makeFile(todaytodayohglorytoday, actTime, whatisthis);
                } else if (actstatus==2) {
                    actTime = getThistime();
                    goToast(actTime + "");
                    whatisthis = "culture";
                    makeFile(todaytodayohglorytoday, actTime, whatisthis);
                }
            return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //새로고침
        if (item.getItemId() == R.id.refresh) {

        }
        //통계보기
        else if (item.getItemId() == R.id.statistics) {
            Intent intent = new Intent(MainActivity.this, Statistic.class);
            startActivity(intent);
        }
        //불러오기
        else if (item.getItemId() == R.id.open) {
            String k=sqlselect();
            Toast.makeText(this,k+"망했냐",Toast.LENGTH_SHORT).show();
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
        if (v.getId() == R.id.btn_meal) {

            inputTime = getThistime();
            if (inputTime > 0600 && inputTime < 1030) {
                breackfast = inputTime;
                whatisthis = "brtime";
                makeFile(todaytodayohglorytoday, breackfast, whatisthis);
            } else if (inputTime > 1030 && inputTime < 1630) {
                lunch = inputTime;
                whatisthis = "luntime";
                makeFile(todaytodayohglorytoday, lunch, whatisthis);
            } else if (inputTime > 1630 && inputTime < 2100) {
                dinner = inputTime;
                whatisthis = "dintime";
                makeFile(todaytodayohglorytoday, dinner, whatisthis);

            }
        } else if (v.getId() == R.id.btn_bed) {
            if (ifitistrueitismorning) {
                wakeTime = getThistime();
                ifitistrueitismorning = false;
                whatisthis = "waketime";
                makeFile(todaytodayohglorytoday, wakeTime, whatisthis);
            } else {
                sleepTime = getThistime();
                ifitistrueitismorning = true;
                whatisthis = "bedtime";
                makeFile(todaytodayohglorytoday, sleepTime, whatisthis);
            }
        } else {
            if (v.getId() == R.id.btn_act) {
                actstatus++;
                if (actstatus%3 == 0) {
                    btn_act.setBackground(getResources().getDrawable(R.drawable.alcohol));
                } else if (actstatus%3 == 1) {
                    btn_act.setBackground(getResources().getDrawable(R.drawable.exercise));
                } else if (actstatus%3 == 2) {
                    btn_act.setBackground(getResources().getDrawable(R.drawable.culture));
                }
            }
        }
    }

    public void goToast(String msg) {
        //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public int getThisday() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String showdate = "" + sdf.format(d);
        sdf = new SimpleDateFormat("yyyyMMdd");
        int calculdate = Integer.parseInt(sdf.format(d).toString());

        return calculdate;
    }

    public int getThistime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hhmm");
        int calculdate = Integer.parseInt(sdf.format(d).toString());

        return calculdate;
    }

    public void createSQL() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(getFilesDir() + "db.txt", true));
        String clear = "";
            /* INSERT 문을 사용하여 테이블에 데이터 추가. */
        bw.write(clear);
        bw.close();

        String sql = "create table if not exists everydayismine(\n" +
                "date integer primary key,\n" +
                "brtime integer,\n" +
                "luntime integer\n" +
                "dintime integer\n" +
                "bedtime integer\n" +
                "waketime integer\n" +
                "alchhol integer\n" +
                "exercise integer\n" +
                "culture integer\n" +
                ")\n";
        String testing = "INSERT OR REPLACE INTO everydayismine (date) VALUES (" + getThisday() + ")";
        try {
            database.execSQL(sql);
            database.execSQL(testing);
            goToast("하루 테이블 생성");
        } catch (SQLiteException e) {
            goToast("이미 있는겁니다");
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
            String newsqltext ="";
            if(heishere)
                newsqltext = "INSERT OR REPLACE INTO everydayismine ("+ key +") VALUES (" + data + ") where date=" + date;
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
                goToast("하루 테이블 생성");
            } catch (SQLiteException e) {
                goToast("이미 있는겁니다");
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


        return day ;
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

    class  checktime extends AsyncTask<Integer,Integer,Void> {

        public int getThistime() {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hhmm");
            int calculdate = Integer.parseInt(sdf.format(d).toString());

            return calculdate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            todaytodayohglorytoday=getThisday();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            //isCancelled가 트루이면 온 프로그레스업데이트로 안간다 바로 캔슬로 간다
            if(isCancelled()==true) return null;
            while (true) {
                try {
                    Thread.sleep(180000);
                    todaytodayohglorytoday=getThisday();
                    publishProgress(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int inputTime=getThistime();

            if (inputTime < 0600 || inputTime > 2100) {
                btn_meal.setClickable(false);
            }
            else
                btn_meal.setClickable(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {

            super.onCancelled();

        }
    }
}

