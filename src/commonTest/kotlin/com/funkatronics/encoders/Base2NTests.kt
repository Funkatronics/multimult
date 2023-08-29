package com.funkatronics.encoders

import com.funkatronics.encoders.error.InvalidInputException
import kotlin.test.*

class Base2NTests {

    @Test
    fun testEncodeEmptyString() {
        // given
        val alphabet = "01"
        val base = 2
        val stringToEncode = ""
        val expectedEncoded = ""

        // when
        val actualEncoded = Base2N.encode(alphabet, base, stringToEncode.encodeToByteArray())

        // then
        assertEquals(expectedEncoded, actualEncoded)
    }

    @Test
    fun testDecodeEmptyString() {
        // given
        val alphabet = "01"
        val base = 2
        val stringToDecode = ""
        val expectedDecoded = ByteArray(0)

        // when
        val actualDecoded = Base2N.decode(alphabet, base, stringToDecode)

        // then
        assertContentEquals(expectedDecoded, actualDecoded)
    }

    @Test
    fun testEncodeBase2() {
        // given
        val alphabet = "01"
        val base = 2
        val stringToEncode = "yes mani !"
        val expectedEncoded = "01111001011001010111001100100000011011010110000101101110011010010010000000100001"

        // when
        val actualEncoded = Base2N.encode(alphabet, base, stringToEncode.encodeToByteArray())

        // then
        assertEquals(expectedEncoded, actualEncoded)
    }

    @Test
    fun testDecodeBase2() {
        // given
        val alphabet = "01"
        val base = 2
        val stringToDecode = "01111001011001010111001100100000011011010110000101101110011010010010000000100001"
        val expectedDecoded = "yes mani !"

        // when
        val actualDecoded = String(Base2N.decode(alphabet, base, stringToDecode))

        // then
        assertEquals(expectedDecoded, actualDecoded)
    }

    @Test
    fun testEncodeDecodeBase64() {
        // given
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        val base = 64
        val stringToEncode = "a string to encode, with some punctuation!!/"

        // when
        val actualEncoded = Base2N.encode(alphabet, base, stringToEncode.encodeToByteArray())
        val actualDecoded = String(Base2N.decode(alphabet, base, actualEncoded))

        // then
        assertEquals(stringToEncode, actualDecoded)
    }

    @Test
    fun testEncodeBase32IncludesDefaultPadding() {
        // given
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
        val base = 32
        val stringToEncode = "Hello!"
        val expectedEncoded ="JBSWY3DPEE======"

        // when
        val actualEncoded = Base2N.encode(alphabet, base, stringToEncode.encodeToByteArray())

        // then
        assertEquals(expectedEncoded, actualEncoded)
    }

    @Test
    fun testDecodeBase32WithDefaultPadding() {
        // given
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
        val base = 32
        val stringToDecode = "JBSWY3DPEE======"
        val expectedDecoded = "Hello!"

        // when
        val actualDecoded = String(Base2N.decode(alphabet, base, stringToDecode))

        // then
        assertEquals(expectedDecoded, actualDecoded)
    }

    @Test
    fun testEncodeBase32WithoutPadding() {
        // given
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
        val base = 32
        val stringToEncode = "Hello!"
        val expectedEncoded ="JBSWY3DPEE"

        // when
        val actualEncoded = Base2N.encode(alphabet, base, stringToEncode.encodeToByteArray(), pad = false)

        // then
        assertEquals(expectedEncoded, actualEncoded)
    }

    @Test
    fun testDecodeBase32WithoutPadding() {
        // given
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
        val base = 32
        val stringToDecode = "JBSWY3DPEE"
        val expectedDecoded = "Hello!"

        // when
        val actualDecoded = String(Base2N.decode(alphabet, base, stringToDecode))

        // then
        assertEquals(expectedDecoded, actualDecoded)
    }

    @Test
    fun testEncodeNonBase2NThrowsInvalidBaseException() {
        // given
        val alphabet = "012"
        val base = 3
        val stringToEncode = "encode me"

        // when
        val block = { Base2N.encode(alphabet, base, stringToEncode.encodeToByteArray()) }

        // then
        assertFailsWith(InvalidInputException.InvalidBase::class) {
            block()
        }
    }

    @Test
    fun testDecodeNonBase2NThrowsInvalidBaseException() {
        // given
        val alphabet = "012"
        val base = 3
        val stringToDecode = "012012"

        // when
        val block = { Base2N.decode(alphabet, base, stringToDecode) }

        // then
        assertFailsWith(InvalidInputException.InvalidBase::class) {
            block()
        }
    }

    @Test
    fun testEncodeInvalidAlphabetForBaseThrowsInvalidAlphabetException() {
        // given
        val alphabet = "01"
        val base = 8
        val stringToEncode = "encode me"

        // when
        val block = { Base2N.encode(alphabet, base, stringToEncode.encodeToByteArray()) }

        // then
        assertFailsWith(InvalidInputException.InvalidAlphabet::class) {
            block()
        }
    }

    @Test
    fun testDecodeInvalidAlphabetForBaseThrowsInvalidAlphabetException() {
        // given
        val alphabet = "01"
        val base = 8
        val stringToDecode = "01234567"

        // when
        val block = { Base2N.decode(alphabet, base, stringToDecode) }

        // then
        assertFailsWith(InvalidInputException.InvalidAlphabet::class) {
            block()
        }
    }
}