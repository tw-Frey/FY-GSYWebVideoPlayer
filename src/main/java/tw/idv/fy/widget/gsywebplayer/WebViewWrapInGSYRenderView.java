package tw.idv.fy.widget.gsywebplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.shuyu.gsyvideoplayer.listener.GSYVideoShotListener;
import com.shuyu.gsyvideoplayer.listener.GSYVideoShotSaveListener;
import com.shuyu.gsyvideoplayer.render.GSYRenderView;
import com.shuyu.gsyvideoplayer.render.glrender.GSYVideoGLViewBaseRender;
import com.shuyu.gsyvideoplayer.render.view.GSYVideoGLView;
import com.shuyu.gsyvideoplayer.render.view.IGSYRenderView;
import com.shuyu.gsyvideoplayer.render.view.listener.IGSYSurfaceListener;
import com.shuyu.gsyvideoplayer.utils.MeasureHelper;

import java.io.File;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

class WebViewWrapInGSYRenderView extends GSYRenderView {

    private final WebView webView;

    WebViewWrapInGSYRenderView(WebView webView) {
        this.webView = webView;
        this.mShowView = new IGSYRenderView() {

            @Override
            public IGSYSurfaceListener getIGSYSurfaceListener() {
                return null;
            }

            /**
             * Surface变化监听，必须
             */
            @Override
            public void setIGSYSurfaceListener(IGSYSurfaceListener surfaceListener) {}

            /**
             * 当前view高度，必须
             */
            @Override
            public int getSizeH() {
                return webView.getHeight();
            }

            /**
             * 当前view宽度，必须
             */
            @Override
            public int getSizeW() {
                return webView.getWidth();
            }

            /**
             * 实现该接口的view，必须
             */
            @Override
            public View getRenderView() {
                return webView;
            }

            /**
             * 渲染view通过MeasureFormVideoParamsListener获取视频的相关参数，必须
             */
            @Override
            public void setVideoParamsListener(MeasureHelper.MeasureFormVideoParamsListener listener) {}

            /**
             * 截图
             */
            @Override
            public void taskShotPic(GSYVideoShotListener gsyVideoShotListener, boolean shotHigh) {
                webView.evaluateJavascript("capture()", base64Data -> {
                    String[] modified = base64Data.split("[,\"]");
                    byte[] bytes = modified.length == 0 ? new byte[0] : Base64.decode(modified[modified.length - 1], Base64.DEFAULT);
                    gsyVideoShotListener.getBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                });
            }

            /**
             * 保存当前帧
             */
            @Override
            public void saveFrame(File file, boolean high, GSYVideoShotSaveListener gsyVideoShotSaveListener) {
                throw new UnsupportedOperationException();
            }

            /**
             * 获取当前画面的bitmap，没有返回空
             */
            @Override
            public Bitmap initCover() {
                return null;
            }

            /**
             * 获取当前画面的高质量bitmap，没有返回空
             */
            @Override
            public Bitmap initCoverHigh() {
                return null;
            }

            @Override
            public void onRenderResume() {
                webView.onResume();
                webView.resumeTimers();
            }

            @Override
            public void onRenderPause() {
                webView.onPause();
                webView.pauseTimers();
            }

            @Override
            public void releaseRenderAll() {
                webView.destroy();
            }

            @Override
            public void setRenderMode(int mode) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setRenderTransform(Matrix transform) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setGLRenderer(GSYVideoGLViewBaseRender renderer) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setGLMVPMatrix(float[] MVPMatrix) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setGLEffectFilter(GSYVideoGLView.ShaderInterface effectFilter) {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * 添加播放的view
     */
    @Override
    public void addView(Context context, ViewGroup textureViewContainer, int rotate, IGSYSurfaceListener gsySurfaceListener, MeasureHelper.MeasureFormVideoParamsListener videoParamsListener, GSYVideoGLView.ShaderInterface effect, float[] transform, GSYVideoGLViewBaseRender customRender, int mode) {
        ViewParent viewParent = webView.getParent();
        if (textureViewContainer.equals(viewParent)) return;
        if (viewParent instanceof ViewGroup) {
            ((ViewGroup) viewParent).removeView(webView);
        }
        textureViewContainer.addView(webView, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

}
