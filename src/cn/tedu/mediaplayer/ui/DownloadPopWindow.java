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
		//��ʼ��popwindow
		setViews();
	}

	/** ���ּ��� ���Ұѳ�ʼ���ؼ� */
	private void setViews() {
		contentView = View.inflate(context, R.layout.view_pop_window_download, null);
		setContentView(contentView );
		//�ؼ���ʼ������
		initRadioGroup();
		//�����ذ�ť��Ӽ���
		setListeners();
		//����SelectPicPopupWindow��������Ŀ�  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //����SelectPicPopupWindow��������ĸ�  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //����SelectPicPopupWindow��������ɵ��  
        this.setFocusable(true); 
        //����SelectPicPopupWindow�������嶯��Ч��  
        this.setAnimationStyle(R.style.PopWindowDownload);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //����window��attrubutes���� alpha����͸����
        final Window window = context.getWindow();
        final android.view.WindowManager.LayoutParams params = window.getAttributes();
		window.setAttributes(params);
		//���õ����Ļ������  ��popowindow����
		this.setOutsideTouchable(true);
		//������ʧ�ļ���
		this.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				ObjectAnimator anim = ObjectAnimator.ofFloat(params, "alpha", 0.3f, 1f);
				anim.addUpdateListener(new AnimatorUpdateListener() {
					//����Ҫ�޸Ŀؼ�����ʱ����ִ�иü�������
					//�����������ִ��Ƶ�ʷǳ���
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
		
        //200����ѱ�����1���0.3
		ObjectAnimator anim = ObjectAnimator.ofFloat(params, "alpha", 1f, 0.3f);
		anim.addUpdateListener(new AnimatorUpdateListener() {
			//����Ҫ�޸Ŀؼ�����ʱ����ִ�иü�������
			//�����������ִ��Ƶ�ʷǳ���
			public void onAnimationUpdate(ValueAnimator animation) {
				float val=(Float)animation.getAnimatedValue();
				params.alpha = val;
				window.setAttributes(params);
			}
		});
		anim.setDuration(200);
		anim.start();
	}

	/** �����ذ�ť��Ӽ��� */
	private void setListeners() {
		Button btn=(Button) contentView.findViewById(R.id.btnDownload);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//��ȡ��ǰѡ��������һ���汾
				int id=radioGroup.getCheckedRadioButtonId();
				SongUrl url = urls.get(id);
				//�Ѿ���ȡ�˵�ǰѡ�е�url�ˣ�ֻ��Ҫ
				//��url���ظ�fragment��ʣ�����صĹ���
				//��fragmentȥִ�С�
				listener.onDownloadPressed(url);
				dismiss();
			}
		});
	}

	/** ��Ҫ����url  �ſ�������radioButton */
	private void initRadioGroup() {
		radioGroup=(RadioGroup) contentView.findViewById(R.id.radioGroup);
		for(int i=0; i<urls.size(); i++){
			SongUrl url = urls.get(i);
			//�ж�Ʒ��
			String pinzhi="";
			switch (url.getFile_bitrate()) {
			case 128:
				pinzhi = "��׼Ʒ��";
				break;
			case 256:
				pinzhi = "��Ʒ��";
				break;
			case 320:
				pinzhi = "����Ʒ��";
				break;
			default:
				if(url.getFile_bitrate() > 320){
					pinzhi = "����Ʒ��";
				}else{
					pinzhi = "��ͨƷ��";
				}
			}
			//��̬����RadioButton
			RadioButton rb = (RadioButton) View.inflate(context, R.layout.view_pop_window_radio_button, null);
			rb.setId(i);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 160);
			rb.setLayoutParams(params);
			//���ݵ�ǰurl�е��ļ���С  �����ı�
			String text = Math.floor(10.0*url.getFile_size()/1024/1024)/10+"M";
			rb.setText(pinzhi+"  ("+text+")");
			rb.setChecked(true);
			//û��·���Ļ�   �޷�ѡ��
			if(url.getFile_link().equals("")){
				rb.setEnabled(false);
				rb.setChecked(false);
			}
			radioGroup.addView(rb);
		}
		//�ж��Ƿ���һ�������ص����ֶ�û��
		if(radioGroup.getCheckedRadioButtonId() == -1){
			Button btn=(Button) contentView.findViewById(R.id.btnDownload);
			btn.setEnabled(false);
		}
		
	}
	
}













