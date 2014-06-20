package mytry.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import mytry.utils.FileUtils;

import org.xmlpull.v1.XmlPullParserException;

public class HttpDownloader {
	private URL url = null;
	//一定要是文本
		public String download(String urlStr) {
			StringBuffer sb = new StringBuffer();
			String line = null;
			BufferedReader buffer = null;
			// 创建一个url对象
			try {
				url = new URL(urlStr);
				// 创建一个Http连接
				HttpURLConnection urlConn = (HttpURLConnection) url
						.openConnection();
				// 使用IO流读取数据
				buffer = new BufferedReader(new InputStreamReader(
						urlConn.getInputStream()));
				while ((line = buffer.readLine()) != null) {
					sb.append(line);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					buffer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return sb.toString();
		}
		
		/**
		 * 该函数返回整形-1：代表下载文件出错 0：代表下载文件成功 1：代表文件已经存在
		 * */
		public int downFile(String urlStr, String path, String fileName){
			InputStream inputStream = null;
			try{
				FileUtils fileUtils = new FileUtils();
				if(fileUtils.isFileExist(fileName, path)){
					return 1;
				}else{
					inputStream = getInputStreamFromUrl(urlStr);
					File resultFile = fileUtils.writeIOFromInput(path, fileName, inputStream);
					if(resultFile == null){
						return -1;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				return -1;
			}finally{
				try{
					inputStream.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return 0;
		}
		
		/**
		 *根据url得到输入流
		 * 
		 * */
		public InputStream getInputStreamFromUrl(String urlStr) throws XmlPullParserException, IOException{
			url = new URL(urlStr);
			HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
			InputStream inputStream = urlconn.getInputStream();
			return inputStream;
		}
}
