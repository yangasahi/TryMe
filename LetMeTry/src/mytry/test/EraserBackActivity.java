package mytry.test;

import java.io.FileOutputStream;
import java.io.IOException;

import mytry.test.ShakeListener.OnShakeListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class EraserBackActivity extends Activity{

	private Vibrator mVibrator = null;
	private ImageView imageView ,imv2= null;
	private LinearLayout sureButton = null;
	private ImageButton returnButton = null;
	private LinearLayout helpButton = null;
//	private LinearLayout manualExtractButton = null;
	private Bitmap bm,tmpbmp ,zoom= null;
	private String path = null;
	private boolean flag,turntoflag = false;
	private SeekBar seekBar = null;
	private LinearLayout seekButton = null;
	private  Paint paint = null;
	private ShakeListener shakeListener = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		 Window window = getWindow();// 得到窗口
		 window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 设置高亮
		setContentView(R.layout.eraserback);

		shake();
	    paint=new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		paint.setStrokeCap(Cap.ROUND);
		paint.setStrokeWidth(20);
		
		imageView = (ImageView)findViewById(R.id.imageView1);
		Intent intent = getIntent();
		path = intent.getStringExtra("path");
		bm = BitmapFactory.decodeFile(path);
		imageView.setImageBitmap(bm);
		imv2= (ImageView)findViewById(R.id.era2);
		zoom=Bitmap.createBitmap(150, 150, Config.ARGB_8888);
		imv2.setImageBitmap(zoom);
		imv2.setVisibility(View.GONE);
		seekButton = (LinearLayout)findViewById(R.id.selectButton);
		seekBar = (SeekBar)findViewById(R.id.seekBar111);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			
				paint.setStrokeWidth(seekBar.getProgress());
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		seekButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(seekBar.isShown()){
					seekButton.setBackgroundColor(Color.TRANSPARENT);
					helpButton.setBackgroundColor(Color.TRANSPARENT);
					seekBar.setVisibility(View.GONE);
				}else{
	                seekButton.setBackgroundColor(Color.BLUE);
	                helpButton.setBackgroundColor(Color.TRANSPARENT);
					seekBar.setVisibility(View.VISIBLE);
				}
			}
		});
		sureButton = (LinearLayout)findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(turntoflag==true){
					if(flag == false){
						FileOutputStream outStream = null;
						try {
							// 打开指定文件对应的输出流
							outStream = new FileOutputStream("/sdcard/TryMe/zzz.png");
							// 把位图输入到指定文件中
							tmpbmp.compress(CompressFormat.PNG, 100, outStream);
							outStream.close();
						} catch (IOException e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						Intent intent = new Intent();
						intent.putExtra("path","/sdcard/TryMe/zzz.png" );
						intent.setClass(EraserBackActivity.this, PreviewItemActivity.class);
						EraserBackActivity.this.startActivityForResult(intent, 1);
						seekButton.setBackgroundColor(Color.TRANSPARENT);
						helpButton.setBackgroundColor(Color.TRANSPARENT);
					}
					
				}else{
//					Toast.makeText(EraserBackActivity.this, "请先擦除多余背景!", Toast.LENGTH_SHORT).show();
					dialog();
				}
				
			}
		});
		
		returnButton = (ImageButton)findViewById(R.id.returnButton);
		returnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EraserBackActivity.this.setResult(2);
				EraserBackActivity.this.finish();
			}
		});
		helpButton = (LinearLayout)findViewById(R.id.helpButton);
		helpButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageView.setImageBitmap(bm);
				paint.setStrokeWidth(20);
				if(seekBar.isShown()){
					seekBar.setVisibility(View.GONE);
				}
				seekButton.setBackgroundColor(Color.TRANSPARENT);
				helpButton.setBackgroundColor(Color.BLUE);
				turntoflag = false;
				manualExtract();
			}
		});
		
		manualExtract();
	
	}
	// 定义震动
	public void startVibrato() {
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1); // 第一个｛｝里面是节奏数组，
																	// 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
	}
	
	//晃动函数
	private void shake(){
		// 晃动处理
				mVibrator = (Vibrator) getApplication().getSystemService(
						VIBRATOR_SERVICE);
				shakeListener = new ShakeListener(this);
				shakeListener.setOnShakeListener(new OnShakeListener() {

					@Override
					public void onShake() {
						// TODO Auto-generated method stub
						shakeListener.stop();
						startVibrato(); // 开始 震动
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(EraserBackActivity.this, "正在努力开发此功能，敬请期待...",
										Toast.LENGTH_SHORT).show();
								mVibrator.cancel();
								shakeListener.start();
							}
						}, 2000);

					}
				});

	}
	void BresenhamLine(int x0, int y0, int x1, int y1,Bitmap src,Bitmap dis) {
		int dx, dy, x, y, d;
		int UpIncre, DownIncre;
		int i,j;
		if (x0 > x1) {
			int tt = x1;
			x1 = x0;
			x0 = x1;
			tt = y1;
			y1 = y0;
			y0 = y1;
		}
		x = x0;
		y = y0;
		dx = x1 - x0;
		dy = y1 - y0;
		if (dy > 0 && dy <= dx) {
			d = dx - 2 * dy;
			UpIncre = 2 * dx - 2 * dy;
			DownIncre = -2 * dy;
			while (x <= x1) {
				eareser(x,y,src,dis);
				x++;
				if (d < 0) {
					y++;
					d += UpIncre;
				} else
					d += DownIncre;
			}
		} 
		else if ((dy > (-dx)) && dy < 0) // 斜率-1<=k<=0的中点Bresenham算法
		{
			d = dx - 2 * dy;
			UpIncre = -2 * dy;
			DownIncre = -2 * dx - 2 * dy;
			while (x <= x1)
			{
				eareser(x,y,src,dis);
				x++;
				if (d > 0)
				{
					y--;
					d += DownIncre;
				} 
				else d += UpIncre;
			}
		}
		else //斜率k>1和k不存在时的的中点Bresenham算法
		 { 
			d=dy-2*dx;
		 UpIncre=2*dy-2*dx;
			DownIncre = -2 * dx;
			while (y <= y1) {
				eareser(x,y,src,dis);
				y++;
				if (d < 0) {
					x++;
					d += UpIncre;
				} else
					d += DownIncre;
			}
		}
	}
	
	private void eareser(int x,int y,Bitmap src,Bitmap dis)
	{
		if (x>=src.getWidth() || y>=src.getHeight()) return;
		int i,j;
		for (i=0;i<20;i++)
		{
			for (j=0;j<(20-i);j++)
			{
				if (x+i<src.getWidth())
				{
					if ((y+j)<src.getHeight()) dis.setPixel(x+i,y+j,0);
					if (y>=j) dis.setPixel(x+i,y-j,0);
				}
				if (x>=i)
				{
					if ((y+j)<src.getHeight()) dis.setPixel(x-i,y+j,0);
					if (y>=j) dis.setPixel(x-i,y-j,0);
				}
			}
		}
	}
	private void manualExtract(){
		tmpbmp=Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
		tmpbmp=bm.copy(Config.ARGB_8888, true);
		final Bitmap bm2=Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),Bitmap.Config.ARGB_8888);
		bm2.eraseColor(Color.TRANSPARENT);
		final Canvas tmpcanvas=new Canvas(bm2);
		final Canvas canvas=new Canvas(tmpbmp);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
		imageView.setImageBitmap(tmpbmp);
		for (int i=65;i<85;i++)
		{
			zoom.setPixel(i, 75, Color.RED);
			zoom.setPixel( 75,i, Color.RED);
		}
		
		
		
		
		imageView.setOnTouchListener(new OnTouchListener() {

			float startx = 0.0f;
			float starty = 0.0f;
			float endx = 0.0f;
			float endy = 0.0f;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				float x0 = event.getX();
				float y0 = event.getY();
				float x = event.getX();
				float y = event.getY();
				
				x=x*(bm2.getWidth()/(float) imageView.getWidth());
				y=y*(bm2.getHeight()/(float) imageView.getHeight());
				
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					startx = x;
					starty = y;
					
					imv2.setVisibility(View.VISIBLE);
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					endx = x;
					endy = y;
					//BresenhamLine((int)startx,(int)starty,(int)endx,(int)endy,bm,tmpbmp);
					canvas.drawLine(startx, starty, endx, endy, paint);
					imageView.invalidate();
					startx = endx;
					starty = endy;
					
					for (int i=-75;i<75;i++)
					{
						for (int j=-75;j<75;j++)
						{
							if ((i>=-10 && i<10) && j==0) continue;
							if ((j>=-10 && j<10) && i==0) continue;
							if ((x+i)>=0 && (x+i)<bm.getWidth() && (y+ j)>=0 && (y+j)<bm.getHeight())
							{
								zoom.setPixel(i+75, j+75, tmpbmp.getPixel((int)x+i, (int)y+j));
							}
							else zoom.setPixel(i+75,j+75,Color.GRAY);
						}
					}
					imv2.invalidate();
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					endx = x;
					endy = y;
					imv2.setVisibility(View.GONE);
					//imageView.invalidate();
				}
				turntoflag = true;
				return true;
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1) {
			switch (requestCode) {
		    case 1:
		    	EraserBackActivity.this.setResult(1);
		    	EraserBackActivity.this.finish();
		    	break;
		}
	}else if (resultCode == 2) {
		switch (requestCode) {
		case 1:
	    	flag = false;
	    	break;
		}
	}else if (resultCode == 3) {
		switch (requestCode) {
	    case 1:
	    	EraserBackActivity.this.setResult(3);
	    	EraserBackActivity.this.finish();
	    	break;
	}
	}
		super.onActivityResult(requestCode, resultCode, data);
	}
private void dialog(){
	AlertDialog.Builder builder = new AlertDialog.Builder(EraserBackActivity.this);  
	builder.setMessage("确定直接提取吗?")  
	       .setCancelable(false)  
	       .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	           public void onClick(DialogInterface dialog, int id) {  
	        		Intent intent = new Intent();
					intent.putExtra("path",path );
					intent.setClass(EraserBackActivity.this, PreviewItemActivity.class);
					EraserBackActivity.this.startActivityForResult(intent, 1);
					seekButton.setBackgroundColor(Color.TRANSPARENT);
					helpButton.setBackgroundColor(Color.TRANSPARENT);
	           }  
	       })  
	       .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
	           public void onClick(DialogInterface dialog, int id) {  
	                dialog.cancel();  
	           }  
	       });  
	AlertDialog alert = builder.create();  
	alert.show();
}
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub
	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		EraserBackActivity.this.setResult(2);
		EraserBackActivity.this.finish();
	}
	return true;
}
}
