package com.mrx

import com.atilika.kuromoji.ipadic.Tokenizer
import com.mrx.AquesUtils.KeyType.DEV
import com.mrx.AquesUtils.KeyType.USER
import com.sun.jna.ptr.IntByReference
import java.util.stream.Collectors

/**
 * AquesTalk 工具类
 * @property instance AquesTalk 单例模式
 */
class AquesUtils private constructor() {

    private val instance = AquesTalk.INSTANCE

    private val tokenizer by lazy { Tokenizer() }

    /**
     * Key 类型
     * Key 的作用是一样的, 不同的是它们的授权使用范围
     * @see <a href="https://www.a-quest.com/download.html">官网说明</a>
     */
    enum class KeyType {
        /**
         * 开发用 Key
         */
        DEV,

        /**
         * 商业使用 Key
         */
        USER
    }

    companion object {
        private lateinit var instance: AquesUtils

        /**
         * 获取本工具类的实例
         * @return AquesUtils 工具类的实例
         */
        fun getInstance(): AquesUtils {
            if (!this::instance.isInitialized) instance = AquesUtils()
            return instance
        }
    }

    /**
     * 设置 AquestTalk 的 Key
     * @param key String Key
     * @param keyType KeyType Key 类型
     */
    fun setKey(key: String, keyType: KeyType = DEV) {
        if (when (keyType) {
                DEV -> instance.AquesTalk_SetDevKey(key)
                USER -> instance.AquesTalk_SetUsrKey(key)
            } == 0
        ) {
            println("Key 注册成功")
            return
        }
        println("Key 注册失败, 生成的语音将会有所限制")
    }

    /**
     * 文字转换语音
     * @param koe String 要转换的 平假名
     * @param voice ByReference 音色数据
     * @return ByteArray 转换成功 返回语音数组
     * @throws AquesTalk.AquesException 转换失败 抛出异常
     */
    private fun tts(koe: String, voice: AquesTalk.AQTK_VOICE.ByReference = AquesTalk.gVoice_F1): ByteArray {
        println(koe)
        val size = IntByReference()
        val wav = instance.AquesTalk_Synthe_Utf8(voice, koe, size)
        val code = size.value
        wav?.let {
            instance.AquesTalk_FreeWave(wav)
            println("生成文件 ${code.div(1024)} KB")
            return it.getByteArray(0, code)
        }
        throw AquesTalk.AquesException("语音生成失败 code -> $code : ${AquesTalk.ErrorCode.valueOf(code)}")
    }

    /**
     * 日语汉字 TTS
     * @param koe String 日语汉字
     * @param voice ByReference 音色数据
     * @return ByteArray 转换结果
     * @see tts
     * @throws AquesTalk.AquesException 转换失败 抛出异常
     */
    @Deprecated(
        message = "已知问题, 有时候 (80% - 90%) 转换出来的文件有问题, 但直接调用 rTTS 就没问题",
        replaceWith = ReplaceWith("rTTS(koe,voice)", "AquesUtils"),
        level = DeprecationLevel.WARNING
    )
    fun kTTS(koe: String, voice: AquesTalk.AQTK_VOICE.ByReference = AquesTalk.gVoice_F1): ByteArray {
        return tts(koe.k2r(), voice)
    }

    /**
     * 文字转换语音
     * @param koe String 要转换的 平假名
     * @param voice ByReference 音色数据
     * @return ByteArray 转换成功 返回语音数组
     * @throws AquesTalk.AquesException 转换失败 抛出异常
     */
    fun rTTS(koe: String, voice: AquesTalk.AQTK_VOICE.ByReference = AquesTalk.gVoice_F1): ByteArray {
        return tts(koe, voice)
    }

    /**
     * 使用 kuromoji 库进行 汉字 转换 平假名
     * @receiver String 要转换的 汉字
     * @return String 转换后的结果
     */
    private fun String.k2r() = tokenizer.tokenize(this)
        .stream().map { it.reading }
        .collect(Collectors.toList())
        .joinToString("")
}