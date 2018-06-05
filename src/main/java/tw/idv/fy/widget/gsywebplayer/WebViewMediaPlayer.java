package tw.idv.fy.widget.gsywebplayer;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.MediaInfo;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class WebViewMediaPlayer implements IMediaPlayer {
    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {

    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> map) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {

    }

    @Override
    public void setDataSource(FileDescriptor fileDescriptor) throws IOException, IllegalArgumentException, IllegalStateException {

    }

    @Override
    public void setDataSource(String s) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {

    }

    @Override
    public String getDataSource() {
        return null;
    }

    @Override
    public void prepareAsync() throws IllegalStateException {

    }

    @Override
    public void start() throws IllegalStateException {

    }

    @Override
    public void stop() throws IllegalStateException {

    }

    @Override
    public void pause() throws IllegalStateException {

    }

    @Override
    public void setScreenOnWhilePlaying(boolean b) {

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
    public void seekTo(long l) throws IllegalStateException {

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
    public void release() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void setVolume(float v, float v1) {

    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public MediaInfo getMediaInfo() {
        return null;
    }

    @Override
    public void setLogEnabled(boolean b) {

    }

    @Override
    public boolean isPlayable() {
        return false;
    }

    @Override
    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {

    }

    @Override
    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {

    }

    @Override
    public void setOnBufferingUpdateListener(OnBufferingUpdateListener onBufferingUpdateListener) {

    }

    @Override
    public void setOnSeekCompleteListener(OnSeekCompleteListener onSeekCompleteListener) {

    }

    @Override
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener) {

    }

    @Override
    public void setOnErrorListener(OnErrorListener onErrorListener) {

    }

    @Override
    public void setOnInfoListener(OnInfoListener onInfoListener) {

    }

    @Override
    public void setOnTimedTextListener(OnTimedTextListener onTimedTextListener) {

    }

    @Override
    public void setAudioStreamType(int i) {

    }

    @Override
    public void setKeepInBackground(boolean b) {

    }

    @Override
    public int getVideoSarNum() {
        return 0;
    }

    @Override
    public int getVideoSarDen() {
        return 0;
    }

    @Override
    public void setWakeMode(Context context, int i) {

    }

    @Override
    public void setLooping(boolean b) {

    }

    @Override
    public boolean isLooping() {
        return false;
    }

    @Override
    public ITrackInfo[] getTrackInfo() {
        return new ITrackInfo[0];
    }

    @Override
    public void setSurface(Surface surface) {

    }

    @Override
    public void setDataSource(IMediaDataSource iMediaDataSource) {

    }
}
