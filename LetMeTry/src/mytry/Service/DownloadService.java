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
			//���ص�ַhttp://192.168.115.1:8080/jpg/1.gif
			String jpgUrl = "http://192.168.115.1:8080/jpg/" + jpgInfo.getName();
			HttpDownloader httpDownloader = new HttpDownloader();
			int result = httpDownloader.downFile(jpgUrl, "/jpg/", jpgInfo.getName());
		   String resultMessage = null;
		   if(result == -1){
			   resultMessage = "����ʧ��";
		   }else if(result == 0){
			   resultMessage = "�ļ����سɹ�";
		   }else if(result == 1){
			   resultMessage = "�ļ��Ѿ�����,����Ҫ��������";
		   }
		//System.out.println( result + resultMessage);
		 notify(resultMessage, jpgInfo.getName());
		}
		/**
		 *  ֪ͨ�û��������
		 * */
		private void notify(String resultMessage, String name){
			 NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			 int icon = android.R.drawable.stat_notify_chat;
			 long when = System.currentTimeMillis();//֪ͨ������ʱ��Ϊϵͳ��ǰ��ʱ��
			 //�½�һ��֪ͨ��ָ����ͼ��ͱ���
			 Notification notification = new Notification(icon, resultMessage, when);
			 notification.defaults = Notification.DEFAULT_SOUND;//����Ĭ������
			 notification.flags = Notification.FLAG_AUTO_CANCEL;
			 Intent openintent = new Intent(DownloadService.this, ReadJpgActivity.class);
		    openintent.putExtra("result",name);
			 PendingIntent pendingIntent = PendingIntent.getActivity(DownloadService.this, 0, openintent, 0);
			 notification.setLatestEventInfo(DownloadService.this, "��������", "ͼƬ"+ resultMessage, pendingIntent);	
			 manager.notify(0, notification);
		}
	}
}


