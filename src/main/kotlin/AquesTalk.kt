@file:Suppress("UNUSED")

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.StdCallLibrary

interface AquesTalk : StdCallLibrary {

    companion object {
        val INSTANCE: AquesTalk = Native.load("AquesTalk.dll", AquesTalk::class.java)

        private object VoiceBase {
            const val F1E = 0
            const val F2E = 1
            const val M1E = 2
        }

        private val voice = AQTK_VOICE.ByReference.apply {
            bas = VoiceBase.F1E
            spd = 100
            vol = 100
            pit = 100
            acc = 100
            lmd = 100
            fsc = 100
        }

        val gVoice_F1E: AQTK_VOICE.ByReference
            get() {
                voice.bas = VoiceBase.F1E
                return voice
            }
        val gVoice_F2E: AQTK_VOICE.ByReference
            get() {
                voice.bas = VoiceBase.F2E
                return voice
            }
        val gVoice_M1E: AQTK_VOICE.ByReference
            get() {
                voice.bas = VoiceBase.M1E
                return voice
            }

        @Structure.FieldOrder(value = ["bas", "spd", "vol", "pit", "acc", "lmd", "fsc"])
        open class AQTK_VOICE : Structure() {
            @JvmField
            var bas = 0

            @JvmField
            var spd = 100

            @JvmField
            var vol = 100

            @JvmField
            var pit = 0

            @JvmField
            var acc = 0

            @JvmField
            var lmd = 100

            @JvmField
            var fsc = 100

            object ByReference : AQTK_VOICE(), Structure.ByReference

            object ByValue : AQTK_VOICE(), Structure.ByValue

            override fun toString(): String {
                return "AQTK_VOICE(bas=$bas, spd=$spd, vol=$vol, pit=$pit, acc=$acc, lmd=$lmd, fsc=$fsc)"
            }

        }
    }


    fun AquesTalk_SetDevKey(key: String): Int

    fun AquesTalk_Synthe_Utf8(voice: AQTK_VOICE.ByReference, koe: String, pSize: IntByReference): Pointer?

    fun AquesTalk_Synthe_Utf16(voice: AQTK_VOICE.ByReference, koe: String, pSize: IntByReference): Pointer?

    fun AquesTalk_Synthe(voice: AQTK_VOICE.ByReference, koe: String, pSize: IntByReference): Pointer?

    fun AquesTalk_FreeWave(wav: Pointer)

    private interface Errors {
        val code: Int
        val msg: String
    }

    enum class ErrorCode : Errors {
        ERROR_0 {
            override val code = 100
            override val msg = "其它错误"
        },
        ERROR_1 {
            override val code = 101
            override val msg = "内存不足"
        },
        ERROR_2 {
            override val code = 103
            override val msg = "音标字符串指定错误（单词开头的长元音，连续 so ku on 等）"
        },
        ERROR_3 {
            override val code = 104
            override val msg = "拼音字符串中没有有效的读音"
        },
        ERROR_4 {
            override val code = 105
            override val msg = "在音标字符串中指定了未定义的阅读符号"
        },
        ERROR_5 {
            override val code = 106
            override val msg = "音标字符串的标签规格不正确"
        },
        ERROR_6 {
            override val code = 107
            override val msg = "标签长度超出限制（或 [>] 未找到）"
        },
        ERROR_7 {
            override val code = 108
            override val msg = "标签中指定的值不正确。"
        },
        ERROR_8 {
            override val code = 120
            override val msg = "拼音字符串太长"
        },
        ERROR_9 {
            override val code = 121
            override val msg = "一句话读多了"
        },
        ERROR_10 {
            override val code = 122
            override val msg = " 长音标字符串（内部缓冲区超过 1）"
        };

        companion object {
            fun valueOf(code: Int): String {
                values().forEach {
                    if (it.code == code) {
                        return it.msg
                    }
                }
                throw IllegalArgumentException("找不到与之对应的异常 $code")
            }
        }
    }
}