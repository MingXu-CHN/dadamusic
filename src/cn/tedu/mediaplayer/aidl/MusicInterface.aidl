package cn.tedu.mediaplayer.aidl;

interface MusicInterface{
	void playMusic(String url);
	int playOrPause();
	int getPlayState();
	void seekTo(int position);
}