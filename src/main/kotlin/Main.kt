import com.sun.jna.ptr.IntByReference
import java.io.File

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val instance = AquesTalk.INSTANCE
        // 官网申请的 Key
        val devKey = "XXXX-XXXX-XXXX-XXXX"
        // 开发者使用的 Key 用 AquesTalk_SetDevKey, 用户购买? 的 Key 使用 AquesTalk_SetUsrKey
        if (instance.AquesTalk_SetDevKey(devKey) == 0) {
            println("Dev Key 注册成功")
        } else {
            println("Dev Key 注册失败, 生成的语音将会有所限制")
        }

        val voice = AquesTalk.gVoice_F1
        println(voice)
        val size = IntByReference()
        val wav = instance.AquesTalk_Synthe_Utf8(
            voice,
            "ここに、おんせー/きご'うれつ、きほんわ、ひらがな'の/よみ'を/きじゅつしま'す。",
            size
        )
        val code = size.value
        wav?.let {
            println("语音生成成功, Size -> ${code.toKB()} KB")
            val bytes = wav.getByteArray(0, code)
            File("1.wav").writeBytes(bytes)
            instance.AquesTalk_FreeWave(wav)
            return
        }
        println("语音生成失败 code -> $code : ${AquesTalk.ErrorCode.valueOf(code)}")
    }

    private fun Int.toKB() = this.div(1024)
}