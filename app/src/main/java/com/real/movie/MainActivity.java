package com.real.movie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


//convert custom listview

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //MoviewData 저장
    ArrayList<MovieData> arr =new ArrayList<>();

    //ListView를 띄워주기 위해 adapter 사용
    MyAdapter adapter;

    //activity_main.xml에 선언된 것들 불러오기
    ListView lv;
    EditText yearEt;
    EditText monthEt;
    EditText dateEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xml에 선언한 것들 java코드랑 연동해주기
        lv = findViewById(R.id.lv);
        yearEt = findViewById(R.id.year_et);
        monthEt = findViewById(R.id.month_et);
        dateEt = findViewById(R.id.date_et);

        /**
         *  Adapter 쓰는 이유 : 사용자 데이터를 입력받아 View를 생성하는 것,
         *  Adapter에서 생성되는 View는 ListView 내 하나의 아이템 영역에 표시됨
         *
         *  Custom ListView : 사용자 정의로 다양한 위젯들로 구성된 ListView
         *
         *  워크플로우
         *  1. ListView가 표시될 위치 구성 (activity_main.xml 에 listview 추가)
         *  2. ListView 아이템에 대한 Layout 구성(item.xml 생성 후 한 줄 구성)
         *  3. 아이템 데이터에 대한 클래스 정의 (class ListViewItem)
         *  4. Adapter 클래스 상속 및 구현 (class ListViewAdapter)
         *  5. Adapter 생성 후 ListView에 지정
         *  6. ListView 클릭 이벤트 처리
         *
         */

        adapter = new MyAdapter(this);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);

        findViewById(R.id.search_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });
    }

    public void request(){
        String year = yearEt.getText().toString();
        String month = monthEt.getText().toString();
        if(month.length()<2){
            month = "0"+month;
        }
        String date = dateEt.getText().toString();
        if(date.length()<2){
            date = "0"+date;
        }

        // 일별 박스오피스 API
        String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?";
        url += "key=a7c45005c41493176da037105d88da0d";
        url += "&targetDt="+year+month+date;

        //HTTP 통신
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, url, successListener, errorListener);
        requestQueue.add(myReq);
    }

    //통신 성공했을 때
    Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("hwa","res: "+response);
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                JSONObject jsonObject2 = jsonObject1.getJSONObject("boxOfficeResult");
                JSONArray jsonArray1 = jsonObject2.getJSONArray("dailyBoxOfficeList");

                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject3 = jsonArray1.getJSONObject(i);
                    String name = jsonObject3.getString("movieNm"); //일별 박스오피스 - 영화명(국문)
                    String salesAmt = jsonObject3.getString("salesAmt"); //일별 박스오피스 - 해당일의 매출액
                    String salesAcc = jsonObject3.getString("salesAcc"); //일별 박스오피스 - 누적매출액
                    String movieCd =jsonObject3.getString("movieCd"); //일별 박스오피스 - 영화의 대표코드
                    arr.add(new MovieData(name, salesAmt, salesAcc,movieCd)); //arr에 저장
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    //통신 실패했을 때
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("hwa","통신 실패");
        }
    };




    // 3.아이템 데이터에 대한 클래스 정의 (class ListViewItem)
    class ItemHolder{
        TextView titleTvHolder;
        TextView audiDayTvHolder;
        TextView audiTotalTvHolder;
    }

    // 4. Adapter 클래스 상속 및 구현 (class ListViewAdapter)
    // 5. Adapter 생성 후 ListView에 지정
    class MyAdapter extends ArrayAdapter {
        LayoutInflater lnf;

        public MyAdapter(Activity context) {
            super(context, R.layout.item, arr);
            lnf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return arr.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return arr.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ItemHolder viewHolder;
            if (convertView == null) {

                convertView = lnf.inflate(R.layout.item, parent, false);
                viewHolder = new ItemHolder();

                viewHolder.titleTvHolder = convertView.findViewById(R.id.title_tv);
                viewHolder.audiDayTvHolder = convertView.findViewById(R.id.audi_day_tv);
                viewHolder.audiTotalTvHolder = convertView.findViewById(R.id.audi_total_tv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ItemHolder) convertView.getTag();
            }

            viewHolder.titleTvHolder.setText("제목: "+arr.get(position).title);
            viewHolder.audiDayTvHolder.setText("오늘 관객: "+arr.get(position).audiDay);
            viewHolder.audiTotalTvHolder.setText("누적 관객: "+arr.get(position).audiTotal);

            return convertView;
        }
    }

    // 6. ListView 클릭 이벤트 처리
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("hwa","cd: "+ arr.get(i).movieCd);
        Intent intent = new Intent(this, com.real.movie.DetailActivity.class);
        intent.putExtra("movieCd", arr.get(i).movieCd); //영화코드
        intent.putExtra("audiCnt", arr.get(i).audiDay); //
        intent.putExtra("audiAcc", arr.get(i).audiTotal); //

        startActivity(intent);
    }
}