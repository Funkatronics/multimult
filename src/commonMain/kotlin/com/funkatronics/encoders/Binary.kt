package com.funkatronics.encoders

object Binary : Encoder, Decoder {

    override fun encode(input: ByteArray): ByteArray = encodeToString(input).encodeToByteArray()

    override fun encodeToString(input: ByteArray): String {
        return input.fold("") { a, c ->
            val binaryString = c.toUByte().toString(2)
            a + "0".repeat(8 - binaryString.length) + binaryString
        }
    }

    override fun decode(input: String): ByteArray {
        val pad = input.length % 8
        return ("0".repeat(pad) + input).chunked(8).map {
            it.toUByte(2).toByte()
        }.toByteArray()
    }

    override fun decodeToString(input: String): String = decode(input).decodeToString()
}