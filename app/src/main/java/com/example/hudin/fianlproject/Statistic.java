package com.example.hudin.fianlproject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.hudin.fianlproject.R.id.comebackhome;

public class Statistic extends AppCompatActivity {

    ListView listview ;
    ListViewAdapter adapter;
    MainActivity main;
    int index=0;
    ArrayList<String> checkbox=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        main=new MainActivity();

        // Adapter 생성
        adapter = new ListViewAdapter(this) ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        updatedate();

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this,R.drawable.mon),
                "월요일", "Account Box Black 36dp") ;
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.wed),
                "수요일", "Account Circle Black 36dp") ;
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.thu),
                "목요일", "Assignment Ind Black 36dp") ;

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

    public void onClick(View v) {
        if(v.getId()==R.id.comebackhome) {
            Intent intent = new Intent(Statistic.this, MainActivity.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.Showstatistic) {

        }
    }
    public void updatedate(){
        index++;
        for(int i=0;i<checkbox.size();i++){
            if(checkbox.get(i).equals(getThisday()+""))
                return;
        }

        try {
            adapter.addItem(drawablefinder(sunmontue()),index+" 번째 하루",getThisday()+"");
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
}
