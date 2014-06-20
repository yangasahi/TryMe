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
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TakeBackgphotoActivity extends Activity implements Callback {
	//�ӱ��ض�ȡ��Ƭ
		public static final String MIME_TYPE_IMAGE_JPEG = "image/jpeg"; 
	    public static final int ACTIVITY_GET_IMAGE = 0; 
	    private static final int FLAG_CHOOSE = 1;
		private static final int FLAG_HANDLEBACK = 2;
	private LinearLayout previewBackgroundButton;
	private LinearLayout homeButton;
	private ImageButton returnButton;
	private LinearLayout localChoiceButton;
	private LinearLayout takePhotoButton;
	private SurfaceView surfaceview = null;// ��������������
	private Camera camera = null;
	private SurfaceHolder surfaceHolder = null;
	private boolean turnFlag,isTakePthoto,turnFlag2 = false;
	private String pathwupin = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// û�б���
		 Window window = getWindow();// �õ�����
		 window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// ���ø���
        setContentView(R.layout.takebackgphoto);
        
        Intent intent = getIntent();
        pathwupin = intent.getStringExtra("path");
       
       
        surfaceview = (SurfaceView) findViewById(R.id.surface);
        previewBackgroundButton=(LinearLayout)findViewById(R.id.previewBackgroundButton);
        previewBackgroundButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (turnFlag == false) {
					Toast.makeText(TakeBackgphotoActivity.this, "��Ǹ���������ջ�ѡ����Ƭ��",
							Toast.LENGTH_SHORT).show();
				} else {
					if(turnFlag2 == false){
						homeButton.setBackgroundColor(Color.TRANSPARENT);
						takePhotoButton.setBackgroundColor(Color.TRANSPARENT);
					Intent intent = new Intent();
					intent.putExtra("pathwupin", pathwupin);
					intent.setClass(TakeBackgphotoActivity.this, PreviewBackgroundActivity.class);
					//ʹ�����Intent����������ResultActivity
					TakeBackgphotoActivity.this.startActivityForResult(intent, 1);
					turnFlag2 = true;
					}
				}
			}
		});
        homeButton=(LinearLayout)findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new HomeButtonListener());
        returnButton=(ImageButton)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new ReturnButtonListener());
        localChoiceButton=(LinearLayout)findViewById(R.id.localChoiceButton);
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
        takePhotoButton=(LinearLayout)findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(new TakePhotoButtonListener());
//        downloadButton=(Button)findViewById(R.id.downloadButton);
//        downloadButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent();
//				intent.setClass(TakeBackgphotoActivity.this, DownloadActivity.class);
//				startActivity(intent);
//			}
//		});
        
        
        surfaceHolder = surfaceview.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		surfaceview.setFocusable(true);
		surfaceview.setFocusableInTouchMode(true);
		surfaceview.setClickable(true);
    }
    
    class HomeButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			takePhotoButton.setBackgroundColor(Color.TRANSPARENT);
			homeButton.setBackgroundColor(Color.BLUE);
			camera.startPreview();
			isTakePthoto = false;
		}
    }
    class ReturnButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
		TakeBackgphotoActivity.this.setResult(6);
			TakeBackgphotoActivity.this.finish();
			
		}
    }
    
   
    class TakePhotoButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(isTakePthoto == false){
				homeButton.setBackgroundColor(Color.TRANSPARENT);
				takePhotoButton.setBackgroundColor(Color.BLUE);
				camera.takePicture(null, null, pictureCallback);
				isTakePthoto = true;
			}else{
				Toast.makeText(TakeBackgphotoActivity.this, "��Ʒ������ϣ�", Toast.LENGTH_SHORT).show();
			}

			
		}
    }
  
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
	private PictureCallback pictureCallback = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			// �����������õ����ݴ���λͼ
			Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

			// ����һ��λ��SD���ϵ��ļ�
			File file = new File("/sdcard/TryMeBack/");
			file.mkdir();// �����ļ���
			FileOutputStream outStream = null;
			try {
				// ��ָ���ļ���Ӧ�������
				outStream = new FileOutputStream("/sdcard/TryMeBack/Background.jpg");
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
					Intent intent = new Intent(this, PreviewBackgroundActivity.class);
					intent.putExtra("pathwupin", pathwupin);
					intent.putExtra("path", path);
					startActivity(intent);
				} else {
					Log.d("may", "path=" + uri.getPath());
					Intent intent = new Intent(this, PreviewBackgroundActivity.class);
					intent.putExtra("pathwupin", pathwupin);
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
		}else if (resultCode == 1) {
			switch (requestCode) {
		    case 1:
		    	TakeBackgphotoActivity.this.setResult(1);
		    	TakeBackgphotoActivity.this.finish();
		    	break;
		    
		}
		}else if (resultCode == 2) {
			switch (requestCode) {
			case 1:
		    	isTakePthoto = false;
		    	turnFlag2 = false;
		    	break;
			}
		}else if (resultCode == 3) {
			switch (requestCode) {
		    case 1:
		    	TakeBackgphotoActivity.this.setResult(3);
		    	TakeBackgphotoActivity.this.finish();
		    	break;
		}
		}
	}
}