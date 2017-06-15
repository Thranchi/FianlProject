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
import android.test.mock.MockContext;
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

    Statistic statistic=new Statistic();
    ListViewItem ListViewItem=new ListViewItem();
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
        Toast.makeText(this,Integer.parseInt(getThisday()+"")+"",Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_meal = (Button) findViewById(R.id.btn_meal);
        btn_bed = (Button) findViewById(R.id.btn_bed);
        btn_act = (Button) findViewById(R.id.btn_act);

        btn_act.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (actstatus==0) {
                    actTime = getThistime();
                    goToast(actTime + "");
                    whatisthis = "alchol";
                    statistic.inputDataToTable(getThisday(),whatisthis,1);
                } else if (actstatus==1) {
                    actTime = getThistime();
                    goToast(actTime + "");
                    whatisthis = "exercise";
                    statistic.inputDataToTable(getThisday(),whatisthis,1);
                } else if (actstatus==2) {
                    actTime = getThistime();
                    goToast(actTime + "");
                    whatisthis = "culture";
                    statistic.inputDataToTable(getThisday(),whatisthis,1);
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

        //통계보기
        if (item.getItemId() == R.id.statistics) {
            Intent intent = new Intent(MainActivity.this, Statistic.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_meal) {

            inputTime = getThistime();
            if (inputTime > 0600 && inputTime < 1030) {
                breackfast = inputTime;
                whatisthis = "brtime";
                statistic.inputDataToTable(getThisday(),whatisthis,breackfast);
            } else if (inputTime > 1030 && inputTime < 1630) {
                lunch = inputTime;
                whatisthis = "luntime";
                statistic.inputDataToTable(getThisday(),whatisthis,lunch);
            } else if (inputTime > 1630 && inputTime < 2100) {
                dinner = inputTime;
                whatisthis = "dintime";
                statistic.inputDataToTable(getThisday(),whatisthis,dinner);

            }
        } else if (v.getId() == R.id.btn_bed) {
            if (ifitistrueitismorning) {
                wakeTime = getThistime();
                ifitistrueitismorning = false;
                whatisthis = "waketime";
                statistic.inputDataToTable(getThisday(),whatisthis,wakeTime);
            } else {
                sleepTime = getThistime();
                ifitistrueitismorning = true;
                whatisthis = "bedtime";
                statistic.inputDataToTable(getThisday(),whatisthis,sleepTime);
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

