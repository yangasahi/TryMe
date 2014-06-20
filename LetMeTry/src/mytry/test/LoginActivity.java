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

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity{

	private ImageButton backButton = null;
	private EditText email,pass = null;
	private Button submitButton = null;
	private ProgressDialog progressDialog = null;
	private Button fogetButton = null;
	private Button regist_button = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.login);
		Parse.initialize(this, "bOGCpOSvl5fXzJDIp0s6PbeE4n3LhmqDGk5gXkMC", "8MWhJZjVTZ2eGRqIxmBKg18KN5HXsW0tJomhZBTC");
		ParseAnalytics.trackAppOpened(null);
		regist_button = (Button)findViewById(R.id.regist_button);
		regist_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, RegistActivity.class);
				LoginActivity.this.startActivity(intent);
				LoginActivity.this.finish();
			}
		});
		backButton = (ImageButton)findViewById(R.id.img_search);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginActivity.this.finish();
			}
		});
		email = (EditText)findViewById(R.id.login_user_edit);
		pass = (EditText)findViewById(R.id.login_passwd_edit);
		
		fogetButton = (Button)findViewById(R.id.forget_passwd);
		fogetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, LoginActivity.class);
				LoginActivity.this.startActivity(intent);
			}
		});
		
		submitButton = (Button)findViewById(R.id.login_login_btn);
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog = ProgressDialog.show(LoginActivity.this, "登录中",
						"请稍等...", true);
				progressDialog.setCancelable(true);
				ParseUser.logInInBackground(email.getText().toString(), pass.getText().toString(), new LogInCallback() {
					
					@Override
					public void done(ParseUser user, ParseException e) {
						// TODO Auto-generated method stub
				        if (user != null) {
									printResult("登录成功","success","fromLogin");
				        } else {
				        	printResult("登录失败","false",null);
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
			LoginActivity.this.finish();
		}
		return true;
	}
	
	private void printResult(String msg,String msg2,String msg3) {
		progressDialog.dismiss();
		if(msg2.equals("success")){
//			Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
//			Intent intent  = new Intent();
//			intent.setClass(LoginActivity.this, MyShareActivity.class);
//			LoginActivity.this.startActivity(intent);
			LoginActivity.this.finish();

		}else{
			Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
		}
	}
}
