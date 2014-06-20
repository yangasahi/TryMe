package mytry.test;

import mytry.mms.MMSInfo;
import mytry.mms.MMSSender;
import mytry.test.R;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class InputActivity extends Activity{

	private EditText input = null; 
	private Button confirmButton = null;
	private Button closeButton = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Window window = getWindow();// �õ�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);// û�б���
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ȫ��
		
		setContentView(R.layout.input);
		closeButton = (Button)findViewById(R.id.closeButton);
		closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		confirmButton = (Button)findViewById(R.id.confirm);
		input = (EditText)findViewById(R.id.inputEdit);
		confirmButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 //�ر�WIFI
		        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		        if (wifiManager.isWifiEnabled()) {  

		        	wifiManager.setWifiEnabled(false);  
		        	
		        	} 
		        //��ȡ��ǰ����
		        ConnectivityManager conManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); 
		        NetworkInfo info = conManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
		        String currentAPN = info.getExtraInfo(); 
		        conManager.startUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, "mms");
		        currentAPN = info.getExtraInfo(); 
		        //ֻ��CMWAP���ܷ��Ͳ���
		        if("cmwap".equals(currentAPN))
		        {
		        	sendMMS();
		        }
		    }
		    void sendMMS()
		    {
		    	final MMSInfo mms = new MMSInfo(InputActivity.this, input.getText().toString());//���͵��ֻ���
		        String path = "file://mnt/sdcard//TryMe/picture.jpg";//�跢�͵�ͼƬ
		        System.out.println("--->" + path);
		        mms.addPart(path);// file://mnt/sdcard//1.jpg
		        final MMSSender sender = new MMSSender();
		        new Thread() {
		                public void run() {
		                        try {
		                                byte[] res = sender.sendMMS(InputActivity.this,
		                                                mms.getMMSBytes());
		                                System.out.println("-==-=-=>>> " + res.toString());
		                        } catch (Exception e) {
		                                // TODO Auto-generated catch block
		                                e.printStackTrace();
		                        }
		                };
		        }.start();
			}
		});
	}

	
	
}
