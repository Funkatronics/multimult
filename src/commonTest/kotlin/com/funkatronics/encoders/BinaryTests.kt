package com.funkatronics.encoders

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BinaryTests {
    
    @Test
    fun testBase2EncodeDecode() {
        // given
        val testString: String = "yes mani !"
        val expectedEncoded = "01111001011001010111001100100000011011010110000101101110011010010010000000100001"

        // when
        val actualEncoded: String = Binary.encodeToString(testString.toByteArray())
        val actualDecoded = Binary.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase2EncodeDecodeLeadingZeros() {
        // given
        val leadingZeros = 2
        val testString: String = String(ByteArray(leadingZeros)) + "yes mani !"
        val expectedEncoded = "000000000000000001111001011001010111001100100000011011010110000101101110011010010010000000100001"

        // when
        val actualEncoded: String = Binary.encodeToString(testString.toByteArray())
        val actualDecoded = Binary.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase2EncodeDecodeEmptyString() {
        // given
        val testString = ""
        val expectedEncoded = ""

        // when
        val actualEncoded: String = Binary.encodeToString(testString.toByteArray())
        val actualDecoded = Binary.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase2DecodeInvalidBase2() {
        // given
        val testString = "This is not valid Base2"

        // when
        val result = runCatching { Binary.decode(testString) }

        // then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is NumberFormatException)
    }
}