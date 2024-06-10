package com.funkatronics.encoders

import com.funkatronics.encoders.error.InvalidInputException
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Base64Tests {

    @Test
    fun testBase64EncodeDecode() {
        // given
        val testString: String = "man"
        val expectedEncoded = "bWFu"

        // when
        val actualEncoded: String = Base64.encodeToString(testString.encodeToByteArray())
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
        val actualEncoded: String = Base64.encodeToString(testString.encodeToByteArray())
        val actualDecoded = Base64.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase64EncodeDecodeLeadingZeros() {
        // given
        val leadingZeros = 3
        val testString: String = ByteArray(leadingZeros).decodeToString() + "Hello!"
        val expectedEncoded = "AAAASGVsbG8h"

        // when
        val actualEncoded: String = Base64.encodeToString(testString.encodeToByteArray())
        val actualDecoded = Base64.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase64EncodeDecodeAllZeros() {
        // given
        val length = 10
        val testString: String = ByteArray(length).decodeToString()
        val expectedEncoded = "AAAAAAAAAAAAAA=="

        // when
        val actualEncoded: String = Base64.encodeToString(testString.encodeToByteArray())
        val actualDecoded = Base64.decodeToString(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase64EncodeDecodeLeadingSlashes() {
        // given
        val leadingSlashes = 3
        val testBytes: ByteArray = ByteArray(leadingSlashes) { -1 } + ("Hello!".encodeToByteArray())
        val expectedEncoded = "////SGVsbG8h"

        // when
        val actualEncoded: String = Base64.encodeToString(testBytes)
        val actualDecoded = Base64.decode(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertContentEquals(testBytes, actualDecoded)
    }

    @Test
    fun testBase64EncodeDecodeAllSlashes() {
        // given
        val length = 9
        val testBytes: ByteArray = ByteArray(length) { -1 }
        val expectedEncoded = "////////////"

        // when
        val actualEncoded: String = Base64.encodeToString(testBytes)
        val actualDecoded = Base64.decode(actualEncoded)

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertContentEquals(testBytes, actualDecoded)
    }

    @Test
    fun testBase64EncodeDecode64ByteSignature() {
        // given
        val base58 = "5v73EHnNSKbhDWBbUVXiGRwnwJC6MBrkGJCqHzv8c4aiM5rCK9EL7LKNMzL7Fcsn7gZU4yxoe29i2Ukz2tsQMefi"
        val testBytes: ByteArray = Base58.decode(base58)
        val expectedEncoded = "9dzrJOzVyp2uF1rK21F6zWwNPF73JToq3cwQw5eeB8LFwbiX6tRNFAVKcICtz6IOAkCS+CHLv37fYBclnITkCQ=="

        // when
        val actualEncoded: String = Base64.encodeToString(testBytes)
        val actualDecoded = Base64.decode(actualEncoded)

        // then
        assertEquals(64, actualDecoded.size)
        assertEquals(base58, Base58.encodeToString(actualDecoded))
        assertEquals(expectedEncoded, actualEncoded)
        assertContentEquals(testBytes, actualDecoded)
    }

    @Test
    fun testBase64EncodeDecodeEmptyString() {
        // given
        val testString = ""
        val expectedEncoded = ""

        // when
        val actualEncoded: String = Base64.encodeToString(testString.encodeToByteArray())
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
            val actualEncoded: String = Base64.encodeToString(entry.key.encodeToByteArray())
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
//        val encoded = Base2N.encode("01", 2, message.encodeToByteArray())
//        //Base2N.encode(Base64Encoder.ALPHABET, 64, "A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test".encodeToByteArray())
//        //Base64.encodeToString("A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test".encodeToByteArray())
//
//        simpleMeasureTest {
//            Base2.encode(message.encodeToByteArray())
////            Base2.decode(encoded)
////            Base2N.decode(Base64Encoder.ALPHABET, 64, encoded)
//        }
//
//        simpleMeasureTest {
////            Base64.encodeToString("A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test".encodeToByteArray())
////            MultiBase.encode(MultiBase.Base.BASE2, "A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test".encodeToByteArray())
//            Base2N.encode("01", 2, message.encodeToByteArray())
////            Base2N.encode(Base64Encoder.ALPHABET, 64, "A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test".encodeToByteArray())
////            Base64.decode(encoded)
////            Base2N.decode("01", 2, encoded)
//        }
//
//        simpleMeasureTest {
//            BaseN.encodeBaseNNew("01", message.encodeToByteArray())
//        }
//
//        simpleMeasureTest {
//            BaseN.encodeNewNew("01", message.encodeToByteArray())
//        }
//    }

//    @Test
//    fun perfTestNew() {
//        val input = "A very very long string to simulate a very very long encode. ig guess its not that long but oh well it should be an okay test"
//        val encoded = BaseN.encodeNewNew(MultiBase.Base64.alphabet, input.encodeToByteArray())
//        simpleMeasureTest {
////            BaseN.encodeNewNew(MultiBase.Base58Btc.alphabet, input.encodeToByteArray())
////            BaseN.encodeNewNew(MultiBase.Base64.alphabet, input.encodeToByteArray())
////            BaseN.encodeNewNew(MultiBase.Base36.alphabet, input.encodeToByteArray())
////            BaseN.decodeFast(MultiBase.Base64.alphabet, 58, encoded)
////            BaseN.encodeNewNew(MultiBase.Base64.alphabet, input.encodeToByteArray())
////            BaseN.encodeNewNew(MultiBase.Base32.alphabet, input.encodeToByteArray())
////            BaseN.encodeNewNew(MultiBase.Base58Btc.alphabet, input.encodeToByteArray())
////            BaseN.decodeFast(MultiBase.Base64.alphabet, 58, encoded)
////            BaseN.decodeBaseNCanonical(MultiBase.Base2.alphabet, 2, Base2.encodeToString(input.encodeToByteArray()))
////            Base2N.encode(MultiBase.Base32.alphabet, 32, input.encodeToByteArray())
//            Base2N.encode(MultiBase.Base64.alphabet, 64, input.encodeToByteArray())
//        }
//
//        simpleMeasureTest {
////            BaseN.encodeBaseNNew(MultiBase.Base58Btc.alphabet, input.encodeToByteArray())
////            BaseN.encodeBaseNNew(MultiBase.Base64.alphabet, input.encodeToByteArray())
////            BaseN.encodeBaseNNew(MultiBase.Base36.alphabet, input.encodeToByteArray())
////            BaseN.decodeSlow(MultiBase.Base64.alphabet, 58, encoded)
////            BaseN.encodeBase2N(MultiBase.Base64.alphabet, input.encodeToByteArray())
////            BaseN.encodeBase2N(MultiBase.Base32.alphabet, input.encodeToByteArray())
////            BaseN.encodeBaseN(MultiBase.Base58Btc.alphabet, input.encodeToByteArray())
////            BaseN.decodeBase2N(MultiBase.Base64.alphabet, 58, encoded)
////            BaseN.encodeBaseN(MultiBase.Base2.alphabet, input.encodeToByteArray())
////            Base2.decode(Base2.encodeToString(input.encodeToByteArray()))
////            BaseN.encode(MultiBase.Base32.alphabet, input.encodeToByteArray())
//            BaseN.encode(MultiBase.Base64.alphabet, input.encodeToByteArray())
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
            val startTime = Clock.System.now().toEpochMilliseconds()

            var i = 0
            while (i++ < ITERATIONS)
                callback()

            if (t <= WARM_COUNT) {
                println("$PRINT_REFIX Warming $t of $WARM_COUNT")
                continue
            }

            val time = Clock.System.now().toEpochMilliseconds() - startTime
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