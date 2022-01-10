import com.sun.jna.ptr.IntByReference
import java.io.File

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val instance = AquesTalk.INSTANCE
        val key = "XXXX-XXXX-XXXX-XXXX"

        if (instance.AquesTalk_SetDevKey(key) == 0) {
            println("Dev Key 注册成功")
        } else {
            println("Dev Key 注册失败, 生成的语音将会有所限制")
        }

        val voice = AquesTalk.gVoice_F1E
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
            return
        }
        println("语音生成失败 code -> $code : ${AquesTalk.ErrorCode.valueOf(code)}")
    }

    private fun Int.toKB() = this.div(1024)
}