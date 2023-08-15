package com.funkatronics.encoders

object Base64 : Encoder, Decoder {

    override fun encode(input: ByteArray): ByteArray = getEncoder().encode(input)
    override fun encodeToString(input: ByteArray): String = getEncoder().encodeToString(input)

    override fun decode(input: String): ByteArray = getDecoder().decode(input)
    override fun decodeToString(input: String): String = getDecoder().decodeToString(input)

    fun getEncoder(withoutPadding: Boolean = false) = Base64Encoder.Default(withoutPadding)
    fun getUrlEncoder(withoutPadding: Boolean = false): Encoder = Base64Encoder.Url(withoutPadding)
    fun getDecoder() = Base64Decoder.Default()
    fun getUrlDecoder() = Base64Decoder.Url()
}

sealed class Base64Encoder(val alphabet: String, private val withoutPadding: Boolean = false): Encoder {
    override fun encode(input: ByteArray): ByteArray = encodeToString(input).toByteArray()
    override fun encodeToString(input: ByteArray): String = Base2N.encode(alphabet, 64, input, !withoutPadding)

    class Default(withoutPadding: Boolean)
        : Base64Encoder("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/", withoutPadding)
    class Url(withoutPadding: Boolean)
        : Base64Encoder("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_", withoutPadding)
}

sealed class Base64Decoder(val alphabet: String): Decoder {
    override fun decode(input: String): ByteArray = Base2N.decode(alphabet, 64, input)
    override fun decodeToString(input: String): String = String(decode(input))

    class Default : Base64Decoder("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")
    class Url : Base64Decoder("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_")
}

//object Base64Encoder : Encoder {
//    const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
//    override fun encode(input: ByteArray): ByteArray = encodeToString(input).toByteArray()
//    override fun encodeToString(input: ByteArray): String = Base2N.encode(ALPHABET, 64, input)
//}
//
//object Base64Decoder : Decoder {
//    override fun decode(input: String): ByteArray = Base2N.decode(Base64Encoder.ALPHABET, 64, input)
//    override fun decodeToString(input: String): String = String(decode(input))
//}
//
//object Base64UrlEncoder : Encoder {
//    const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
//    override fun encode(input: ByteArray): ByteArray = Base2N.encode(ALPHABET, 64, input).toByteArray()
//    override fun encodeToString(input: ByteArray): String = Base2N.encode(ALPHABET, 64, input)
//}
//
//object Base64UrlDecoder : Decoder {
//    override fun decode(input: String): ByteArray = Base2N.decode(Base64UrlEncoder.ALPHABET, 64, input)
//    override fun decodeToString(input: String): String = String(decode(input))
//}