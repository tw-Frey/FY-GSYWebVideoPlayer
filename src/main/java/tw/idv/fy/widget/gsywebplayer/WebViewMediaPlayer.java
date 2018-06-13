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
        mHandler.post(() -> {
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
        if (mWebView == null) return;
        mHandler.post(() -> mWebView.evaluateJavascript("player.play()", null));
    }

    @Override
    public void stop() throws IllegalStateException {
        Log.i("Faty", "stop");
    }

    @Override
    public void pause() throws IllegalStateException {
        if (mWebView == null) return;
        mHandler.post(() -> mWebView.evaluateJavascript("player.pause()", null));
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
        if (mWebView == null) return;
        mHandler.post(() -> mWebView.evaluateJavascript(String.format(Locale.TAIWAN, "player.currentTime(%f)", time / 1000f), null));
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
        if (mWebView == null) return;
        mHandler.post(() -> mWebView.evaluateJavascript("player.dispose()", null));
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
        return true;
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
        return 0;
    }

    @Override
    public int getVideoSarDen() {
        return 0;
    }

    @Deprecated
    @Override
    public void setWakeMode(Context context, int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLooping(boolean isLooping) {
        mLooping = isLooping;
        if (mWebView == null) return;
        mHandler.post(() -> mWebView.evaluateJavascript(isLooping ? "player.loop(true)" : "player.loop(false)", null));
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
        if (mWebView == null) return;
        mHandler.post(() -> mWebView.evaluateJavascript(needMute ? "player.muted(true)" : "player.muted(false)", null));
    }

    public void setSpeedPlaying(float speed) {
        mSpeed = speed;
        if (mWebView == null) return;
        mHandler.post(() -> mWebView.evaluateJavascript(String.format(Locale.TAIWAN, "player.playbackRate(%f)", speed), null));
    }

    public WebView getWebView() {
        return mWebView;
    }

    @JavascriptInterface
    public void ready() {
        if (mWebView == null) return;
        mHandler.post(() -> mWebView.evaluateJavascript(String.format(Locale.TAIWAN,"play('%s', %f, %d, %b, %b)", mUri, mSpeed, mCurrentTime, mNeedMute, mLooping), null));
    }

    @JavascriptInterface
    public void canplay(float width, float height, float duration) {
        mWidth = (int) width;
        mHeight = (int) height;
        mDuration = (long) (duration * 1000L);
        notifyOnPrepared();
    }

    @JavascriptInterface
    public void seeked() {
        Log.i("Faty", "seeked");
        notifyOnSeekComplete();
    }

    @JavascriptInterface
    public void ended() {
        Log.i("Faty", "ended");
        notifyOnCompletion();
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
    private int mWidth;
    private int mHeight;
    private long mDuration;
    private long mCurrentTime;
    private float mSpeed = 1;
    private boolean mLooping = false;
    private boolean mNeedMute = false;

}
