package mytry.test;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import mytry.Service.DownloadService;
import mytry.download.HttpDownloader;
import mytry.model.JpgInfo;
import mytry.xml.JpgListContentHandler;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class DownloadActivity extends ListActivity {
    /** Called when the activity is first created. */
	private static final int UPDATE = 1;
	private static final int ABOUT = 2;
	private List<JpgInfo> jpgInfos = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        if(!(isNetworkAvailable(DownloadActivity.this) || isWiFiActive(DownloadActivity.this))){
        	Toast.makeText(DownloadActivity.this,
					"无网络连接，请先启动网络！", Toast.LENGTH_LONG).show();
        }else{
        updateListView();
        }
    }

    //在用户点击menu按钮之后，会调用该方法，我们可以在这个方法中加入自己的按钮控件
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, UPDATE, 1, "更新");//第一个 groupid，第二个itemid，第三个是排序
		menu.add(0, ABOUT, 2, "关于");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {//通过item的id判断用户当前点击的是哪个按钮
		// TODO Auto-generated method stub
		System.out.println("itemId----->" + item.getItemId());
		if(item.getItemId() == UPDATE){
			updateListView();
		}
		else if(item.getItemId() == ABOUT){
			//用户点击了关于按钮
		}
		return super.onOptionsItemSelected(item);
	}
    
    private String downloadXML(String urlStr){
    	HttpDownloader httpDownloader = new HttpDownloader();
    	String result = httpDownloader.download(urlStr);
    	return result;
    }
    
    
    
    private List<JpgInfo> parse(String xmlStr){
    	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    	List<JpgInfo> infos = new ArrayList<JpgInfo>();
    	try{
    		XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
    		
    		JpgListContentHandler jpgListContentHandler = new JpgListContentHandler(infos);
    		xmlReader.setContentHandler(jpgListContentHandler);
    		xmlReader.parse(new InputSource(new StringReader(xmlStr)));
    		for (Iterator iterator = infos.iterator(); iterator.hasNext();) {
				JpgInfo jpgInfo = (JpgInfo) iterator.next();
				System.out.println(jpgInfo);
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return infos;
    }
    
    private void updateListView(){
    	//用户点击了更新列表按钮
		//下载包含所有mp3基本信息的xml文件
		String xml = downloadXML("http://192.168.115.1:8080/jpg/resources.xml");
		//对xml文件进行解析，并将解析的结果放置到MP3info对象当中，最后将这些MP3Info对象放置到list当中
	
		jpgInfos = parse(xml);
		//生成一个list对象，并按照simpleAdapter的标准，将MP3Info当中的数据添加到list当中去
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
	
		for(Iterator iterator = jpgInfos.iterator(); iterator.hasNext(); ){
			JpgInfo jpginfo = (JpgInfo)iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("jpg_name", jpginfo.getName());

				map.put("jpg_size", jpginfo.getSize());

			list.add(map);
		}
		
		//创建一个simpleadapter对象，
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,R.layout.jpginfo_item , new String[]{"jpg_name","jpg_size"}, new int[]{R.id.jpg_name,R.id.jpg_size});
		//将simpleadapter对象设置到activity当中
		setListAdapter(simpleAdapter);
		
		System.out.println("xml------>" + xml);
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//根据用户点击列表当中的位置来得到相应的mp3Info对象
		JpgInfo jpgInfo = jpgInfos.get(position);
		//System.out.println("jpgInfo--->"+jpgInfo);
		//生成intent对象
		Intent intent = new Intent();
		//将mp3info对象存入到intent对象当中
		intent.putExtra("jpgInfo",jpgInfo );
		intent.setClass(this, DownloadService.class);
		//启动service
		startService(intent);
		super.onListItemClick(l, v, position, id);
	}
    
	/**
	 * 判断wifi网络是否连接
	 * 
	 * */
	public static boolean isWiFiActive(Context inContext) {
		        WifiManager mWifiManager = (WifiManager) inContext
		          .getSystemService(Context.WIFI_SERVICE);
		          WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
		          int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
		          if (mWifiManager.isWifiEnabled() && ipAddress != 0) {
		          System.out.println("**** WIFI is on");
		              return true;
		          } else {
		             System.out.println("**** WIFI is off");
		             return false;   
		         }
		 }
	
	/**
	 * 判断3g网络是否连接
	 * 
	 * */
	public static boolean isNetworkAvailable( Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivity == null) {
                      System.out.println("**** newwork is off");
                        return false;
                } else {
                        NetworkInfo info = connectivity.getActiveNetworkInfo();
                        if(info == null){
                              System.out.println("**** newwork is off");
                                return false;
                        }else{
                                if(info.isAvailable()){
                                      System.out.println("**** newwork is on");
                                        return true;
                                }
                              
                        }
                }
                  System.out.println("**** newwork is off");
        return false;
    }
}