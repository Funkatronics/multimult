package com.funkatronics.encoders

object Base32 : Encoder, Decoder {

    override fun encode(input: ByteArray): ByteArray = getEncoder().encode(input)
    override fun encodeToString(input: ByteArray): String = getEncoder().encodeToString(input)

    override fun decode(input: String): ByteArray = getDecoder().decode(input)
    override fun decodeToString(input: String): String = getDecoder().decodeToString(input)

    fun getEncoder(withoutPadding: Boolean = false) = Base32Encoder.Default(withoutPadding)
    fun getHexEncoder(withoutPadding: Boolean = false): Encoder = Base32Encoder.Hex(withoutPadding)
    fun getDecoder() = Base32Decoder.Default()
    fun getHexDecoder() = Base32Decoder.Hex()
}

sealed class Base32Encoder(val alphabet: String, private val withoutPadding: Boolean = false): Encoder {
    override fun encode(input: ByteArray): ByteArray = encodeToString(input).toByteArray()
    override fun encodeToString(input: ByteArray): String = Base2N.encode(alphabet, 32, input, !withoutPadding)

    class Default(withoutPadding: Boolean)
        : Base32Encoder("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567", withoutPadding)
    class Hex(withoutPadding: Boolean)
        : Base32Encoder("0123456789ABCDEFGHIJKLMNOPQRSTUV", withoutPadding)
}

sealed class Base32Decoder(val alphabet: String): Decoder {
    override fun decode(input: String): ByteArray = Base2N.decode(alphabet, 32, input)
    override fun decodeToString(input: String): String = String(decode(input))

    class Default : Base32Decoder("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567")
    class Hex : Base32Decoder("0123456789ABCDEFGHIJKLMNOPQRSTUV")
}