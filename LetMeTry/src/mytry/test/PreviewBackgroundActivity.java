package mytry.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PreviewBackgroundActivity extends Activity {
	private LinearLayout tryonButton,retakeButton;
//	private LinearLayout shareButton;
	private ImageButton returnButton;
//	private LinearLayout adornButton;
	private Bitmap bm = null;
	private ImageView imageview = null;
	private String path = null;
	private String pathwupin = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		 Window window = getWindow();// 得到窗口
		 window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 设置高亮
        setContentView(R.layout.previewbackground);
        retakeButton = (LinearLayout)findViewById(R.id.retakeButton);
        retakeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PreviewBackgroundActivity.this.setResult(2);
				PreviewBackgroundActivity.this.finish();
			}
		});
        imageview = (ImageView) findViewById(R.id.imageView1);
		
      //接收上一个Activity传过来的相片地址
        Intent intent = getIntent();
		path = intent.getStringExtra("path");
		pathwupin = intent.getStringExtra("pathwupin");
		
		if(path == null){
			 path = "/sdcard/TryMeBack/Background.jpg";
		}
			bm = BitmapFactory.decodeFile(path);
       
		imageview.setImageBitmap(bm);
        tryonButton=(LinearLayout)findViewById(R.id.tryonButton);
        tryonButton.setOnClickListener(new TryonButtonListener());
//        shareButton=(LinearLayout)findViewById(R.id.shareButton);
//        shareButton.setOnClickListener(new ShareButtonListener());
        returnButton=(ImageButton)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new ReturnButtonListener());
//        adornButton=(LinearLayout)findViewById(R.id.adornButton);
//        adornButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.putExtra("filepath", path);
//				intent.setClass(PreviewBackgroundActivity.this, AdorActivity.class);
//				startActivity(intent);
//			}
//		});
    }
    class TryonButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra("pathwupin", pathwupin);
			intent.putExtra("path", path);
			intent.setClass(PreviewBackgroundActivity.this, MergerPhotoActivity.class);
			//使用这个Intent对象来启动ResultActivity
			PreviewBackgroundActivity.this.startActivityForResult(intent, 1);
		}
    }
    class ShareButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
		
			
			Intent intent = new Intent();
			intent.setClass(PreviewBackgroundActivity.this, ShareActivity.class);
			//使用这个Intent对象来启动ResultActivity
			PreviewBackgroundActivity.this.startActivity(intent);
		}
    }
    class HomeButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
		
			
			Intent intent = new Intent();
			intent.setClass(PreviewBackgroundActivity.this, TakeItemphotoActivity.class);
			//使用这个Intent对象来启动ResultActivity
			PreviewBackgroundActivity.this.startActivity(intent);
		}
    }
    class ReturnButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
		PreviewBackgroundActivity.this.setResult(2);
		PreviewBackgroundActivity.this.finish();
			
		}
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1) {
			switch (requestCode) {
		    case 1:
		    	PreviewBackgroundActivity.this.setResult(1);
		    	PreviewBackgroundActivity.this.finish();
		    	break;
		}
	}else if (resultCode == 1) {
		switch (requestCode) {
	    case 1:
	    	PreviewBackgroundActivity.this.setResult(1);
	    	PreviewBackgroundActivity.this.finish();
	    	break;
	}
}else if (resultCode == 3) {
	switch (requestCode) {
    case 1:
    	PreviewBackgroundActivity.this.setResult(3);
    	PreviewBackgroundActivity.this.finish();
    	break;
}
}
		super.onActivityResult(requestCode, resultCode, data);
	}
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			PreviewBackgroundActivity.this.setResult(2);
			PreviewBackgroundActivity.this.finish();
		}
		return true;
	}
}