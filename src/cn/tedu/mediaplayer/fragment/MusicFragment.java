package cn.tedu.mediaplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.adapter.CommonPagerAdapter;

public class MusicFragment extends Fragment{
	private RadioGroup radioGroup;
	private RadioButton radioTuijian;
	private RadioButton radioGedan;
	private RadioButton radioBangdan;
	private RadioButton radioShipin;
	private RadioButton radioKge;
	private TextView tvIndicator;
	
	private ViewPager viewPager;
	private List<Fragment> fragments;
	private CommonPagerAdapter pagerAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_music, null);
		//�ؼ���ʼ��
		setViews(view);
		//����pager������
		setPagerAdapter();
		//���ü���
		setListeners();
		return view;
	}
	
	/** ���ü��� */
	private void setListeners() {
		//����viewPagerʱ ����radioGroup
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			//��viewpager��ҳ֮��ִ��
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					radioTuijian.setChecked(true);
					break;
				case 1:
					radioGedan.setChecked(true);
					break;
				case 2:
					radioBangdan.setChecked(true);
					break;
				case 3:
					radioShipin.setChecked(true);
					break;
				case 4:
					radioKge.setChecked(true);
					break;
				}
			}
			//��viewpager����ʱִ��
			public void onPageScrolled(int i, float p, int px) {
				//����translationX��ֵ
				DisplayMetrics metric = new DisplayMetrics();  
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);  
				int width = metric.widthPixels/5;     // ��Ļ���/5
				tvIndicator.setTranslationX((i+p)*width);
			}
			//��viewpager����״̬�ı�ʱִ��
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		
		//���radioGroup ����viewpager
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				DisplayMetrics metric = new DisplayMetrics();  
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);  
				int width = metric.widthPixels;     // ��Ļ��ȣ����أ�
				int w=tvIndicator.getLayoutParams().width = width/5;
				switch (checkedId) {
				case R.id.radioTuijian:
					viewPager.setCurrentItem(0);
					//�޸�tvIndicator��translationX����
					tvIndicator.setTranslationX(w*0);
					break;
				case R.id.radioGedan:
					viewPager.setCurrentItem(1);
					tvIndicator.setTranslationX(w*1);
					break;
				case R.id.radioBangdan:
					viewPager.setCurrentItem(2);
					tvIndicator.setTranslationX(w*2);
					break;
				case R.id.radioShipin:
					viewPager.setCurrentItem(3);
					tvIndicator.setTranslationX(w*3);
					break;
				case R.id.radioKge:
					viewPager.setCurrentItem(4);
					tvIndicator.setTranslationX(w*4);
					break;
				}
			}
		});
		
	}

	/** ����pager������ */
	private void setPagerAdapter() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new TestFragment());
		fragments.add(new MusicGedanFragment());
		fragments.add(new MusicBangdanFragment());
		fragments.add(new TestFragment());
		fragments.add(new TestFragment());
		pagerAdapter = new CommonPagerAdapter(getFragmentManager(), fragments);
		viewPager.setAdapter(pagerAdapter);
	}

	/** �ؼ���ʼ�� */
	private void setViews(View view) {
		radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
		radioTuijian = (RadioButton) view.findViewById(R.id.radioTuijian);
		radioGedan = (RadioButton) view.findViewById(R.id.radioGedan);
		radioBangdan = (RadioButton) view.findViewById(R.id.radioBangdan);
		radioShipin = (RadioButton) view.findViewById(R.id.radioShipin);
		radioKge = (RadioButton) view.findViewById(R.id.radioKge);
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		tvIndicator = (TextView) view.findViewById(R.id.tvIndicator);
		//��ʼ��tvIndicator�Ŀ��
		DisplayMetrics metric = new DisplayMetrics();  
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);  
		int width = metric.widthPixels;     // ��Ļ��ȣ����أ�
		tvIndicator.getLayoutParams().width = width/5;
	}
}










