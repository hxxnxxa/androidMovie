package com.real.movie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

public class DetailActivity extends AppCompatActivity {

    TextView tv_mvname;
    TextView tv_audience;
    TextView tv_accumulate;
    TextView tv_director;
    TextView tv_actor;
    TextView tv_movietime;

    String movieCd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_mvname = findViewById(R.id.tv_mvname);
        tv_audience = findViewById(R.id.tv_audience);
        tv_accumulate = findViewById(R.id.tv_accumulate);
        tv_director = findViewById(R.id.tv_director);
        tv_actor = findViewById(R.id.tv_actor);
        tv_movietime = findViewById(R.id.tv_movietime);

        String movieCd = getIntent().getStringExtra("movieCd");
        String audiCnt = getIntent().getStringExtra("audiCnt");
        String audiAcc = getIntent().getStringExtra("audiAcc");

        Log.d("hwa", movieCd + "," + audiCnt + "," + audiAcc);

        request();
    }

    public void request() {
        String movieCd = getIntent().getStringExtra("movieCd");

        // 영화 상세정보 API
        String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?";
        url += "key=a7c45005c41493176da037105d88da0d";
        url += "&movieCd=" + getIntent().getStringExtra("movieCd");
        ;

        // HTTP 통신
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest myReq = new StringRequest(Request.Method.GET, url, successListener, errorListener);
        requestQueue.add(myReq);
    }

    //통신 성공했을 때
    Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("hwa", "res: " + response);
            Log.d("hwa", "통신성공");
            try {
                JSONObject jsonObject1 = new JSONObject(response);
                Log.d("hwa", "jsonObject1 : "+jsonObject1);

                JSONObject jsonObject2 = new JSONObject(jsonObject1); //movieCd(o), movieNm, movieNmEn, movieNmOg, showTm(o), prdfYear, openDt, prdStafNm, typeNm
                Log.d("hwa", "jsonObject2 : "+jsonObject2);

                String jsonObject3 = jsonObject2

//                String directors = jsonObject2.getString("directors"); //영화 상세 정보 - 감독
//                String actors = jsonObject2.getString("actors"); //영화 상세 정보 - 배우
//                String showTm = jsonObject2.getString("showTm");
//
//                Log.d("hwa", "directors : " + directors + ", "+actors+", "+showTm);
//
//                for (int i = 0; i < jsonArray1.length(); i++) {
//                    JSONObject jsonObject4 = jsonArray1.getJSONObject(i);
//                    String movieCd = jsonObject4.getString("movieCd"); //일별 박스오피스 - 영화의 대표코드
//                    //String directors = jsonObject4.getString("directors"); //영화 상세 정보 - 감독
//                    //String actors = jsonObject3.getString("actors"); //영화 상세 정보 - 배우
//                    //String showTm = jsonObject2.getString("showTm"); //영화 상세 정보 - 상영시간
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    //통신 실패했을 때
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("hwa", "통신 실패");
        }
    };
}