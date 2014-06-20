package mytry.test;

import java.util.List;

import mytry.utils.ShareItemAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class SharePlatformActivity extends Activity {

	private GridView gridView = null;
	private ShareItemAdapter imageAdapter = null;
	private ProgressDialog progressDialog = null;
	private ImageButton returnButton = null;
	private LinearLayout net_Button = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.shareplatform);
		Parse.initialize(this, "bOGCpOSvl5fXzJDIp0s6PbeE4n3LhmqDGk5gXkMC", "8MWhJZjVTZ2eGRqIxmBKg18KN5HXsW0tJomhZBTC");
		ParseAnalytics.trackAppOpened(null);
		progressDialog = ProgressDialog.show(SharePlatformActivity.this, "请稍等",
				"正在努力加载中...", true);
		progressDialog.setCancelable(true);
		net_Button = (LinearLayout)findViewById(R.id.net_Button);
		net_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ParseUser currentUser = ParseUser.getCurrentUser();
				if (currentUser != null) {
					Intent intent = new Intent();
					intent.setClass(SharePlatformActivity.this, MyShareActivity.class);
					SharePlatformActivity.this.startActivity(intent);
				}else{
					Intent intent2 = new Intent();
					intent2.setClass(SharePlatformActivity.this, LoginActivity.class);
					SharePlatformActivity.this.startActivity(intent2);
				}
				
			}
		});
		gridView = (GridView) findViewById(R.id.gridview);
		returnButton = (ImageButton)findViewById(R.id.returnButton);
		returnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharePlatformActivity.this.finish();
			}
		});
		ParseQuery parses = new ParseQuery("sharepic");
		parses.findInBackground(new FindCallback() {
			
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				// TODO Auto-generated method stub
				if(arg0.size() == 0){
					Toast.makeText(SharePlatformActivity.this, "抱歉,暂无分享.!", Toast.LENGTH_SHORT).show();
				}else{
					imageAdapter = new ShareItemAdapter(arg0, SharePlatformActivity.this);
					gridView.setAdapter(imageAdapter);
				}
				progressDialog.dismiss();
			}
		});
	
		// 添加消息处理
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> arg0,// The AdapterView where
//														// the click happened
//					View arg1,// The view within the AdapterView that was
//								// clicked
//					int arg2,// The position of the view in the adapter
//					long arg3// The row id of the item that was clicked
//			) {
//				// 在本例中arg2=arg3
//				HashMap<String, Object> item = (HashMap<String, Object>) arg0
//						.getItemAtPosition(arg2);
//				// 显示所选Item的ItemText
//				setTitle((String) item.get("ItemText"));
//			}
//		});
	}
}
