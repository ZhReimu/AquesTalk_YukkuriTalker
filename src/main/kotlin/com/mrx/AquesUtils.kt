package com.mrx

import com.mrx.AquesUtils.KeyType.DEV
import com.mrx.AquesUtils.KeyType.USER
import com.sun.jna.ptr.IntByReference

/**
 * AquesTalk 工具类
 * @property instance AquesTalk 单例模式
 */
class AquesUtils private constructor() {

    private val instance = AquesTalk.INSTANCE

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
    fun tts(koe: String, voice: AquesTalk.AQTK_VOICE.ByReference = AquesTalk.gVoice_F1): ByteArray {
        val size = IntByReference()
        val wav = instance.AquesTalk_Synthe_Utf8(voice, koe, size)
        val code = size.value
        wav?.let {
            instance.AquesTalk_FreeWave(wav)
            return it.getByteArray(0, code)
        }
        throw AquesTalk.AquesException("语音生成失败 code -> $code : ${AquesTalk.ErrorCode.valueOf(code)}")
    }

}