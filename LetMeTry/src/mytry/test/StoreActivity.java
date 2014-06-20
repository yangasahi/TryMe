package mytry.test;

import java.util.List;

import mytry.utils.ImageAdapter;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class StoreActivity extends ListActivity{

	private ListView listView = null;
	private ImageAdapter imageAdapter = null;
	private ProgressDialog progressDialog = null;
	private Button button = null;
	private ImageButton imageButton,searchButton = null;
	private String hoddy = null;
	private EditText editText1 = null;
//	private ImageItem imageItem = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 没有标题
		setContentView(R.layout.store);
		Parse.initialize(this, "bOGCpOSvl5fXzJDIp0s6PbeE4n3LhmqDGk5gXkMC", "8MWhJZjVTZ2eGRqIxmBKg18KN5HXsW0tJomhZBTC");
		ParseAnalytics.trackAppOpened(null);
		  //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
        
        hoddy = "全部";
        editText1 = (EditText)findViewById(R.id.editText1);
		imageButton = (ImageButton)findViewById(R.id.returnButton);
		imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StoreActivity.this.finish();
			}
		});
		progressDialog = ProgressDialog.show(StoreActivity.this, "请稍等",
				"正在努力加载中...", true);
		progressDialog.setCancelable(true);
		listView = getListView();
		listView.setCacheColorHint(Color.TRANSPARENT);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				ParseObject parse = (ParseObject)listView.getItemAtPosition(position);
				ParseFile bmobFile;
				bmobFile = parse.getParseFile("goodsimg");
				Intent intent = new Intent();
				intent.putExtra("imageUrl", bmobFile.getUrl().toString());
				intent.putExtra("imgId", parse.getObjectId());
				intent.setClass(StoreActivity.this, ImgItemActivity.class);
				StoreActivity.this.startActivityForResult(intent, 1);
			}
		});
		button = (Button)findViewById(R.id.selectButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(StoreActivity.this);
			}
		});
		searchButton = (ImageButton)findViewById(R.id.img_Search);
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog.show();
				parse(hoddy,editText1.getText().toString());
			}
		});
		parse("全部",null);
	}
	private void showDialog(Context context){

		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);  
		
        builder.setTitle("请选择分类");  
       builder.setSingleChoiceItems(R.array.select, 0, new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			hoddy=getResources().getStringArray(R.array.select)[which];
			button.setText(hoddy);
            dialog.dismiss();
		}
	});
       AlertDialog dialog = builder.create();
       dialog.show();
	}
	
	private void parse(String type,String seach){
		
		ParseQuery parses = new ParseQuery("goods");
		if(!type.equals("全部")){
			parses.whereEqualTo("goodstype", type);
		}
if(seach != null){
	parses.whereContains("goodsname", seach);
		}
		parses.findInBackground(new FindCallback() {
			
			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				// TODO Auto-generated method stub
				if(arg0.size() == 0){
					Toast.makeText(StoreActivity.this, "抱歉,暂时没有符合条件的物品!", Toast.LENGTH_SHORT).show();
				}else{
					imageAdapter = new ImageAdapter(arg0, StoreActivity.this);
					listView.setAdapter(imageAdapter);
					
				}
				progressDialog.dismiss();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 1){
		StoreActivity.this.setResult(1);
		StoreActivity.this.finish();
	}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
