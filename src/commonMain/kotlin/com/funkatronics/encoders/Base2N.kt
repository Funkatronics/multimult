package com.funkatronics.encoders

import com.funkatronics.encoders.error.InvalidInputException
import kotlin.math.ceil
import kotlin.math.log2

object Base2N {

    const val PAD_CHAR = '='

    fun encode(alphabet: String, base: Int, input: ByteArray, pad: Boolean = true): String {

        if (!(base and (base - 1) == 0 && base != 0))
            throw InvalidInputException.InvalidBase("Provided base { $base } is not a power of 2.")

        if (input.isEmpty()) return ""

        if (alphabet.length < base) throw InvalidInputException.InvalidAlphabet(base, alphabet)

        val bitsPerChar = ceil(log2(base.toDouble())).toInt()
        val blockSize = lcm(bitsPerChar, 8) / 8
        val groupSize = blockSize*8/bitsPerChar
        val mask = (255 shr (8 - bitsPerChar) and 0xff)

        var result = ""
        var index = 0
        val byteMask = 255L
        while (index < input.size) {
            val symbolsLeft = input.size - index
            val padSize = if (symbolsLeft >= blockSize) 0 else (blockSize - symbolsLeft)*8/bitsPerChar
            var chunk = 0L

            (0 until blockSize).forEach { blockIndex ->
                chunk = chunk or (
                        input.getOrElse(index + blockIndex) { 0 }.toLong() and byteMask
                                shl 8 * (blockSize - blockIndex - 1)
                )
            }

            index += blockSize

            (groupSize - 1 downTo  padSize).forEach { i ->
                val char = (chunk shr (bitsPerChar * i)) and mask.toLong()
                result += alphabet[char.toInt()]
            }

            if (pad) repeat(padSize) { result += PAD_CHAR }
        }

        return result
    }

    fun decode(alphabet: String, base: Int, input: String): ByteArray {

        if (!(base and (base - 1) == 0 && base != 0))
            throw InvalidInputException.InvalidBase("Provided base { $base } is not a power of 2.")

        if (input.isEmpty()) return ByteArray(0)

        if (alphabet.length < base) throw InvalidInputException.InvalidAlphabet(base, alphabet)

        val chunkSize = ceil(log2(base.toDouble())).toInt()

        val data = input.replace(PAD_CHAR.toString(), "")

        val result = ByteArray(data.length*chunkSize/8)
        data.forEachIndexed { i, c ->
            val dataIndex = i*chunkSize/8

            if (dataIndex >= result.size) return@forEachIndexed

            val b1 = result[dataIndex].toInt()
            val b2 = if (dataIndex + 1 < result.size)  result[dataIndex + 1].toInt() else 0
            val fullChunk = (b1 shl 8) or (b2 and 0xff)
            val chunk = alphabet.indexOf(c)

            if (chunk < 0) throw InvalidInputException.InvalidCharacter(c, i)

            val shift = 16 - (i*chunkSize + chunkSize - dataIndex*8)

            val rb1 = fullChunk or (chunk shl shift)

            result[dataIndex] = (rb1 shr 8).toByte()

            if (dataIndex + 1 < result.size)
                result[dataIndex + 1] = (rb1 and 0xff).toByte()
        }

        return result
    }

    private fun gcd(a: Int, b: Int): Int = if (a == 0) b else gcd(b%a, a)
    private fun lcm(a: Int, b: Int) = (a/gcd(a, b)) * b
}