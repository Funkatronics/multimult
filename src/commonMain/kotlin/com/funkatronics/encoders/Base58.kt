package com.funkatronics.encoders

object Base58 : Encoder, Decoder {

    override fun encode(input: ByteArray) = Base58BtcEncoder.encode(input)
    override fun encodeToString(input: ByteArray) = Base58BtcEncoder.encodeToString(input)

    override fun decode(input: String) = Base58BtcDecoder.decode(input)
    override fun decodeToString(input: String) = Base58BtcDecoder.decodeToString(input)
}

object Base58BtcEncoder : Encoder {
    const val ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
    override fun encode(input: ByteArray): ByteArray = encodeToString(input).toByteArray()
    override fun encodeToString(input: ByteArray): String = BaseN.encode(ALPHABET, input)
}

object Base58BtcDecoder : Decoder {
    override fun decode(input: String): ByteArray = BaseN.decode(Base58BtcEncoder.ALPHABET, 58, input)
    override fun decodeToString(input: String): String = String(decode(input))
}