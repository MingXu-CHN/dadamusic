package cn.tedu.mediaplayer.ui;

import java.util.List;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout.LayoutParams;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.entity.SongUrl;
import cn.tedu.mediaplayer.listener.OnDownloadPressListener;

public class DownloadPopWindow extends PopupWindow{
	private Activity context;
	private View contentView;
	private RadioGroup radioGroup;
	private List<SongUrl> urls;
	private OnDownloadPressListener listener;
	
	public DownloadPopWindow(Activity context, List<SongUrl> urls, OnDownloadPressListener listener) {
		this.listener = listener;
		this.context = context;
		this.urls = urls;
		//初始化popwindow
		setViews();
	}

	/** 布局加载 并且把初始化控件 */
	private void setViews() {
		contentView = View.inflate(context, R.layout.view_pop_window_download, null);
		setContentView(contentView );
		//控件初始化操作
		initRadioGroup();
		//给下载按钮添加监听
		setListeners();
		//设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true); 
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.PopWindowDownload);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //设置window的attrubutes属性 alpha控制透明度
        final Window window = context.getWindow();
        final android.view.WindowManager.LayoutParams params = window.getAttributes();
		window.setAttributes(params);
		//设置点击屏幕外区域  是popowindow销毁
		this.setOutsideTouchable(true);
		//设置消失的监听
		this.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				ObjectAnimator anim = ObjectAnimator.ofFloat(params, "alpha", 0.3f, 1f);
				anim.addUpdateListener(new AnimatorUpdateListener() {
					//当需要修改控件属性时将会执行该监听方法
					//所以这个方法执行频率非常快
					public void onAnimationUpdate(ValueAnimator animation) {
						float val=(Float)animation.getAnimatedValue();
						params.alpha = val;
						window.setAttributes(params);
					}
				});
				anim.setDuration(100);
				anim.start();
			}
		});
		
        //200毫秒把背景从1变成0.3
		ObjectAnimator anim = ObjectAnimator.ofFloat(params, "alpha", 1f, 0.3f);
		anim.addUpdateListener(new AnimatorUpdateListener() {
			//当需要修改控件属性时将会执行该监听方法
			//所以这个方法执行频率非常快
			public void onAnimationUpdate(ValueAnimator animation) {
				float val=(Float)animation.getAnimatedValue();
				params.alpha = val;
				window.setAttributes(params);
			}
		});
		anim.setDuration(200);
		anim.start();
	}

	/** 给下载按钮添加监听 */
	private void setListeners() {
		Button btn=(Button) contentView.findViewById(R.id.btnDownload);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//获取当前选中项是哪一个版本
				int id=radioGroup.getCheckedRadioButtonId();
				SongUrl url = urls.get(id);
				//已经获取了当前选中的url了，只需要
				//把url返回给fragment，剩下下载的工作
				//让fragment去执行。
				listener.onDownloadPressed(url);
				dismiss();
			}
		});
	}

	/** 需要遍历url  才可以设置radioButton */
	private void initRadioGroup() {
		radioGroup=(RadioGroup) contentView.findViewById(R.id.radioGroup);
		for(int i=0; i<urls.size(); i++){
			SongUrl url = urls.get(i);
			//判断品质
			String pinzhi="";
			switch (url.getFile_bitrate()) {
			case 128:
				pinzhi = "标准品质";
				break;
			case 256:
				pinzhi = "高品质";
				break;
			case 320:
				pinzhi = "超高品质";
				break;
			default:
				if(url.getFile_bitrate() > 320){
					pinzhi = "无损品质";
				}else{
					pinzhi = "普通品质";
				}
			}
			//动态创建RadioButton
			RadioButton rb = (RadioButton) View.inflate(context, R.layout.view_pop_window_radio_button, null);
			rb.setId(i);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 160);
			rb.setLayoutParams(params);
			//根据当前url中的文件大小  设置文本
			String text = Math.floor(10.0*url.getFile_size()/1024/1024)/10+"M";
			rb.setText(pinzhi+"  ("+text+")");
			rb.setChecked(true);
			//没有路径的话   无法选中
			if(url.getFile_link().equals("")){
				rb.setEnabled(false);
				rb.setChecked(false);
			}
			radioGroup.addView(rb);
		}
		//判断是否是一首能下载的音乐都没有
		if(radioGroup.getCheckedRadioButtonId() == -1){
			Button btn=(Button) contentView.findViewById(R.id.btnDownload);
			btn.setEnabled(false);
		}
		
	}
	
}













