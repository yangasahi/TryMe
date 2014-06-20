package mytry.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MergerPhotoActivity extends Activity {

	private TouchView imgBackground = null;
	private TouchView imgWuPin = null;
	private TouchView imgWuPin2 = null;
	private Bitmap BmWupin = null;
	private Bitmap BmWupin2 = null;
	private Bitmap BmBackground = null;
    private LinearLayout helpButton = null;
	private LinearLayout shareButton = null;
    private LinearLayout reTry_Button = null;
    private LinearLayout gaojiButton = null;
	private ImageButton returnButton = null;
	private LinearLayout saveButton = null;
//	private LinearLayout adornButton = null;
	private String filepathBack = null;
	private String filepathWupin = null;
	private LinearLayout exitButton = null;
	private RelativeLayout bottomLayout = null;
	private FrameLayout fram = null;
	private boolean isExit = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// û�б���
		 Window window = getWindow();// �õ�����
		 window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// ���ø���
		setContentView(R.layout.mergerphoto);
		Parse.initialize(this, "bOGCpOSvl5fXzJDIp0s6PbeE4n3LhmqDGk5gXkMC", "8MWhJZjVTZ2eGRqIxmBKg18KN5HXsW0tJomhZBTC");
		ParseAnalytics.trackAppOpened(null);
		gaojiButton = (LinearLayout)findViewById(R.id.gaojiButton);
		gaojiButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				helpButton.setBackgroundColor(Color.TRANSPARENT);
				gaojiButton.setBackgroundColor(Color.BLUE);
				dialog();
			}
		});
		
		reTry_Button = (LinearLayout)findViewById(R.id.reTry_Button);
		reTry_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MergerPhotoActivity.this.setResult(3);
				MergerPhotoActivity.this.finish();
			}
		});
		bottomLayout = (RelativeLayout)findViewById(R.id.bottomLayout);
		exitButton = (LinearLayout)findViewById(R.id.exitButton);
		fram = (FrameLayout)findViewById(R.id.twill);
		exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isExit == true){
					   MergerPhotoActivity.this.setResult(1);
					   MergerPhotoActivity.this.finish();
				}else{
					Toast.makeText(MergerPhotoActivity.this, "�ٰ�һ���˳���һ�ԣ�", Toast.LENGTH_SHORT).show();
					isExit = true;
				}
	
			}
		});
        helpButton = (LinearLayout)findViewById(R.id.helpButton);
        helpButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				helpButton.setBackgroundColor(Color.BLUE);
				gaojiButton.setBackgroundColor(Color.TRANSPARENT);
				imgWuPin.setImageBitmap(BmWupin);
				imgBackground.setImageBitmap(BmBackground);
			}
		});
		imgBackground = (TouchView) findViewById(R.id.imgBackground);

		imgWuPin = (TouchView) findViewById(R.id.imgWuPin);
        
		imgWuPin2 = (TouchView) findViewById(R.id.imgWuPin2);

		Intent intent = getIntent();
		filepathBack = intent.getStringExtra("path");
		filepathWupin = intent.getStringExtra("pathwupin");
		BmWupin = BitmapFactory.decodeFile(filepathWupin);
		BmBackground = BitmapFactory.decodeFile(filepathBack);
		
		
		imgWuPin.setImageBitmap(BmWupin);
		imgBackground.setImageBitmap(BmBackground);

		shareButton = (LinearLayout) findViewById(R.id.shareButton);
		shareButton.setOnClickListener(new ShareButtonListener());
		returnButton = (ImageButton) findViewById(R.id.returnButton);
		returnButton.setOnClickListener(new ReturnButtonListener());
		saveButton = (LinearLayout) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new SaveButtonListener());
//		adornButton = (LinearLayout) findViewById(R.id.adornButton);
//		adornButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

	}

	class ShareButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
            share();
			helpButton.setBackgroundColor(Color.TRANSPARENT);
			gaojiButton.setBackgroundColor(Color.TRANSPARENT);
			Intent intent = new Intent();
			intent.putExtra("path", "/sdcard/TryMeShare/SharePicture.jpg");
			intent.setClass(MergerPhotoActivity.this, ShareActivity.class);
			// ʹ�����Intent����������ResultActivity
			MergerPhotoActivity.this.startActivity(intent);
		}
	}


	class ReturnButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			finish();

		}
	}

	
	class SaveButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			helpButton.setBackgroundColor(Color.TRANSPARENT);
			gaojiButton.setBackgroundColor(Color.TRANSPARENT);
			  Rect frame = new Rect();
			  getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
	        int statusBarHeight = frame.top;
			View view = getWindow().getDecorView();
			view.layout(0,0,fram.getWidth(),
					fram.getHeight()+bottomLayout.getHeight() + statusBarHeight);
			view.setDrawingCacheEnabled(true);
			view.buildDrawingCache();
			Bitmap bitmap = view.getDrawingCache();
			Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0,
					bottomLayout.getHeight()+statusBarHeight, fram.getWidth(),
					fram.getHeight()-10);
			// ����һ��λ��SD���ϵ��ļ�
			File file = new File("/sdcard/TryMeShare/");
			file.mkdir();// �����ļ���
			FileOutputStream outStream = null;
			try {
				// ��ָ���ļ���Ӧ�������
				outStream = new FileOutputStream("/sdcard/TryMeShare/SharePicture.jpg");
				// ��λͼ���뵽ָ���ļ���
				bitmap2.compress(CompressFormat.JPEG, 100, outStream);
				outStream.close();
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			Toast.makeText(MergerPhotoActivity.this,
					"ͼƬTryMeShare�ѱ�����SD��TryMeShare�ļ��£�", Toast.LENGTH_SHORT).show();
			// imgWuPin2.setImageBitmap(bitmap2);
		}
	}
   
	private void dialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(MergerPhotoActivity.this);  
		builder.setTitle("�߼�ѡ��").setItems( new String[] {"ֻ����Ʒ", "ֻ������","һ�����"}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(which == 0){
					imgBackground.setable(false);
					imgWuPin.setable(true);
					dialog.cancel();  
					gaojiButton.setBackgroundColor(Color.TRANSPARENT);
				}else if(which == 1){
					imgBackground.setable(true);
					imgWuPin.setable(false);
					dialog.cancel();  
					gaojiButton.setBackgroundColor(Color.TRANSPARENT);
				}else if(which == 2){
					imgBackground.setable(true);
					imgWuPin.setable(true);
					dialog.cancel();  
					gaojiButton.setBackgroundColor(Color.TRANSPARENT);
				}
//				else if(which == 3){
//					share();
//					ParseUser currentUser = ParseUser.getCurrentUser();
//					if(currentUser != null){
//						Intent intent = new Intent();
//						intent.putExtra("path", "/sdcard/TryMeShare/SharePicture.jpg");
//						intent.setClass(MergerPhotoActivity.this, AddImgActivity.class);
//						MergerPhotoActivity.this.startActivity(intent);
//					}else{
//						Intent intent = new Intent();
//						intent.setClass(MergerPhotoActivity.this, LoginActivity.class);
//						MergerPhotoActivity.this.startActivity(intent);
//					}
//					dialog.cancel();  
//					gaojiButton.setBackgroundColor(Color.TRANSPARENT);
//				}
//				}else if(which == 3){
//					imgBackground.setable(true);
//					imgWuPin.setable(true);
//					imgWuPin.setxzFlag(true);
//					dialog.cancel();  
//					gaojiButton.setBackgroundColor(Color.TRANSPARENT);
//				}
			}
		}) .setCancelable(false)  
		       .setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {  
		           public void onClick(DialogInterface dialog, int id) {  
		                dialog.cancel();  
		                gaojiButton.setBackgroundColor(Color.TRANSPARENT);
		           }  
		       });  
		AlertDialog alert = builder.create();  
		alert.show();
	}
	private void share(){
		  Rect frame = new Rect();
		  getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
		View view = getWindow().getDecorView();
		view.layout(0,0,fram.getWidth(),
				fram.getHeight()+bottomLayout.getHeight() + statusBarHeight);
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0,
				bottomLayout.getHeight()+statusBarHeight, fram.getWidth(),
				fram.getHeight()-10);
		// ����һ��λ��SD���ϵ��ļ�
		File file = new File("/sdcard/TryMeShare/");
		file.mkdir();// �����ļ���
		FileOutputStream outStream = null;
		try {
			// ��ָ���ļ���Ӧ�������
			outStream = new FileOutputStream("/sdcard/TryMeShare/SharePicture.jpg");
			// ��λͼ���뵽ָ���ļ���
			bitmap2.compress(CompressFormat.JPEG, 100, outStream);
			outStream.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}