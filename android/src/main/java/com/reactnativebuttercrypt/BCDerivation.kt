package com.reactnativebuttercrypt

import java.nio.charset.StandardCharsets
import org.spongycastle.crypto.PBEParametersGenerator
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator
import org.spongycastle.crypto.digests.SHA256Digest
import org.spongycastle.crypto.params.KeyParameter

object BCDerivation {
  fun deriveKeyFromPassword(password:String, salt:String, rounds:Int):String {
    val passwordData = password.toCharArray()
    val saltData = salt.toByteArray(StandardCharsets.UTF_8)
    val generator = PKCS5S2ParametersGenerator(SHA256Digest())
    generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(passwordData), saltData, rounds)
    val key = generator.generateDerivedMacParameters(64 * 8) as KeyParameter
    return BCHelpers.byteArrayToHexString(key.getKey())
  }
}
