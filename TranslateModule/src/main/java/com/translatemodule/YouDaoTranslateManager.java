package com.translatemodule;

import android.content.Context;
import android.support.annotation.NonNull;

import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.app.YouDaoApplication;
import com.youdao.sdk.chdict.ChDictTranslate;
import com.youdao.sdk.chdict.ChDictor;
import com.youdao.sdk.chdict.DictListener;
import com.youdao.sdk.ydonlinetranslate.TranslateErrorCode;
import com.youdao.sdk.ydonlinetranslate.TranslateListener;
import com.youdao.sdk.ydonlinetranslate.TranslateParameters;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.EnWordTranslator;
import com.youdao.sdk.ydtranslate.Translate;

import java.util.List;

/**
 * Created by yangc on 2017/9/17.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:
 */

public class YouDaoTranslateManager {
    public static final String TAG = YouDaoTranslateManager.class.getName();

    public static YouDaoTranslateManager getSingleton() {
        return Holder.holder;
    }

    private static class Holder {
        static YouDaoTranslateManager holder = new YouDaoTranslateManager();
    }


    private YouDaoTranslateManager() {

    }

    public void inti(@NonNull Context context, @NonNull String appKey) {
        //注册应用ID ，建议在应用启动时，初始化，所有功能的使用都需要该初始化，调用一次即可，demo中在MainActivity类中
        YouDaoApplication.init(context.getApplicationContext(), appKey);
    }

    /**
     * 离线汉语词典查词功能
     * 可以查词之前设置离线词库路径，则采用默认路径Environment
     * .getExternalStorageDirectory().getPath() + "/yuwen/backup/"
     * 请确保设置的路径或者默认路径下包含汉语词典的离线包
     * chDictor = new ChDictor(dictFilePath);
     * 表示Environment.getExternalStorageDirectory().getAbsolutePath() + dictFilePath路径下存在离线库
     *
     * @param text         内容
     * @param dictListener
     */
    public void OfflineChinese(@NonNull String text, @NonNull DictListener dictListener) {
        //查词对象初始化
        ChDictor chDictor = new ChDictor();
        //注意，每次查询之前都需要初始化
        chDictor.init();
        //开始查询
        chDictor.lookup(text, dictListener);
    }

    /****
     * 在线查词和翻译功能（支持小语种）
     *
     * @param body     查询内容
     * @param listener 回调
     **/
    public void searchWordOnline(@NonNull String body, @NonNull TranslateListener listener) {
        searchWordOnline(body, Language.CHINESE, listener);
    }

    /****
     * 在线查词和翻译功能（支持小语种）
     *
     * @param body     查询内容
     * @param language 语言类型 {@link Language }
     * @param listener 回调
     **/
    public void searchWordOnline(@NonNull String body, Language language, @NonNull TranslateListener listener) {
        //查词对象初始化，请设置source参数为app对应的名称（英文字符串）
        Language langFrom = LanguageUtils.getLangByName("中文");
        //若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
        //Language langFrom = LanguageUtils.getLangByName("自动");
        //Language langFrom = LanguageUtils.getLangByName("自动");
        TranslateParameters tps = new TranslateParameters.Builder().source("ydtranslate-demo")
                .from(langFrom).to(language).build();
        Translator translator = Translator.getInstance(tps);
        //查询，返回两种情况，一种是成功，相关结果存储在result参数中，另外一种是失败，失败信息放在TranslateErrorCode 是一个枚举类，整个查询是异步的，为了简化操作，回调都是在主线程发生。
        translator.lookup(body, listener);
    }

    /**
     * 离线汉英单词互译功能
     * 可以查词之前设置离线词库路径，若不设置，采用默认路径
     * Environment.getExternalStorageDirectory().getAbsolutePath() +
     * "/Youdao/localdict/"; 开发者应保证设置的路径或者默认路径下存在离线词库文件
     *
     * @param body 词典
     * @return Translate
     */
    public Translate offlineChinese(@NonNull String body) {
        //查词对象初始化 EnWordTranslator全部为静态方法，查询之前可以初始化离线词库位置
        //表示Environment.getExternalStorageDirectory().getAbsolutePath() + dictFilePath路径下存在离线库
        // EnWordTranslator.initDictPath(dictFilePath);
        //开始查询
        return EnWordTranslator.lookupNative(body);
    }

}
