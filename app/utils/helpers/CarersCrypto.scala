package utils.helpers

import app.ConfigProperties._
import play.api.libs.Crypto
import java.util.UUID

object CarersCrypto {
  val encrypt = getProperty("encryptFields", true)

  val staticSecret = getProperty("staticSecret", false)

  val secretKey = if (staticSecret) "1234567890123456" else generateKey

  def decryptAES(v: String): String = if (encrypt) Crypto.decryptAES(v, secretKey) else v

  def encryptAES(v: String) = if (encrypt) Crypto.encryptAES(v, secretKey) else v

  def generateKey: String = UUID.randomUUID().toString.substring(0, 16)
}