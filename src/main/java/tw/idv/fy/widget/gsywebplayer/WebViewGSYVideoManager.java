package tw.idv.fy.widget.gsywebplayer;

import com.shuyu.gsyvideoplayer.GSYVideoBaseManager;
import com.shuyu.gsyvideoplayer.player.IPlayerManager;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;

public class WebViewGSYVideoManager extends GSYVideoBaseManager {

    private static GSYVideoViewBridge videoManager;

    /**
     * singleton
     */
    public static synchronized GSYVideoViewBridge instance() {
        if (videoManager == null) {
            videoManager = new WebViewGSYVideoManager();
        }
        return videoManager;
    }

    private WebViewGSYVideoManager() {
        init();
    }

    @Override
    protected IPlayerManager getPlayManager(int videoType) {
        return WebViewPlayerManager.instance();
    }
}
