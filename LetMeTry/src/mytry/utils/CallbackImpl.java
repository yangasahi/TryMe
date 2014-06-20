package mytry.utils;
import java.io.FileOutputStream;
import java.io.IOException;

import mytry.test.R;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.widget.ImageView;
public class CallbackImpl implements AsyncImageLoader.ImageCallback{
	private ImageView imageView;
	private ImageButton imageButton;
	private boolean isButtonNull = false;
	public CallbackImpl (ImageView imageView,ImageButton imageButton){
		super();
		this.imageView = imageView;
		this.imageView.setImageResource(R.drawable.card_feed_b_1);
		if(imageButton != null){
			this.imageButton = imageButton;
		}else{
			isButtonNull = true;
		}
		
	}
	
	@Override
	public void imageLoaded(Drawable imageDrawable) {
		// TODO Auto-generated method stub
		FileOutputStream outStream = null;
		Bitmap bm = drawableToBitmap(imageDrawable);
		try {
			// 打开指定文件对应的输出流
			outStream = new FileOutputStream("/sdcard/TryMe/download.png");
			// 把位图输入到指定文件中
			bm.compress(CompressFormat.PNG, 100, outStream);
			outStream.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		imageView.setImageBitmap(bm);
		if(!isButtonNull){
			imageButton.setBackgroundResource(R.drawable.trybutton);
		}
	
	}
	public static Bitmap drawableToBitmap(Drawable drawable) {
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();

		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}
}
