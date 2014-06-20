package mytry.Service;

import mytry.download.HttpDownloader;
import mytry.model.JpgInfo;
import mytry.test.ReadJpgActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		JpgInfo jpgInfo = (JpgInfo)intent.getSerializableExtra("jpgInfo");
		//System.out.println("service--->" + jpgInfo);
		DownloadThread downloadThread = new DownloadThread(jpgInfo);
		Thread thread = new Thread(downloadThread);
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}

	class DownloadThread implements Runnable{
        private JpgInfo jpgInfo = null;
		public DownloadThread(JpgInfo jpgInfo){
			this.jpgInfo = jpgInfo;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//下载地址http://192.168.115.1:8080/jpg/1.gif
			String jpgUrl = "http://192.168.115.1:8080/jpg/" + jpgInfo.getName();
			HttpDownloader httpDownloader = new HttpDownloader();
			int result = httpDownloader.downFile(jpgUrl, "/jpg/", jpgInfo.getName());
		   String resultMessage = null;
		   if(result == -1){
			   resultMessage = "下载失败";
		   }else if(result == 0){
			   resultMessage = "文件下载成功";
		   }else if(result == 1){
			   resultMessage = "文件已经存在,不需要重新下载";
		   }
		//System.out.println( result + resultMessage);
		 notify(resultMessage, jpgInfo.getName());
		}
		/**
		 *  通知用户下载情况
		 * */
		private void notify(String resultMessage, String name){
			 NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			 int icon = android.R.drawable.stat_notify_chat;
			 long when = System.currentTimeMillis();//通知发生的时间为系统当前的时间
			 //新建一个通知，指定其图标和标题
			 Notification notification = new Notification(icon, resultMessage, when);
			 notification.defaults = Notification.DEFAULT_SOUND;//发出默认声音
			 notification.flags = Notification.FLAG_AUTO_CANCEL;
			 Intent openintent = new Intent(DownloadService.this, ReadJpgActivity.class);
		    openintent.putExtra("result",name);
			 PendingIntent pendingIntent = PendingIntent.getActivity(DownloadService.this, 0, openintent, 0);
			 notification.setLatestEventInfo(DownloadService.this, "下载内容", "图片"+ resultMessage, pendingIntent);	
			 manager.notify(0, notification);
		}
	}
}


