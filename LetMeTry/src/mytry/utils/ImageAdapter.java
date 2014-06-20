package mytry.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mytry.test.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;

public class ImageAdapter extends BaseAdapter{

	private AsyncImageLoader loader = new AsyncImageLoader();
	private List<ParseObject>info = null;
	private Map<Integer,View> rowViews = new HashMap<Integer, View>();
	private Context context = null;
	
	public ImageAdapter(List<ParseObject> info, Context context){
		this.info = info;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return info.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return info.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View rowView = rowViews.get(position);
		if(rowView == null){
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			rowView = layoutInflater.inflate(R.layout.picitem, null);
			ImageView image = (ImageView)rowView.findViewById(R.id.itemImage0);
			TextView goodsname = (TextView)rowView.findViewById(R.id.hotelname);
			TextView downCount = (TextView)rowView.findViewById(R.id.price0);
			TextView seller = (TextView)rowView.findViewById(R.id.tele);
			TextView price = (TextView)rowView.findViewById(R.id.address);
			ParseObject parse = (ParseObject)getItem(position);
			goodsname.setText(parse.getString("goodsname"));
			downCount.setText("试用人气: " + parse.getInt("downcount") + " 次");
			seller.setText("卖家: " +parse.getString("seller"));
			price.setText("售价: " + parse.getString("price") + " 元");
			ParseFile bmobFile;
			bmobFile = parse.getParseFile("goodsimg");
			loadImage(bmobFile.getUrl(), image);
		}
		return rowView;
	}
	private void loadImage(final String url, ImageView imageView) {
		CallbackImpl callbackImpl = new CallbackImpl(imageView,null);
		Drawable cacheImage = loader.loadDrawable(url, callbackImpl);
		if (cacheImage != null) {
			imageView.setImageDrawable(cacheImage);
		}
	}

}
