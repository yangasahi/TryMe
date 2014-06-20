package mytry.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

public class ExtractItemActivity extends Activity {

	private ImageView imageview ,imv2= null;
	private LinearLayout sureButton = null;
//	private LinearLayout homeButton = null;
	private ImageButton returnButton = null;
	private LinearLayout helpButton = null;
	// private Button kuangExtractButton = null;
//	private LinearLayout manualExtractButton = null;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	private static final int REQUEST_CROP_IMAGE = 0;
	private Bitmap bm ,zoom= null;
	private String path = null;
	private Bitmap bm1,bm2,bm3=null,tmpbmp=null;
	private Canvas canvas=null;
	private Path pth=null;
	private boolean turntoflag,flag,clickFlag = false;

	int left,right=0, top, bottom=0;
	int viewloc[]=new int[2];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		 Window window = getWindow();// 得到窗口
		 window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 设置高亮
		setContentView(R.layout.extractitem);
		imageview = (ImageView) findViewById(R.id.imageView1);
		imageview.setBackgroundColor(Color.TRANSPARENT);
		imv2 = (ImageView) findViewById(R.id.era2);
		imv2.setVisibility(View.GONE);
		Intent intent = getIntent();
	    
		path = intent.getStringExtra("path");
		if (path == null) {
			path = "/sdcard/TryMe/picture.jpg";
		}
		bm = BitmapFactory.decodeFile(path);

		imageview.setImageBitmap(bm);

		sureButton = (LinearLayout) findViewById(R.id.sureButton);
		sureButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(flag == true){
					if(clickFlag == false){
						helpButton.setBackgroundColor(Color.TRANSPARENT);
						pth.close();
						Canvas cv=new Canvas(bm3);
						Paint paint=new Paint();
						paint.setColor(Color.GREEN);
						paint.setStyle(Style.FILL);
						cv.drawPath(pth, paint);
						tmpbmp =Bitmap.createBitmap(right-left, bottom-top, Config.ARGB_8888);
						tmpbmp.eraseColor(Color.TRANSPARENT);
						for (int i=left;i<right;i++)
						{
							for (int j=top;j<bottom;j++)
							{
								if (bm3.getPixel(i, j)==Color.GREEN)
								{
									tmpbmp.setPixel(i-left, j-top, bm1.getPixel(i, j));
								}
							}
						}
//						imageview.setImageBitmap(tmpbmp);
						turntoflag = true;
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
							intent.setClass(ExtractItemActivity.this, EraserBackActivity.class);
							ExtractItemActivity.this.startActivityForResult(intent, 1);
							clickFlag = true;
					}
					
					
				}else{
					Toast.makeText(ExtractItemActivity.this, "请先圈定范围!", Toast.LENGTH_SHORT).show();
				}
				
			}
		});

		returnButton = (ImageButton) findViewById(R.id.returnButton);
		returnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ExtractItemActivity.this.setResult(2);
				ExtractItemActivity.this.finish();
			}
		});
		helpButton = (LinearLayout) findViewById(R.id.helpButton);
		helpButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				helpButton.setBackgroundColor(Color.BLUE);
				imageview.setImageBitmap(bm);
				turntoflag = false;
				flag = false;
				manualExtract();
			}
		});
        manualExtract();

	}

//	public void jiance() {
//		Mat mColor = new Mat();
//		// mColor = Utils.bitmapToMat(cpbmp);
//		mColor = Highgui.imread("/sdcard/TryMe/womann.bmp");
////		Mat mGray = new Mat(mColor.rows(), mColor.cols(), CvType.CV_8UC1,
////				new Scalar(0));
//		//Imgproc.cvtColor(mColor, mGray, Imgproc.COLOR_RGB2GRAY);
//		Size ksize = new Size();
//		// ksize.height=0;ksize.width=0;
////		//Imgproc.Canny(mGray, mGray, 2, 50,3);
////		Mat FourChannelMat = change2Four(mGray);
//////		Bitmap bmpOut = Bitmap.createBitmap(mGray.cols(), mGray.rows(),Bitmap.Config.ARGB_8888);
////		Utils.matToBitmap(FourChannelMat, bmpOut);
//System.out.println("yangxu--->>>>>");
//		Bitmap newbm = Bitmap.createBitmap(mColor.width(), mColor.height(),Bitmap.Config.ARGB_8888);
//				newbm = BitmapFactory.decodeFile("/sdcard/TryMeBack/woman.jpg").copy(Bitmap.Config.ARGB_8888, true);
//		Bitmap cpbmp=Bitmap.createBitmap(BitmapFactory.decodeFile("/sdcard/TryMe/womann.bmp"));
//		int hh = newbm.getHeight(), ww = newbm.getWidth();
////		// newbm = cpbmp.copy(Bitmap.Config.ARGB_8888, true);
////
////		int i, j;
////		boolean in = false;
////		double[] coll = new double[0];
////		boolean[][] map=new boolean[hh][ww];
//		 
//		//map=ScanlineSeedfill(300,500,hh,ww,mColor);
//		
//		boolean map[][]=new boolean[hh][ww];
//		double col[]= new double [4];
//		int flag=0;
//		hh=mColor.height();
//		ww=mColor.width();
//		int i,j;
//		for (i=0;i<100;i++)
//		{
//			for (j=0;j<800;j++) newbm.setPixel(j, i, Color.TRANSPARENT);
//		}
//		for (i=100;i<200;i++)
//		{
//			for (j=0;j<400-i;j++) newbm.setPixel(j, i, Color.TRANSPARENT);
//			for (j=400+i;j<800;j++) newbm.setPixel(j, i, Color.TRANSPARENT);
//		}
//		for (i=200;i<250;i++)
//		{
//			for (j=0;j<i;j++) newbm.setPixel(j, i, Color.TRANSPARENT);
//			for (j=500-i;j<300;j++) newbm.setPixel(j, i, Color.TRANSPARENT);
//			for (j=800-i;j<800;j++) newbm.setPixel(j, i, Color.TRANSPARENT);
//			for (j=500;j<300+i;j++) newbm.setPixel(j, i, Color.TRANSPARENT);
//		}
//		for (i=250;i<400;i++)
//		{
//			for (j=0;j<300;j++) newbm.setPixel(j, i, Color.TRANSPARENT);
//			for (j=500;j<800;j++) newbm.setPixel(j, i, Color.TRANSPARENT);
//		}
//		for (i=400;i<600;i++)
//		{
//			for (j=0;j<800;j++) newbm.setPixel(j, i, Color.TRANSPARENT);
//		}
//		//imageview.setImageBitmap(newbm);
//		imageview.setImageBitmap(newbm);
//	}
//
//	public static Mat change2Four(Mat oneChannelMat) {
//		List<Mat> oneChannelMatList = new ArrayList<Mat>();// list类表用于存放单通道的Mat
//		oneChannelMatList.add(oneChannelMat);// 添加4个单通道的Mat，A通道
//		oneChannelMatList.add(oneChannelMat);// G通道
//		oneChannelMatList.add(oneChannelMat);// 判断后确定是B。
//
//		// 第四个通道我建立了一个新的单通道Mat（Mat没有内容），结果对图片效果无影响，所以判断最后一个通道是a通道
//		oneChannelMatList.add(new Mat(oneChannelMat.rows(), oneChannelMat
//				.cols(), CvType.CV_8UC1, new Scalar(0)));// a通道
//
//		// 创建一个4通道的Mat
//		Mat fourChannelsMat = new Mat(oneChannelMat.rows(),
//				oneChannelMat.cols(), CvType.CV_8UC4, new Scalar(0));
//		// 将四个单通道融合成一个4通道图片
//		Core.merge(oneChannelMatList, fourChannelsMat);
//
//		return fourChannelsMat;
//
//	}


	class KuangExtractButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Uri uri = Uri.fromFile(new File(path));
			Intent intent = new Intent("com.android.camera.action.CROP");
			// 传入裁剪图片的路径uri和需要裁剪的格式
			intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
			// 可裁剪状态
			intent.putExtra("crop", true);
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 64);
			intent.putExtra("outputY", 64);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CROP_IMAGE);
		}
	}

	// 保存截取后的图片
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CROP_IMAGE) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				FileOutputStream outStream = null;
				try {
					// 打开指定文件对应的输出流
					outStream = new FileOutputStream(
							"/sdcard/TryMe/picture1.jpg");
					// 把位图输入到指定文件中
					photo.compress(CompressFormat.JPEG, 50, outStream);
					outStream.close();
				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				photo.compress(Bitmap.CompressFormat.JPEG, 50, stream);// (0 -
																		// 100)压缩文件
				imageview.setImageBitmap(photo);
			}
		}else if (resultCode == 1) {
			switch (requestCode) {
		    case 1:
		    	ExtractItemActivity.this.setResult(1);
		    	ExtractItemActivity.this.finish();
		    	break;
		}
	}else if (resultCode == 2) {
		switch (requestCode) {
		case 1:
	    	clickFlag = false;
	    	break;
		}
	}else if (resultCode == 3) {
		switch (requestCode) {
	    case 1:
	    	ExtractItemActivity.this.setResult(2);
			ExtractItemActivity.this.finish();
	    	break;
	}
	}
		if (data == null)
			return;
		super.onActivityResult(requestCode, resultCode, data);
	}


	
	/*public void ScanlineSeedfill(int x,int y,Bitmap bmp)
	{
		int i,j;
		int x0,xl=0,xr,y0,xid=0;
		boolean flag;
		int count =0;
		Stack <Integer> sx=new Stack ();
		Stack <Integer> sy=new Stack ();
		int px,py;
		sx.add(x);sy.add(y);
		
		while (!sx.empty())
		{
			px=sx.pop();py=sy.pop();
			x=px;y=py;
			bmp.setPixel(px, py, Color.GREEN);
			tmpbmp.setPixel(px,py, bm1.getPixel(px, py));
			x0=x+1;
			while (x0<bmp.getWidth() && bmp.getPixel(x0, y)==Color.RED)
			{
				bmp.setPixel(x0, y, Color.GREEN);
				tmpbmp.setPixel(x0,y, bm1.getPixel(x0, y));
				x0++;
			}
			xr=x0-1;
			x0=x-1;
			while (x0>=0 && bmp.getPixel(x0, y)==Color.RED)
			{
				bmp.setPixel(x0, y, Color.GREEN);
				tmpbmp.setPixel(x0,y, bm1.getPixel(x0, y));
				x0--;
			}
			xl=x0+1;
			y0=y;
			for (i=1;i>=-1;i-=2)
			{
				x0=xr;
				y=y0+i;
				System.out.println("zzzzzzz"+xl);
				while (x0>=xl)
				{
					flag=false;
					System.out.println("zzzzzzzz"+x0);
					while (x0>=xl && bmp.getPixel(x0, y)!=Color.GREEN)
					{
						if (!flag)
						{
							flag=true;
							xid=x0;
						}
						x0--;
					}
					if (flag)
					{
						sx.add(xid);sy.add(y);
						flag=false;
					}
					while (x0>=xl && (bmp.getPixel(x0, y)!=Color.RED))
					{
						x0--;
					}
				}
			}
		}
	}*/

	private void manualExtract(){
		bm1 = BitmapFactory.decodeFile(path).copy(
				Bitmap.Config.ARGB_8888, true);
		bm2 = Bitmap.createBitmap(bm1);
		bm3 = Bitmap.createBitmap(bm.getWidth(),bm.getHeight(),Bitmap.Config.ARGB_8888);
		bm3.eraseColor(Color.RED);
		zoom = Bitmap.createBitmap(150, 150, Config.ARGB_8888);
		imv2.setImageBitmap(zoom);
		canvas = new Canvas(bm2);
		
		left=bm1.getWidth();right=0;
		top=bm1.getHeight();bottom=0;
		
		//final Canvas cv2 = new Canvas(bm3);
		final Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(10);
		paint.setColor(Color.GREEN);
		pth=new Path();
		imageview.setImageBitmap(bm2);
		for (int i=65;i<85;i++)
		{
			zoom.setPixel(i, 75, Color.RED);
			zoom.setPixel( 75,i, Color.RED);
		}
		imageview.setOnTouchListener(new OnTouchListener() {
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
				
				x=x*(bm2.getWidth()/(float) imageview.getWidth());
				y=y*(bm2.getHeight()/(float) imageview.getHeight());
				
				
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					startx = x;
					starty = y;
					
					pth.moveTo(x, y);
					imv2.setVisibility(View.VISIBLE);
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					endx = x;
					endy = y;
					if (endx<0) endx=0;
					if (endx>=bm2.getWidth()) endx=bm2.getWidth()-1;
					if (endy<0) endy=0;
					if (endy>=bm2.getHeight()) endy=bm2.getHeight()-1;
					pth.rLineTo(endx-startx, endy-starty);
					canvas.drawPath(pth, paint);
					//BresenhamLine((int)startx,(int)starty,(int)endx,(int)endy,bm1,tmpbmp);
					imageview.invalidate();
					for (int i=-75;i<75;i++)
					{
						for (int j=-75;j<75;j++)
						{
							if ((i>=-10 && i<10) && j==0) continue;
							if ((j>=-10 && j<10) && i==0) continue;
							if ((x+i)>=0 && (x+i)<bm2.getWidth() && (y+ j)>=0 && (y+j)<bm2.getHeight())
							{
								zoom.setPixel(i+75, j+75, bm2.getPixel((int)x+i, (int)y+j));
							}
							else zoom.setPixel(i+75,j+75,Color.GRAY);
						}
					}
					imv2.invalidate();
					startx = endx;
					starty = endy;
					
					if (endx<left) left=(int) endx;
					if (endx>right) right = (int) endx;
					if (endy<top) top=(int) endy;
					if (endy>bottom) bottom=(int) endy;
				}
				if (event.getAction() == MotionEvent.ACTION_UP) {
					endx = x;
					endy = y;
					pth.rLineTo(endx-startx, endy-starty);
					canvas.drawPath(pth, paint);
					imageview.invalidate();
					imv2.setVisibility(View.GONE);
					flag = true;
				}

				return true;
			}
		});
	
	
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			ExtractItemActivity.this.setResult(2);
			ExtractItemActivity.this.finish();
		}
		return true;
	}
}
