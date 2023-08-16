package com.funkatronics.encoders

import com.funkatronics.encoders.error.InvalidInputException
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.math.min

object BaseN {

    fun encode(alphabet: String, input: ByteArray): String =
        when (alphabet.filter { it != '=' }.length) {
            2 -> encodeBase2N(alphabet, input)
            8 -> encodeBase2N(alphabet.filter { it != '=' }, input, alphabet.find { it == '=' })
            10 -> encodeBaseN(alphabet, input)
            16 -> encodeBase2N(alphabet.filter { it != '=' }, input, alphabet.find { it == '=' })
            32 -> encodeBase2N(alphabet.filter { it != '=' }, input, alphabet.find { it == '=' })
            36 -> encodeBaseN(alphabet, input)
            58 -> encodeBaseN(alphabet, input)
            64 -> encodeBase2N(alphabet.filter { it != '=' }, input, alphabet.find { it == '=' })
            else -> ""
        }

    fun encodeBase2N(alphabet: String, input: ByteArray, padWith: Char? = null): String {

        val base = alphabet.filter { it != '=' }.length
        val blockSize = if (base and (base - 1) == 0 && base != 0) log2(base.toDouble()).toInt() else null
        val pad = blockSize?.let { if (input.size%blockSize > 0) blockSize - input.size%blockSize else 0 } ?: 0

        val encoded = encodeBaseNCanonical(alphabet.filter { it != padWith }, input + ByteArray(pad))

        val finalSize = ceil(8f / log2(base.toDouble()) * (input.size)).toInt()

        return if (padWith != null) {
            var length = finalSize - 1
            while (++length < encoded.size) encoded[length] = '='
            String(encoded, 0, encoded.size)
        } else String(encoded, 0, min(finalSize, encoded.size))
    }

    fun encodeBaseN(alphabet: String, input: ByteArray): String = String(encodeBaseNCanonical(alphabet, input))

    private fun encodeBaseNCanonical(alphabet: String, input: ByteArray): CharArray {
        if (input.isEmpty()) return CharArray(0)

        val base = alphabet.length
        val encodedZero = alphabet[0]

        // Count leading zeros.
        var zeros = input.indexOfFirst { it.toInt() != 0 }

        // Convert base-256 digits to base-N digits (plus conversion to ASCII characters)
        val input = input.copyOf(input.size) // since we modify it in-place

        val maxEncodedSize = ceil(8f / log2(base.toDouble()) * (input.size)).toInt()
        val encoded = CharArray(maxEncodedSize) // upper bound = input.size * 2

        var outputStart = encoded.size
        var inputStart = zeros
        while (inputStart < input.size) {
            encoded[--outputStart] = alphabet[divmod(input, inputStart, 256, base)]
            if (input[inputStart].toInt() == 0) ++inputStart // optimization - skip leading zeros
        }

        // Preserve exactly as many leading encoded zeros in output as there were leading zeros in input.
        while (outputStart < encoded.size && encoded[outputStart] == encodedZero) {
            ++outputStart
        }
        while (--zeros >= 0) {
            encoded[--outputStart] = encodedZero
        }

//        return String(encoded, outputStart, encoded.size - outputStart)
        return encoded.sliceArray(outputStart until encoded.size)
    }

    fun decode(alphabet: String, input: String): ByteArray =
        when (alphabet.filter { it != '=' }.length) {
            2 -> decodeBaseN(alphabet, alphabet.length, input)
            8 -> decodeBase2N(alphabet, alphabet.filter { it != '=' }.length, input, alphabet.find { it == '=' })
            10 -> decodeBaseN(alphabet, alphabet.length, input)
            16 -> decodeBase2N(alphabet, alphabet.filter { it != '=' }.length, input, alphabet.find { it == '=' })
            32 -> decodeBase2N(alphabet, alphabet.filter { it != '=' }.length, input, alphabet.find { it == '=' })
            36 -> decodeBaseN(alphabet, alphabet.length, input)
            58 -> decodeBaseN(alphabet, alphabet.length, input)
            64 -> decodeBase2N(alphabet, alphabet.filter { it != '=' }.length, input, alphabet.find { it == '=' })
            else -> ByteArray(0)
        }

    fun decode(alphabet: String, base: Int, input: String): ByteArray {
        return decodeBaseNCanonical(alphabet, base, input)
    }

    fun decodeBaseN(alphabet: String, base: Int, input: String) = decodeBaseNCanonical(alphabet, base, input)

    fun decodeBase2N(alphabet: String, base: Int, input: String, padWith: Char? = null): ByteArray {

        val blockSize = if (base == 2 || !(base and (base - 1) == 0 && base != 0)) null else {
            val bitsPerChar = ceil(log2(base.toDouble())).toInt()
            lcm(bitsPerChar, 8) / bitsPerChar
        }

        val pad = blockSize?.let { blockSize - input.length%blockSize } ?: 0
        val data = if (padWith != null ) input.replace(padWith, alphabet[0]) else input + // correct padding
                buildString(pad) { repeat(pad) { insert(0, alphabet[0]) } } // pad to canonical length

        val decoded = decodeBaseNCanonical(alphabet, base, data)

        return decoded.filter { it != 0.toByte() }.toByteArray()
    }

    private fun decodeBaseNCanonical(alphabet: String, base: Int, input: String): ByteArray {
        if (input.isEmpty()) return ByteArray(0)

        // Convert the baseN-encoded ASCII chars to a baseN byte sequence (base58 digits).
        val bytes = ByteArray(input.length) { i ->
            val idx = alphabet.indexOf(input[i]).toByte()
            if (idx < 0) throw InvalidInputException.InvalidCharacter(input[i], i)
            idx
        }

        // Count leading zeros.
        val zeros = bytes.indexOfFirst { it.toInt() != 0 }

        // Convert base-58 digits to base-256 digits.
        val decoded = ByteArray(input.length)
        var outputStart = decoded.size
        var start = zeros
        while (start < bytes.size) {
            if (bytes[start] == 0.toByte()) ++start
            else decoded[--outputStart] = divmod(bytes, start, base, 256).toByte()
        }

        // Ignore extra leading zeroes that were added during the calculation.
        while (outputStart < decoded.size && decoded[outputStart].toInt() == 0) {
            ++outputStart
        }

        // Return decoded data (including original number of leading zeros).
        return decoded.copyOfRange(outputStart - zeros, decoded.size)
    }

    private fun divmod(number: ByteArray, firstDigit: Int, base: Int, divisor: Int): Int {
        // this is just long division which accounts for the base of the input digits
        var remainder = 0
        for (i in firstDigit until number.size) {
            val digit = number[i].toInt() and 0xFF
            val temp = remainder * base + digit
            number[i] = (temp / divisor).toByte()
            remainder = temp % divisor
        }
        return remainder
    }

    private fun gcd(a: Int, b: Int): Int = if (a == 0) b else gcd(b%a, a)
    private fun lcm(a: Int, b: Int) = (a/gcd(a, b)) * b
}