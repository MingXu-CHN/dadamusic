package cn.tedu.mediaplayer.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.app.DaDaApp;
import cn.tedu.mediaplayer.entity.Music;
import cn.tedu.mediaplayer.model.BitmapCallback;
import cn.tedu.mediaplayer.util.BitmapUtils;
import cn.tedu.mediaplayer.util.GlobalConsts;

/** ��������еķ���Fragment */
public class PlayPanelCoverFragment extends Fragment{
	private ImageView ivCoverPic;
	private ImageView ivCoverBackground;
	private TextView tvCoverTitle;
	private TextView tvCoverSinger;
	private BroadcastReceiver receiver;
	private ImageView ivShadow;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_panel_cover, null);
		//��ʼ���ؼ�
		setViews(view);
		//ע��㲥������
		registReceivers();
		//��ʼ����������   ��ȡ������Ϣ ����UI
		updateCoverView();
		return view;
	}
	
	/** ע��㲥������ */
	private void registReceivers() {
		receiver = new UpdatePicReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalConsts.ACTION_MUSIC_STARTED);
		getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onDestroyView() {
		getActivity().unregisterReceiver(receiver);
		super.onDestroyView();
	}
	
	/** ��ʼ���ؼ� */
	private void setViews(View view) {
		ivCoverPic = (ImageView) view.findViewById(R.id.ivCoverPic);
		ivCoverBackground = (ImageView) view.findViewById(R.id.ivCoverBackground);
		tvCoverTitle = (TextView) view.findViewById(R.id.tvCoverTitle);
		tvCoverSinger = (TextView) view.findViewById(R.id.tvCoverSinger);
		ivShadow = (ImageView) view.findViewById(R.id.ivShadow);
	}
	
	class UpdatePicReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			//���ܵ������ֿ�ʼ���Ź㲥
			updateCoverView();
		}
	}
	
	/*** ���·������� */
	private void updateCoverView() {
		Music music=DaDaApp.getApp().getCurrentMusic();
		//��������textView
		tvCoverTitle.setText(Html.fromHtml(music.getTitle()));
		tvCoverSinger.setText(Html.fromHtml(music.getAuthor()));
		//��������ͼƬ  ivCoverPic  ivCoverBackground
		//��ȡ��ǰ�����е�ר��ͼƬ·��
		String picPath = music.getSongInfo().getAlbum_500_500();
		BitmapUtils.loadBitmap(picPath, new BitmapCallback() {
			public void onBitmapLoaded(Bitmap bitmap) {
				ivCoverPic.setImageBitmap(bitmap);
			}
		});
		//���ر���ͼƬ 
		BitmapUtils.loadBitmap(picPath, 4, new BitmapCallback() {
			public void onBitmapLoaded(Bitmap bitmap) {
				if(bitmap==null){
					return;
				}
				//ģ���������  ���ñ���ͼƬ
				BitmapUtils.loadBlurBitmap(bitmap, 10, new BitmapCallback() {
					public void onBitmapLoaded(Bitmap bitmap) {
						ivCoverBackground.setImageBitmap(bitmap);						
					}
				});
			}
		});
		
	}

	public void slide(float f) {
		ivCoverBackground.setAlpha(f);
		if(f<0.8){
			f = 0.8f;  //�ƽ����  
		}
		ivCoverPic.setScaleX(f);
		ivCoverPic.setScaleY(f);
		ivShadow.setScaleX(f);
		ivShadow.setScaleY(f);
		tvCoverTitle.setScaleX(f);
		tvCoverTitle.setScaleY(f);
		tvCoverSinger.setScaleX(f);
		tvCoverSinger.setScaleY(f);
	}
	
	
}





