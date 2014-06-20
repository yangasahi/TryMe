package mytry.test;

import mytry.test.ShakeListener.OnShakeListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SelectExtractActivity extends Activity {
	private Button directExtractButton;
	private Button autoExtractButton;
	private Button closeButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectextract);
		directExtractButton = (Button) findViewById(R.id.directExtractButton);
		directExtractButton
				.setOnClickListener(new DirectExtractButtonListener());
		autoExtractButton = (Button) findViewById(R.id.autoExtractButton);
		autoExtractButton.setOnClickListener(new AutoExtractButtonListener());
		closeButton = (Button) findViewById(R.id.closeButton);
		closeButton.setOnClickListener(new CloseButtonListener());
		// 晃动处理
		ShakeListener shakeListener = new ShakeListener(this);
		shakeListener.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake() {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
		        intent.setClass(SelectExtractActivity.this, PreviewItemActivity.class);
		        startActivity(intent);
			}
		});
	}

	class DirectExtractButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			Intent intent = new Intent();
			intent.setClass(SelectExtractActivity.this,
					PreviewItemActivity.class);
			// 使用这个Intent对象来启动ResultActivity
			SelectExtractActivity.this.startActivity(intent);
		}
	}

	class AutoExtractButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			Intent intent = new Intent();
			intent.setClass(SelectExtractActivity.this,
					PreviewItemActivity.class);
			// 使用这个Intent对象来启动ResultActivity
			SelectExtractActivity.this.startActivity(intent);
		}
	}

	class CloseButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			finish();

		}
	}

}