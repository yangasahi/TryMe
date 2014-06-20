package mytry.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegistActivity extends Activity{
	private ProgressDialog progressDialog = null;
	private ImageButton backButton = null;
	private EditText email,pass,name = null;
	private Button submitButton = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.regist);
		Parse.initialize(this, "bOGCpOSvl5fXzJDIp0s6PbeE4n3LhmqDGk5gXkMC", "8MWhJZjVTZ2eGRqIxmBKg18KN5HXsW0tJomhZBTC");
		ParseAnalytics.trackAppOpened(null);
		email = (EditText)findViewById(R.id.login_user_edit);
		pass = (EditText)findViewById(R.id.login_passwd_edit);
		name = (EditText)findViewById(R.id.login_user_edit_2);
		
		backButton = (ImageButton)findViewById(R.id.img_search);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegistActivity.this.finish();
			}
		});
		submitButton = (Button)findViewById(R.id.login_login_btn);
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog = ProgressDialog.show(RegistActivity.this, "注册中",
						"请稍等...", true);
				progressDialog.setCancelable(true);
				ParseUser user = new ParseUser();
				user.setUsername(name.getText().toString());
				user.setEmail(email.getText().toString());
				user.setPassword(pass.getText().toString());
				user.signUpInBackground(new SignUpCallback() {
					
					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub

						if (e == null) {
							printResult("提交成功","success");
							
						} else {
							printResult("提交失败,用户名已被注册!","false");
						
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
			RegistActivity.this.finish();
		}
		return true;
	}
	
	private void printResult(String msg,String msg2) {
		progressDialog.dismiss();
		if(msg2.equals("success")){
//			Toast.makeText(RegistActivity.this, msg, Toast.LENGTH_SHORT).show();
//			Intent intent  = new Intent();
//			intent.setClass(RegistActivity.this, MyShareActivity.class);
//			RegistActivity.this.startActivity(intent);
			RegistActivity.this.finish();
		}else{
			Toast.makeText(RegistActivity.this, msg, Toast.LENGTH_SHORT).show();
		}
	}
}
