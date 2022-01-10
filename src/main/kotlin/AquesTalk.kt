@file:Suppress("UNUSED")

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.StdCallLibrary

interface AquesTalk : StdCallLibrary {

    companion object {
        val INSTANCE: AquesTalk = Native.load("AquesTalk.dll", AquesTalk::class.java)

        /**
         *基本素片
         */
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

        /**
         * 预置设置 女声 1
         */
        val gVoice_F1: AQTK_VOICE.ByReference
            get() {
                voice.bas = VoiceBase.F1E
                return voice
            }

        /**
         * 预置设置 女声 2
         */
        val gVoice_F2: AQTK_VOICE.ByReference
            get() {
                with(voice) {
                    bas = VoiceBase.F2E
                    pit = 77
                    acc = 150
                }
                return voice
            }

        /**
         * 预置设置 女声 3
         */
        val gVoice_F3: AQTK_VOICE.ByReference
            get() {
                with(voice) {
                    bas = VoiceBase.F1E
                    spd = 80
                    lmd = 61
                    fsc = 148
                }
                return voice
            }

        /**
         * 预置设置 男声 1
         */
        val gVoice_M1: AQTK_VOICE.ByReference
            get() {
                with(voice) {
                    bas = VoiceBase.M1E
                    pit = 30
                }
                return voice
            }

        /**
         * 预置设置 男声 2
         */
        val gVoice_M2: AQTK_VOICE.ByReference
            get() {
                with(voice) {
                    bas = VoiceBase.M1E
                    spd = 105
                    pit = 45
                    acc = 130
                    lmd = 120
                }
                return voice
            }

        /**
         * 预置设置 机器人声 1
         */
        val gVoice_R1: AQTK_VOICE.ByReference
            get() {
                with(voice) {
                    bas = VoiceBase.M1E
                    pit = 30
                    acc = 20
                    lmd = 190
                }
                return voice
            }

        /**
         * 预置设置 机器人声 2
         */
        val gVoice_R2: AQTK_VOICE.ByReference
            get() {
                with(voice) {
                    bas = VoiceBase.F2E
                    spd = 70
                    pit = 50
                    acc = 50
                    lmd = 50
                    fsc = 180
                }
                return voice
            }

        /**
         *  音色数据结构体
         * @property bas Int 基本素片 F1E/F2E/M1E (0/1/2)
         * @property spd Int 話速 	50-300 default:100
         * @property vol Int 音量 	0-300 default:100
         * @property pit Int 高さ 	20-200 default:基本素片に依存
         * @property acc Int アクセント 0-200 default:基本素片に依存
         * @property lmd Int 音程１ 	0-200 default:100
         * @property fsc Int 音程２(サンプリング周波数) 50-200 default:100
         */
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

            /**
             * JNA 指针引用对象
             */
            object ByReference : AQTK_VOICE(), Structure.ByReference

            /**
             * JNA 值引用对象
             */
            object ByValue : AQTK_VOICE(), Structure.ByValue

            override fun toString(): String {
                return "AQTK_VOICE(bas=$bas, spd=$spd, vol=$vol, pit=$pit, acc=$acc, lmd=$lmd, fsc=$fsc)"
            }

        }
    }

    /**
     * 设置 开发者 Key
     * @param key String Key
     * @return Int 0 为 成功, 1 为失败
     */
    fun AquesTalk_SetDevKey(key: String): Int

    /**
     * 设置 用户 Key
     * @param key String Key
     * @return Int 0 为 成功, 1 为失败
     */
    fun AquesTalk_SetUsrKey(key: String): Int

    /**
     *  TTS 方法, UTF8 语言环境下
     * @param voice ByReference AQTK_VOICE 的引用象
     * @param koe String    要生成语音的 平假名 字符串
     * @param pSize IntByReference 存放生成结果的 Int 引用对象
     * @return Pointer? 生成失败就返回 NULL 否则返回 指针对象
     */
    fun AquesTalk_Synthe_Utf8(voice: AQTK_VOICE.ByReference, koe: String, pSize: IntByReference): Pointer?

    /**
     *  TTS 方法, UTF16 语言环境下
     * @param voice ByReference AQTK_VOICE 的引用象
     * @param koe String    要生成语音的 平假名 字符串
     * @param pSize IntByReference 存放生成结果的 Int 引用对象
     * @return Pointer? 生成失败就返回 NULL 否则返回 指针对象
     */
    fun AquesTalk_Synthe_Utf16(voice: AQTK_VOICE.ByReference, koe: String, pSize: IntByReference): Pointer?

    /**
     *  TTS 方法, 其他语言环境下
     * @param voice ByReference AQTK_VOICE 的引用象
     * @param koe String    要生成语音的 平假名 字符串
     * @param pSize IntByReference 存放生成结果的 Int 引用对象
     * @return Pointer? 生成失败就返回 NULL 否则返回 指针对象
     */
    fun AquesTalk_Synthe(voice: AQTK_VOICE.ByReference, koe: String, pSize: IntByReference): Pointer?

    /**
     * 释放内存
     * @param wav Pointer 要释放的内存
     */
    fun AquesTalk_FreeWave(wav: Pointer)

    /**
     * 封装错误代码接口类
     * @property code Int 错误代码
     * @property msg String 错误说明
     */
    private interface Errors {
        val code: Int
        val msg: String
    }

    /**
     *错误代码实现类
     */
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
            /**
             * 根据错误代码找到其对应的解释
             * @param code Int 错误代码
             * @return String 解释
             */
            fun valueOf(code: Int): String {
                values().forEach {
                    if (it.code == code) {
                        return it.msg
                    }
                }
                throw IllegalArgumentException("找不到与之对应的错误 $code")
            }
        }
    }
}