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

/** 下拉回弹的ListView */
public class PullbackListView extends ListView {
	//记录起始点的位置
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
					//判断是否显示ivActionbarBackground
					if(getScrollYPosition() > headerImageView.getHeight() - ivHeaderActionBarImageView.getHeight()){
						//显示ivHeaderActionBarImageView
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
			//获取移动的距离
			int moveHeight = (int)ev.getRawY() - startPoint.y;
			if(moveHeight > 0  && getScrollYPosition()==0){ //从顶部向下拉动
				//设置ListView   y轴上的平移距离
				listView.setTranslationY(moveHeight*0.3f);
				//listView的header头部中的ImageView隐藏
				headerImageView.setVisibility(View.INVISIBLE);
				//下拉中  不需要ListView捕获该触摸监听  需要返回true
				//让当前的PullbackListView消费掉该事件 
				pulling = true;
			}else{
				headerImageView.setVisibility(View.VISIBLE);
			}
			//判断是否显示tvUpdateDate
			if(getScrollYPosition() > headerImageView.getHeight() - 140){
				//隐藏textView
				headerTextView.setVisibility(View.INVISIBLE);
			}else{
				headerTextView.setVisibility(View.VISIBLE);
			}
			//判断是否显示ivActionbarBackground
			if(getScrollYPosition() > headerImageView.getHeight() - ivHeaderActionBarImageView.getHeight()){
				//显示ivHeaderActionBarImageView
				ivHeaderActionBarImageView.setVisibility(View.VISIBLE);
			}else{
				ivHeaderActionBarImageView.setVisibility(View.INVISIBLE);
			}
			
			break;
		case MotionEvent.ACTION_UP:
			//让listView的translationY属性从当前值逐渐变换到0
			//需要使用属性动画
			ObjectAnimator anim = ObjectAnimator.ofFloat(listView, "translationY", listView.getTranslationY(), 0);
			anim.setDuration(100);
			anim.start();
			pulling = false;
			break;
		}
		return pulling || super.onTouchEvent(ev);
	}
	
	/** 获取当前滚动的Y轴距离 */
	public float getScrollYPosition(){
		if(getFirstVisiblePosition() == 0){ //看到了header
			return -headerView.getTop();
		}
		return 10000;
	}

	/** 设置头 */
	public void setHeaderView(View headerView) {
		this.headerView = headerView;
		this.headerImageView = headerView.findViewById(R.id.ivHeaderPic);
		this.headerTextView = headerView.findViewById(R.id.tvUpdateDate);
		
	}

	public void setHeaderActionbarImageView(ImageView ivActionbarBackground) {
		this.ivHeaderActionBarImageView = ivActionbarBackground;
	}
	
}



