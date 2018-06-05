package tw.idv.fy.widget.gsywebplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoViewBridge;

import java.io.File;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class WebViewGSYVideoPlayer extends StandardGSYVideoPlayer {

    static {
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
    }

    static final String ANDROID_ASSET = "file:///android_asset/";

    public WebViewGSYVideoPlayer(Context context) {
        this(context, null);
    }

    public WebViewGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public GSYVideoViewBridge getGSYVideoManager() {
        return WebViewGSYVideoManager.instance();
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        addTextureView();
    }

    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    @Override
    protected void addTextureView() {
        WebView webView = new WebView(getContext()) {
            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return false;
            }
        };
        webView.setWebViewClient(new WebViewClient() {
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url != null && url.contains(ANDROID_ASSET) && url.endsWith("tw_idv_fy_widget_webplayer")) {
                    return new WebResourceResponse(
                            "text/javascript",
                            "utf-8",
                            getResources().openRawResource(R.raw.tw_idv_fy_widget_webplayer)
                    );
                }
                return super.shouldInterceptRequest(view, url);
            }
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri != null && ANDROID_ASSET.equals(uri.getHost()) && "tw_idv_fy_widget_webplayer".equals(uri.getLastPathSegment())) {
                    return new WebResourceResponse(
                            "text/javascript",
                            "utf-8",
                            getResources().openRawResource(R.raw.tw_idv_fy_widget_webplayer)
                    );
                }
                return super.shouldInterceptRequest(view, request);
            }
        });
        webView.postDelayed(() -> {
            //webView.loadDataWithBaseURL(ANDROID_ASSET, VIDEO_HTML, "text/html", "UTF-8", null);
            webView.loadDataWithBaseURL(ANDROID_ASSET, getResources().getString(R.string.tw_idv_fy_widget_webplayer), "text/html", "UTF-8", null);
        }, 2000);
        webView.setBackgroundColor(Color.BLACK);

        WebSettings settings = webView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setJavaScriptEnabled(true);
        mTextureViewContainer.addView(webView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
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
    public boolean setUp(String url, boolean cacheWithPlay, File cachePath, String title) {
        Log.v("Faty", String.valueOf(url));
        return super.setUp(url, cacheWithPlay, cachePath, title);
    }

    @Override
    protected boolean isShowNetConfirm() {
        return false;
    }
}
