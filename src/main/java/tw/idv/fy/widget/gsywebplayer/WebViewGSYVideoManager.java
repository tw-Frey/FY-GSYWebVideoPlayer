package tw.idv.fy.widget.gsywebplayer;

import android.content.Context;

import com.shuyu.gsyvideoplayer.GSYVideoBaseManager;
import com.shuyu.gsyvideoplayer.player.IPlayerManager;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;

public class WebViewGSYVideoManager extends GSYVideoBaseManager {

    private static GSYVideoViewBridge videoManager;

    /**
     * singleton
     */
    public static synchronized GSYVideoViewBridge instance(Context context) {
        if (videoManager == null) {
            videoManager = new WebViewGSYVideoManager(context);
        }
        return videoManager;
    }

    private WebViewGSYVideoManager(Context context) {
        initContext(context);
        init();
    }

    @Override
    protected IPlayerManager getPlayManager(int videoType) {
        return new WebViewPlayerManager();
    }
}
