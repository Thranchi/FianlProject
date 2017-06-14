package com.example.hudin.fianlproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;
    Button btn_meal, btn_bed, btn_act;
    int inputTime = 0, inputDate = 0;
    int breackfast = 0, lunch = 0, dinner = 0;
    int sleepTime = 0, wakeTime = 0;
    int actTime = 0;
    boolean ifitistrueitismorning = true;
    String whatisthis;
    int todaytodayohglorytoday = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_meal = (Button) findViewById(R.id.btn_meal);
        btn_bed = (Button) findViewById(R.id.btn_bed);
        btn_act = (Button) findViewById(R.id.btn_act);

        database=openOrCreateDatabase("테스트중",MODE_PRIVATE,null);
        Toast.makeText(this, "테스트중"+"으로 생성성공", Toast.LENGTH_SHORT).show();

        try {
            createSQL();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getFile();


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
            goToast(sqlselect());

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
        } else if (v.getId() == R.id.btn_act) {
            if (btn_act.getText().toString().equals("alchol")) {
                actTime = getThistime();
                goToast(actTime + "");
                whatisthis = "alchol";
                makeFile(todaytodayohglorytoday, actTime, whatisthis);
            } else if (btn_act.getText().toString().equals("exercise")) {
                actTime = getThistime();
                goToast(actTime + "");
                whatisthis = "exercise";
                makeFile(todaytodayohglorytoday, actTime, whatisthis);
            } else if (btn_act.getText().toString().equals("culture")) {
                actTime = getThistime();
                goToast(actTime + "");
                whatisthis = "culture";
                makeFile(todaytodayohglorytoday, actTime, whatisthis);
            }
        }
    }

    public void goToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
        String testing = "INSERT INTO everydayismine (date) VALUES (" + 333 + ")";
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
                newsqltext = "INSERT INTO everydayismine ("+ key +") VALUES (" + data + ") where date=" + date;
            else
                newsqltext = "INSERT INTO everydayismine ("+date +"," + key + ") VALUES ("+date +"," + data + ")";

            /* INSERT 문을 사용하여 테이블에 데이터 추가. */
            readStr = readStr + newsqltext;
            bw.write(readStr);
            bw.close();
            Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String sqlselect() {
        String sql1 = "select date from everydayismine";

        try {
            Cursor recordset = database.rawQuery(sql1, null);
            recordset.moveToFirst();
            String str = "이거 안되네";
            do {
                str += recordset.getInt(0) + "/"+ "\n";
            } while (recordset.moveToNext());
            recordset.close();
            return str;
        } catch (SQLiteException e) {

        }
        return "스큐엘 테스트 실패";
    }
}
