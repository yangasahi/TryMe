package mytry.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {

	private String SDCardRoot;
	
	public FileUtils(){
		//得到当前外部存储设备的目录
		SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	/**
	 * 在SD卡上创建文件
	 * */
	public File createFileInSDCard(String fileName,String dir) throws IOException{
		File file = new File(SDCardRoot + dir + File.separator + fileName);
		System.out.println(file);
		return file;
	}
	
	/**
	 * 在SD卡上创建目录
	 * 
	 * */
	public File createSDCar(String dir){
		File dirFile = new File(SDCardRoot + dir + File.separator);
		System.out.println(dirFile.mkdir());
		return dirFile;
	}
	
	/**
	 * 判断SD卡上的文件夹是否存在
	 * */
	public boolean isFileExist(String fileName, String path){
		File file = new File(SDCardRoot + path + File.separator + fileName);
		return file.exists();
	}
	
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * */
	public File writeIOFromInput(String path, String fileName, InputStream inputStream){
		File file = null;
		OutputStream output = null;
		try{
			createSDCar(path);
			file = createFileInSDCard(fileName, path);
			output = new FileOutputStream(file);
			byte buffer [] = new byte[4 * 1024];
			int temp;
			while((temp = inputStream.read(buffer)) != -1){
				output.write(buffer, 0, temp);
			}
			output.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				output.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}

	
	
	
}
