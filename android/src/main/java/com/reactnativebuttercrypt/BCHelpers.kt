package com.reactnativebuttercrypt
import org.spongycastle.util.encoders.Base64

object BCHelpers {
  private val HEX_CHARS = "0123456789ABCDEF"

  fun base64ToString(base64String:String):String {
    val decodedData = Base64.decode(base64String)
    return String(decodedData)
  }
  fun byteArrayToHexString(bytes:ByteArray):String {
    val builder = StringBuilder()
    for (b in bytes)
    {
      builder.append(String.format("%02x", b))
    }
    return builder.toString()
  }
  fun hexStringToByteArray(s:String):ByteArray {
    val length = s.length
    val result = ByteArray(length / 2)

    for (i in 0 until length step 2) {
        val firstIndex = HEX_CHARS.indexOf(s[i])
        val secondIndex = HEX_CHARS.indexOf(s[i + 1])

        val octet = firstIndex.shl(4).or(secondIndex)
        result.set(i.shr(1), octet.toByte())
    }

    return result
  }

  fun stringToBase64(s:String):String {
    val encodedBuffer = Base64.encode(s.toByteArray())
    return String(encodedBuffer)
  }
}
