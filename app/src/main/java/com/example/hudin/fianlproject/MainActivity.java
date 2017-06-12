package com.example.hudin.fianlproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_meal,btn_bed,btn_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //새로고침
        if (item.getItemId() == R.id.refresh) {

        }
        //통계보기
        else if (item.getItemId() == R.id.statistics) {

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
    public void onClick(View v){
        if(v.getId()==R.id.btn_meal)
        {

        }
        else if(v.getId()==R.id.btn_bed)
        {

        }
        else if(v.getId()==R.id.btn_act)
        {

        }
    }
}
