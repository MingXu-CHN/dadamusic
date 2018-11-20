package cn.tedu.mediaplayer.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.model.BitmapCallback;

/** 图片相关的工具类 */
public class BitmapUtils {
	
	/*** 异步模糊化处理一张图片 使用callback返回 */
	public static void loadBlurBitmap(final Bitmap bitmap,final int radius, final BitmapCallback callback){
		AsyncTask<String, String, Bitmap> task = new AsyncTask<String, String, Bitmap>(){
			protected Bitmap doInBackground(String... params) {
				Bitmap bm = createBlurBitmap(bitmap, radius);
				return bm;
			}
			protected void onPostExecute(Bitmap result) {
				callback.onBitmapLoaded(result);
			}
		};
		task.execute();
	}
	
	/**
	 * 传递bitmap 传递模糊半径 返回一个被模糊的bitmap 比较耗时
	 * 该方法是一个耗时方法    所以需要在工作线程中执行
	 * @param sentBitmap
	 * @param radius
	 * @return
	 */
	private static Bitmap createBlurBitmap(Bitmap sentBitmap, int radius) {
		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
		if (radius < 1) {
			return (null);
		}
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int[] pix = new int[w * h];
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);
		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;
		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];
		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);

		}
		yw = yi = 0;
		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;
		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

				}

			}
			stackpointer = radius;
			for (x = 0; x < w; x++) {
				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];
				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;
				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];
				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];
				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);

				}
				p = pix[yw + vmin[x]];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];
				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;
				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];
				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];
				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];
				yi++;

			}
			yw += w;

		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;
				sir = stack[i + radius];
				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];
				rbs = r1 - Math.abs(i);
				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];

				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];

				}
				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];
				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;
				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];
				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];
				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;

				}
				p = x + vmin[y];
				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];
				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];
				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;
				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];
				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];
				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];
				yi += w;
			}
		}
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);
		return (bitmap);
	}
		
	
	/**
	 * 通过一个url路径下载一张图片
	 * @param url
	 * @param callback
	 */
	public static void loadBitmap(String url, final BitmapCallback callback){
		if(url == null || url.equals("")){
			callback.onBitmapLoaded(null);
			return;
		}
		String filename = url.substring(url.lastIndexOf("/"));
		final File file = new File(DaDaApp.getApp().getCacheDir(), "images"+filename);
		if(file.exists()){
			Bitmap bitmap = loadBitmap(file);
			callback.onBitmapLoaded(bitmap);
			return;
		}
		RequestQueue queue = DaDaApp.getQueue();
		ImageRequest request = new ImageRequest(
				url, new Listener<Bitmap>() {
					public void onResponse(Bitmap bitmap) {
						try {
							save(bitmap, file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						callback.onBitmapLoaded(bitmap);
					}
				}, 0, 0, Config.RGB_565, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
					}
				});
		queue.add(request);
	}
	
	/**
	 * 通过一个url路径下载一张图片
	 * @param url
	 * @param scale  压缩比例
	 * @param callback
	 */
	public static void loadBitmap(String url, int scale, final BitmapCallback callback){
		if(url == null || url.equals("")){
			callback.onBitmapLoaded(null);
			return;
		}
		final Options opts = new Options();
		opts.inSampleSize = scale;
		String filename = url.substring(url.lastIndexOf("/"));
		final File file = new File(DaDaApp.getApp().getCacheDir(), "images"+filename);
		if(file.exists()){
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
			callback.onBitmapLoaded(bitmap);
			return;
		}
		//文件中没有 需要从服务端下载
		RequestQueue queue = DaDaApp.getQueue();
		ImageRequest request = new ImageRequest(
				url, new Listener<Bitmap>() {
					public void onResponse(Bitmap bitmap) {
						try {
							save(bitmap, file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
						callback.onBitmapLoaded(bitmap);
					}
				}, 0, 0, Config.RGB_565, new ErrorListener() {
					public void onErrorResponse(VolleyError error) {
					}
				});
		queue.add(request);
	}
	
	
	/**
	 * 从文件中加载一个Bitmap对象
	 * @param file
	 * @return
	 */
	public static Bitmap loadBitmap(File file){
		if(!file.exists()){
			return null;
		}
		//调用BitmapFactory的方法 加载图片
		Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
		return bm;
	}
	
	/**
	 * 把bitmap压缩为jpg格式 存入file文件中
	 * @param bitmap
	 * @param file
	 * @throws FileNotFoundException 
	 */
	public static void save(Bitmap bitmap, File file) throws FileNotFoundException{
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(file);
		//压缩并输出
		bitmap.compress(CompressFormat.JPEG, 100, fos);
	}
}



