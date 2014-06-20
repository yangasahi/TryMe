package mytry.test;

import mytry.utils.AsyncImageLoader;
import mytry.utils.CallbackImpl;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class ImgItemActivity extends Activity{

	private String imgUrl = null;
	private AsyncImageLoader loader = new AsyncImageLoader();
	private ImageView imageView = null;
	private ImageButton imageButton,returnButton = null;
	private Bitmap bm = null;
	private String imgId = null;
	private ProgressDialog progressDialog = null;
	private boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.itemimg);
		Parse.initialize(this, "bOGCpOSvl5fXzJDIp0s6PbeE4n3LhmqDGk5gXkMC", "8MWhJZjVTZ2eGRqIxmBKg18KN5HXsW0tJomhZBTC");
		ParseAnalytics.trackAppOpened(null);
		
		imageView = (ImageView)findViewById(R.id.imageView);
		imageButton = (ImageButton)findViewById(R.id.imageButton);
		returnButton = (ImageButton)findViewById(R.id.returnButton);
		returnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImgItemActivity.this.finish();
			}
		});
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(flag == false){
					progressDialog = ProgressDialog.show(ImgItemActivity.this, "请稍等",
							"正在提取物品...", true);
					progressDialog.setCancelable(true);
					ParseQuery paQuery = new ParseQuery("goods");
					 paQuery.getInBackground(imgId, new GetCallback() {
						
						@Override
						public void done(ParseObject parse, ParseException arg1) {
							// TODO Auto-generated method stub
							if(arg1 == null){
								if(parse != null){
									parse.put("downcount", parse.getInt("downcount") + 1);
									parse.saveInBackground(new SaveCallback() {
										
										@Override
										public void done(ParseException arg0) {
											// TODO Auto-generated method stub
											if(arg0 == null){
												progressDialog.dismiss();
												Intent intent = new Intent();
												intent.putExtra("path", "/sdcard/TryMe/download.png");
												intent.setClass(ImgItemActivity.this, TakeBackgphotoActivity.class);
												ImgItemActivity.this.startActivityForResult(intent, 1);
											}
										}
									});
								}
							}
						}
					});
				}else{
					Intent intent = new Intent();
					intent.putExtra("path", "/sdcard/TryMe/download.png");
					intent.setClass(ImgItemActivity.this, TakeBackgphotoActivity.class);
					ImgItemActivity.this.startActivityForResult(intent, 1);
				}
				
				
			}
		});
		Intent intent = getIntent();
		imgUrl = intent.getStringExtra("imageUrl");
		imgId = intent.getStringExtra("imgId");
		loadImage(imgUrl, imageView);
	}
	
	private void loadImage(final String url, ImageView imageView) {
		CallbackImpl callbackImpl = new CallbackImpl(imageView,imageButton);
		Drawable cacheImage = loader.loadDrawable(url, callbackImpl);
		if (cacheImage != null) {
			imageView.setImageBitmap(bm);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1){
			if(resultCode == 1){
				ImgItemActivity.this.setResult(1);
				ImgItemActivity.this.finish();
			}else{
				if(resultCode == 6){
					flag = true;
				}
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
