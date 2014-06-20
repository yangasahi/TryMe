package mytry.test;

import mytry.gallary.GallaryActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class ReadJpgActivity extends Activity{

	private ImageView imageView = null;
	private Bitmap bm = null;
	private Button button = null;
	private Button button1 = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Window window = getWindow();// 得到窗口
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setContentView(R.layout.readjpg);
		
		Intent intent = getIntent();
	    String name = intent.getStringExtra("result");
		final String filepath = "/sdcard/jpg/" +name ;
		bm = BitmapFactory.decodeFile(filepath);
		imageView = (ImageView)findViewById(R.id.imageView);
		imageView.setImageBitmap(bm);
		button = (Button)findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			     Intent intent = new Intent();
			     intent.setClass(ReadJpgActivity.this, GallaryActivity.class);
			     startActivity(intent);
			}
		});
		button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("pathku", filepath);
				intent.setClass(ReadJpgActivity.this, PreviewItemActivity.class);
				startActivity(intent);
			}
		});
	}

}
