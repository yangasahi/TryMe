package mytry.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mytry.test.R;

import com.parse.ParseFile;
import com.parse.ParseObject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareItemAdapter extends BaseAdapter{
	private AsyncImageLoader loader = new AsyncImageLoader();
	private List<ParseObject>info = null;
	private Map<Integer,View> rowViews = new HashMap<Integer, View>();
	private Context context = null;
	
	public ShareItemAdapter(List<ParseObject> info, Context context){
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
			rowView = layoutInflater.inflate(R.layout.share_item, null);
			ImageView image = (ImageView)rowView.findViewById(R.id.ItemImage);
			TextView goodsname = (TextView)rowView.findViewById(R.id.ItemText);
			ParseObject parse = (ParseObject)getItem(position);
			goodsname.setText(parse.getString("imgname"));
			ParseFile bmobFile;
			bmobFile = parse.getParseFile("img");
			loadImage(bmobFile.getUrl(), image);
		}
		return rowView;
	}
	private void loadImage(final String url, ImageView imageView) {
		CallImpl callbackImpl = new CallImpl(imageView);
		Drawable cacheImage = loader.loadDrawable(url, callbackImpl);
		if (cacheImage != null) {
			imageView.setImageDrawable(cacheImage);
		}
	}
}
