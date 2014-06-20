package mytry.test;





import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseUser;

public class ShareActivity extends Activity {
	
	private static final String URL_ACTIVITY_CALLBACK = "weiboandroidsdk://TimeLineActivity";
	private static final String FROM = "xweibo";
//	 Weibo mWeibo = Weibo.getInstance();
	// 设置appkey及appsecret，如何获取新浪微博appkey和appsecret请另外查询相关信息，此处不作介绍
	private static final String CONSUMER_KEY = "2693633982";// 替换为开发者的appkey，例如"1646212960";
	private static final String CONSUMER_SECRET = "005823414c29d073941bce6f6ed8d276";// 替换为开发者的appkey，例如"94098772160b6f8ffc1315374d8861f9";
	
	private String username = "";
	private String password = "";

	private TextView mToken = null; 
	private Button emailButton = null;
	private Button closeButton = null;
//	private Button microblogButton = null;
	private Button mmsButton,shareButton = null;
	String path = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);
        Parse.initialize(this, "bOGCpOSvl5fXzJDIp0s6PbeE4n3LhmqDGk5gXkMC", "8MWhJZjVTZ2eGRqIxmBKg18KN5HXsW0tJomhZBTC");
		ParseAnalytics.trackAppOpened(null);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        shareButton = (Button)findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ParseUser currentUser = ParseUser.getCurrentUser();
				if(currentUser != null){
					Intent intent = new Intent();
					intent.putExtra("path", path);
					intent.setClass(ShareActivity.this, AddImgActivity.class);
					ShareActivity.this.startActivity(intent);
					ShareActivity.this.finish();
				}else{
					Intent intent = new Intent();
					intent.setClass(ShareActivity.this, LoginActivity.class);
					ShareActivity.this.startActivity(intent);
					ShareActivity.this.finish();
				}
			}
		});
        mmsButton = (Button)findViewById(R.id.mmsButton);
        mmsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent MMsintent = new Intent();
				MMsintent.setClass(ShareActivity.this, InputActivity.class);
				startActivity(MMsintent);
			}
		});
        
        emailButton=(Button)findViewById(R.id.emailButton);
        emailButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_SEND); 				
				//i.setType("text/plain"); //use this line for testing in the emulator 				
				intent.setType("message/rfc822") ; // use from live device				
				intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""}); 				
				intent.putExtra(Intent.EXTRA_SUBJECT,""); 				
				intent.putExtra(Intent.EXTRA_TEXT,"body goes here"); 
				startActivity(Intent.createChooser(intent, "Select email application."));		
			}
		});
        closeButton=(Button)findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
//        microblogButton = (Button)findViewById(R.id.microblogButton);
        mToken = (TextView)findViewById(R.id.OauthtextView);
//        microblogButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (v == microblogButton) {
//					Weibo weibo = Weibo.getInstance();
//					weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);
//
//					// Oauth2.0
//					// 隐式授权认证方式
//					weibo.setRedirectUrl("http://www.zu.com");// 此处回调页内容应该替换为与appkey对应的应用回调页
//					// 对应的应用回调页可在开发者登陆新浪微博开发平台之后，
//					// 进入我的应用--应用详情--应用信息--高级信息--授权设置--应用回调页进行设置和查看，
//					// 应用回调页不可为空
//
//					weibo.authorize(ShareActivity.this,
//							new AuthDialogListener());
//
//					// try {
//					// // Oauth2.0 认证方式
//					// Weibo.setSERVER("https://api.weibo.com/2/");
//					// Oauth2AccessToken at =
//					// weibo.getOauth2AccessToken(AuthorizeActivity.this,
//					// Weibo.getAppKey(), Weibo.getAppSecret(), username,
//					// password);
//					// // xauth认证方式
//					// /*
//					// * Weibo.setSERVER("http://api.t.sina.com.cn/");
//					// * AccessToken at =
//					// * weibo.getXauthAccessToken(TextActivity.this,
//					// * Weibo.APP_KEY, Weibo.APP_SECRET, "", "");
//					// * mToken.setText(at.getToken());
//					// */
//					// RequestToken requestToken =
//					// weibo.getRequestToken(AuthorizeActivity.this,
//					// Weibo.getAppKey(), Weibo.getAppSecret(),
//					// AuthorizeActivity.URL_ACTIVITY_CALLBACK);
//					// mToken.setText(requestToken.getToken());
//					// Uri uri =
//					// Uri.parse(AuthorizeActivity.URL_ACTIVITY_CALLBACK);
//					// startActivity(new Intent(Intent.ACTION_VIEW, uri));
//					//
//					// } catch (WeiboException e) {
//					// e.printStackTrace();
//					// } // mToken.setText(at.getToken());
//					//
//
//				  
//					
//					
//				}
//			}
//		});
    
    
    }
    public void onResume() {
		super.onResume();
	}
    
//    class AuthDialogListener implements WeiboDialogListener{
//
//		@Override
//		public void onComplete(Bundle values) {
//			// TODO Auto-generated method stub
//			String token = values.getString("access_token");
//			String expires_in = values.getString("expires_in");
//			mToken.setText("access_token : " + token + "  expires_in: "
//					+ expires_in);
//			AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
//			accessToken.setExpiresIn(expires_in);
//			Weibo.getInstance().setAccessToken(accessToken);
//			//Intent intent = new Intent();
//			//intent.setClass(ShareActivity.this, TestActivity.class);
//			//startActivity(intent);
//			
//			File file = Environment.getExternalStorageDirectory();
//            String sdPath = file.getAbsolutePath();
//            // 请保证SD卡根目录下有这张图片文件
//            String picPath = sdPath + "/" + "TryMeShar.jpg";
//            File picFile = new File(picPath);
//            if (!picFile.exists()) {
//                Toast.makeText(ShareActivity.this, "图片" + picPath + "不存在！", Toast.LENGTH_SHORT)
//                        .show();
//                picPath = null;
//            }
//            try {
//            	
//                share2weibo("abc", picPath);
//                Intent i = new Intent(ShareActivity.this, com.weibo.net.ShareActivity.class);
//                ShareActivity.this.startActivity(i);
//
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } finally {
//
//            }
//
//		}
//
//		@Override
//		public void onWeiboException(WeiboException e) {
//			// TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(),
//					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
//					.show();
//		}
//
//		@Override
//		public void onError(DialogError e) {
//			// TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(),
//					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
//		}
//
//		@Override
//		public void onCancel() {
//			// TODO Auto-generated method stub
//			Toast.makeText(getApplicationContext(), "Auth cancel",
//					Toast.LENGTH_LONG).show();
//		}
//    	
//    }
//    private void share2weibo(String content, String picPath) throws WeiboException {
//        Weibo weibo = Weibo.getInstance();
//        weibo.share2weibo(this, weibo.getAccessToken().getToken(), weibo.getAccessToken()
//                .getSecret(), content, picPath);
//    }

//    private String getPublicTimeline(Weibo weibo) throws MalformedURLException, IOException,
//            WeiboException {
//        String url = Weibo.SERVER + "statuses/public_timeline.json";
//        WeiboParameters bundle = new WeiboParameters();
//        bundle.add("source", Weibo.getAppKey());
//        String rlt = weibo.request(this, url, bundle, "GET", mWeibo.getAccessToken());
//        return rlt;
//    }
//
//    private String upload(Weibo weibo, String source, String file, String status, String lon,
//            String lat) throws WeiboException {
//        WeiboParameters bundle = new WeiboParameters();
//        bundle.add("source", source);
//        bundle.add("pic", file);
//        bundle.add("status", status);
//        if (!TextUtils.isEmpty(lon)) {
//            bundle.add("lon", lon);
//        }
//        if (!TextUtils.isEmpty(lat)) {
//            bundle.add("lat", lat);
//        }
//        String rlt = "";
//        String url = Weibo.SERVER + "statuses/upload.json";
//        try {
//            rlt = weibo
//                    .request(this, url, bundle, Utility.HTTPMETHOD_POST, mWeibo.getAccessToken());
//        } catch (WeiboException e) {
//            throw new WeiboException(e);
//        }
//        return rlt;
//    }
//
//    private String update(Weibo weibo, String source, String status, String lon, String lat)
//            throws WeiboException {
//        WeiboParameters bundle = new WeiboParameters();
//        bundle.add("source", source);
//        bundle.add("status", status);
//        if (!TextUtils.isEmpty(lon)) {
//            bundle.add("lon", lon);
//        }
//        if (!TextUtils.isEmpty(lat)) {
//            bundle.add("lat", lat);
//        }
//        String rlt = "";
//        String url = Weibo.SERVER + "statuses/update.json";
//        rlt = weibo.request(this, url, bundle, Utility.HTTPMETHOD_POST, mWeibo.getAccessToken());
//        return rlt;
//    }
}
