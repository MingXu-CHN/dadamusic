package cn.tedu.mediaplayer.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import cn.tedu.mediaplayer.R;
import cn.tedu.mediaplayer.adapter.MusicBangdanAdapter;
import cn.tedu.mediaplayer.entity.Bangdan;
import cn.tedu.mediaplayer.model.BangdanListCallback;
import cn.tedu.mediaplayer.model.MusicModel;

public class MusicBangdanFragment extends Fragment{
	private ListView listView;
	private MusicBangdanAdapter adapter;
	private List<Bangdan> bangdans;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_music_bangdan, null);
		//�ؼ���ʼ��
		listView = (ListView) view.findViewById(R.id.listView);
		//��Ӽ���
		setListeners();
		//���ذ��б�����
		MusicModel model = new MusicModel();
		model.loadBangdanList(new BangdanListCallback() {
			public void onBangdanListLoaded(List<Bangdan> bangdans) {
				showBangdanList(bangdans);
			}
		});
		return view;
	}
	
	//���ü���
	private void setListeners() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Bangdan bangdan = bangdans.get(position);
				FragmentTransaction tc = getFragmentManager().beginTransaction();
				Fragment fragment = new BangdanDetailFragment();
				//��Ҫ�ѵ�ǰѡ�а񵥵�type��fragment����ȥ
				Bundle bundle = new Bundle();
				bundle.putInt("type", bangdan.getType());
				fragment.setArguments(bundle);
				//tc.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				tc.setCustomAnimations(R.anim.bangdan_detail_fragment_enter, R.anim.bangdan_detail_fragment_exit,
						R.anim.bangdan_detail_fragment_enter, R.anim.bangdan_detail_fragment_exit);
				//��fragment��ӵ�����ջ��
				//������finishʱ�����������ٻ���ջ�е�fragment
				//�������ջ��û��fragment ��ô�˳�activity
				tc.add(R.id.frameLayoutBangdanDetail, fragment, "bangdan_detail");
				tc.addToBackStack("bangdan_detail");
				tc.commit();
			}
		});
	}

	/** ��ʾ���б� */
	public void showBangdanList(List<Bangdan> bangdans){
		this.bangdans = bangdans;
		//����ListView��������
		adapter = new MusicBangdanAdapter(getActivity(), bangdans);
		listView.setAdapter(adapter);
	}
}






