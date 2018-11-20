package cn.tedu.mediaplayer.util;

import java.io.File;
import java.io.FileNotFoundException;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import cn.tedu.mediaplayer.app.DaDaApp;

/**
 * 基于LruCache的图片缓存工具类
 */
public class LruBitmapCache implements ImageCache {  
    
    private LruCache<String, Bitmap> mCache;  
  
    public LruBitmapCache() {  
        int maxSize = 70 * 1024 * 1024;  
        mCache = new LruCache<String, Bitmap>(maxSize) {  
            /**
             * 当向LruCache中添加数据后，将会执行该
             * 方法计算bitmap对象占用多少资源
             * （一共才10M）
             * LruCache将会自动计算，是否超出上限，
             * 如果超出上限，则销毁旧对象，释放内存。
             */
            protected int sizeOf(String key, Bitmap bitmap) {  
                return bitmap.getRowBytes() * bitmap.getHeight();  
            }  
        };  
    } 
    
    /** 仅仅去内存缓存中寻找 看有没有图片 */
    public Bitmap getMemoryCacheBitmap(String url){
    	return mCache.get(url);
    }
  
    @Override  
    public Bitmap getBitmap(String url) {  
        Bitmap bm = mCache.get(url);
        if(bm!=null){ //内存缓存中找到了图片
        	return bm;
        }
		//内存缓存中没有图片  去文件缓存中寻找
        String filename = url.substring(url.lastIndexOf("/"));
    	File file = new File(DaDaApp.getApp().getCacheDir(), "images"+filename);
    	bm = BitmapUtils.loadBitmap(file);
    	if(bm!=null){ //文件缓存中找到了  存入内存缓存即可
    		mCache.put(url, bm);
    	}
        return bm;
    }  
  
    @Override  
    public void putBitmap(String url, Bitmap bitmap) {  
        //向内存缓存中存储
    	mCache.put(url, bitmap);
		//向文件缓存中存储    项目的缓存文件夹：
    	///data/data/cn.tedu.mediaplayer/cache/images/xxxx.jpg
    	try {
    		String filename = url.substring(url.lastIndexOf("/"));
        	File file = new File(DaDaApp.getApp().getCacheDir(), "images"+filename);
        	BitmapUtils.save(bitmap, file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }  
  
}  


