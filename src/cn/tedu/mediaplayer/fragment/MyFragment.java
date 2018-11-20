package cn.tedu.mediaplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import cn.tedu.mediaplayer.R;

public class MyFragment extends Fragment{
	private LinearLayout linearLayoutLocalMusic;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my, null);
		setViews(view);
		setListeners();
		return view;
	}

	private void setListeners() {
		linearLayoutLocalMusic.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				FragmentTransaction tc = getFragmentManager().beginTransaction();
				tc.setCustomAnimations(R.anim.bangdan_detail_fragment_enter, R.anim.bangdan_detail_fragment_exit,R.anim.bangdan_detail_fragment_enter, R.anim.bangdan_detail_fragment_exit)
					.add(R.id.frameLayoutSearch, new LocalMusicFragment(), "local_music")
					.addToBackStack("local_music");
				tc.commit();
			}
		});
	}

	private void setViews(View view) {
		this.linearLayoutLocalMusic = (LinearLayout) view.findViewById(R.id.linearLayoutLocalMusic);
	}
}
