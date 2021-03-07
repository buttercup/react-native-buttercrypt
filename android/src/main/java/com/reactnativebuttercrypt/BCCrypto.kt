package com.reactnativebuttercrypt

import org.spongycastle.util.encoders.Base64
import java.util.Random
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac

object BCCrypto {
  private val IV_BYTE_LEN = 16
  private val RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXZY0123456789!@#$%^&*(){}[]<>,.?~|-=_+"

  fun decryptText(encryptedText:String, keyHex:String, ivHex:String, saltHex:String, hmacKeyHex:String, hmacHex:String):String {
    val encryptedData = Base64.decode(encryptedText)
    val keyData = BCHelpers.hexStringToByteArray(keyHex)
    val ivData = BCHelpers.hexStringToByteArray(ivHex)
    val hmacKeyData = BCHelpers.hexStringToByteArray(hmacKeyHex)
    val iv = IvParameterSpec(ivData)
    val skeySpec = SecretKeySpec(keyData, "AES")
    try
    {
      // HMAC verification
      val sha256HMAC = Mac.getInstance("HmacSHA256")
      val hmacSecretKey = SecretKeySpec(hmacKeyData, "HmacSHA256")
      sha256HMAC.init(hmacSecretKey)
      val hmacTarget = encryptedText + ivHex + saltHex
      val hmacData = sha256HMAC.doFinal(hmacTarget.toByteArray(StandardCharsets.UTF_8))
      val newHmacHex = BCHelpers.byteArrayToHexString(hmacData)
      if (newHmacHex != hmacHex)
      {
        throw Exception("Authentication failed - possible tampering")
      }
      // AES decryption
      val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
      cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
      val decrypted = cipher.doFinal(encryptedData)
      return decrypted.toString(StandardCharsets.UTF_8)
    }
    catch (ex:Exception) {
      return "Error:" + ex.message
    }
  }
  fun encryptText(text:String, keyHex:String, saltHex:String, ivHex:String, hmacHexKey:String):String {
    val ivData = BCHelpers.hexStringToByteArray(ivHex)
    val keyData = BCHelpers.hexStringToByteArray(keyHex)
    val hmacKeyData = BCHelpers.hexStringToByteArray(hmacHexKey)
    val iv = IvParameterSpec(ivData)
    val skeySpec = SecretKeySpec(keyData, "AES")
    try
    {
      // AES encryption
      val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
      val encrypted = cipher.doFinal(text.toByteArray())
      val encodedEncryptedText = Base64.encode(encrypted)
      val encryptedText = String(encodedEncryptedText)
      // HMAC
      val sha256HMAC = Mac.getInstance("HmacSHA256")
      val hmacSecretKey = SecretKeySpec(hmacKeyData, "HmacSHA256")
      sha256HMAC.init(hmacSecretKey)
      val hmacTarget = encryptedText + ivHex + saltHex
      var hmacData = sha256HMAC.doFinal(hmacTarget.toByteArray(StandardCharsets.UTF_8))
      val hmacHex = BCHelpers.byteArrayToHexString(hmacData)
      // Output
      return encryptedText + "|" + hmacHex + "|" + ivHex + "|" + saltHex
    }
    catch (ex:Exception) {
      return "Error:" + ex.message
    }
  }
  fun generateIV():String {
    // Use salt to generate the hex
    return generateSalt(IV_BYTE_LEN * 2)
  }
  fun generateRandomString(length:Int):String {
    val randomString = (1..length)
      .map { i -> kotlin.random.Random.nextInt(0, RANDOM_CHARS.length) }
      .map(RANDOM_CHARS::get)
      .joinToString("");
    return randomString;
  }
  fun generateSalt(length:Int):String {
    val randomText = generateRandomString(length * 2)
    val hex = String.format("%040x", BigInteger(1, randomText.toByteArray(StandardCharsets.UTF_8)))
    return hex.substring(0, length).toLowerCase()
  }
}
