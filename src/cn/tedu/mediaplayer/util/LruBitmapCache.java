package cn.tedu.mediaplayer.util;

import java.io.File;
import java.io.FileNotFoundException;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import cn.tedu.mediaplayer.app.DaDaApp;

/**
 * ����LruCache��ͼƬ���湤����
 */
public class LruBitmapCache implements ImageCache {  
    
    private LruCache<String, Bitmap> mCache;  
  
    public LruBitmapCache() {  
        int maxSize = 70 * 1024 * 1024;  
        mCache = new LruCache<String, Bitmap>(maxSize) {  
            /**
             * ����LruCache��������ݺ󣬽���ִ�и�
             * ��������bitmap����ռ�ö�����Դ
             * ��һ����10M��
             * LruCache�����Զ����㣬�Ƿ񳬳����ޣ�
             * ����������ޣ������پɶ����ͷ��ڴ档
             */
            protected int sizeOf(String key, Bitmap bitmap) {  
                return bitmap.getRowBytes() * bitmap.getHeight();  
            }  
        };  
    } 
    
    /** ����ȥ�ڴ滺����Ѱ�� ����û��ͼƬ */
    public Bitmap getMemoryCacheBitmap(String url){
    	return mCache.get(url);
    }
  
    @Override  
    public Bitmap getBitmap(String url) {  
        Bitmap bm = mCache.get(url);
        if(bm!=null){ //�ڴ滺�����ҵ���ͼƬ
        	return bm;
        }
		//�ڴ滺����û��ͼƬ  ȥ�ļ�������Ѱ��
        String filename = url.substring(url.lastIndexOf("/"));
    	File file = new File(DaDaApp.getApp().getCacheDir(), "images"+filename);
    	bm = BitmapUtils.loadBitmap(file);
    	if(bm!=null){ //�ļ��������ҵ���  �����ڴ滺�漴��
    		mCache.put(url, bm);
    	}
        return bm;
    }  
  
    @Override  
    public void putBitmap(String url, Bitmap bitmap) {  
        //���ڴ滺���д洢
    	mCache.put(url, bitmap);
		//���ļ������д洢    ��Ŀ�Ļ����ļ��У�
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


