package tw.idv.fy.widget.gsywebplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class WebViewGSYVideoPlayer extends StandardGSYVideoPlayer {

    static {
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
    }

    public WebViewGSYVideoPlayer(Context context) {
        this(context, null);
    }

    public WebViewGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public GSYVideoViewBridge getGSYVideoManager() {
        return WebViewGSYVideoManager.instance(getContext());
    }

    @Override
    protected void addTextureView() {
        mTextureViewContainer
                .addView(
                        ((WebViewMediaPlayer) getGSYVideoManager().getPlayer().getMediaPlayer()).getWebView(),
                        new LayoutParams(MATCH_PARENT, MATCH_PARENT)
                );
    }

    /**
     * 将自定义的效果也设置到全屏
     */
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        return super.startWindowFullscreen(context, actionBar, statusBar);
    }

    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }

    @Override
    protected void resolveFullVideoShow(Context context, GSYBaseVideoPlayer gsyVideoPlayer, FrameLayout frameLayout) {
        super.resolveFullVideoShow(context, gsyVideoPlayer, frameLayout);
    }

    @Override
    protected boolean isShowNetConfirm() {
        return false;
    }

}
