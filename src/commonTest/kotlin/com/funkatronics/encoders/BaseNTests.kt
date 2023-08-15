package com.funkatronics.encoders

import com.funkatronics.multibase.MultiBase
import kotlin.test.Test
import kotlin.test.assertEquals

class BaseNTests {

    val sampleString = "yes mani !"

    val encodedSamples = mapOf(
//        "01" to "1111001011001010111001100100000011011010110000101101110011010010010000000100001",
        "01234567" to "362625631006654133464440102",
        "0123456789" to "573277761329450583662625",
        "0123456789abcdef" to "796573206d616e692021",
        "abcdefghijklmnopqrstuvwxyz234567" to "pfsxgidnmfxgsibb",
        "0123456789abcdefghijklmnopqrstuvwxyz" to "2lcpzo5yikidynfl",
        "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz" to "7paNL19xttacUY",
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/" to "eWVzIG1hbmkgIQ",
    )

    val sampleString2 = "Decentralize everything!!!"

    var encodedSamples2 = mapOf(
        MultiBase.Base16 to "446563656e7472616c697a652065766572797468696e67212121",
        MultiBase.Base16Upper to "446563656E7472616C697A652065766572797468696E67212121",
        MultiBase.Base32 to "irswgzloorzgc3djpjssazlwmvzhs5dinfxgoijbee",
        MultiBase.Base32Upper to "IRSWGZLOORZGC3DJPJSSAZLWMVZHS5DINFXGOIJBEE",
        MultiBase.Base32Pad to "irswgzloorzgc3djpjssazlwmvzhs5dinfxgoijbee======",
        MultiBase.Base32UpperPad to "IRSWGZLOORZGC3DJPJSSAZLWMVZHS5DINFXGOIJBEE======",
        MultiBase.Base32Hex to "8him6pbeehp62r39f9ii0pbmclp7it38d5n6e89144",
        MultiBase.Base32HexUpper to "8HIM6PBEEHP62R39F9II0PBMCLP7IT38D5N6E89144",
        MultiBase.Base32HexPad to "8him6pbeehp62r39f9ii0pbmclp7it38d5n6e89144======",
        MultiBase.Base32HexUpperPad to "8HIM6PBEEHP62R39F9II0PBMCLP7IT38D5N6E89144======",
        MultiBase.Base32Z to "et1sg3mqqt3gn5djxj11y3msci3817depfzgqejbrr",
        MultiBase.Base36 to "m552ng4dabi4neu1oo8l4i5mndwmpc3mkukwtxy9",
        MultiBase.Base36Upper to "M552NG4DABI4NEU1OO8L4I5MNDWMPC3MKUKWTXY9",
        MultiBase.Base58Flickr to "36tpRGiQ9Endr7dHahm9xwQdhmoER4emaRVT",
        MultiBase.Base58Btc to "36UQrhJq9fNDS7DiAHM9YXqDHMPfr4EMArvt",
        MultiBase.Base64 to "RGVjZW50cmFsaXplIGV2ZXJ5dGhpbmchISE",
        MultiBase.Base64Url to "RGVjZW50cmFsaXplIGV2ZXJ5dGhpbmchISE",
        MultiBase.Base64Pad to "RGVjZW50cmFsaXplIGV2ZXJ5dGhpbmchISE=",
        MultiBase.Base64UrlPad to "RGVjZW50cmFsaXplIGV2ZXJ5dGhpbmchISE="
    )

    @Test
    fun testBase() {
        encodedSamples2.forEach { base, input ->
            val origin = BaseN.decodeBase2N(base.alphabet.filter { it != '=' }, base.alphabet.filter { it != '=' }.length, input.replace('=', base.alphabet[0]))
            val encode = BaseN.encodeBase2N(base.alphabet.filter { it != '=' }, origin)
            assertEquals(input.filter { it != '=' }, encode)
        }
    }

    @Test
    fun testy() {
        // given
        val testString = sampleString

        // when
        encodedSamples.forEach { (alphabet, expectedEncoding) ->
            val actualEncoding = BaseN.encode(alphabet, testString.toByteArray())
            val actualDecoded = BaseN.decode(alphabet, actualEncoding)

            // then
            assertEquals(expectedEncoding, actualEncoding)
            assertEquals(sampleString, String(actualDecoded))
        }
    }

    @Test
    fun testyy() {
        // given
        val testString = sampleString

        // when
        encodedSamples.forEach { (alphabet, expectedEncoding) ->
            val actualEncoding = BaseN.encodeBase2N(alphabet, testString.toByteArray())
            val actualDecoded = BaseN.decodeBase2N(alphabet, alphabet.length, actualEncoding)

            // then
            assertEquals(expectedEncoding, actualEncoding)
            assertEquals(sampleString, String(actualDecoded), "decode failed: base = ${alphabet.length}")
        }
    }

//    @Test
//    fun testyyy() {
//        // given
//        val testString = sampleString
//
//        // when
//        encodedSamples.forEach { (alphabet, expectedEncoding) ->
//            val actualEncoding = BaseN.encodeBaseN(alphabet, testString.toByteArray())
//            val actualDecoded = BaseN.decodeBaseNCanonical(alphabet, alphabet.length, actualEncoding)
//
//            // then
//            assertEquals(expectedEncoding, actualEncoding)
//            assertEquals(sampleString, String(actualDecoded))
//        }
//    }


//    private fun runBaseTest(base: MultiBase) {
//        // given
//        val testString: String = sampleString
//        val expectedEncoded = encodedSamples[base]
//            ?: throw Exception("Test base not provided for base: $base")
//
//        // when
//        val actualEncoded: String = MultiBase.encode(base, testString.toByteArray())
//        val actualDecoded = String(MultiBase.decode(actualEncoded))
//
//        println("++++ Base: $base")
//        println("++++ decoded = $actualDecoded")
//        println("++++ encoded = $actualEncoded")
//        println("++++ expectd = $expectedEncoded")
////        println("++++    test = _${Base64.getEncoder().encodeToString(testString.toByteArray())}")
//        println("++++   test2 = ${String(MultiBase.decode(expectedEncoded))}")
//
//        // then
//        assertEquals(expectedEncoded, actualEncoded)
//        assertEquals(testString, actualDecoded)
//    }
}