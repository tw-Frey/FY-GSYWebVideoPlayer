package tw.idv.fy.widget.gsywebplayer;

import android.content.Context;
import android.os.Message;

import com.shuyu.gsyvideoplayer.cache.ICacheManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.player.IPlayerManager;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class WebViewPlayerManager implements IPlayerManager {

    private static IPlayerManager playerManager;
    private IMediaPlayer mediaPlayer;

    /**
     * singleton
     */
    public static synchronized IPlayerManager instance() {
        if (playerManager == null) {
            playerManager = new WebViewPlayerManager();
        }
        return playerManager;
    }

    private WebViewPlayerManager() {
        mediaPlayer = new WebViewMediaPlayer();
    }

    @Override
    public IMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void initVideoPlayer(Context context, Message message, List<VideoOptionModel> optionModelList, ICacheManager cacheManager) {

    }

    @Override
    public void showDisplay(Message msg) {

    }

    @Override
    public void setNeedMute(boolean needMute) {

    }

    @Override
    public void releaseSurface() {

    }

    @Override
    public void release() {

    }

    @Override
    public int getBufferedPercentage() {
        return 0;
    }

    @Override
    public long getNetSpeed() {
        return 0;
    }

    @Override
    public void setSpeedPlaying(float speed, boolean soundTouch) {

    }

    @Override
    public void setSpeed(float speed, boolean soundTouch) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getVideoWidth() {
        return 0;
    }

    @Override
    public int getVideoHeight() {
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void seekTo(long time) {

    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public int getVideoSarNum() {
        return 0;
    }

    @Override
    public int getVideoSarDen() {
        return 0;
    }
}
