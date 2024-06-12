package com.funkatronics.hash

/**
 * Kotlin implementation of SHA-256 hash algorithm.
 * Original Java version at https://github.com/meyfa/java-sha256/blob/master/src/main/java/net/meyfa/sha256/Sha256.java
 *
 * @author Funkatronics
 */
object Sha256 {
    private val K = intArrayOf(
        0x428a2f98, 0x71374491, -0x4a3f0431, -0x164a245b, 0x3956c25b, 0x59f111f1, -0x6dc07d5c, -0x54e3a12b,
        -0x27f85568, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, -0x7f214e02, -0x6423f959, -0x3e640e8c,
        -0x1b64963f, -0x1041b87a, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
        -0x67c1aeae, -0x57ce3993, -0x4ffcd838, -0x40a68039, -0x391ff40d, -0x2a586eb9, 0x06ca6351, 0x14292967,
        0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, -0x7e3d36d2, -0x6d8dd37b,
        -0x5d40175f, -0x57e599b5, -0x3db47490, -0x3893ae5d, -0x2e6d17e7, -0x2966f9dc, -0xbf1ca7b, 0x106aa070,
        0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
        0x748f82ee, 0x78a5636f, -0x7b3787ec, -0x7338fdf8, -0x6f410006, -0x5baf9315, -0x41065c09, -0x398e870e
    )

    private val H0 = intArrayOf(
        0x6a09e667, -0x4498517b, 0x3c6ef372, -0x5ab00ac6, 0x510e527f, -0x64fa9774, 0x1f83d9ab, 0x5be0cd19
    )

    private const val BLOCK_BITS = 512
    private const val BLOCK_BYTES = BLOCK_BITS / 8

    /**
     * Hashes the given message with SHA-256 and returns the hash.
     *
     * @param message The bytes to hash.
     * @return The hash's bytes.
     */
    fun hash(message: ByteArray): ByteArray {
        // working arrays
        val W = IntArray(64)
        val H = H0.copyOf()
        val TEMP = IntArray(8)

        // initialize all words
        val words = pad(message)

        // enumerate all blocks (each containing 16 words)
        (0 until words.size/16).forEach { i ->
            // initialize W from the block's words
            words.copyInto(W, 0, i*16, i*16 + 16)
            (16 until W.size).forEach { t ->
                W[t] = smallSig1(W[t - 2]) + W[t - 7] + smallSig0(W[t - 15]) + W[t - 16]
            }

            // let TEMP = H
            H.copyInto(TEMP)

            // operate on TEMP
            W.indices.forEach { t ->
                val t1 = TEMP[7] + bigSig1(TEMP[4]) + ch(TEMP[4], TEMP[5], TEMP[6]) + K[t] + W[t]
                val t2 = bigSig0(TEMP[0]) + maj(TEMP[0], TEMP[1], TEMP[2])
                TEMP.copyInto(TEMP, 1, 0, TEMP.size - 1)
                TEMP[4] += t1
                TEMP[0] = t1 + t2
            }

            // add values in TEMP to values in H
            H.indices.forEach { t ->
                H[t] += TEMP[t]
            }
        }

        return H.toByteArray()
    }

    /**
     * **Internal method, no need to call.** Pads the given message to have a length
     * that is a multiple of 512 bits (64 bytes), including the addition of a
     * 1-bit, k 0-bits, and the message length as a 64-bit integer.
     * The result is a 32-bit integer array with big-endian byte representation.
     *
     * @param message The message to pad.
     * @return A new array with the padded message bytes.
     */
    internal fun pad(message: ByteArray): IntArray {
        // new message length: original + 1-bit and padding + 8-byte length
        // --> block count: whole blocks + (padding + length rounded up)
        val finalBlockLength = message.size % BLOCK_BYTES
        val blockCount = message.size/BLOCK_BYTES + if (finalBlockLength + 1 + 8 > BLOCK_BYTES) 2 else 1

        val result = IntArray(blockCount*(BLOCK_BYTES/Int.SIZE_BYTES))

        // copy message and append 1 bit
        (message + 128.toByte()).forEachIndexed { i, b ->
            result[i/Int.SIZE_BYTES] = result[i/Int.SIZE_BYTES] or
                    ((b.toInt() and 0xFF) shl 8 * (Int.SIZE_BYTES - 1 - i%Int.SIZE_BYTES))
        }

        // place original message length as 64-bit integer at the end
        val msgLength = message.size * 8L
        result[result.size - 2] = (msgLength ushr 32).toInt()
        result[result.size - 1] = msgLength.toInt()

        return result
    }

    private fun IntArray.toByteArray(): ByteArray =
        ByteArray(size*Int.SIZE_BYTES) { i ->
            (this[i/Int.SIZE_BYTES] shr (8*(Int.SIZE_BYTES - 1 - i%Int.SIZE_BYTES))).toByte()
        }

    private fun ch(x: Int, y: Int, z: Int): Int = (x and y) or (x.inv() and z)

    private fun maj(x: Int, y: Int, z: Int): Int = (x and y) or (x and z) or (y and z)

    private fun bigSig0(x: Int): Int =
        (x rotateRight 2) xor (x rotateRight 13) xor (x rotateRight 22)

    private fun bigSig1(x: Int): Int =
        (x rotateRight 6) xor (x rotateRight 11) xor (x rotateRight 25)

    private fun smallSig0(x: Int): Int =
        (x rotateRight 7) xor (x rotateRight 18) xor (x ushr 3)

    private fun smallSig1(x: Int): Int =
        (x rotateRight 17) xor (x rotateRight 19) xor (x ushr 10)

    private infix fun Int.rotateRight(distance: Int): Int =
        this.ushr(distance) or (this shl -distance)
}