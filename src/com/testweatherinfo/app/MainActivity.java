package com.testweatherinfo.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button sendButton;
	private Button swicthButton;
	private EditText locationEditText;
	private TextView infoTextView;
	private String path = "http://www.weather.com.cn/data/cityinfo/101270101.html";
	private String path2 = "http://www.weather.com.cn/data/list3/city270101.xml";

	private final String PATH_HEAD = "http://www.weather.com.cn/data/list3/city";
	private final String PATH_TAIL = ".xml";
	private final int SHOW_RESPONSE = 0;
	private final int SHOW_EXCEPTION = 1;
	private final int SHOW_FAIL = 0;
	
	//Ϊë������ private ���� Ҳ���� ֱ�Ӵ��ݵ���һ������У�������������������
	private String responseResult = null ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sendButton = (Button) findViewById(R.id.bt_request);
		swicthButton = (Button) findViewById(R.id.bt_swicth);

		locationEditText = (EditText) findViewById(R.id.ed_location);
		infoTextView = (TextView) findViewById(R.id.info);
		
		locationEditText.setText(path);

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				path = locationEditText.getText().toString();
				sendRequestWithHttpUrlConnetion();
			}
		});

		/**
		 * button����¼� �л��� �� WeatherInfo � ��ʽ����ʾ ������Ϣ��
		 */
		swicthButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(MainActivity.this, WeatherInfo.class);

				intent.putExtra( "response", responseResult);
				
				startActivity(intent);
			}
		});
	}

	/**
	 * �����߳��ж���һ��Handler �������� ���� Message ��Ϣ����ʾ�ӷ��������ص���Ϣ��
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SHOW_RESPONSE) {
				String response = (String) msg.obj;

				responseResult = response;
				
				infoTextView.setText(response);

			} else if (msg.what == SHOW_FAIL) {
				String response = (String) msg.obj;
				infoTextView.setText(response);
			} else if (msg.what == SHOW_EXCEPTION) {
				String response = (String) msg.obj;
				infoTextView.setText(response);
			}

		};

	};

	/**
	 * �������粢 ���شӷ������õ�������
	 */
	public void sendRequestWithHttpUrlConnetion() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(path);

					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();

					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);

					if (connection.getResponseCode() == 200) {
						InputStream iStream = connection.getInputStream();

						BufferedReader bufferedReader = new BufferedReader(
								new InputStreamReader(iStream));

						String line = null;
						//���� response ���������� null ���� ��response�������� �����  ���null �ַ� Ӱ�� ���յĽ����
						String response ="";

						while ((line = bufferedReader.readLine()) != null) {
							response += line;
						}

						Message message = new Message();

						message.what = SHOW_RESPONSE;

						message.obj = response;

						handler.sendMessage(message);

					} else {
						Message message = new Message();

						message.what = SHOW_RESPONSE;

						message.obj = "��ȡ��������ʧ�ܣ���Ӧ�� != 200";

						handler.sendMessage(message);

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message message = new Message();

					message.what = SHOW_RESPONSE;

					message.obj = "�������ʱ���׳��쳣";

					handler.sendMessage(message);

				}

			}
		}).start();

	}

}
