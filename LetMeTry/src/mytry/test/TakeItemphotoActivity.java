package mytry.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TakeItemphotoActivity extends Activity implements
		SurfaceHolder.Callback {
	// �ӱ��ض�ȡ��Ƭ
	public static final String MIME_TYPE_IMAGE_JPEG = "image/jpeg";
	public static final int ACTIVITY_GET_IMAGE = 0;
	private static final int FLAG_CHOOSE = 1;
	private static final int FLAG_HANDLEBACK = 2;
	private LinearLayout extractItemButton = null;
	private LinearLayout homeButton = null;
	private ImageButton helpButton = null;
	private LinearLayout localChoiceButton = null;
	private LinearLayout takePhotoButton = null;
	private LinearLayout net_Button = null;
	private SurfaceView surfaceview = null;// ��������������
	private Camera camera = null;
	private SurfaceHolder surfaceHolder = null;
	private boolean turnFlag,turnFlag2, isTakePthoto = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		 Window window = getWindow();// �õ�����
		 window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// ���ø���
		requestWindowFeature(Window.FEATURE_NO_TITLE);// û�б���
		// window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ȫ��
	

		setContentView(R.layout.takeitemphoto);

	
		
		surfaceview = (SurfaceView) findViewById(R.id.surface);
		extractItemButton = (LinearLayout) findViewById(R.id.extractItemButton);
		extractItemButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (turnFlag == false) {
					Toast.makeText(TakeItemphotoActivity.this, "��Ǹ���������ջ�ѡ����Ƭ��",
							Toast.LENGTH_SHORT).show();
				} else {
					if(turnFlag2 == false){
						homeButton.setBackgroundColor(Color.TRANSPARENT);
						takePhotoButton.setBackgroundColor(Color.TRANSPARENT);
						Intent intent = new Intent();
						intent.setClass(TakeItemphotoActivity.this,
								ExtractItemActivity.class);
						// ʹ�����Intent����������ResultActivity
						TakeItemphotoActivity.this
								.startActivityForResult(intent, 1);
						turnFlag2 = true;
					}
					
				}
			}
		});
		net_Button = (LinearLayout)findViewById(R.id.net_Button);
		net_Button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(TakeItemphotoActivity.this, StoreActivity.class);
				TakeItemphotoActivity.this.startActivityForResult(intent, 1);
			}
		});
		homeButton = (LinearLayout) findViewById(R.id.homeButton);
		homeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				takePhotoButton.setBackgroundColor(Color.TRANSPARENT);
				homeButton.setBackgroundColor(Color.BLUE);
				camera.startPreview();
				isTakePthoto = false;
			}
		});
		helpButton = (ImageButton) findViewById(R.id.helpButton);
		helpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(TakeItemphotoActivity.this, HelpActivity.class);
				// ʹ�����Intent����������HelpActivity
				TakeItemphotoActivity.this.startActivity(intent);
			}
		});
		localChoiceButton = (LinearLayout) findViewById(R.id.localChoiceButton);
		localChoiceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				takePhotoButton.setBackgroundColor(Color.TRANSPARENT);
				homeButton.setBackgroundColor(Color.TRANSPARENT);
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, FLAG_CHOOSE);
			}
		});
		takePhotoButton = (LinearLayout) findViewById(R.id.takePhotoButton);
		takePhotoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isTakePthoto == false) {
					homeButton.setBackgroundColor(Color.TRANSPARENT);
					takePhotoButton.setBackgroundColor(Color.BLUE);
					camera.takePicture(null, null, pictureCallback);
					isTakePthoto = true;
				} else {
					Toast.makeText(TakeItemphotoActivity.this, "��Ʒ������ϣ�",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		// downloadButton = (Button) findViewById(R.id.downloadButton);
		// downloadButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent();
		// intent.setClass(TakeItemphotoActivity.this, DownloadActivity.class);
		// startActivity(intent);
		// }
		// });

		surfaceHolder = surfaceview.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		surfaceview.setFocusable(true);
		surfaceview.setFocusableInTouchMode(true);
		surfaceview.setClickable(true);
	}

	private PictureCallback pictureCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			// �����������õ����ݴ���λͼ
			Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

			// ����һ��λ��SD���ϵ��ļ�
			File file = new File("/sdcard/TryMe/");
			file.mkdir();// �����ļ���
			FileOutputStream outStream = null;
			try {
				// ��ָ���ļ���Ӧ�������
				outStream = new FileOutputStream("/sdcard/TryMe/picture.jpg");
				// ��λͼ���뵽ָ���ļ���
				bm.compress(CompressFormat.JPEG, 100, outStream);
				outStream.close();
				turnFlag = true;
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			camera = null;
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

		camera.startPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub

		camera = Camera.open();
		try {
			camera.setPreviewDisplay(holder);
			Camera.Parameters parameters = camera.getParameters();
			if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
				parameters.set("orientation", "portrait");
				// Android 2.2�����ϰ汾
				camera.setDisplayOrientation(90);

				// Android 2.0�����ϰ汾
				parameters.setRotation(90);
			}
			// ����Android 2.0�͸��߰汾��Ч��
			List<String> colorEffects = parameters.getSupportedColorEffects();
			Iterator<String> cei = colorEffects.iterator();
			while (cei.hasNext()) {
				String currentEffect = cei.next();
				if (currentEffect.equals(Camera.Parameters.EFFECT_NONE)) {
					parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
					break;
				}
			}
			// ����Android 2.0�͸��߰汾��Ч��
			camera.setParameters(parameters);
		} catch (IOException exception) {
			camera.release();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

		camera.stopPreview();
		camera.release();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK && null != data) {
			switch (requestCode) {
			case FLAG_CHOOSE:
				Uri uri = data.getData();
				Log.d("may", "uri=" + uri + ", authority=" + uri.getAuthority());
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						Toast.makeText(this, "û���ҵ�ͼƬ!", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.d("may", "path=" + path);
					Intent intent = new Intent(this, ExtractItemActivity.class);
					intent.putExtra("path", path);
					startActivity(intent);
				} else {
					Log.d("may", "path=" + uri.getPath());
					Intent intent = new Intent(this, ExtractItemActivity.class);
					intent.putExtra("path", uri.getPath());
					startActivity(intent);
				}
				break;
			case FLAG_HANDLEBACK:
				String imagePath = data.getStringExtra("path");
				Log.d("may", imagePath);
				// mImageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
				break;
			}
		} else if (resultCode == 1) {
			switch (requestCode) {
			    case 1:
			    	TakeItemphotoActivity.this.finish();
			    	break;
			}
		}else if (resultCode == 2) {
			switch (requestCode) {
			case 1:
		    	isTakePthoto = false;
		    	turnFlag2 = false;
		    	break;
			}
		}

	}

}