package com.funkatronics.encoders

import com.funkatronics.encoders.error.InvalidInputException
import com.funkatronics.multibase.MultiBase
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Base64Tests {

    @Test
    fun testBase64EncodeDecode() {
        // given
        val testString: String = "man"
        val expectedEncoded = "bWFu"

        // when
        val actualEncoded: String = Base64.encodeToString(testString.toByteArray())
        val actualDecoded = Base64.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase64EncodeDecode2() {
        // given
        val testString: String = "Hello!"
        val expectedEncoded = "SGVsbG8h"

        // when
        val actualEncoded: String = Base64.encodeToString(testString.toByteArray())
        val actualDecoded = Base64.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase64EncodeDecodeLeadingZeros() {
        // given
        val leadingZeros = 3
        val testString: String = String(ByteArray(leadingZeros)) + "Hello!"
        val expectedEncoded = "AAAASGVsbG8h"

        // when
        val actualEncoded: String = Base64.encodeToString(testString.toByteArray())
        val actualDecoded = Base64.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase64EncodeDecodeEmptyString() {
        // given
        val testString = ""
        val expectedEncoded = ""

        // when
        val actualEncoded: String = Base64.encodeToString(testString.toByteArray())
        val actualDecoded = Base64.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase64DecodeInvalidBase64() {
        // given
        val testString = "This is not valid Base64"

        // when
        val result = runCatching { Base64.decode(testString) }

        // then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is InvalidInputException.InvalidCharacter)
    }

    @Test
    fun testBase64RFC4648TestVector() {
        // given
        val testVector = mapOf(
            "" to "",
            "f" to "Zg==",
            "fo" to "Zm8=",
            "foo" to "Zm9v",
            "foob" to "Zm9vYg==",
            "fooba" to "Zm9vYmE=",
            "foobar" to "Zm9vYmFy"
        )

        for (entry in testVector) {

            // when
            val actualEncoded: String = Base64.encodeToString(entry.key.toByteArray())
            val actualDecoded = Base64.decodeToString(actualEncoded)

            // then
            assertEquals(entry.value, actualEncoded)
            assertEquals(entry.key, actualDecoded)
        }
    }

//    @Test
//    fun perfTestEncode() {
//
//        val message = "A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test"
//        val encoded = Base2N.encode("01", 2, message.toByteArray())
//        //Base2N.encode(Base64Encoder.ALPHABET, 64, "A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test".toByteArray())
//        //Base64.encodeToString("A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test".toByteArray())
//
//        simpleMeasureTest {
//            Base2.encode(message.toByteArray())
////            Base2.decode(encoded)
////            Base2N.decode(Base64Encoder.ALPHABET, 64, encoded)
//        }
//
//        simpleMeasureTest {
////            Base64.encodeToString("A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test".toByteArray())
////            MultiBase.encode(MultiBase.Base.BASE2, "A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test".toByteArray())
//            Base2N.encode("01", 2, message.toByteArray())
////            Base2N.encode(Base64Encoder.ALPHABET, 64, "A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test".toByteArray())
////            Base64.decode(encoded)
////            Base2N.decode("01", 2, encoded)
//        }
//
//        simpleMeasureTest {
//            BaseN.encodeBaseNNew("01", message.toByteArray())
//        }
//
//        simpleMeasureTest {
//            BaseN.encodeNewNew("01", message.toByteArray())
//        }
//    }

//    @Test
//    fun perfTestNew() {
//        val input = "A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test"
//        val encoded = BaseN.encodeNewNew(MultiBase.Base64.alphabet, input.toByteArray())
//        simpleMeasureTest {
////            BaseN.encodeNewNew(MultiBase.Base58Btc.alphabet, input.toByteArray())
////            BaseN.encodeNewNew(MultiBase.Base64.alphabet, input.toByteArray())
////            BaseN.encodeNewNew(MultiBase.Base36.alphabet, input.toByteArray())
////            BaseN.decodeFast(MultiBase.Base64.alphabet, 58, encoded)
////            BaseN.encodeNewNew(MultiBase.Base64.alphabet, input.toByteArray())
////            BaseN.encodeNewNew(MultiBase.Base32.alphabet, input.toByteArray())
////            BaseN.encodeNewNew(MultiBase.Base58Btc.alphabet, input.toByteArray())
////            BaseN.decodeFast(MultiBase.Base64.alphabet, 58, encoded)
////            BaseN.decodeBaseNCanonical(MultiBase.Base2.alphabet, 2, Base2.encodeToString(input.toByteArray()))
////            Base2N.encode(MultiBase.Base32.alphabet, 32, input.toByteArray())
//            Base2N.encode(MultiBase.Base64.alphabet, 64, input.toByteArray())
//        }
//
//        simpleMeasureTest {
////            BaseN.encodeBaseNNew(MultiBase.Base58Btc.alphabet, input.toByteArray())
////            BaseN.encodeBaseNNew(MultiBase.Base64.alphabet, input.toByteArray())
////            BaseN.encodeBaseNNew(MultiBase.Base36.alphabet, input.toByteArray())
////            BaseN.decodeSlow(MultiBase.Base64.alphabet, 58, encoded)
////            BaseN.encodeBase2N(MultiBase.Base64.alphabet, input.toByteArray())
////            BaseN.encodeBase2N(MultiBase.Base32.alphabet, input.toByteArray())
////            BaseN.encodeBaseN(MultiBase.Base58Btc.alphabet, input.toByteArray())
////            BaseN.decodeBase2N(MultiBase.Base64.alphabet, 58, encoded)
////            BaseN.encodeBaseN(MultiBase.Base2.alphabet, input.toByteArray())
////            Base2.decode(Base2.encodeToString(input.toByteArray()))
////            BaseN.encode(MultiBase.Base32.alphabet, input.toByteArray())
//            BaseN.encode(MultiBase.Base64.alphabet, input.toByteArray())
//        }
//    }

    fun simpleMeasureTest(
        ITERATIONS: Int = 5000,
        TEST_COUNT: Int = 10,
        WARM_COUNT: Int = 5,
        callback: ()->Unit
    ) {
        val results = ArrayList<Long>()
        var totalTime = 0L
        var t = 0

        println("$PRINT_REFIX -> go")

        while (++t <= TEST_COUNT + WARM_COUNT) {
            val startTime = System.currentTimeMillis()

            var i = 0
            while (i++ < ITERATIONS)
                callback()

            if (t <= WARM_COUNT) {
                println("$PRINT_REFIX Warming $t of $WARM_COUNT")
                continue
            }

            val time = System.currentTimeMillis() - startTime
            println(PRINT_REFIX+" "+time.toString()+"ms")

            results.add(time)
            totalTime += time
        }

        results.sort()

        val average = totalTime / TEST_COUNT
        val median = results[results.size / 2]

        println("$PRINT_REFIX -> average=${average}ms / median=${median}ms")
    }

    /**
     * Used to filter console messages easily
     */
    private val PRINT_REFIX = "[PERFORMANCE]"
}