package tw.idv.fy.widget.gsywebplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
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
        mTextureViewContainer.addView(
                ((WebViewMediaPlayer) getGSYVideoManager().getPlayer().getMediaPlayer()).getWebView(),
                new LayoutParams(MATCH_PARENT, MATCH_PARENT)
        );
    }

    protected void removeIMediaPlayer() {
        mTextureViewContainer.removeAllViews();
    }

    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        /*
            因為 full player (oldF) 沒有經過 initVideoPlayer
            所以 WebView 沒變 (共用了)
            如果沒先把 WebView 除去 Parent
            則再加回原 mTextureViewContainer 會報錯
         */
        if (oldF instanceof WebViewGSYVideoPlayer) {
            ((WebViewGSYVideoPlayer) oldF).removeIMediaPlayer();
        }
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }

    @Override
    protected boolean isShowNetConfirm() {
        return false;
    }

    /**
     * 触摸亮度dialog，調整亮度棒位置
     */
    @Override
    protected void showBrightnessDialog(float percent) {
        super.showBrightnessDialog(percent);
        if (mBrightnessDialog != null) {
            WindowManager.LayoutParams localLayoutParams = mBrightnessDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.TOP | Gravity.START;
            mBrightnessDialog.getWindow().setAttributes(localLayoutParams);
        }
    }
}
