package com.testweatherinfo.app;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherInfo extends Activity {

	private String resposneString = null;

	private TextView cityNameTextView;
	private TextView publishTimeTextView;
	private LinearLayout weatherInfoLayout;
	private TextView weatherDateTextView;
	private TextView weatherDespTextView;
	private TextView temp1TextView;
	private TextView temp2TextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);

		cityNameTextView = (TextView) findViewById(R.id.city_name);
		publishTimeTextView = (TextView) findViewById(R.id.publish_time);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		
		
		weatherDespTextView = (TextView) findViewById(R.id.weather_desp);
		weatherDateTextView = (TextView) findViewById(R.id.weather_date);
		temp1TextView = (TextView) findViewById(R.id.temp1);
		temp2TextView = (TextView) findViewById(R.id.temp2);

		Intent intent = getIntent();
		resposneString = intent.getStringExtra("response");

		handleJSON();
		Toast.makeText( WeatherInfo.this, resposneString,Toast.LENGTH_LONG).show();
	}

	/**
	 * 解析JSON数据，并且显示到对应的组件当中。
	 */
	public void handleJSON() {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy年M月d日",Locale.CHINA);
		
		
		try {
			JSONObject jsonObject = new JSONObject(resposneString);
			JSONObject weatherInfoJsonObject = jsonObject
					.getJSONObject("weatherinfo");

			String cityNameString = weatherInfoJsonObject.getString("city");
			String publishTimeString = weatherInfoJsonObject.getString("ptime");
			String temp1 = weatherInfoJsonObject.getString("temp1");
			String temp2 = weatherInfoJsonObject.getString("temp2");
			String weatherString = weatherInfoJsonObject.getString("weather");

			
			cityNameTextView.setText( cityNameString);
			publishTimeTextView.setText( "今天"+publishTimeString+ "发布");
			temp1TextView.setText(temp1);
			temp2TextView.setText(temp2);
			weatherDespTextView.setText(weatherString);
			weatherDateTextView.setText(simpleDateFormat.format(new Date()));
			//weatherInfoLayout.setBackgroundResource(R.drawable.meinv);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
