package com.funkatronics.multibase

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MultiBaseTests {

    val sampleString = "Decentralize everything!!!"

    var encodedSamples = mapOf(
        MultiBase.Base16 to "f446563656e7472616c697a652065766572797468696e67212121",
        MultiBase.Base16Upper to "F446563656E7472616C697A652065766572797468696E67212121",
        MultiBase.Base32 to "birswgzloorzgc3djpjssazlwmvzhs5dinfxgoijbee",
        MultiBase.Base32Upper to "BIRSWGZLOORZGC3DJPJSSAZLWMVZHS5DINFXGOIJBEE",
        MultiBase.Base32Pad to "cirswgzloorzgc3djpjssazlwmvzhs5dinfxgoijbee======",
        MultiBase.Base32UpperPad to "CIRSWGZLOORZGC3DJPJSSAZLWMVZHS5DINFXGOIJBEE======",
        MultiBase.Base32Hex to "v8him6pbeehp62r39f9ii0pbmclp7it38d5n6e89144",
        MultiBase.Base32HexUpper to "V8HIM6PBEEHP62R39F9II0PBMCLP7IT38D5N6E89144",
        MultiBase.Base32HexPad to "t8him6pbeehp62r39f9ii0pbmclp7it38d5n6e89144======",
        MultiBase.Base32HexUpperPad to "T8HIM6PBEEHP62R39F9II0PBMCLP7IT38D5N6E89144======",
        MultiBase.Base32Z to "het1sg3mqqt3gn5djxj11y3msci3817depfzgqejbrr",
        MultiBase.Base36 to "km552ng4dabi4neu1oo8l4i5mndwmpc3mkukwtxy9",
        MultiBase.Base36Upper to "KM552NG4DABI4NEU1OO8L4I5MNDWMPC3MKUKWTXY9",
        MultiBase.Base58Flickr to "Z36tpRGiQ9Endr7dHahm9xwQdhmoER4emaRVT",
        MultiBase.Base58Btc to "z36UQrhJq9fNDS7DiAHM9YXqDHMPfr4EMArvt",
        MultiBase.Base64 to "mRGVjZW50cmFsaXplIGV2ZXJ5dGhpbmchISE",
        MultiBase.Base64Url to "uRGVjZW50cmFsaXplIGV2ZXJ5dGhpbmchISE",
        MultiBase.Base64Pad to "MRGVjZW50cmFsaXplIGV2ZXJ5dGhpbmchISE=",
        MultiBase.Base64UrlPad to "URGVjZW50cmFsaXplIGV2ZXJ5dGhpbmchISE="
    )

    @Test
    fun testBase() {
        encodedSamples.forEach { base, input ->
            val origin = MultiBase.decode(input)
            val encode = MultiBase.encode(base, origin)
            assertEquals(input, encode)
        }
    }

    @Test
    fun testBase2() {
        // given
        val testString: String = "yes mani !"
        val expectedEncoded = "001111001011001010111001100100000011011010110000101101110011010010010000000100001"

        // when
        val actualEncoded: String = MultiBase.Base2.encode(testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase8() {
        // given
        val testString: String = "yes mani !"
        val expectedEncoded = "7362625631006654133464440102"

        // when
        val actualEncoded: String = MultiBase.encode(MultiBase.Base8, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase10() {
        // given
        val testString: String = "yes mani !"
        val expectedEncoded = "9573277761329450583662625"

        // when
        val actualEncoded: String = MultiBase.encode(MultiBase.Base10, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase32rq() {
        // given
        val testString: String = "yes mani !"
        val expectedEncoded = "bpfsxgidnmfxgsibb"

        // when
        val actualEncoded: String = MultiBase.encode(MultiBase.Base32, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase32zrq() {
        // given
        val testString: String = "yes mani !"
        val expectedEncoded = "hxf1zgedpcfzg1ebb"

        // when
        val actualEncoded: String = MultiBase.encode(MultiBase.Base32Z, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase64rq() {
        // given
        val testString: String = "yes mani !"
        val expectedEncoded = "meWVzIG1hbmkgIQ"

        // when
        val actualEncoded: String = MultiBase.encode(MultiBase.Base64, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase64Padrq() {
        // given
        val testString: String = "yes mani !"
        val expectedEncoded = "MeWVzIG1hbmkgIQ=="

        // when
        val actualEncoded: String = MultiBase.encode(MultiBase.Base64Pad, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun testBase16() = runBaseTest(MultiBase.Base16)

    @Test
    fun testBase16Upper() = runBaseTest(MultiBase.Base16Upper)

    @Test
    fun testBase32() = runBaseTest(MultiBase.Base32)
    @Test
    fun testBase32Upper() = runBaseTest(MultiBase.Base32Upper)
    @Test
    fun testBase32Pad() = runBaseTest(MultiBase.Base32Pad)
    @Test
    fun testBase32PadUpper() = runBaseTest(MultiBase.Base32UpperPad)
    @Test
    fun testBase32Hex() = runBaseTest(MultiBase.Base32Hex)
    @Test
    fun testBase32HexPad() = runBaseTest(MultiBase.Base32HexPad)
    @Test
    fun testBase32HexUpper() = runBaseTest(MultiBase.Base32HexUpper)
    @Test
    fun testBase32HexPadUpper() = runBaseTest(MultiBase.Base32HexUpperPad)
    @Test
    fun testBase32z() = runBaseTest(MultiBase.Base32Z)

    @Test
    fun testBase36() = runBaseTest(MultiBase.Base36)
    @Test
    fun testBase36Upper() = runBaseTest(MultiBase.Base36Upper)

    @Test
    fun testBase58Btc() = runBaseTest(MultiBase.Base58Btc)
    @Test
    fun testBase58Flickr() = runBaseTest(MultiBase.Base58Flickr)

    @Test
    fun testBase64() = runBaseTest(MultiBase.Base64)
    @Test
    fun testBase64Url() = runBaseTest(MultiBase.Base64Url)
    @Test
    fun testBase64Pad() = runBaseTest(MultiBase.Base64Pad)
    @Test
    fun testBase64UrlPad() = runBaseTest(MultiBase.Base64UrlPad)

    @Test
    fun myTest() {
        // given
        val testString: String = "yessir"
        val base = MultiBase.Base32
        val expectedEncoded = "bpfsxg43joi"

        // when
        val actualEncoded: String = MultiBase.encode(base, testString.encodeToByteArray())
        println("++++ Base: $base")
        println("++++ encoded = $actualEncoded")
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        println("++++ decoded = $actualDecoded")
        println("++++ expectd = $expectedEncoded")
        println("++++   test = ${String(MultiBase.decode(expectedEncoded))}")

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun myTest346356() {
        // given
        val testString: String = "man"
        val base = MultiBase.Base32
        val expectedEncoded = "bnvqw4"

        // when
        val actualEncoded: String = MultiBase.encode(base, testString.encodeToByteArray())
        println("++++ Base: $base")
        println("++++ encoded = $actualEncoded")
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        println("++++ decoded = $actualDecoded")
        println("++++ expectd = $expectedEncoded")
        println("++++   test = ${String(MultiBase.decode(expectedEncoded))}")

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun myTest346345() {
        // given
        val testString: String = "a"
        val base = MultiBase.Base32
        val expectedEncoded = "bme"

        // when
        val actualEncoded: String = MultiBase.encode(base, testString.encodeToByteArray())
        println("++++ Base: $base")
        println("++++ encoded = $actualEncoded")
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        println("++++ decoded = $actualDecoded")
        println("++++ expectd = $expectedEncoded")
        println("++++   test = ${String(MultiBase.decode(expectedEncoded))}")

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun myTest2() {
        // given
        val testString: String = "man"
        val base = MultiBase.Base64
        val expectedEncoded = "mbWFu"

        // when
        val actualEncoded: String = MultiBase.encode(base, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        println("++++ Base: $base")
        println("++++ decoded = $actualDecoded")
        println("++++ encoded = $actualEncoded")
        println("++++ expectd = $expectedEncoded")
        println("++++    test = m${Base64.getEncoder().encodeToString(testString.encodeToByteArray())}")
        println("++++   test2 = ${String(MultiBase.decode(expectedEncoded))}")

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun myTest22() {
        // given
        val testString: String = "Hello!"
        val base = MultiBase.Base64
        val expectedEncoded = "mSGVsbG8h"

        // when
        val actualEncoded: String = MultiBase.encode(base, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        println("++++ Base: $base")
        println("++++ decoded = $actualDecoded")
        println("++++ encoded = $actualEncoded")
        println("++++ expectd = $expectedEncoded")
        println("++++    test = m${Base64.getEncoder().encodeToString(testString.encodeToByteArray())}")
        println("++++   test2 = ${String(MultiBase.decode(expectedEncoded))}")

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun myTest222() {
        // given
        val testString: String = "Decentralize everything!!!"
        val base = MultiBase.Base64
        val expectedEncoded = "mRGVjZW50cmFsaXplIGV2ZXJ5dGhpbmchISE"

        // when
        val actualEncoded: String = MultiBase.encode(base, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        println("++++ Base: $base")
        println("++++ decoded = $actualDecoded")
        println("++++ encoded = $actualEncoded")
        println("++++ expectd = $expectedEncoded")
        println("++++    test = m${Base64.getEncoder().encodeToString(testString.encodeToByteArray())}")
        println("++++   test2 = ${String(MultiBase.decode(expectedEncoded))}")

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    @Test
    fun myTest3() {
        // given
        val testString: String = "Hello World!"
        val base = MultiBase.Base58Btc
        val expectedEncoded = "z2NEpo7TZRRrLZSi2U"

        print("+++ test string = ")
        testString.encodeToByteArray().forEach {
            print("${it.toString(2)} ")
        }
        println()

        print("+++     encoded = ")
        "2NEpo7TZRRrLZSi2U".encodeToByteArray().forEach {
            print("${it.toString(2)} ")
        }
        println()

        // SGVsbG8gV29ybGQh
//        println("+++ quick base64 = ${MultiBase.encode(MultiBase.Base.BASE64, testString.encodeToByteArray())}")

        // need 1032 bits/176 bytes per block to make this work.
        // or maybe need 96 bits/17 bytes per block to make this work.
        // http://kvanttt.github.io/BaseNcoding/

        // when
        val actualEncoded: String = MultiBase.encode(base, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        println("++++ Base: $base")
        println("++++ decoded = $actualDecoded")
        println("++++ encoded = $actualEncoded")
        println("++++ expectd = $expectedEncoded")
//        println("++++    test = m${Base64.getEncoder().encodeToString(testString.encodeToByteArray())}")
//        println("++++   test2 = ${String(MultiBase.decode(expectedEncoded))}")

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }

    private fun runBaseTest(base: MultiBase) {
        // given
        val testString: String = sampleString
        val expectedEncoded = encodedSamples[base]
            ?: throw Exception("Test base not provided for base: $base")

        // when
        val actualEncoded: String = MultiBase.encode(base, testString.encodeToByteArray())
        val actualDecoded = String(MultiBase.decode(actualEncoded))

        println("++++ Base: $base")
        println("++++ decoded = $actualDecoded")
        println("++++ encoded = $actualEncoded")
        println("++++ expectd = $expectedEncoded")
//        println("++++    test = _${Base64.getEncoder().encodeToString(testString.encodeToByteArray())}")
        println("++++   test2 = ${String(MultiBase.decode(expectedEncoded))}")

        // then
        assertEquals(expectedEncoded, actualEncoded)
        assertEquals(testString, actualDecoded)
    }
}