package mytry.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class PreviewItemActivity extends Activity {
	private LinearLayout choiceBackgroundButton;
	private LinearLayout shareButton;
	private ImageButton returnButton;
//	private LinearLayout adornButton;
	private ImageView imageview = null;
	private Bitmap bm = null;
	private String path = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		 Window window = getWindow();// 得到窗口
		 window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 设置高亮
        setContentView(R.layout.previewitem);
        imageview = (ImageView) findViewById(R.id.imageView1);
        Intent intent = getIntent();
   
		path = intent.getStringExtra("path");
			bm = BitmapFactory.decodeFile(path);
		imageview.setImageBitmap(bm);
        choiceBackgroundButton=(LinearLayout)findViewById(R.id.choiceBackgroundButton);
        choiceBackgroundButton.setOnClickListener(new ChoiceBackgroundButtonListener());
        shareButton=(LinearLayout)findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new ShareButtonListener());
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
//				intent.setClass(PreviewItemActivity.this, AdorActivity.class);
//				startActivity(intent);
//			}
//		});
    }
    class ChoiceBackgroundButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			Intent intent = new Intent();
			intent.putExtra("path", path);
			intent.setClass(PreviewItemActivity.this, TakeBackgphotoActivity.class);
			//使用这个Intent对象来启动ResultActivity
			PreviewItemActivity.this.startActivityForResult(intent, 1);
		}
    }
    class ShareButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
		
			
			Intent intent = new Intent();
			intent.putExtra("path", path);
			intent.setClass(PreviewItemActivity.this, ShareActivity.class);
			//使用这个Intent对象来启动ResultActivity
			PreviewItemActivity.this.startActivity(intent);
		}
    }

    class ReturnButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
		    PreviewItemActivity.this.setResult(2);
			finish();
			
		}
    }
   
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1) {
			switch (requestCode) {
		    case 1:
		    	PreviewItemActivity.this.setResult(1);
		    	PreviewItemActivity.this.finish();
		    	break;
		}
	}else if(resultCode == 3){
		PreviewItemActivity.this.setResult(3);
		PreviewItemActivity.this.finish();
	}
		super.onActivityResult(requestCode, resultCode, data);
	}
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			PreviewItemActivity.this.setResult(2);
			PreviewItemActivity.this.finish();
		}
		return true;
	}
}