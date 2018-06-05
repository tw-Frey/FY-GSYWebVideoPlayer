package tw.idv.fy.widget.gsywebplayer;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.shuyu.gsyvideoplayer.cache.ICacheManager;
import com.shuyu.gsyvideoplayer.model.GSYModel;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.player.IPlayerManager;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class WebViewPlayerManager implements IPlayerManager {

    private WebViewMediaPlayer mediaPlayer;

    @Override
    public IMediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    /**
     * 初始化播放内核
     *
     * @param context
     * @param message         播放器所需初始化内容
     * @param optionModelList 配置信息
     * @param cacheManager    缓存管理
     */
    @Override
    public void initVideoPlayer(Context context, Message message, List<VideoOptionModel> optionModelList, ICacheManager cacheManager) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        GSYModel gsyModel = (GSYModel) message.obj;
        mediaPlayer = new WebViewMediaPlayer(context);
        mediaPlayer.setDataSource(gsyModel.getUrl());
        mediaPlayer.setLooping(gsyModel.isLooping());
        setSpeed(gsyModel.getSpeed(), true);
    }

    /**
     * 设置渲染显示
     */
    @Override
    public void showDisplay(Message msg) {
        Log.v("Faty", "showDisplay");
    }

    /**
     * 是否静音
     *
     * @param needMute
     */
    @Override
    public void setNeedMute(boolean needMute) {
        if (mediaPlayer == null) return;
        mediaPlayer.setNeedMute(needMute);
    }

    /**
     * 释放渲染
     */
    @Override
    public void releaseSurface() {
        Log.v("Faty", "releaseSurface");
    }

    /**
     * 释放内核
     */
    @Override
    public void release() {
        if (mediaPlayer == null) return;
        mediaPlayer.release();
    }

    /**
     * 缓存进度
     */
    @Override
    public int getBufferedPercentage() {
        return 0;
    }

    /**
     * 网络速度
     */
    @Override
    public long getNetSpeed() {
        return 0;
    }

    /**
     * 播放速度
     */
    @Override
    public void setSpeedPlaying(float speed, boolean soundTouch) {
        setSpeed(speed, soundTouch);
    }

    @Override
    public void setSpeed(float speed, boolean soundTouch) {
        if (mediaPlayer == null) return;
        mediaPlayer.setSpeedPlaying(speed);
    }

    @Override
    public void start() {
        if (mediaPlayer == null) return;
        mediaPlayer.start();
    }

    @Override
    public void stop() {
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
    }

    @Override
    public void pause() {
        if (mediaPlayer == null) return;
        mediaPlayer.pause();
    }

    @Override
    public int getVideoWidth() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getVideoHeight();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        if (mediaPlayer == null) return;
        mediaPlayer.seekTo(time);
    }

    @Override
    public long getCurrentPosition() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getDuration();
    }

    @Override
    public int getVideoSarNum() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getVideoSarNum();
    }

    @Override
    public int getVideoSarDen() {
        if (mediaPlayer == null) return 0;
        return mediaPlayer.getVideoSarDen();
    }
}
