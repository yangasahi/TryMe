package mytry.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

public class FindActivity extends Activity{

	private ImageButton backButton = null;
	private Button submitButton = null;
	private EditText email = null;
	private ProgressDialog progressDialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.find_pass);
		Parse.initialize(this, "bOGCpOSvl5fXzJDIp0s6PbeE4n3LhmqDGk5gXkMC", "8MWhJZjVTZ2eGRqIxmBKg18KN5HXsW0tJomhZBTC");
		ParseAnalytics.trackAppOpened(null);
		email = (EditText)findViewById(R.id.login_user_edit);
		backButton = (ImageButton)findViewById(R.id.img_search);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FindActivity.this.finish();
			}
		});
		submitButton = (Button)findViewById(R.id.login_login_btn);
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog = ProgressDialog.show(FindActivity.this, "发送请求中",
						"请稍等...", true);
				progressDialog.setCancelable(true);
				ParseUser.requestPasswordResetInBackground(email.getText().toString(), new com.parse.RequestPasswordResetCallback() {
					
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub
						 if (e == null) {
					        	printResult("已发送一份重置密码的指令到用户的邮箱","success");
					        } else {
					        	printResult("重置密码出错","false");
					        }
					}
				});

			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//   			FindActivity.this.progressDialog.cancel();
//   			FindActivity.this.progressDialog.dismiss();
			FindActivity.this.finish();
		}
		return true;
	}
	
	private void printResult(String msg,String msg2) {
		progressDialog.dismiss();
		if(msg2.equals("success")){
			Toast.makeText(FindActivity.this, msg, Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(FindActivity.this, msg, Toast.LENGTH_SHORT).show();
		}
	}
}
