package com.reactnativebuttercrypt

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise

class ButtercryptModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "Buttercrypt"
    }

    @ReactMethod
    fun deriveKeyFromPassword(password: String, salt: String, rounds: Int, promise: Promise) {
      try {
          val derivedKey = BCDerivation.deriveKeyFromPassword(password, salt, rounds);
          promise.resolve(derivedKey);
      } catch (e: Exception) {
          promise.reject(e);
      }
    }

    @ReactMethod
    fun generateSaltWithLength(length: Int, promise: Promise) {
      val salt = BCCrypto.generateSalt(length)
      promise.resolve(salt)
    }

    @ReactMethod
    fun decryptText(encryptedText: String, keyHex: String, ivHex: String, saltHex: String, hmacHexKey: String, hmacHex: String, promise: Promise) {
      val decryptedText = BCCrypto.decryptText(encryptedText, keyHex, ivHex, saltHex, hmacHexKey, hmacHex)
      promise.resolve(decryptedText)
    }

    @ReactMethod
    fun encryptText(encodedText: String, keyHex: String, saltHex: String, hmacHexKey: String, promise: Promise) {
      val encryptedText = BCCrypto.encryptText(encodedText, keyHex, saltHex, hmacHexKey)
      promise.resolve(encryptedText)
    }
}
