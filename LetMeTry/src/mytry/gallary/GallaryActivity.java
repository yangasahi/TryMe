package mytry.gallary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mytry.test.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery.LayoutParams;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.ViewSwitcher.ViewFactory;

public class GallaryActivity extends Activity
{
	private static final String TAG = "==CrazyIt.org==";
	int[] imageIds = new int[]
	{
		R.drawable.bomb5 , R.drawable.bomb6 , R.drawable.bomb7 
		, R.drawable.bomb8 , R.drawable.bomb9 , R.drawable.bomb10
		, R.drawable.bomb11 , R.drawable.bomb12	, R.drawable.bomb13
		, R.drawable.bomb14 , R.drawable.bomb15 , R.drawable.bomb16
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readgraph);
		//����һ��List����List�����Ԫ����Map
		List<Map<String, Object>> listItems 
			= new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imageIds.length; i++)
		{
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image" , imageIds[i]);
			listItems.add(listItem);
		}
		//��ȡ��ʾͼƬ��ImageSwitcher
		final ImageSwitcher switcher = (ImageSwitcher)
			findViewById(R.id.switcher);
		//����ͼƬ�����Ķ���Ч��
		switcher.setInAnimation(AnimationUtils.loadAnimation(this,
			android.R.anim.fade_in));
		switcher.setOutAnimation(AnimationUtils.loadAnimation(this,
			android.R.anim.fade_out));
		//ΪImageSwitcher����ͼƬ�л��Ķ���Ч��
		switcher.setFactory(new ViewFactory()
		{
			@Override
			public View makeView()
			{
				ImageView imageView = new ImageView(GallaryActivity.this);
				imageView.setBackgroundColor(0xff0000);
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new ImageSwitcher.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				return imageView;
			}
		});		
		//����һ��SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this
			, listItems 
			//ʹ��/layout/cell.xml�ļ���Ϊ���沼��
			, R.layout.cell
			, new String[]{"image"}
			, new int[]{R.id.image1});
		GridView grid = (GridView)findViewById(R.id.grid01);
		//ΪGridView����Adapter
		grid.setAdapter(simpleAdapter);
		//����б��ѡ�еļ�����
		grid.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, 
				int position , long id)
			{
				//��ʾ��ǰ��ѡ�е�ͼƬ
				switcher.setImageResource(imageIds[position % imageIds.length]);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent){}			
		});
		//����б�������ļ�����
		grid.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent
				, View view, int position, long id)
			{
				//��ʾ��������ͼƬ��ͼƬ
				switcher.setImageResource(imageIds[position % imageIds.length]);
			}
		});
	}
}