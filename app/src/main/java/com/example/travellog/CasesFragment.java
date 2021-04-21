package com.example.travellog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CasesFragment extends Fragment implements View.OnClickListener {
    Button btn;
    TextView caseInfo;
    String a = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cases, container, false);

        btn = view.findViewById(R.id.casesBtn);
        caseInfo = view.findViewById(R.id.place);
        btn.setOnClickListener(this);

        return view;
    }

    public void onClick(View v) {

        String lon = "114.22506642557767";
        String lat = "22.31554554826026";
        //获取地址信息
        String urlGeo = MessageFormat.format("http://api.map.baidu.com/reverse_geocoding/v3/?ak=iBfpVE9e3BeGRPm1i2Gkq8bGenQm6h3b7&output=json&coordtype=wgs84ll&location={0},{1}",lat,lon);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(urlGeo).get().build();
        Call ca = okHttpClient.newCall(request);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = ca.execute();


                    assert response.body() != null;
                    String rebody = response.body().string();
                    //JSONObject jsonObject = JSONObject.parseObject(rebody);
                    HashMap map = JSONObject.parseObject(rebody, HashMap.class);

                    String str1 = JSON.toJSONString(map.get("result"));
                    HashMap result = JSONObject.parseObject(str1,HashMap.class);

                    String str2 = JSON.toJSONString(result.get("addressComponent"));
                    HashMap addr = JSONObject.parseObject(str2,HashMap.class);

                    String district = JSONObject.toJSONString(addr.get("district"));

                    //中西区，东区，葵青区，观塘区，西贡区，深水埗区，南区，荃湾区，
                    //湾仔区，黄大仙区，油尖旺区，元朗区，离岛区，九龙城区

                    switch (district){
                        case "\"中西区\"":a = "Central @ Western";System.out.println("中西区有comfirmed cases,请担心");break;
                        case "\"东区\"":a = "Eastern";System.out.println("东区有comfirmed cases,请担心");break;
                        case "\"葵青区\"":a = "Kwai Tsing";System.out.println("葵青区有comfirmed cases,请担心");break;
                        case "\"观塘区\"":a = "Kwun Tong";System.out.println("观塘区有comfirmed cases,请担心");break;
                        case "\"西贡区\"":a = "Sai Kung";System.out.println("西贡区有comfirmed cases,请担心");break;
                        case "\"深水埗区\"":a = "Sham Shui Po";System.out.println("深水埗区有comfirmed cases,请担心");break;
                        case "\"南区\"":a = "Southern";System.out.println("南区有comfirmed cases,请担心");break;
                        case "\"荃湾区\"":a = "Tsuen Wan";System.out.println("荃湾区有comfirmed cases,请担心");break;
                        case "\"湾仔区\"":a = "Wan Chai";System.out.println("湾仔区有comfirmed cases,请担心");break;
                        case "\"黄大仙区\"":a = "Wong Tai Sin";System.out.println("黄大仙区有comfirmed cases,请担心");break;
                        case "\"油尖旺区\"":a = "Yau Tsim Mong";System.out.println("油尖旺区有comfirmed cases,请担心");break;
                        case "\"元朗区\"":a = "Yuen Long";System.out.println("元朗区有comfirmed cases,请担心");break;
                        case "\"离岛区\"":a = "Islands";System.out.println("离岛区有comfirmed cases,请担心");break;
                        case "\"九龙城区\"":a = "Kowloon City";System.out.println("九龙城区有comfirmed cases,请担心");break;
                        case "\"屯门区\"":a = "Tuen Mun";System.out.println("屯门区有comfirmed cases,请担心");break;
                        default:a = "";
                    }
                    //获取类型名称的方法System.out.println(result.get("addressComponent").getClass().getName().toString());
                    //获取hkdata
                    String url = MessageFormat.format("https://api.data.gov.hk/v2/filter?q=%7B%22resource%22%3A%22http%3A%2F%2Fwww.chp.gov.hk%2Ffiles%2Fmisc%2Fbuilding_list_eng.csv%22%2C%22section%22%3A1%2C%22format%22%3A%22json%22%2C%22filters%22%3A%5B%5B1%2C%22eq%22%2C%5B%22{0}%22%5D%5D%5D%7D",a);
                    OkHttpClient confirms = new OkHttpClient();
                    Request conRe = new Request.Builder().url(url).get().build();
                    Call ca2 = confirms.newCall(conRe);

                    Response response2 = ca2.execute();
                    assert response2.body() != null;
                    String rebody2 = response2.body().string();
                    if(rebody2.equals("[]")){
                        caseInfo.setText("you are in "+ district +", it is safe here, just have fun.");
                    }else {
                        caseInfo.setText("you are in "+ district +", there are some confirmed cases, please take care.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}