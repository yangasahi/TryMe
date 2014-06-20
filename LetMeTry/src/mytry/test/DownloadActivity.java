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
					"���������ӣ������������磡", Toast.LENGTH_LONG).show();
        }else{
        updateListView();
        }
    }

    //���û����menu��ť֮�󣬻���ø÷��������ǿ�������������м����Լ��İ�ť�ؼ�
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, UPDATE, 1, "����");//��һ�� groupid���ڶ���itemid��������������
		menu.add(0, ABOUT, 2, "����");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {//ͨ��item��id�ж��û���ǰ��������ĸ���ť
		// TODO Auto-generated method stub
		System.out.println("itemId----->" + item.getItemId());
		if(item.getItemId() == UPDATE){
			updateListView();
		}
		else if(item.getItemId() == ABOUT){
			//�û�����˹��ڰ�ť
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
    	//�û�����˸����б�ť
		//���ذ�������mp3������Ϣ��xml�ļ�
		String xml = downloadXML("http://192.168.115.1:8080/jpg/resources.xml");
		//��xml�ļ����н��������������Ľ�����õ�MP3info�����У������ЩMP3Info������õ�list����
	
		jpgInfos = parse(xml);
		//����һ��list���󣬲�����simpleAdapter�ı�׼����MP3Info���е�������ӵ�list����ȥ
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
	
		for(Iterator iterator = jpgInfos.iterator(); iterator.hasNext(); ){
			JpgInfo jpginfo = (JpgInfo)iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("jpg_name", jpginfo.getName());

				map.put("jpg_size", jpginfo.getSize());

			list.add(map);
		}
		
		//����һ��simpleadapter����
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,R.layout.jpginfo_item , new String[]{"jpg_name","jpg_size"}, new int[]{R.id.jpg_name,R.id.jpg_size});
		//��simpleadapter�������õ�activity����
		setListAdapter(simpleAdapter);
		
		System.out.println("xml------>" + xml);
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		//�����û�����б��е�λ�����õ���Ӧ��mp3Info����
		JpgInfo jpgInfo = jpgInfos.get(position);
		//System.out.println("jpgInfo--->"+jpgInfo);
		//����intent����
		Intent intent = new Intent();
		//��mp3info������뵽intent������
		intent.putExtra("jpgInfo",jpgInfo );
		intent.setClass(this, DownloadService.class);
		//����service
		startService(intent);
		super.onListItemClick(l, v, position, id);
	}
    
	/**
	 * �ж�wifi�����Ƿ�����
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
	 * �ж�3g�����Ƿ�����
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