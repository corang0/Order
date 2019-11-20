package com.superdroid.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GridActivity extends AppCompatActivity {

    private TextView selected_item_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        selected_item_textview = (TextView)findViewById(R.id.selected_item_textview);

        List<String> list = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {

                //클릭한 아이템의 문자열을 가져옴
                String selected_item = (String)adapterView.getItemAtPosition(position);

                //텍스트뷰에 출력
                selected_item_textview.setText(selected_item);

                Intent intent = new Intent(getBaseContext(), ResultActivity.class);

                intent.putExtra("item1", selected_item);

                startActivity(intent);
            }
        });

        list.add("사과");
        list.add("배");
        list.add("귤");
        list.add("바나나");
        list.add("사과");
        list.add("배");
        list.add("귤");
        list.add("바나나");
        list.add("사과");
        list.add("배");
        list.add("귤");
        list.add("바나나");
        list.add("사과");
        list.add("배");
        list.add("귤");
        list.add("바나나");
    }
}
