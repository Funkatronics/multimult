package com.funkatronics.encoders

import com.funkatronics.encoders.error.InvalidInputException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Base32Tests {

    @Test
    fun testBase32EncodeDecode() {
        // given
        val testString: String = "man"
        val expectedEncoded = "NVQW4==="

        // when
        val actualEncoded: String = Base32.encodeToString(testString.encodeToByteArray())
        val actualDecoded = Base32.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase32EncodeDecode2() {
        // given
        val testString: String = "Hello!"
        val expectedEncoded = "JBSWY3DPEE======"

        // when
        val actualEncoded: String = Base32.encodeToString(testString.encodeToByteArray())
        val actualDecoded = Base32.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase32EncodeDecodeLeadingZeros() {
        // given
        val leadingZeros = 5
        val testString: String = ByteArray(leadingZeros).decodeToString() + "Hello!"
        val expectedEncoded = "AAAAAAAAJBSWY3DPEE======"

        // when
        val actualEncoded: String = Base32.encodeToString(testString.encodeToByteArray())
        val actualDecoded = Base32.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase32EncodeDecodeAllZeros() {
        // given
        val length = 9
        val testString: String = ByteArray(length).decodeToString()
        val expectedEncoded = "AAAAAAAAAAAAAAA="

        // when
        val actualEncoded: String = Base32.encodeToString(testString.encodeToByteArray())
        val actualDecoded = Base32.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase32EncodeDecodeEmptyString() {
        // given
        val testString = ""
        val expectedEncoded = ""

        // when
        val actualEncoded: String = Base32.encodeToString(testString.encodeToByteArray())
        val actualDecoded = Base32.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase32DecodeInvalidBase32() {
        // given
        val testString = "This is not valid Base32"

        // when
        val result = runCatching { Base32.decode(testString) }

        // then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidInputException.InvalidCharacter)
    }

    @Test
    fun testBase32RFC4328TestVector() {
        // given
        val testVector = mapOf(
            "" to "",
            "f" to "MY======",
            "fo" to "MZXQ====",
            "foo" to "MZXW6===",
            "foob" to "MZXW6YQ=",
            "fooba" to "MZXW6YTB",
            "foobar" to "MZXW6YTBOI======"
        )

        for (entry in testVector) {

            // when
            val actualEncoded: String = Base32.encodeToString(entry.key.encodeToByteArray())
            val actualDecoded = Base32.decodeToString(actualEncoded)

            // then
            assertEquals(entry.value, actualEncoded)
            assertEquals(entry.key, actualDecoded)
        }
    }
    @Test
    fun testBase32HexRFC4328TestVector() {
        // given
        val testVector = mapOf(
            "" to "",
            "f" to "CO======",
            "fo" to "CPNG====",
            "foo" to "CPNMU===",
            "foob" to "CPNMUOG=",
            "fooba" to "CPNMUOJ1",
            "foobar" to "CPNMUOJ1E8======",
        )

        for (entry in testVector) {

            // when
            val actualEncoded: String = Base32.getHexEncoder().encodeToString(entry.key.encodeToByteArray())
            val actualDecoded = Base32.getHexDecoder().decodeToString(actualEncoded)

            // then
            assertEquals(entry.value, actualEncoded)
            assertEquals(entry.key, actualDecoded)
        }
    }
}