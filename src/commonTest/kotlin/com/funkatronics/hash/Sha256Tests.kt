package com.funkatronics.hash

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class Sha256Tests {

    @Test
    fun `empty hash`() {
        // given
        val message = ByteArray(0)
        val expectedHash: ByteArray =
            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855".decodeHex()

        // when
        val actualHash = Sha256.hash(message)

        // then
        assertContentEquals(expectedHash, actualHash)
    }

    @Test
    fun `RFC test vector 24 bit`() {
        // given
        val message = "abc".encodeToByteArray()
        val expectedHash: ByteArray =
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad".decodeHex()

        // when
        val actualHash = Sha256.hash(message)

        // then
        assertContentEquals(expectedHash, actualHash)
    }

    @Test
    fun `RFC test vector 448 bit`() {
        // given
        val message = "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".encodeToByteArray()
        val expectedHash: ByteArray =
            "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1".decodeHex()

        // when
        val actualHash = Sha256.hash(message)

        // then
        assertContentEquals(expectedHash, actualHash)
    }

    @Test
    fun `RFC test vector 896 bit`() {
        // given
        val message = ("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmn" +
                "hijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu").encodeToByteArray()
        val expectedHash: ByteArray =
            "cf5b16a778af8380036ce59e7b0492370b249b11e8f07a51afac45037afee9d1".decodeHex()

        // when
        val actualHash = Sha256.hash(message)

        // then
        assertContentEquals(expectedHash, actualHash)
    }

    @Test
    fun `RFC test vector 1 million repetitions of character 'a'`() {
        // given
        val message = ByteArray(1000000) { 'a'.code.toByte() }
        val expectedHash: ByteArray =
            "cdc76e5c9914fb9281a1c7e284d73e67f1809a48a497200e046d39ccc7112cd0".decodeHex()

        // when
        val actualHash = Sha256.hash(message)

        // then
        assertContentEquals(expectedHash, actualHash)
    }

    @Test
    fun `hello world hash`() {
        // given
        val message = "Hello world!".encodeToByteArray()
        val expectedHash: ByteArray =
            "c0535e4be2b79ffd93291305436bf889314e4a3faec05ecffcbb7df31ad9e51a".decodeHex()

        // when
        val actualHash = Sha256.hash(message)

        // then
        assertContentEquals(expectedHash, actualHash)
    }

    @Test
    fun `long hash`() {
        // given
        val message = ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
                + "Proin pulvinar turpis purus, sit amet dapibus magna commodo "
                + "quis metus.").encodeToByteArray()
        val expectedHash: ByteArray =
            "60497604d2f6b4df42cea5efb8956f587f81a4ad66fa1b65d9e085224d255036".decodeHex()

        // when
        val actualHash = Sha256.hash(message)

        // then
        assertContentEquals(expectedHash, actualHash)
    }

    @Test
    fun `hash raw bytes`() {
        // given
        val message = ByteArray(256) { it.toByte() }
        val expectedHash: ByteArray =
            "40aff2e9d2d8922e47afd4648e6967497158785fbd1da870e7110266bf944880".decodeHex()

        // when
        val actualHash = Sha256.hash(message)

        // then
        assertContentEquals(expectedHash, actualHash)
    }

    @Test
    fun `hash 55`() {
        // given
        val message = ByteArray(55) { 'a'.code.toByte() }
        val expectedHash: ByteArray =
            "9f4390f8d30c2dd92ec9f095b65e2b9ae9b0a925a5258e241c9f1e910f734318".decodeHex()

        // when
        val actualHash = Sha256.hash(message)

        // then
        assertContentEquals(expectedHash, actualHash)
    }

    @Test
    fun `padded message length divisible by 512`() {
        (0..128).forEach { length ->
            // given
            val b = ByteArray(length)

            // when
            val padded = Sha256.pad(b)
            val paddedLengthBits: Int = padded.size * Int.SIZE_BYTES * 8

            // then
            assertEquals(0, paddedLengthBits % 512)
        }
    }

    @Test
    fun `padded message has 1 bit`() {
        // given
        val message = ByteArray(64)
        val expectedInt = 0b1000_0000_0000_0000_0000_0000_0000_0000.toInt()

        // when
        val padded = Sha256.pad(message)

        // then
        assertEquals(expectedInt, padded[message.size / Int.SIZE_BYTES])
    }

    @Test
    fun `pad message length and original message size`() {
        // given
        val message = "hello".encodeToByteArray()
        val originalMessageSizeBits = message.size*8

        // when
        val padded = Sha256.pad(message)

        // then
        assertEquals(0, (padded.size * Int.SIZE_BYTES * 8) % 512)
        assertEquals(originalMessageSizeBits, padded[padded.size - 2] or padded.last())
    }

    private fun String.decodeHex(): ByteArray =
        chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}