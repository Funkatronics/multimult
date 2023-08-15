package com.funkatronics.extensions

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class ByteArrayExtensionsTests {

    //region BITSHIFTS
    @Test
    fun testByteArrayShiftRight() {
        // given
        val int = 256
        val shift = 2
        val intResult = int shr shift

        val byteArray1 = byteArrayOf(
            ((int shr 8) and 0xff).toByte(),
            (int and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 shr shift

        // then
        assertContentEquals(expectedResult, actualResult)
    }

    @Test
    fun testByteArrayShiftRight2() {
        // given
        val int = 65535
        val shift = 2
        val intResult = int shr shift

        val byteArray1 = byteArrayOf(
            ((int shr 24) and 0xff).toByte(),
            ((int shr 16) and 0xff).toByte(),
            ((int shr 8) and 0xff).toByte(),
            (int and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 24) and 0xff).toByte(),
            ((intResult shr 16) and 0xff).toByte(),
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 shr shift

        // then
        assertContentEquals(expectedResult, actualResult)
    }

    @Test
    fun testByteArrayShiftRightLarge() {
        // given
        val int = 2048
        val shift = 10
        val intResult = int shr shift

        val byteArray1 = byteArrayOf(
            ((int shr 8) and 0xff).toByte(),
            (int and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 shr shift

        // then
        assertContentEquals(expectedResult, actualResult)
    }

    @Test
    fun testByteArrayShiftRightNegative() {
        // given
        val int = -128
        val shift = 4
        val intResult = int shr shift

        val byteArray1 = byteArrayOf(
            ((int shr 8) and 0xff).toByte(),
            (int and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 shr shift

        // then
        assertContentEquals(expectedResult, actualResult)
    }


    @Test
    fun testByteArrayLeft() {
        // given
        val int = 64
        val shift = 2
        val intResult = int shl shift

        val byteArray1 = byteArrayOf(
            ((int shr 8) and 0xff).toByte(),
            (int and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 16) and 0xff).toByte(),
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 shl shift

        // then
        assertContentEquals(expectedResult, actualResult)
    }

    @Test
    fun testByteArrayShiftLeftLarge() {
        // given
        val int = 2048
        val shift = 10
        val intResult = int shl shift

        val byteArray1 = byteArrayOf(
            ((int shr 8) and 0xff).toByte(),
            (int and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 24) and 0xff).toByte(),
            ((intResult shr 16) and 0xff).toByte(),
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 shl shift

        // then
        assertContentEquals(expectedResult, actualResult)
    }

    @Test
    fun testByteArrayShiftLeftNegative() {
        // given
        val int = -128
        val shift = 4
        val intResult = int shl shift

        val byteArray1 = byteArrayOf(
            ((int shr 24) and 0xff).toByte(),
            ((int shr 16) and 0xff).toByte(),
            ((int shr 8) and 0xff).toByte(),
            (int and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 24) and 0xff).toByte(),
            ((intResult shr 16) and 0xff).toByte(),
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 shl shift

        // then
        assertContentEquals(expectedResult, actualResult)
    }
    //endregion
    //endregion

    //region ADD
    @Test
    fun testByteArrayAdd() {
        // given
        val int1 = 1234
        val int2 = 5678
        val intResult = int1 + int2

        val byteArray1 = byteArrayOf(
            ((int1 shr 8) and 0xff).toByte(),
            (int1 and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((int2 shr 8) and 0xff).toByte(),
            (int2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 plus byteArray2

        // then
        assertContentEquals(expectedResult, actualResult.takeLast(4).toByteArray())
    }

    @Test
    fun testByteArrayAddNegative() {
        // given
        val int1 = 21
        val int2 = -78
        val intResult = int1 + int2

        val byteArray1 = byteArrayOf(
            ((int1 shr 24) and 0xff).toByte(),
            ((int1 shr 16) and 0xff).toByte(),
            ((int1 shr 8) and 0xff).toByte(),
            (int1 and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((int2 shr 24) and 0xff).toByte(),
            ((int2 shr 16) and 0xff).toByte(),
            ((int2 shr 8) and 0xff).toByte(),
            (int2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 24) and 0xff).toByte(),
            ((intResult shr 16) and 0xff).toByte(),
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 plus byteArray2

        // then
        assertContentEquals(expectedResult, actualResult.takeLast(4).toByteArray())
    }

    @Test
    fun testByteArrayAddNegatives() {
        // given
        val int1 = -356
        val int2 = -23548
        val intResult = int1 + int2

        val byteArray1 = byteArrayOf(
            ((int1 shr 24) and 0xff).toByte(),
            ((int1 shr 16) and 0xff).toByte(),
            ((int1 shr 8) and 0xff).toByte(),
            (int1 and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((int2 shr 24) and 0xff).toByte(),
            ((int2 shr 16) and 0xff).toByte(),
            ((int2 shr 8) and 0xff).toByte(),
            (int2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 24) and 0xff).toByte(),
            ((intResult shr 16) and 0xff).toByte(),
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 plus byteArray2

        // then
        assertContentEquals(expectedResult, actualResult.takeLast(4).toByteArray())
    }

    @Test
    fun testByteArrayAddNegativeReturnsNegative() {
        // given
        val int1 = 1
        val int2 = -2
        val intResult = int1 + int2

        val byteArray1 = byteArrayOf(
            ((int1 shr 24) and 0xff).toByte(),
            ((int1 shr 16) and 0xff).toByte(),
            ((int1 shr 8) and 0xff).toByte(),
            (int1 and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((int2 shr 24) and 0xff).toByte(),
            ((int2 shr 16) and 0xff).toByte(),
            ((int2 shr 8) and 0xff).toByte(),
            (int2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 24) and 0xff).toByte(),
            ((intResult shr 16) and 0xff).toByte(),
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 plus byteArray2

        // then
        assertContentEquals(expectedResult, actualResult)
    }

    @Test
    fun testByteArrayAddDifferentLengths() {
        // given
        val short = 12
        val int = 3456
        val intResult = short + int

        val byteArray1 = byteArrayOf(
            ((short shr 8) and 0xff).toByte(),
            (short and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((int shr 24) and 0xff).toByte(),
            ((int shr 16) and 0xff).toByte(),
            ((int shr 8) and 0xff).toByte(),
            (int and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 24) and 0xff).toByte(),
            ((intResult shr 16) and 0xff).toByte(),
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 plus byteArray2

        // then
        assertContentEquals(expectedResult, actualResult)
    }

    @Test
    fun testByteArrayAddLong() {
        // given
        val long1: Long = 123456789
        val long2: Long = 987654321
        val longResult = long1 + long2

        val byteArray1 = byteArrayOf(
            ((long1 shr 56) and 0xff).toByte(),
            ((long1 shr 48) and 0xff).toByte(),
            ((long1 shr 40) and 0xff).toByte(),
            ((long1 shr 32) and 0xff).toByte(),
            ((long1 shr 24) and 0xff).toByte(),
            ((long1 shr 16) and 0xff).toByte(),
            ((long1 shr 8) and 0xff).toByte(),
            (long1 and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((long2 shr 56) and 0xff).toByte(),
            ((long2 shr 48) and 0xff).toByte(),
            ((long2 shr 40) and 0xff).toByte(),
            ((long2 shr 32) and 0xff).toByte(),
            ((long2 shr 24) and 0xff).toByte(),
            ((long2 shr 16) and 0xff).toByte(),
            ((long2 shr 8) and 0xff).toByte(),
            (long2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((longResult shr 56) and 0xff).toByte(),
            ((longResult shr 48) and 0xff).toByte(),
            ((longResult shr 40) and 0xff).toByte(),
            ((longResult shr 32) and 0xff).toByte(),
            ((longResult shr 24) and 0xff).toByte(),
            ((longResult shr 16) and 0xff).toByte(),
            ((longResult shr 8) and 0xff).toByte(),
            (longResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 plus byteArray2

        // then
        assertContentEquals(expectedResult, actualResult.takeLast(8).toByteArray())
    }

    @Test
    fun testByteArrayAddWithRollover() {
        // given
        val value = 128
        val result: Long = value.toLong()*2

        val byteArray1 = byteArrayOf(
            ((value shr 24) and 0xff).toByte(),
            ((value shr 16) and 0xff).toByte(),
            ((value shr 8) and 0xff).toByte(),
            (value and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((result ushr 24) and 0xff).toByte(),
            ((result ushr 16) and 0xff).toByte(),
            ((result ushr 8) and 0xff).toByte(),
            (result and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 plus byteArray1

        // then
        assertContentEquals(expectedResult, actualResult.takeLast(8).toByteArray())
    }

    @Test
    fun testByteArrayAddNegativeWithRollover() {
        // given
        val highInt = Int.MIN_VALUE
        val longResult: Long = highInt.toLong()*2

        val byteArray1 = byteArrayOf(
            ((highInt shr 24) and 0xff).toByte(),
            ((highInt shr 16) and 0xff).toByte(),
            ((highInt shr 8) and 0xff).toByte(),
            (highInt and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(1)

        val expectedResult = byteArrayOf(
            ((longResult ushr 32) and 0xff).toByte(),
            ((longResult ushr 24) and 0xff).toByte(),
            ((longResult ushr 16) and 0xff).toByte(),
            ((longResult ushr 8) and 0xff).toByte(),
            (longResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 plus byteArray1 //add byteArray1

        // then
        assertContentEquals(expectedResult, actualResult.takeLast(8).toByteArray())
    }
    //endregion

    //region MULTIPLY
    @Test
    fun testByteArrayMultiply() {
        // given
        val int1 = 12345
        val int2 = 67890
        val intResult = int1 * int2

        val byteArray1 = byteArrayOf(
            ((int1 shr 24) and 0xff).toByte(),
            ((int1 shr 16) and 0xff).toByte(),
            ((int1 shr 8) and 0xff).toByte(),
            (int1 and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((int2 shr 24) and 0xff).toByte(),
            ((int2 shr 16) and 0xff).toByte(),
            ((int2 shr 8) and 0xff).toByte(),
            (int2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 24) and 0xff).toByte(),
            ((intResult shr 16) and 0xff).toByte(),
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 times byteArray2

        // then
        assertContentEquals(expectedResult, actualResult.takeLast(4).toByteArray())
    }

    @Test
    fun testByteArrayMultiplyNegative() {
        // given
        val long1: Long = 21
        val long2: Long = -79
        val longResult = long1 * long2

        val byteArray1 = byteArrayOf(
            ((long1 shr 56) and 0xff).toByte(),
            ((long1 shr 48) and 0xff).toByte(),
            ((long1 shr 40) and 0xff).toByte(),
            ((long1 shr 32) and 0xff).toByte(),
            ((long1 shr 24) and 0xff).toByte(),
            ((long1 shr 16) and 0xff).toByte(),
            ((long1 shr 8) and 0xff).toByte(),
            (long1 and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((long2 shr 56) and 0xff).toByte(),
            ((long2 shr 48) and 0xff).toByte(),
            ((long2 shr 40) and 0xff).toByte(),
            ((long2 shr 32) and 0xff).toByte(),
            ((long2 shr 24) and 0xff).toByte(),
            ((long2 shr 16) and 0xff).toByte(),
            ((long2 shr 8) and 0xff).toByte(),
            (long2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((longResult shr 56) and 0xff).toByte(),
            ((longResult shr 48) and 0xff).toByte(),
            ((longResult shr 40) and 0xff).toByte(),
            ((longResult shr 32) and 0xff).toByte(),
            ((longResult shr 24) and 0xff).toByte(),
            ((longResult shr 16) and 0xff).toByte(),
            ((longResult shr 8) and 0xff).toByte(),
            (longResult and 0xff).toByte(),
        )

//        val expectedResult = byteArrayOf(
//            0.toByte(),
//            ((longResult shr 8) and 0xff).toByte(),
//            (longResult and 0xff).toByte(),
//        )

        // when
        val actualResult = byteArray1 times byteArray2

        // then
        println("+++ expected result = ${expectedResult.contentToString()}")
//        assertEquals(longResult, expectedResult.foldIndexed(0L) { i, acc, byte ->
//            (acc shl 8) + (byte and 255.toByte())
//        })
        assertContentEquals(expectedResult, actualResult.takeLast(8).toByteArray())
    }

    @Test
    fun testByteArrayMultiplyNegatives() {
        // given
        val long1: Long = -29476
        val long2: Long = -8937643
        val longResult = long1 * long2

        val byteArray1 = byteArrayOf(
            ((long1 shr 56) and 0xff).toByte(),
            ((long1 shr 48) and 0xff).toByte(),
            ((long1 shr 40) and 0xff).toByte(),
            ((long1 shr 32) and 0xff).toByte(),
            ((long1 shr 24) and 0xff).toByte(),
            ((long1 shr 16) and 0xff).toByte(),
            ((long1 shr 8) and 0xff).toByte(),
            (long1 and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((long2 shr 56) and 0xff).toByte(),
            ((long2 shr 48) and 0xff).toByte(),
            ((long2 shr 40) and 0xff).toByte(),
            ((long2 shr 32) and 0xff).toByte(),
            ((long2 shr 24) and 0xff).toByte(),
            ((long2 shr 16) and 0xff).toByte(),
            ((long2 shr 8) and 0xff).toByte(),
            (long2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((longResult shr 56) and 0xff).toByte(),
            ((longResult shr 48) and 0xff).toByte(),
            ((longResult shr 40) and 0xff).toByte(),
            ((longResult shr 32) and 0xff).toByte(),
            ((longResult shr 24) and 0xff).toByte(),
            ((longResult shr 16) and 0xff).toByte(),
            ((longResult shr 8) and 0xff).toByte(),
            (longResult and 0xff).toByte(),
        )

//        val expectedResult = byteArrayOf(
//            0.toByte(),
//            ((longResult shr 8) and 0xff).toByte(),
//            (longResult and 0xff).toByte(),
//        )

        // when
        val actualResult = byteArray1 times byteArray2

        // then
        println("+++ expected result = ${expectedResult.contentToString()}")
//        assertEquals(longResult, expectedResult.foldIndexed(0L) { i, acc, byte ->
//            (acc shl 8) + (byte and 255.toByte())
//        })
        assertContentEquals(expectedResult, actualResult.takeLast(8).toByteArray())
    }

    @Test
    fun testByteArrayMultiplyDifferentLengths() {
        // given
        val int1 = 12
        val int2 = 66666
        val intResult = int1 * int2

        val byteArray1 = byteArrayOf(
            ((int1 shr 8) and 0xff).toByte(),
            (int1 and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((int2 shr 24) and 0xff).toByte(),
            ((int2 shr 16) and 0xff).toByte(),
            ((int2 shr 8) and 0xff).toByte(),
            (int2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((intResult shr 24) and 0xff).toByte(),
            ((intResult shr 16) and 0xff).toByte(),
            ((intResult shr 8) and 0xff).toByte(),
            (intResult and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 times byteArray2

        // then
        assertContentEquals(expectedResult, actualResult.takeLast(4).toByteArray())
    }

    @Test
    fun testByteArrayMultiplyLong() {
        // given
        val long1: Long = 123456789
        val long2: Long = 987654321
        val longResult = long1 * long2

        val byteArray1 = byteArrayOf(
            ((long1 shr 56) and 0xff).toByte(),
            ((long1 shr 48) and 0xff).toByte(),
            ((long1 shr 40) and 0xff).toByte(),
            ((long1 shr 32) and 0xff).toByte(),
            ((long1 shr 24) and 0xff).toByte(),
            ((long1 shr 16) and 0xff).toByte(),
            ((long1 shr 8) and 0xff).toByte(),
            (long1 and 0xff).toByte(),
        )

        val byteArray2 = byteArrayOf(
            ((long2 shr 56) and 0xff).toByte(),
            ((long2 shr 48) and 0xff).toByte(),
            ((long2 shr 40) and 0xff).toByte(),
            ((long2 shr 32) and 0xff).toByte(),
            ((long2 shr 24) and 0xff).toByte(),
            ((long2 shr 16) and 0xff).toByte(),
            ((long2 shr 8) and 0xff).toByte(),
            (long2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((longResult shr 56) and 0xff).toByte(),
            ((longResult shr 48) and 0xff).toByte(),
            ((longResult shr 40) and 0xff).toByte(),
            ((longResult shr 32) and 0xff).toByte(),
            ((longResult shr 24) and 0xff).toByte(),
            ((longResult shr 16) and 0xff).toByte(),
            ((longResult shr 8) and 0xff).toByte(),
            (longResult and 0xff).toByte(),
        )

//        val expectedResult = byteArrayOf(
//            0.toByte(),
//            ((longResult shr 8) and 0xff).toByte(),
//            (longResult and 0xff).toByte(),
//        )

        // when
        val actualResult = byteArray1 times byteArray2

        // then
        println("+++ expected result = ${expectedResult.contentToString()}")
//        assertEquals(longResult, expectedResult.foldIndexed(0L) { i, acc, byte ->
//            (acc shl 8) + (byte and 255.toByte())
//        })
        assertContentEquals(expectedResult, actualResult.takeLast(8).toByteArray())
    }
    @Test
    fun testByteArrayMultiplyLong2() {
        // given
        val long1: Long = 21
        val long2: Long = 54321
        val longResult = long1 * long2

//        val byteArray1 = byteArrayOf(
//            ((long1 shr 56) and 0xff).toByte(),
//            ((long1 shr 48) and 0xff).toByte(),
//            ((long1 shr 40) and 0xff).toByte(),
//            ((long1 shr 32) and 0xff).toByte(),
//            ((long1 shr 24) and 0xff).toByte(),
//            ((long1 shr 16) and 0xff).toByte(),
//            ((long1 shr 8) and 0xff).toByte(),
//            (long1 and 0xff).toByte(),
//        )

        val byteArray1 = byteArrayOf(21)

        val byteArray2 = byteArrayOf(
            ((long2 shr 56) and 0xff).toByte(),
            ((long2 shr 48) and 0xff).toByte(),
            ((long2 shr 40) and 0xff).toByte(),
            ((long2 shr 32) and 0xff).toByte(),
            ((long2 shr 24) and 0xff).toByte(),
            ((long2 shr 16) and 0xff).toByte(),
            ((long2 shr 8) and 0xff).toByte(),
            (long2 and 0xff).toByte(),
        )

        val expectedResult = byteArrayOf(
            ((longResult shr 56) and 0xff).toByte(),
            ((longResult shr 48) and 0xff).toByte(),
            ((longResult shr 40) and 0xff).toByte(),
            ((longResult shr 32) and 0xff).toByte(),
            ((longResult shr 24) and 0xff).toByte(),
            ((longResult shr 16) and 0xff).toByte(),
            ((longResult shr 8) and 0xff).toByte(),
            (longResult and 0xff).toByte(),
        )

//        val expectedResult = byteArrayOf(
//            0.toByte(),
//            ((longResult shr 8) and 0xff).toByte(),
//            (longResult and 0xff).toByte(),
//        )

        // when
        val actualResult = byteArray1 times byteArray2

        // then
        println("+++ expected result = ${expectedResult.contentToString()}")
//        assertEquals(longResult, expectedResult.foldIndexed(0L) { i, acc, byte ->
//            (acc shl 8) + (byte and 255.toByte())
//        })
        assertContentEquals(expectedResult, actualResult.takeLast(8).toByteArray())
    }

    @Test
    fun testByteArrayMultiplyByte() {
        // given
        val byte1: Byte = 21
        val byte2: Byte = -79
        val result = byte1 * byte2

        val byteArray1 = byteArrayOf(byte1)
        val byteArray2 = byteArrayOf(byte2)

        val expectedResult = byteArrayOf(
            ((result shr 8) and 0xff).toByte(),
            (result and 0xff).toByte(),
        )

        // when
        val actualResult = byteArray1 times byteArray2

        // then
        assertContentEquals(expectedResult, actualResult.takeLast(8).toByteArray())
    }
    //endregion

    //region DIVIDE
//    @Test
//    fun testByteArrayDivide() {
//        // given
//        val int1 = 12345
//        val int2 = 678
//        val divResult = int1 / int2
//        val modResult = int1 % int2
//
//        val byteArray1 = byteArrayOf(
//            ((int1 shr 24) and 0xff).toByte(),
//            ((int1 shr 16) and 0xff).toByte(),
//            ((int1 shr 8) and 0xff).toByte(),
//            (int1 and 0xff).toByte(),
//        )
//
//        val byteArray2 = byteArrayOf(
//            ((int2 shr 24) and 0xff).toByte(),
//            ((int2 shr 16) and 0xff).toByte(),
//            ((int2 shr 8) and 0xff).toByte(),
//            (int2 and 0xff).toByte(),
//        )
//
//        val expectedDivResult = byteArrayOf(
//            ((divResult shr 24) and 0xff).toByte(),
//            ((divResult shr 16) and 0xff).toByte(),
//            ((divResult shr 8) and 0xff).toByte(),
//            (divResult and 0xff).toByte(),
//        )
//
//        val expectedModResult = byteArrayOf(
//            ((modResult shr 24) and 0xff).toByte(),
//            ((modResult shr 16) and 0xff).toByte(),
//            ((modResult shr 8) and 0xff).toByte(),
//            (modResult and 0xff).toByte(),
//        )
//
//        // when
//        val actualResult = byteArray1 div byteArray2
//
//        // then
//        assertContentEquals(expectedDivResult, actualResult.first.takeLast(4).toByteArray())
//        assertContentEquals(expectedModResult, actualResult.second.takeLast(4).toByteArray())
//    }

//    @Test
//    fun testByteArrayDivide2N() {
//        // given
//        val int = 12345
//        val divisor = 64
//        val divResult = int / divisor
////        val modResult = int1 % int2
//
//        val byteArray = byteArrayOf(
//            ((int shr 24) and 0xff).toByte(),
//            ((int shr 16) and 0xff).toByte(),
//            ((int shr 8) and 0xff).toByte(),
//            (int and 0xff).toByte(),
//        )
//
//        val expectedDivResult = byteArrayOf(
//            ((divResult shr 24) and 0xff).toByte(),
//            ((divResult shr 16) and 0xff).toByte(),
//            ((divResult shr 8) and 0xff).toByte(),
//            (divResult and 0xff).toByte(),
//        )
//
////        val expectedModResult = byteArrayOf(
////            ((modResult shr 24) and 0xff).toByte(),
////            ((modResult shr 16) and 0xff).toByte(),
////            ((modResult shr 8) and 0xff).toByte(),
////            (modResult and 0xff).toByte(),
////        )
//
//        // when
//        val actualResult = byteArray.divideUnsigned2N(divisor)
//
//        // then
//        assertContentEquals(expectedDivResult, actualResult)
////        assertContentEquals(expectedModResult, actualResult.second.takeLast(4).toByteArray())
//    }
//
//    @Test
//    fun testByteArrayDivideNegative2N() {
//        // given
//        val int = 12345
//        val divisor = -64
//        val divResult = int / divisor
////        val modResult = int1 % int2
//
//        val byteArray = byteArrayOf(
//            ((int shr 24) and 0xff).toByte(),
//            ((int shr 16) and 0xff).toByte(),
//            ((int shr 8) and 0xff).toByte(),
//            (int and 0xff).toByte(),
//        )
//
//        val expectedDivResult = byteArrayOf(
//            ((divResult shr 24) and 0xff).toByte(),
//            ((divResult shr 16) and 0xff).toByte(),
//            ((divResult shr 8) and 0xff).toByte(),
//            (divResult and 0xff).toByte(),
//        )
//
////        val expectedModResult = byteArrayOf(
////            ((modResult shr 24) and 0xff).toByte(),
////            ((modResult shr 16) and 0xff).toByte(),
////            ((modResult shr 8) and 0xff).toByte(),
////            (modResult and 0xff).toByte(),
////        )
//
//        // when
//        val actualResult = byteArray.divideUnsigned2N(divisor)
//
//        // then
//        assertContentEquals(expectedDivResult, actualResult)
////        assertContentEquals(expectedModResult, actualResult.second.takeLast(4).toByteArray())
//    }
    //endregion
}