import com.mrx.AquesUtils
import java.io.File

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        // 官网申请的 Key
        // 开发者使用的 Key 用 AquesTalk_SetDevKey, 用户购买? 的 Key 使用 AquesTalk_SetUsrKey
        val devKey = "XXXX-XXXX-XXXX-XXXX"
        val instance = AquesUtils.getInstance().also { it.setKey(devKey) }

        val res = instance.rTTS("ここに、おんせー/きご'うれつ、きほんわ、ひらがな'の/よみ'を/きじゅつしま'す。")
        File("1.wav").writeBytes(res)

        val res2 = instance.kTTS("お寿司が食べたい。")
        File("2.wav").writeBytes(res2)

    }

}