package mytry.image.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import mytry.test.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class ImageSpecific
{
	private Context mContext;
	
	public static final int FLAG_OLD_REMEMBER = 0;
	public static final int FLAG_BLUR = FLAG_OLD_REMEMBER + 1;
	public static final int FLAG_SHARPEN = FLAG_OLD_REMEMBER + 2;
	public static final int FLAG_OVERLAY = FLAG_OLD_REMEMBER + 3;
	public static final int FLAG_FILM = FLAG_OLD_REMEMBER + 4;
	public static final int FLAG_EMBOSS = FLAG_OLD_REMEMBER + 5;
	public static final int FLAG_SUNSHINE = FLAG_OLD_REMEMBER + 6;
	public static final int FLAG_NEON = FLAG_OLD_REMEMBER + 7;
	public static final int FLAG_ALPHA_LAYER = FLAG_OLD_REMEMBER + 8;
	
	public ImageSpecific(Context context)
	{
		mContext = context;
	}
	
	/**
	 * 图片特效处理
	 * @param bmp
	 * @param flag
	 * @return
	 */
	public Bitmap imageSpecific(Bitmap bmp, int flag)
	{
		Bitmap bm = null;
		switch (flag)
		{
		case FLAG_OLD_REMEMBER:
//			bm = oldRemeber(bmp);
//			bm = halo(bmp, 0, 0, 0);
//			bm = stria(bmp);
//			bm = chessbord(400);
			bm = cropBitmap(bmp);
			break;
		case FLAG_BLUR:
//			bm = blurImage(bmp, 3);
			bm = blurImageAmeliorate(bmp);
			break;
		case FLAG_SHARPEN:
//			bm = sharpenImage(bmp, 3);
			bm = sharpenImageAmeliorate(bmp);
			break;
		case FLAG_OVERLAY:
//			bm = overlay(bmp);
			bm = overlayAmeliorate(bmp);
			break;
		case FLAG_FILM:
//			bm = film(bmp);
			bm = filmAmeliorate(bmp);
			break;
		case FLAG_EMBOSS:
//			bm = emboss(bmp);
			bm = embossAmeliorate(bmp);
			break;
		case FLAG_SUNSHINE:
//			bm = sunshine(bmp);
			bm = sunshineAmeliorate(bmp);
			break;
		case FLAG_NEON:
			bm = neon(bmp);
			break;
		case FLAG_ALPHA_LAYER:
			bm = alphaLayer(bmp);
			break;
		}
		
		return bm;
	}
	
	/**
	 * �?��效果(相对之前做了优化快一�?
	 * @param bmp
	 * @return
	 */
	private Bitmap oldRemeber(Bitmap bmp)
	{
		// 速度测试
		long start = System.currentTimeMillis();
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		int pixColor = 0;
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 0; i < height; i++)
		{
			for (int k = 0; k < width; k++)
			{
				pixColor = pixels[width * i + k];
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
				newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
				newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
				int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
				pixels[width * i + k] = newColor;
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * 模糊效果
	 * @param bmp
	 * @return
	 */
	private Bitmap blurImage(Bitmap bmp)
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int newColor = 0;
		
		int[][] colors = new int[9][3];
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				for (int m = 0; m < 9; m++)
				{
					int s = 0;
					int p = 0;
					switch(m)
					{
					case 0:
						s = i - 1;
						p = k - 1;
						break;
					case 1:
						s = i;
						p = k - 1;
						break;
					case 2:
						s = i + 1;
						p = k - 1;
						break;
					case 3:
						s = i + 1;
						p = k;
						break;
					case 4:
						s = i + 1;
						p = k + 1;
						break;
					case 5:
						s = i;
						p = k + 1;
						break;
					case 6:
						s = i - 1;
						p = k + 1;
						break;
					case 7:
						s = i - 1;
						p = k;
						break;
					case 8:
						s = i;
						p = k;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}
				
				for (int m = 0; m < 9; m++)
				{
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}
				
				newR = (int) (newR / 9F);
				newG = (int) (newG / 9F);
				newB = (int) (newB / 9F);
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);
				
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		
		return bitmap;
	}
	
	/**
	 * 柔化效果(高斯模糊)(优化后比上面快三�?
	 * @param bmp
	 * @return
	 */
	private Bitmap blurImageAmeliorate(Bitmap bmp)
	{
		long start = System.currentTimeMillis();
		// 高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int delta = 16; // 值越小图片会越亮，越大则越暗
		
		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				idx = 0;
				for (int m = -1; m <= 1; m++)
				{
					for (int n = -1; n <= 1; n++)
					{
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);
						
						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}
				
				newR /= delta;
				newG /= delta;
				newB /= delta;
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * 锐化效果
	 * @param bmp
	 * @return
	 */
	private Bitmap sharpenImage(Bitmap bmp, float sharpen)
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		float alpha = 0.9F;
		int newColor = 0;
		
		int[][] colors = new int[8][3];
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				for (int m = 0; m < 8; m++)
				{
					int s = 0;
					int p = 0;
					switch(m)
					{
					case 0:
						s = i - 1;
						p = k - 1;
						break;
					case 1:
						s = i;
						p = k - 1;
						break;
					case 2:
						s = i + 1;
						p = k - 1;
						break;
					case 3:
						s = i + 1;
						p = k;
						break;
					case 4:
						s = i + 1;
						p = k + 1;
						break;
					case 5:
						s = i;
						p = k + 1;
						break;
					case 6:
						s = i - 1;
						p = k + 1;
						break;
					case 7:
						s = i - 1;
						p = k;
						break;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}
				
				pixColor = bmp.getPixel(i, k);
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				for (int m = 0; m < 8; m++)
				{
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}
				
				newR = (int) ((pixR - newR / 8F) * alpha) + pixR;
				newG = (int) ((pixG - newG / 8F) * alpha) + pixG;
				newB = (int) ((pixB - newB / 8F) * alpha) + pixB;
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);
				
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		
		return bitmap;
	}
	
	/**
	 * 图片锐化（拉普拉斯变换）
	 * @param bmp
	 * @return
	 */
	private Bitmap sharpenImageAmeliorate(Bitmap bmp)
	{
		long start = System.currentTimeMillis();
		// 拉普拉斯矩阵
		int[] laplacian = new int[] { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int idx = 0;
		float alpha = 0.3F;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				idx = 0;
				for (int m = -1; m <= 1; m++)
				{
					for (int n = -1; n <= 1; n++)
					{
						pixColor = pixels[(i + n) * width + k + m];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);
						
						newR = newR + (int) (pixR * laplacian[idx] * alpha);
						newG = newG + (int) (pixG * laplacian[idx] * alpha);
						newB = newB + (int) (pixB * laplacian[idx] * alpha);
						idx++;
					}
				}
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				pixels[i * width + k] = Color.argb(255, newR, newG, newB);
				newR = 0;
				newG = 0;
				newB = 0;
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * 图片效果叠加
	 * @param bmp
	 * @return
	 */
	private Bitmap overlay(Bitmap bmp)
	{
		long start = System.currentTimeMillis();
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		Bitmap overlay = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rainbow_overlay);
		int w = overlay.getWidth();
		int h = overlay.getHeight();
		float scaleX = width * 1F / w;
		float scaleY = height * 1F / h;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		
		Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix, true);
		
		int pixColor = 0;
		int layColor = 0;
		int newColor = 0;
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixA = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newA = 0;
		
		int layR = 0;
		int layG = 0;
		int layB = 0;
		int layA = 0;
		
		float alpha = 0.5F;
		for (int i = 0; i < width; i++)
		{
			for (int k = 0; k < height; k++)
			{
				pixColor = bmp.getPixel(i, k);
				layColor = overlayCopy.getPixel(i, k);
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				pixA = Color.alpha(pixColor);
				
				layR = Color.red(layColor);
				layG = Color.green(layColor);
				layB = Color.blue(layColor);
				layA = Color.alpha(layColor);
				
				newR = (int) (pixR * alpha + layR * (1 - alpha));
				newG = (int) (pixG * alpha + layG * (1 - alpha));
				newB = (int) (pixB * alpha + layB * (1 - alpha));
				layA = (int) (pixA * alpha + layA * (1 - alpha));
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				newA = Math.min(255, Math.max(0, layA));
				
				newColor = Color.argb(newA, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);
			}
		}
		
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * 图片效果叠加
	 * @param bmp 限制了尺寸大小的Bitmap
	 * @return
	 */
	private Bitmap overlayAmeliorate(Bitmap bmp)
	{
		long start = System.currentTimeMillis();
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		// 对边框图片进行缩�?
		Bitmap overlay = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rainbow_overlay);
		int w = overlay.getWidth();
		int h = overlay.getHeight();
		float scaleX = width * 1F / w;
		float scaleY = height * 1F / h;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		
		Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix, true);
		
		int pixColor = 0;
		int layColor = 0;
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixA = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newA = 0;
		
		int layR = 0;
		int layG = 0;
		int layB = 0;
		int layA = 0;
		
		final float alpha = 0.5F;
		
		int[] srcPixels = new int[width * height];
		int[] layPixels = new int[width * height];
		bmp.getPixels(srcPixels, 0, width, 0, 0, width, height);
		overlayCopy.getPixels(layPixels, 0, width, 0, 0, width, height);
		
		int pos = 0;
		for (int i = 0; i < height; i++)
		{
			for (int k = 0; k < width; k++)
			{
				pos = i * width + k;
				pixColor = srcPixels[pos];
				layColor = layPixels[pos];
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				pixA = Color.alpha(pixColor);
				
				layR = Color.red(layColor);
				layG = Color.green(layColor);
				layB = Color.blue(layColor);
				layA = Color.alpha(layColor);
				
				newR = (int) (pixR * alpha + layR * (1 - alpha));
				newG = (int) (pixG * alpha + layG * (1 - alpha));
				newB = (int) (pixB * alpha + layB * (1 - alpha));
				layA = (int) (pixA * alpha + layA * (1 - alpha));
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				newA = Math.min(255, Math.max(0, layA));
				
				srcPixels[pos] = Color.argb(newA, newR, newG, newB);
			}
		}
		
		bitmap.setPixels(srcPixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "overlayAmeliorate used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * 浮雕效果
	 * @param bmp
	 * @return
	 */
	private Bitmap emboss(Bitmap bmp)
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				pixColor = bmp.getPixel(i, k);
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				pixColor = bmp.getPixel(i+1, k);
				newR = Color.red(pixColor) - pixR + 127;
				newG = Color.green(pixColor) - pixG + 127;
				newB = Color.blue(pixColor) - pixB + 127;
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				bitmap.setPixel(i, k, Color.argb(255, newR, newG, newB));
			}
		}
		
		return bitmap;
	}
	
	/**
	 * 浮雕效果
	 * @param bmp
	 * @return
	 */
	private Bitmap embossAmeliorate(Bitmap bmp)
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				pos = i * width + k;
				pixColor = pixels[pos];
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				pixColor = pixels[pos + 1];
				newR = Color.red(pixColor) - pixR + 127;
				newG = Color.green(pixColor) - pixG + 127;
				newB = Color.blue(pixColor) - pixB + 127;
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				pixels[pos] = Color.argb(255, newR, newG, newB);
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/**
	 * 底片效果
	 * @param bmp
	 * @return
	 */
	private Bitmap film(Bitmap bmp)
	{
		// 255 - x
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				pixColor = bmp.getPixel(i, k);
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				newR = 255 - pixR;
				newG = 255 - pixG;
				newB = 255 - pixB;
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				bitmap.setPixel(i, k, Color.argb(255, newR, newG, newB));
			}
		}
		
		return bitmap;
	}
	
	/**
	 * 底片效果
	 * @param bmp
	 * @return
	 */
	private Bitmap filmAmeliorate(Bitmap bmp)
	{
		// RGBA的最大�?
		final int MAX_VALUE = 255;
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				pos = i * width + k;
				pixColor = pixels[pos];
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				newR = MAX_VALUE - pixR;
				newG = MAX_VALUE - pixG;
				newB = MAX_VALUE - pixB;
				
				newR = Math.min(MAX_VALUE, Math.max(0, newR));
				newG = Math.min(MAX_VALUE, Math.max(0, newG));
				newB = Math.min(MAX_VALUE, Math.max(0, newB));
				
				pixels[pos] = Color.argb(MAX_VALUE, newR, newG, newB);
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/**
	 * 光照效果
	 * @param bmp
	 * @return
	 */
	private Bitmap sunshine(Bitmap bmp)
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int centerX = width / 2;
		int centerY = height / 2;
		int radius = Math.min(centerX, centerY);
		
		float strength = 150F; // 光照强度 100~150
		
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				pixColor = bmp.getPixel(i, k);
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				newR = pixR;
				newG = pixG;
				newB = pixB;
				
				int distance = (int) (Math.pow((centerX - i), 2) + Math.pow(centerY - k, 2));
				if (distance < radius * radius)
				{
					int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
					newR = pixR + result;
					newG = pixG + result;
					newB = pixB + result;
				}
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				bitmap.setPixel(i, k, Color.argb(255, newR, newG, newB));
			}
		}
		
		return bitmap;
	}
	
	/**
	 * 光照效果
	 * @param bmp
	 * @return
	 */
	private Bitmap sunshineAmeliorate(Bitmap bmp)
	{
		final int width = bmp.getWidth();
		final int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int centerX = width / 2;
		int centerY = height / 2;
		int radius = Math.min(centerX, centerY);
		
		final float strength = 150F; // 光照强度 100~150
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		int pos = 0;
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				pos = i * width + k;
				pixColor = pixels[pos];
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				newR = pixR;
				newG = pixG;
				newB = pixB;
				
				// 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
				int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
				if (distance < radius * radius)
				{
					// 按照距离大小计算增加的光照�?
					int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
					newR = pixR + result;
					newG = pixG + result;
					newB = pixB + result;
				}
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				pixels[pos] = Color.argb(255, newR, newG, newB);
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	/**
	 * 霓虹效果
	 * @param bmp
	 * @return
	 */
	private Bitmap neon(Bitmap bmp)
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		int pixColor1 = 0;
		int pixColor2 = 0;
		
		int pixR1 = 0;
		int pixG1 = 0;
		int pixB1 = 0;
		int pixR2 = 0;
		int pixG2 = 0;
		int pixB2 = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int delta = 2;
		for (int i = 1, length = width - 1; i < length; i++)
		{
			for (int k = 1, len = height - 1; k < len; k++)
			{
				pixColor = bmp.getPixel(i, k);
				pixColor1 = bmp.getPixel(i+1, k);
				pixColor2 = bmp.getPixel(i, k+1);
				
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				
				pixR1 = Color.red(pixColor1);
				pixG1 = Color.green(pixColor1);
				pixB1 = Color.blue(pixColor1);
				
				pixR2 = Color.red(pixColor2);
				pixG2 = Color.green(pixColor2);
				pixB2 = Color.blue(pixColor2);
				
				newR = (int) (Math.sqrt(Math.pow(pixR - pixR1, 2) + Math.pow(pixR - pixR2, 2)) * delta);
				newG = (int) (Math.sqrt(Math.pow(pixG - pixG1, 2) + Math.pow(pixG - pixG2, 2)) * delta);
				newB = (int) (Math.sqrt(Math.pow(pixB - pixB1, 2) + Math.pow(pixB - pixB2, 2)) * delta);
				
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				
				bitmap.setPixel(i, k, Color.argb(255, newR, newG, newB));
			}
		}
		
		return bitmap;
	}
	
	/**
	 * 叠加边框图片有用部分
	 * @param bmp
	 * @return
	 */
	private Bitmap alphaLayer(Bitmap bmp)
	{
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		// 边框图片
		Bitmap overlay = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.black);
		int w = overlay.getWidth();
		int h = overlay.getHeight();
		float scaleX = width * 1F / w;
		float scaleY = height * 1F / h;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleX, scaleY);
		
		Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix, true);
		
		int pixColor = 0;
		int layColor = 0;
		int newColor = 0;
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		int pixA = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		int newA = 0;
		
		int layR = 0;
		int layG = 0;
		int layB = 0;
		int layA = 0;
		
		float alpha = 0.3F;
		float alphaR = 0F;
		float alphaG = 0F;
		float alphaB = 0F;
		for (int i = 0; i < width; i++)
		{
			for (int k = 0; k < height; k++)
			{
				pixColor = bmp.getPixel(i, k);
				layColor = overlayCopy.getPixel(i, k);
				
				// 获取原图片的RGBA�?
				pixR = Color.red(pixColor);
				pixG = Color.green(pixColor);
				pixB = Color.blue(pixColor);
				pixA = Color.alpha(pixColor);
				
				// 获取边框图片的RGBA�?
				layR = Color.red(layColor);
				layG = Color.green(layColor);
				layB = Color.blue(layColor);
				layA = Color.alpha(layColor);
				
				// 颜色与纯黑色相近的点
				if (layR < 20 && layG < 20 && layB < 20)
				{
					alpha = 1F;
				}
				else
				{
					alpha = 0.3F;
				}
				
				alphaR = alpha;
				alphaG = alpha;
				alphaB = alpha;
				
				// 两种颜色叠加
				newR = (int) (pixR * alphaR + layR * (1 - alphaR));
				newG = (int) (pixG * alphaG + layG * (1 - alphaG));
				newB = (int) (pixB * alphaB + layB * (1 - alphaB));
				layA = (int) (pixA * alpha + layA * (1 - alpha));
				
				// 值在0~255之间
				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));
				newA = Math.min(255, Math.max(0, layA));
				
				newColor = Color.argb(newA, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);
			}
		}
		
		
		return bitmap;
	}
	
	/**
	 * 光晕效果
	 * @param bmp
	 * @param x 光晕中心点在bmp中的x坐标
	 * @param y 光晕中心点在bmp中的y坐标
	 * @param r 光晕的半�?
	 * @return
	 */
	private Bitmap halo(Bitmap bmp, int x, int y, float r)
	{
		long start = System.currentTimeMillis();
		// 高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		x = (width - 40) / 2;
		y = (height - 40) / 2;
		r = 60;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		int pixR = 0;
		int pixG = 0;
		int pixB = 0;
		
		int pixColor = 0;
		
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int delta = 18; // 值越小图片会越亮，越大则越暗
		
		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++)
		{
			for (int k = 1, len = width - 1; k < len; k++)
			{
				idx = 0;
				int distance = (int) (Math.pow(k - x, 2) + Math.pow(i - y, 2));
				// 不是中心区域的点做模糊处�?
				if (distance > r * r)
				{
					for (int m = -1; m <= 1; m++)
					{
						for (int n = -1; n <= 1; n++)
						{
							pixColor = pixels[(i + m) * width + k + n];
							pixR = Color.red(pixColor);
							pixG = Color.green(pixColor);
							pixB = Color.blue(pixColor);
							
							newR = newR + (int) (pixR * gauss[idx]);
							newG = newG + (int) (pixG * gauss[idx]);
							newB = newB + (int) (pixB * gauss[idx]);
							idx++;
						}
					}
					
					newR /= delta;
					newG /= delta;
					newB /= delta;
					
					newR = Math.min(255, Math.max(0, newR));
					newG = Math.min(255, Math.max(0, newG));
					newB = Math.min(255, Math.max(0, newB));
					
					pixels[i * width + k] = Color.argb(255, newR, newG, newB);
					
					newR = 0;
					newG = 0;
					newB = 0;
				}
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * 黑色条纹效果
	 * @param bmp
	 * @return
	 */
	private Bitmap stria(Bitmap bmp)
	{
		long start = System.currentTimeMillis();
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565); // 创建�?��相同大小的图�?
		
		// 保存像素点的RGB�?
		int newR = 0;
		int newG = 0;
		int newB = 0;
		
		int[] pixels = new int[width * height]; // 保存图片的像素点信息
		bmp.getPixels(pixels, 0, width, 0, 0, width, height); // 将整个图片保存到�?��数组中，每width个长度为�?��
		
		final int delta = 40; // �?0个像素的高度作为�?��单位
		final int blackHeight = 10; // 黑色区域高度
		final int BLACK = 0;
		
		for (int i = 0; i < height; i++)
		{
			// 对图片的进行纵向处理
			// 每隔30个像素的高度就会产生�?��高度�?0个像素的黑色宽带
			// �?0个像素为�?��单位，前面的10像素就会被处理成黑色
			if (i % delta <= blackHeight)
			{
				for (int k = 0; k < width; k++)
				{
					// 对当前像素点赋新的RGB�?
					newR = BLACK;
					newG = BLACK;
					newB = BLACK;
					
					// Color.argb()，是将四�?~255的�?组成�?��像素点，也就是RGBA值，A是alpha，即透明�?
					pixels[i * width + k] = Color.argb(255, newR, newG, newB); // 修改像素�?
				}
			}
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height); // 向创建的相同大小的新图片绘像素点�?
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/**
	 * 绘制国际象棋棋盘
	 * @param width 棋盘的宽�?
	 * @return
	 */
	private Bitmap chessbord(int width)
	{
		long start = System.currentTimeMillis();
		Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565);
		int[] pixels = new int[width * width];
		
		final int delta = width / 8; // 将整个图片分�? X 8个区域，delta表示每个区域的宽度或高度
		final int blackPix = Color.BLACK; // 黑色�?
		final int whitePix = Color.WHITE; // 白色�?
		
		int pixColor = whitePix;
		boolean isWhite = false; // 白色的标�?
		for (int i = 0; i < width; i++) // 纵向
		{
			isWhite = !(i / delta % 2 == 0); // 第一块区域开始为黑色，纵向的奇数块位置是白色
			for (int k = 0; k < width; k++) // 横向
			{
				if (k / delta % 2 != 0) // 横向上的黑白相间，偶数块区域是黑�?
				{
					pixColor = isWhite ? blackPix : whitePix; // 如果前面的是白色则要变成黑色
				}
				else
				{
					pixColor = isWhite ? whitePix : blackPix; // 在纵向上是黑白相�?
				}
				
//				pixColor = k / delta % 2 != 0 ? (isWhite ? blackPix : whitePix) : (isWhite ? whitePix : blackPix);
				pixels[i * width + k] = pixColor;
			}
			
		}
		
		bitmap.setPixels(pixels, 0, width, 0, 0, width, width);
		long end = System.currentTimeMillis();
		Log.d("may", "used time="+(end - start));
		return bitmap;
	}
	
	/** 图片的上半部分标�?*/
	public static final int FLAG_TOP = 0x1;
	/** 图片的下半部分标�?*/
	public static final int FLAG_BOTTOM = FLAG_TOP << 1;
	/** 图片的左半部分标�?*/
	public static final int FLAG_LEFT = FLAG_TOP << 2;
	/** 图片的右半部分标�?*/
	public static final int FLAG_RIGHT = FLAG_TOP << 3;
	
	public Bitmap cropBitmap(Bitmap bmp)
	{
		// 将图片切成左右两�?
		Bitmap left = cropBitmapLandscape(bmp, FLAG_LEFT);
		Bitmap right = cropBitmapLandscape(bmp, FLAG_RIGHT);
		
		// 再将左右两块图片切成四块
		Bitmap leftTop = cropBitmapPortrait(left, FLAG_TOP);
		Bitmap leftBottom = cropBitmapPortrait(left, FLAG_BOTTOM);
		Bitmap rightTop = cropBitmapPortrait(right, FLAG_TOP);
		Bitmap rightBottom = cropBitmapPortrait(right, FLAG_BOTTOM);
		
		// 原左右两块图片回�?
		left.recycle();
		right.recycle();
		left = null;
		right = null;
		
		// 创建新图片，中间�?0个像素的间距
		final int width = bmp.getWidth() + 10;
		final int height = bmp.getHeight() + 10;
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		// 组合图片
		final int leftPos = width - leftBottom.getWidth();
		final int topPos = height - leftBottom.getHeight();
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(rightBottom, 0, 0, null); // 放在左上�?
		canvas.drawBitmap(leftBottom, leftPos, 0, null); // 放在右上�?
		canvas.drawBitmap(rightTop, 0, topPos, null); // 放在左下�?
		canvas.drawBitmap(leftTop, leftPos, topPos, null); // 放在右下�?
		
		// 图片回收
		leftTop.recycle();
		leftBottom.recycle();
		rightTop.recycle();
		rightBottom.recycle();
		
		rightTop = null;
		leftTop = null;
		leftBottom = null;
		rightBottom = null;
		
		return bitmap;
	}
	
	/**
	 * 水平切割图片，也就是将图片分成左右两�?
	 * @param bmp 图片
	 * @param flag 是保留左边还是右边的标识
	 * @return
	 */
	private Bitmap cropBitmapLandscape(Bitmap bmp, int flag)
	{
		final int width = bmp.getWidth();
		final int height = bmp.getHeight();
		int startWidth = 0; // 起始宽度位置
		int endWidth = width / 2; // 结束宽度位置
		Bitmap bitmap = Bitmap.createBitmap(endWidth, height, Bitmap.Config.RGB_565); // 创建新的图片，宽度只有原来的�?��
		
		switch (flag)
		{
		case FLAG_LEFT:
			break;
		case FLAG_RIGHT:
			startWidth = endWidth;
			endWidth = width;
			break;
		}
		
		Rect r = new Rect(startWidth, 0, endWidth, height); // 图片要切的范�?
		Rect rect = new Rect(0, 0, width / 2, height); // 新图片的大小
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(bmp, r, rect, null); // 切割图片
		return bitmap;
	}
	
	/**
	 * 垂直切割图片，也就是说将图片切成上下两块
	 * @param bmp
	 * @param flag 图片是保留上面还是下�?
	 * @return
	 */
	private Bitmap cropBitmapPortrait(Bitmap bmp, int flag)
	{
		final int width = bmp.getWidth();
		final int height = bmp.getHeight();
		int startHeight = 0; // 高度的起始位�?
		int endHeight = height / 2; // 高度的结束位�?
		Bitmap bitmap = Bitmap.createBitmap(width, height / 2, Bitmap.Config.RGB_565); // 创建新图片，高度只有原来的一�?
		
		switch (flag)
		{
		case FLAG_TOP:
			break;
		case FLAG_BOTTOM:
			startHeight = endHeight;
			endHeight = height;
			break;
		}
		
		Rect r = new Rect(0, startHeight, width, endHeight); // 图片要切的范�?
		Rect rect = new Rect(0, 0, width, height / 2); // 新图片的大小
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(bmp, r, rect, null); // 切割图片
		return bitmap;
	}
	
	/**
	 * 保存图片到本�?
	 * @param bm
	 * @return
	 */
	private String saveToLocal(Bitmap bm)
    {
    	String path = "/sdcard/overlay.jpg";
    	try
		{
			FileOutputStream fos = new FileOutputStream(path);
			bm.compress(CompressFormat.JPEG, 75, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
		return path;
    }
}
