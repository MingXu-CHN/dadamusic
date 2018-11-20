package cn.tedu.mediaplayer.model;

import java.util.List;

import cn.tedu.mediaplayer.entity.MatchPic;

public interface SearchMatchPicListCallback {
    public void onMatchPicsLoaded(List<MatchPic> pics);
}
