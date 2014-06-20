package mytry.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class CallImpl implements AsyncImageLoader.ImageCallback{
	private ImageView imageView;

	public CallImpl (ImageView imageView){
		super();
		this.imageView = imageView;
	}
	
	@Override
	public void imageLoaded(Drawable imageDrawable) {
		// TODO Auto-generated method stub
		imageView.setImageDrawable(imageDrawable);
	}

}
