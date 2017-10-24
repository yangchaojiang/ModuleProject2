package me.shaohui.shareutil.share;

import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import static me.shaohui.shareutil.ShareLogger.INFO;

/**
 * Created by shaohui on 2016/11/18.
 */

public abstract class ShareListener implements IUiListener, WbShareCallback {
    @Override
    public final void onComplete(Object o) {
        shareSuccess();
    }

    @Override
    public final void onError(UiError uiError) {
        shareFailure(
                new Exception(uiError == null ? INFO.DEFAULT_QQ_SHARE_ERROR : uiError.errorDetail));
    }

    @Override
    public final void onCancel() {
        shareCancel();
    }

    @Override
    public void onWbShareSuccess() {
        shareSuccess();
    }

    @Override
    public void onWbShareCancel() {
        shareCancel();
    }

    @Override
    public void onWbShareFail() {
        shareFailure(new Exception("分享失败"));
    }

    public abstract void shareSuccess();

    public abstract void shareFailure(Exception e);

    public abstract void shareCancel();

    // 用于缓解用户焦虑
    public void shareRequest() {
    }
}
