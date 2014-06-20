package mytry.test;

import java.util.List;

import mytry.utils.ShareItemAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class MyShareActivity extends Activity{
	private GridView gridView = null;
	private ShareItemAdapter imageAdapter = null;
	private ProgressDialog progressDialog = null;
	private ImageButton returnButton = null;
	private LinearLayout net_Button = null;
	private TextView widget40 = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		 Window window = getWindow();// 得到窗口
		 window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 设置高亮
		setContentView(R.layout.shareplatform);
		Parse.initialize(this, "bOGCpOSvl5fXzJDIp0s6PbeE4n3LhmqDGk5gXkMC", "8MWhJZjVTZ2eGRqIxmBKg18KN5HXsW0tJomhZBTC");
		ParseAnalytics.trackAppOpened(null);
		ParseUser currentUser = ParseUser.getCurrentUser();
		widget40 = (TextView)findViewById(R.id.widget40);
		widget40.setText("我的分享");
			progressDialog = ProgressDialog.show(MyShareActivity.this, "请稍等",
					"正在努力加载中...", true);
			progressDialog.setCancelable(true);
			net_Button = (LinearLayout)findViewById(R.id.net_Button);
			net_Button.setVisibility(View.GONE);
			gridView = (GridView) findViewById(R.id.gridview);
			returnButton = (ImageButton)findViewById(R.id.returnButton);
			returnButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MyShareActivity.this.finish();
				}
			});
			ParseQuery parses = new ParseQuery("sharepic");
			parses.whereEqualTo("username", currentUser.getUsername().toString());
			parses.findInBackground(new FindCallback() {
				
				@Override
				public void done(List<ParseObject> arg0, ParseException arg1) {
					// TODO Auto-generated method stub
					if(arg0.size() == 0){
						Toast.makeText(MyShareActivity.this, "抱歉,暂无分享.!", Toast.LENGTH_SHORT).show();
					}else{
						imageAdapter = new ShareItemAdapter(arg0, MyShareActivity.this);
						gridView.setAdapter(imageAdapter);
					}
					progressDialog.dismiss();
				}
			});
		
		
	}

}
