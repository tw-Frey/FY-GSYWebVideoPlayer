package tw.idv.fy.widget.gsywebplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.FileDescriptor;
import java.util.Locale;
import java.util.Map;

import tv.danmaku.ijk.media.player.AbstractMediaPlayer;
import tv.danmaku.ijk.media.player.MediaInfo;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

@SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class WebViewMediaPlayer extends AbstractMediaPlayer {

    private static final String JAVASCRIPTOBJ = "JAVASCRIPTOBJ";
    private static final String ANDROID_ASSET = "file:///android_asset/";

    private Context mContext;
    private WebView mWebView;
    private Uri mUri;
    private Map<String, String> mMap;

    /*package*/ WebViewMediaPlayer(Context context) {
        mContext = context.getApplicationContext();
        mHandler = new Handler(mContext.getMainLooper());
    }

    @Override
    public void setDataSource(FileDescriptor fileDescriptor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDataSource(String s) {
        setDataSource(null, s == null ? null : Uri.parse(s));
    }

    @Override
    public void setDataSource(Context context, Uri uri) {
        setDataSource(context, uri, null);
    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> map) {
        mUri = uri;
        mMap = map;
    }

    @Override
    public String getDataSource() {
        return mUri == null ? null : mUri.toString();
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSurface(Surface surface) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        if (mHandler == null) return; // 防呆
        mHandler.postAtFrontOfQueue(() -> {
            if (mHandler == null || mContext == null) return; // 防呆
            mWebView = new WebView(mContext) {
                @Override
                public boolean onTouchEvent(MotionEvent event) {
                    return false;
                }
            };
            WebSettings settings = mWebView.getSettings();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            settings.setMediaPlaybackRequiresUserGesture(false);
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowFileAccess(true);
            settings.setAllowContentAccess(true);
            settings.setJavaScriptEnabled(true);
            mWebView.setBackgroundColor(Color.BLACK);
            mWebView.setWebViewClient(new WebViewClient() {
                private String check(Uri uri) {
                    String name;
                    if(String.valueOf(uri).startsWith(ANDROID_ASSET)
                    && String.valueOf(name = uri.getLastPathSegment()).startsWith("tw_idv_fy_widget")) {
                        return name;
                    }
                    return null;
                }
                @Nullable
                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                    String name = check(Uri.parse(url));
                    if (name != null) {
                        int rawID = view.getResources().getIdentifier(name, "raw", view.getContext().getPackageName());
                        return new WebResourceResponse("text/javascript", "utf-8", view.getResources().openRawResource(rawID));
                    }
                    return super.shouldInterceptRequest(view, url);
                }
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Nullable
                @Override
                public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                    String name = check(request.getUrl());
                    if (name != null) {
                        int rawID = view.getResources().getIdentifier(name, "raw", view.getContext().getPackageName());
                        return new WebResourceResponse("text/javascript", "utf-8", view.getResources().openRawResource(rawID));
                    }
                    return super.shouldInterceptRequest(view, request);
                }
            });
            mWebView.addJavascriptInterface(WebViewMediaPlayer.this, JAVASCRIPTOBJ);
            mWebView.loadDataWithBaseURL(ANDROID_ASSET, mContext.getResources().getString(R.string.tw_idv_fy_widget_webplayer), "text/html", "UTF-8", null);
        });
    }

    @Override
    public void start() throws IllegalStateException {
        if (mHandler == null) return;
        mHandler.post(() -> {
            if (mWebView == null) return;
            mWebView.evaluateJavascript("player.play()", null);
        });
    }

    @Override
    public void stop() throws IllegalStateException {
        Log.i("Faty", "stop");
    }

    @Override
    public void pause() throws IllegalStateException {
        if (mHandler == null) return;
        mHandler.post(() -> {
            if (mWebView == null) return;
            mWebView.evaluateJavascript("player.pause()", null);
        });
    }

    @Override
    public void setScreenOnWhilePlaying(boolean b) {}

    @Override
    public int getVideoWidth() {
        return mWidth;
    }

    @Override
    public int getVideoHeight() {
        return mHeight;
    }

    @Override
    public boolean isPlaying() {
        return true;
    }

    @Override
    public void seekTo(long time) throws IllegalStateException {
        mCurrentTime = time;
        if (mHandler == null) return;
        mHandler.post(() -> {
            if (mWebView == null) return;
            mWebView.evaluateJavascript(String.format(Locale.TAIWAN, "player.currentTime(%f)", time / 1000f), null);
        });
    }

    @Override
    public long getCurrentPosition() {
        return mCurrentTime;
    }

    @Override
    public long getDuration() {
        return mDuration;
    }

    @Override
    public void release() {
        Log.i("Faty", "release");
        if (mHandler == null) return;
        mHandler.postAtFrontOfQueue(() -> {
            if (mWebView == null) return;
            mWebView.evaluateJavascript("player.dispose()", null);
            mWebView = null;
        });
        mHandler = null;
        mContext = null;
        resetListeners();
    }

    @Override
    public void reset() {
        Log.i("Faty", "reset");
    }

    @Override
    public void setVolume(float v, float v1) {
        Log.i("Faty", "setVolume");
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public MediaInfo getMediaInfo() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void setLogEnabled(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean isPlayable() {
        return mWebView != null;
    }

    @Override
    public void setAudioStreamType(int i) {}

    @Deprecated
    @Override
    public void setKeepInBackground(boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getVideoSarNum() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getVideoSarDen() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void setWakeMode(Context context, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLooping(boolean isLooping) {
        mLooping = isLooping;
        if (mHandler == null) return;
        mHandler.post(() -> {
            if (mWebView == null) return;
            mWebView.evaluateJavascript(isLooping ? "player.loop(true)" : "player.loop(false)", null);
        });
    }

    @Override
    public boolean isLooping() {
        return mLooping;
    }

    @Override
    public ITrackInfo[] getTrackInfo() {
        throw new UnsupportedOperationException();
    }

    public void setNeedMute(boolean needMute) {
        mNeedMute = needMute;
        if (mHandler == null) return;
        mHandler.post(() -> {
            if (mWebView == null) return;
            mWebView.evaluateJavascript(needMute ? "player.muted(true)" : "player.muted(false)", null);
        });
    }

    public void setSpeedPlaying(float speed) {
        mSpeed = speed;
        if (mHandler == null) return;
        mHandler.post(() -> {
            if (mWebView == null) return;
            mWebView.evaluateJavascript(String.format(Locale.TAIWAN, "player.playbackRate(%f)", speed), null);
        });
    }

    public WebView getWebView() {
        return mWebView;
    }

    @JavascriptInterface
    public void ready() {
        if (mHandler == null) return;
        mHandler.post(() -> {
            if (mWebView == null) return;
            mWebView.evaluateJavascript(String.format(Locale.TAIWAN, "play('%s', %f, %d, %b, %b)", mUri, mSpeed, mCurrentTime, mNeedMute, mLooping), null);
        });
    }

    @JavascriptInterface
    public void canplay(float width, float height, float duration) {
        if (mHandler == null || mWebView == null) return;
        mWidth = (int) width;
        mHeight = (int) height;
        mDuration = (long) (duration * 1000L);
        if (isPrepared) return;
        else isPrepared = true;
        notifyOnPrepared();
    }

    @JavascriptInterface
    public void seeked() {
        if (mHandler == null || mWebView == null) return;
        notifyOnSeekComplete();
        Log.i("Faty", "seeked");
    }

    @JavascriptInterface
    public void ended() {
        if (mHandler == null || mWebView == null) return;
        notifyOnCompletion();
        Log.i("Faty", "ended");
    }

    @JavascriptInterface
    public void timeupdate(float currentTime) {
        mCurrentTime = (long) (currentTime * 1000L);
    }

    @JavascriptInterface
    public void progress(float percent) {
        notifyOnBufferingUpdate((int) (percent * 100));
    }

    private Handler mHandler;
    private int mWidth = -1;
    private int mHeight = -1;
    private long mDuration = -1;
    private long mCurrentTime = -1;
    private float mSpeed = 1;
    private boolean mLooping = false;
    private boolean mNeedMute = false;
    private boolean isPrepared = false;
}
