package com.funkatronics.encoders

import com.funkatronics.encoders.error.InvalidInputException
import com.funkatronics.extensions.*
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

//    fun encodeBase2(input: ByteArray): String {
//        return input.fold("") { a, c ->
//            val binaryString = c.toUByte().toString(2)
//            a + "0".repeat(8 - binaryString.length) + binaryString
//        }
//    }

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

    @Deprecated("its slower")
    fun decodeSlow(alphabet: String, base: Int, input: String, blockSize: Int? = null): ByteArray {

        var power = 0
        while(base shr power > 1 || base shr power < -1) power++

        val pad = blockSize?.let { blockSize - input.length%blockSize } ?: 0
        val data = input + buildString(pad) { repeat(pad) { insert(0, '=') } }
        val maxDecodedSize = data.length
        val decoded = ByteArray(maxDecodedSize)

        // Convert from alphabet characters to the corresponding value
        val bytes = ByteArray(data.length) { i ->
            val idx = alphabet.indexOf(data[i]).toByte()
            if (idx > 0) idx else 0
        }

        // Skip all leading zeroes; we'll handle these separately at the end
//        var start = -1
//        bytes.find { start++; it != 0.toByte() }
//        val zeroes = start
        val zeroes = bytes.indexOfFirst { it.toInt() != 0 }
        var start = zeroes

        var pos = bytes.size - 1 // NOTE: pos can go as low as -1
        while (start < bytes.size) {
            if (bytes[start] == 0.toByte()) ++start
            else {
                var mod = 0
                for (i in start until bytes.size) {
                    mod = mod * base + bytes[i]
                    bytes[i] = (mod / 256).toByte()
                    mod %= 256
                }
                decoded[pos--] = mod.toByte()
            }
        }

        val result = ByteArray(zeroes + bytes.size - pos - 1)
        System.arraycopy(decoded, pos + 1, result, zeroes, bytes.size - pos - 1)

        val zeroBits = power*data.reversed().count { it == '=' }
        val shift = blockSize?.let{ zeroBits + if (zeroBits%8 > 0) (8 - zeroBits%8) else 0} ?: 0

        return result shr shift
    }

    @Deprecated("replaced with improved api, use decodeBase2n or decodeBaseNCanonical")
    fun decodeFast(alphabet: String, base: Int, input: String, blockSize: Int? = null): ByteArray {
        if (input.isEmpty()) {
            return ByteArray(0)
        }

        // Convert the baseN-encoded ASCII chars to a baseN byte sequence (base58 digits).
//        val input58 = ByteArray(input.length)
//        for (i in 0 until input.length) {
//            val c = input[i]
//            val digit = if (c.code < 128) alphabet.indexOf(c) else -1
////            if (digit < 0) {
////                throw InvalidCharacter(c, i)
////            }
//            input58[i] = digit.toByte()
//        }

        val bytes = ByteArray(input.length) { i ->
            val idx = alphabet.indexOf(input[i]).toByte()
            if (idx > 0) idx else 0
        }

        // Count leading zeros.
        val zeros = bytes.indexOfFirst { it.toInt() != 0 }
//        var zeros = 0
//        while (zeros < bytes.size && bytes[zeros].toInt() == 0) {
//            ++zeros
//        }

        // Convert base-58 digits to base-256 digits.
        val decoded = ByteArray(input.length)
        var outputStart = decoded.size
        var start = zeros
        while (start < bytes.size) {
            if (bytes[start] == 0.toByte()) ++start
            else {
//                var mod = 0
//                for (i in start until bytes.size) {
//                    mod = mod * base + bytes[i]
//                    bytes[i] = (mod / 256).toByte()
//                    mod %= 256
//                }
//                decoded[--outputStart] = mod.toByte()
                decoded[--outputStart] = divmod(bytes, start, base, 256).toByte()
            }
        }

        // Ignore extra leading zeroes that were added during the calculation.
        while (outputStart < decoded.size && decoded[outputStart].toInt() == 0) {
            ++outputStart
        }

        // Return decoded data (including original number of leading zeros).
        return decoded.copyOfRange(outputStart - zeros, decoded.size)
    }

    @Deprecated("its slower")
    fun encodeBaseNNew(alphabet: String, input: ByteArray, bitsPerChar: Int? = null): String {
        // Max output size is ceil(log2(256) / log2(58) * input_size). In efficient integer math,
        // a slight overestimate of this is (((input_size * 352) + 255) / 256).
//        val maxEncodedSize = (((bytes.size * 352) + 255) / 256)

        if (input.isEmpty()) return ""

//        val pad = bitsPerChar - input.size%bitsPerChar
        val pad = bitsPerChar?.let { if (input.size%bitsPerChar > 0) bitsPerChar - input.size%bitsPerChar else 0 } ?: 0
        val bytes = input + ByteArray(pad)

        val base = alphabet.filter { it != '=' }.length
        val maxEncodedSize = ceil(8f / log2(base.toDouble()) * bytes.size).toInt()
        val encoded = ByteArray(maxEncodedSize)

        var start = 0
        while (start < bytes.size && bytes[start] == 0.toByte()) {
            encoded[start] = alphabet[0].code.toByte()
            start++
        }

        var pos = maxEncodedSize - 1 // NOTE: pos can go as low as -1
        for (i in start until bytes.size) {
            var carry: Int = bytes[i].toUByte().toInt()
            var j = maxEncodedSize - 1
            while(carry != 0 || j > pos) {
                carry += encoded[j].toUByte().toInt() * 256
                encoded[j] = (carry % base).toByte()
                carry /= base
                j--
            }
            pos = j
        }

        for (i in (pos + 1) until maxEncodedSize) {
            encoded[start++] = alphabet[encoded[i].toInt()].code.toByte()
        }

        val doPad = alphabet.contains('=')

        var length = encoded.indexOfLast {
            it != alphabet[0].code.toByte()
        }

        return (if (doPad) {
            while (++length < maxEncodedSize) encoded[length] = '='.code.toByte()
            String(encoded, 0, start, Charsets.UTF_8)
        } else String(encoded, 0, length + 1, Charsets.UTF_8)).filter {
            alphabet.contains(it)
        }
    }

    @Deprecated("improved api, use encodeBaseN or encodeBase2N")
    fun encodeNewNew(alphabet: String, input: ByteArray, bitsPerChar: Int? = null): String {

        if (input.isEmpty()) return ""

        val encodedZero = alphabet[0]

        // Count leading zeros.
        var zeros = input.indexOfFirst { it.toInt() != 0 }

        val base = alphabet.filter { it != '=' }.length
        val blockSize = if (base and (base - 1) == 0 && base != 0) log2(base.toDouble()).toInt() else null
//        val factor = 8f / bitsPerChar
//        val pad: Int = if (input.size%bitsPerChar > 0) bitsPerChar - input.size%bitsPerChar else 0
        val pad = blockSize?.let { if (input.size%blockSize > 0) blockSize - input.size%blockSize else 0 } ?: 0
//        val bytes = input + ByteArray(pad)

        // Convert base-256 digits to base-N digits (plus conversion to ASCII characters)
        var input = input.copyOf(input.size) + ByteArray(pad) // since we modify it in-place

        val finalSize = ceil(8f / log2(base.toDouble()) * (input.size - pad)).toInt()
        val maxEncodedSize = ceil(8f / log2(base.toDouble()) * (input.size)).toInt()
        val encoded = CharArray(maxEncodedSize) // upper bound = input.size * 2

        var outputStart = encoded.size
        var inputStart = zeros
        while (inputStart < input.size) {
            encoded[--outputStart] = alphabet[divmod(input, inputStart, 256, base)]
            if (input[inputStart].toInt() == 0) {
                ++inputStart // optimization - skip leading zeros
            }
        }

        // Preserve exactly as many leading encoded zeros in output as there were leading zeros in input.
        while (outputStart < encoded.size && encoded[outputStart] == encodedZero) {
            ++outputStart
        }
        while (--zeros >= 0) {
            encoded[--outputStart] = encodedZero
        }

        val doPad = alphabet.contains("=")

        // Return encoded string (including encoded leading zeros).
        return if (doPad) {
            var length = finalSize - 1
            while (++length < maxEncodedSize) encoded[length] = '='
            String(encoded, outputStart, encoded.size - outputStart)
        }
        else String(encoded, outputStart, finalSize - outputStart)
    }
}