package mytry.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

public class PlatformActivity extends Activity{

	private ImageButton returnButton,storeButton,sharePlatformButon = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.platform);
		returnButton = (ImageButton)findViewById(R.id.returnButton);
		returnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PlatformActivity.this.finish();
			}
		});
		storeButton = (ImageButton)findViewById(R.id.storeButton);
		storeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(PlatformActivity.this, StoreActivity.class);
			    PlatformActivity.this.startActivityForResult(intent, 1);
			}
		});
		sharePlatformButon =  (ImageButton)findViewById(R.id.sharePlatformButon);
		sharePlatformButon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(PlatformActivity.this, SharePlatformActivity.class);
				PlatformActivity.this.startActivity(intent);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 1){
		PlatformActivity.this.setResult(1);
		PlatformActivity.this.finish();
	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
