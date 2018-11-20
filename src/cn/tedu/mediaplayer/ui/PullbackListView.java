package cn.tedu.mediaplayer.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import cn.tedu.mediaplayer.R;

/** �����ص���ListView */
public class PullbackListView extends ListView {
	//��¼��ʼ���λ��
	private Point startPoint;
	private PullbackListView listView = this;
	private View headerView;
	private boolean pulling;
	private View headerImageView;
	private View headerTextView;
	private ImageView ivHeaderActionBarImageView;
	
	public PullbackListView(Context context) {
		super(context);
	}

	public PullbackListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PullbackListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				try {
					//�ж��Ƿ���ʾivActionbarBackground
					if(getScrollYPosition() > headerImageView.getHeight() - ivHeaderActionBarImageView.getHeight()){
						//��ʾivHeaderActionBarImageView
						ivHeaderActionBarImageView.setVisibility(View.VISIBLE);
					}else{
						ivHeaderActionBarImageView.setVisibility(View.INVISIBLE);
					}
				} catch (Exception e) {
				}
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startPoint = new Point((int)ev.getRawX(), (int)ev.getRawY());
			break;
		case MotionEvent.ACTION_MOVE:
			//��ȡ�ƶ��ľ���
			int moveHeight = (int)ev.getRawY() - startPoint.y;
			if(moveHeight > 0  && getScrollYPosition()==0){ //�Ӷ�����������
				//����ListView   y���ϵ�ƽ�ƾ���
				listView.setTranslationY(moveHeight*0.3f);
				//listView��headerͷ���е�ImageView����
				headerImageView.setVisibility(View.INVISIBLE);
				//������  ����ҪListView����ô�������  ��Ҫ����true
				//�õ�ǰ��PullbackListView���ѵ����¼� 
				pulling = true;
			}else{
				headerImageView.setVisibility(View.VISIBLE);
			}
			//�ж��Ƿ���ʾtvUpdateDate
			if(getScrollYPosition() > headerImageView.getHeight() - 140){
				//����textView
				headerTextView.setVisibility(View.INVISIBLE);
			}else{
				headerTextView.setVisibility(View.VISIBLE);
			}
			//�ж��Ƿ���ʾivActionbarBackground
			if(getScrollYPosition() > headerImageView.getHeight() - ivHeaderActionBarImageView.getHeight()){
				//��ʾivHeaderActionBarImageView
				ivHeaderActionBarImageView.setVisibility(View.VISIBLE);
			}else{
				ivHeaderActionBarImageView.setVisibility(View.INVISIBLE);
			}
			
			break;
		case MotionEvent.ACTION_UP:
			//��listView��translationY���Դӵ�ǰֵ�𽥱任��0
			//��Ҫʹ�����Զ���
			ObjectAnimator anim = ObjectAnimator.ofFloat(listView, "translationY", listView.getTranslationY(), 0);
			anim.setDuration(100);
			anim.start();
			pulling = false;
			break;
		}
		return pulling || super.onTouchEvent(ev);
	}
	
	/** ��ȡ��ǰ������Y����� */
	public float getScrollYPosition(){
		if(getFirstVisiblePosition() == 0){ //������header
			return -headerView.getTop();
		}
		return 10000;
	}

	/** ����ͷ */
	public void setHeaderView(View headerView) {
		this.headerView = headerView;
		this.headerImageView = headerView.findViewById(R.id.ivHeaderPic);
		this.headerTextView = headerView.findViewById(R.id.tvUpdateDate);
		
	}

	public void setHeaderActionbarImageView(ImageView ivActionbarBackground) {
		this.ivHeaderActionBarImageView = ivActionbarBackground;
	}
	
}



