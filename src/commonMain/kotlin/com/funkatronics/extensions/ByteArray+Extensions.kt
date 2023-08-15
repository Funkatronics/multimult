package com.funkatronics.extensions

import java.math.BigInteger
import java.util.BitSet
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.math.ceil
import kotlin.math.log

internal infix fun ByteArray.and(other: ByteArray) = ByteArray(size) {
    this[it] and (other.getOrNull(other.size - size + it) ?: 0)
}

internal infix fun ByteArray.and(other: Int) = ByteArray(size) {
    val shift = size - it - 1
    if (shift > Int.SIZE_BYTES) 0
    else this[it] and (other ushr shift).toByte()
}

internal fun ByteArray.twosComp() = ByteArray(size) { i ->
    this[i].inv().run { if (i == size - 1) this + 1 else this }.toByte()
}

internal infix fun ByteArray.shl(bits: Int): ByteArray {

    // if bits > 8, insert zeros etc
    val byteShift = bits/8
    val bitShift = bits%8
//    println("+++ INIT: bits = $bitShift, byteShift = $byteShift")

    val result = ByteArray(size + byteShift + 1)
    var overflow: UByte = 0u
    for (b in size downTo 1) {
//        println("+++ START $b: byte = ${this[b-1].toString(2)}, overflow = ${overflow.toString(2)}")
//        if (this[b - 1] == 0.toByte()) continue
        val shifted = (this[b - 1].toUInt() shl bitShift) or overflow.toUInt()
//        println("+++   MID $b: shifted = ${shifted.toString(2)}")
        overflow = ((shifted shr 8) and 1u).toUByte()
        result[b] = (shifted and 255u).toByte()
//        println("+++   END $b: byte = ${result[b].toString(2)}, overflow = ${overflow.toString(2)}")
    }

    result[0] = overflow.toByte()

    return if (result[0].toInt() == -1) result.sliceArray(1..size) else result
}

internal infix fun ByteArray.shr(bits: Int): ByteArray {

    if (bits == 0) return this

    val byteShift = bits/8
    val bitShift = bits%8
//    println("+++ START: input = ${this.contentToString()}")

    if (bitShift == 0) return this.sliceArray(0 until size - byteShift)

    val result = ByteArray(size - byteShift)
    var carry: Byte = 0
    for (b in result.indices) {
//        println("+++ $b: carry = $carry")
//        println("+++ $b: ibyte = ${this[b].toString(2)}")
//        println("+++ $b: ishift = ${((this[b].toUByte() and 255u).toInt() shr bitShift).toString(2)}")
        val byteAsInt = if (b == 0) this[b].toInt() else (this[b].toUByte() and 255u).toInt()
        result[b] = ((byteAsInt shr bitShift) or (carry.toInt() shl (8-bitShift))).toByte()
        carry = this[b] and BitSet(bitShift).apply { set(0, bitShift) }.toByteArray()[0]
    }

    return result
}

internal infix fun ByteArray.rem2N(divisor: Int): ByteArray {
    require((divisor and (divisor - 1)) == 0) { "Divisor must be a power of 2" }
    require(divisor != 0) { "Division by zero" }
    return this@rem2N and (divisor - 1)
}

internal infix fun ByteArray.rem(divisor: Int): Long {
    require(divisor != 0) { "Division by zero" }

    var result = 0L

    // Computing remainder using a variation of Horner's Method
    // https://en.wikipedia.org/wiki/Horner%27s_method
    for (byte in this) {
        result *= 256 % divisor
        result %= divisor
        result += (byte.toInt() and 0xff) % divisor
        result %= divisor
    }

    return result
}

internal infix fun ByteArray.plus(other: ByteArray): ByteArray {

    val first = (if (size > other.size) this else ByteArray(other.size - size) + this)
    val second = (if (size < other.size) other else ByteArray(size - other.size) + other)
    val result = ByteArray(first.size)

    var carry: Byte = 0
    for(b in first.size - 1 downTo 0) {
        val add = first[b].toUByte() + second[b].toUByte() + carry.toUByte()
        val newByte: Byte = (add and 255u).toByte()
        carry = ((add shr 8) and 255u).toByte()

        result[b] = newByte
    }

    return if (carry.toInt() != 0) byteArrayOf(carry) + result else result
}

internal infix fun ByteArray.minus(other: ByteArray): ByteArray {

    val first = (if (size > other.size) this else ByteArray(other.size - size) + this)
    val second = (if (size < other.size) other else ByteArray(size - other.size) + other)
    val result = ByteArray(first.size)

    var carry: Byte = 0
    for(b in first.size - 1 downTo 0) {
        val add = first[b].toUByte() - second[b].toUByte() - carry.toUByte()
        val newByte: Byte = (add and 255u).toByte()
        carry = ((add shr 8) and 1u).toByte()

        result[b] = newByte
    }

    return result
}

internal infix fun ByteArray.times(other: ByteArray): ByteArray {

    var resultIsNegative = false

    val first = if (first().toInt() < 0) {
        resultIsNegative = true
        this.twosComp()
    } else this

    val second = if (other.first().toInt() < 0) {
        resultIsNegative = !resultIsNegative
        other.twosComp()
    } else other

    var result = ByteArray(0)

    for (i in first.size - 1 downTo 0) {
        var carry: Byte = 0
        var midResult = ByteArray(other.size + size - 1 - i) { 0 }

        for (j in second.size - 1 downTo 0) {
            val multiply: UInt = first[i].toUByte() * second[j].toUByte() + carry.toUByte()
            val newByte: Byte = (multiply and 255u).toByte()
            carry = ((multiply shr 8) and 255u).toByte()
            midResult[j] = newByte
        }

        if (carry != 0.toByte()) midResult = byteArrayOf(carry) + midResult

        result = result plus midResult
    }

    if (!resultIsNegative && result[0].toInt() < 0) result = byteArrayOf(0) + result

    return if (resultIsNegative) result.twosComp() else result
}

infix fun ByteArray.div(other: ByteArray): Pair<ByteArray, ByteArray> {
    /*
    function divide(N, D)
      if D = 0 then error(DivisionByZero) end
      if D < 0 then (Q, R) := divide(N, −D); return (−Q, R) end
      if N < 0 then
        (Q,R) := divide(−N, D)
        if R = 0 then return (−Q, 0)
        else return (−Q − 1, D − R) end
      end
      -- At this point, N ≥ 0 and D > 0
      return divide_unsigned(N, D)
    end
     */
    return divideUnsigned(other)
}

internal fun ByteArray.divideUnsigned(other: ByteArray): Pair<ByteArray, ByteArray> {
    return divideUnsignedSlow(other)
}

//internal fun ByteArray.divideUnsigned2N(divisor: Int): ByteArray = divideUnsigned2N(divisor.toLong())
//
//internal fun ByteArray.divideUnsigned2N(divisor: Long): ByteArray {
//
//    if (divisor == 0L) throw IllegalArgumentException("Division by zero")
//    if ((divisor and (divisor - 1)) != 0L) throw IllegalArgumentException("Divisor must be a power of 2")
//
////    if (divisor < 0) throw IllegalArgumentException("Division by negative")
//
//    var power = 0
//    while(divisor shr power > 1 || divisor shr power < -1) power++
//
//    return this shr power
//}

internal fun ByteArray.divideUnsignedSlow(other: ByteArray): Pair<ByteArray, ByteArray> {
    /*
    function divide_unsigned(N, D)
      Q := 0; R := N
      while R ≥ D do
        Q := Q + 1
        R := R − D
      end
      return (Q, R)
    end
     */

    if (other.all { it == 0.toByte() }) throw IllegalArgumentException("Division by zero")

    var q = ByteArray(size)
    var r = this
    while (true) {
        val newQ = q plus byteArrayOf(1)
        val newR = r minus other

//        println("+++  Q = ${q.contentToString()} |  R = ${r.contentToString()}")
//        println("+++ nQ = ${newQ.contentToString()} | nR = ${newR.contentToString()}")

        if (newR.first().toInt() < 0) break

        q = newQ
        r = newR
    }

    return q to r
}

//internal infix fun ByteArray.gt(other: ByteArray): Boolean {
//    val longer = if(this.size > other.size) this else other
//    val shorter = if(other.size < size) other else this
//
//    longer.forEachIndexed { i, b ->
//        if (b > shorter.getOrElse(i) { 0 }) return true
//    }
//
//    return false
//}

//internal fun ByteArray.divideUnsignedLong(other: ByteArray): Pair<ByteArray, ByteArray> {
//    /*
//    function divide_unsigned(N, D)
//      Q := 0; R := N
//      while R ≥ D do
//        Q := Q + 1
//        R := R − D
//      end
//      return (Q, R)
//    end
//     */
//
//    if (other.all { it == 0.toByte() }) throw IllegalArgumentException("Division by zero")
//
//    var q = ByteArray(size)
//    var r = ByteArray(size)
//    println("+++++++++++++ START ++++++++++++++")
//    println("+++++ N = ${this.joinToString { it.toString(2) }}")
//    println("+++++ D = ${other.joinToString { it.toString(2) }}")
//
//    for (i in (size*8 - 1) downTo 0) {
//        println("+++++ r = ${r.joinToString { it.toString(2) }}")
//
//        // left shift r
//        r = r shl 1
//        r[r.size-1] = (r.last() and 0xfe.toByte()) or ((this[size - 1 - i/8].toInt() shr i%8) and 1).toByte()
//        println("+++++ r = ${r.joinToString { it.toString(2) }}")
//
//        if (r gt other) {
//            r = r minus other
//            q[i/8] = q[i/8] or (1 shl i%8).toByte()
//            println("+++++ q = ${q.joinToString { it.toString(2) }}")
//        }
//    }
//
//    return q to r
//}

    fun decode(alphabet: String, base: Int, input: String, blockSize: Int? = null): ByteArray {

        var power = 0
        while(base shr power > 1 || base shr power < -1) power++

        val pad = blockSize?.let { blockSize - input.length%blockSize } ?: 0

        println("++++ PAD = $pad")

        var bi = byteArrayOf(0)

        var zeros = 0

        val data = input + buildString(pad) { repeat(pad) { insert(0, '=') } }
        println("++++ data = $data")

        data.forEachIndexed { i, c ->
            val alphaIndex = if (c == '=') 0.also { zeros++ } else alphabet.indexOf(c)
            if (alphaIndex == -1) throw IllegalStateException("Illegal character $c at $i")
            bi = (bi times byteArrayOf(base.toByte())) plus byteArrayOf(alphaIndex.toByte())
        }

        val zeroBits = power*data.reversed().count { it == '=' }
        val shift = blockSize?.let{ zeroBits + if (zeroBits%8 > 0) (8 - zeroBits%8) else 0} ?: 0
        println("++++ shift = $shift")

        return bi shr shift
    }

    fun encode(alphabet: String, input: ByteArray): String =
        when (alphabet.filter { it != '=' }.length) {
            2 -> encode(alphabet, input, 1)
            8 -> encode(alphabet, input, 3)
            10 -> encodeBaseN(alphabet, input, 80)
            16 -> encode(alphabet, input, 4)
            32 -> encode(alphabet, input, 5, '=')
            36 -> encodeBaseN(alphabet, input, 208)
            58 -> encodeBaseN(alphabet, input, 208)
            64 -> encode(alphabet, input, 3, '=')
            else -> ""
        }

    fun encode(alphabet: String, input: ByteArray, blockSize: Int, padChar: Char? = null): String {

        val base = alphabet.filter { it != '=' }.length
        var power = 0
        while(base shr power > 1 || base shr power < -1) power++

        val pad = if (input.size%blockSize > 0) blockSize - input.size%blockSize else 0

        var bytes = input + ByteArray(pad)
        val resultBuffer = StringBuffer()

        var zeros = true
        while (bytes.find { it != 0.toByte() } != null) {
            val mod = (bytes rem2N base)

            if (zeros && mod.last().toInt() > 0) zeros = false

            if (zeros && padChar != null)
                resultBuffer.insert(0, padChar)
            else if (!zeros)
                resultBuffer.insert(0, alphabet[mod.last().toInt()])

            bytes = bytes shr power

        }

        return resultBuffer.toString()
    }

    fun encodeBaseN(alphabet: String, input: ByteArray, bitsPerChar: Int): String {

        val base = alphabet.length
        val pow = bitsPerChar
        val divisor = byteArrayOf(2) shl (pow - 1)
        val denomArray = BigInteger(divisor).divide(BigInteger.valueOf(base.toLong())).add(BigInteger.ONE).toByteArray()
//        val divisor = byteArrayOf(2) shl pow
//        val denom = round(divisor / base).toLong()
//        val denomArray = byteArrayOf(
//            ((denom shr 56) and 0xff).toByte(),
//            ((denom shr 48) and 0xff).toByte(),
//            ((denom shr 40) and 0xff).toByte(),
//            ((denom shr 32) and 0xff).toByte(),
//            ((denom shr 24) and 0xff).toByte(),
//            ((denom shr 16) and 0xff).toByte(),
//            ((denom shr 8) and 0xff).toByte(),
//            (denom and 0xff).toByte(),
//        )
//        val denomArray = (divisor div byteArrayOf(base.toByte())).first

//        println("++++ denom = ${denomArray.contentToString()}")

//        val blockSize = 1
//        val padChar: Char? = null
//        val pad = 0//blockSize - input.size%blockSize

        var whoop = (input) //+ ByteArray(pad)
        val resultBuffer = StringBuffer()

//        var zeros = true
        while (whoop.find { it != 0.toByte() } != null) {
            val mod = whoop rem base

//            if (zeros && mod.toInt() > 0) zeros = false

//            if (zeros && padChar != null)
//                resultBuffer.insert(0, padChar)
//            else
//                if (!zeros)
            resultBuffer.insert(0, alphabet[mod.toInt()])

            whoop = (whoop times denomArray) shr pow
        }

        return resultBuffer.toString()
    }

    fun encode2(alphabet: String, base: Int, input: ByteArray): String {
    //        if (base.toInt() == 64 || base.toInt() == 32) return encode2(alphabet, base, input)
    //        println("+++++++++++++++++++++++++ encode $base start +++++++++++++++++++++++++")
    //        println("+++ input: ${input.contentToString()}")
    //        println("+++ size: ${input.size}")

        val radix = base.toDouble()
        val chunkSize = ceil(log(radix, 2.0)).toInt()
    //        val blockSize = 5
    //        println("+++ chunk size: $chunkSize")
        val over = input.size%chunkSize
        val pad = if (over > 0) chunkSize - over else 0
        val data = input + if (pad > 0) ByteArray(chunkSize - input.size%chunkSize) else ByteArray(0)
    //        println("+++ data: ${data.contentToString()}")

    //        val fullBlocks = data.size/blockSize
    //        val remaining = data.size%blockSize
    //        println("+++ full blocks = ${fullBlocks}")
    //        println("+++ remaining = ${remaining}")
    //        val blocks = MutableList<ByteArray>(fullBlocks + if (remaining > 0) 1 else 0) { ByteArray(blockSize) }
    //        //ByteArray(fullBlocks + if (remaining > 0) 1 else 0)
    //        (0 until fullBlocks).forEach { b ->
    ////            data.copyInto(blocks)
    //            blocks[b] = data.sliceArray(b*blockSize until (b + 1)*blockSize)
    //        }
    //
    //        if (remaining > 0) {
    //            blocks[fullBlocks] = data.sliceArray(blockSize*(fullBlocks) until blockSize*(fullBlocks)+remaining) + ByteArray(blockSize - remaining)
    //        }
    //
    //        print("+++ data = ")
    //        data.forEach {
    //            print("${it.toString(2)} ")
    //        }
    //        println()

    //        var pos = 0
    //        val chunks = ByteArray(data.size*8/chunkSize) { c ->
    //            pos += chunkSize
    //            val chunkMask = (255 shr (8 - chunkSize) and 0xff) shl (8*(pos/8 + 1))%pos and 0xff
    //            println("+++ chunk mask: ${chunkMask.toString(2)}")
    //            val b: Byte = ((data[pos/8] and chunkMask.toByte()).toInt() shr (8*(pos/8 + 1))%pos).toByte()
    //            println("+++ chunk $c = ${b.toString(2)}")
    //            b
    //        }

        var pos = 0
        val chunks = ByteArray(data.size*8/chunkSize) { c ->
            val dataIndex = c*chunkSize/8
    //            println("++++++++++++++++\n+++ data index = $dataIndex")

            val b1 = data[dataIndex].toInt()
            val b2 = if (dataIndex + 1 < data.size) data[dataIndex + 1].toInt() else 0
            val fullChunk = (b1 shl 8) or (b2 and 0xff)
    //            println("+++ chunk bytes = ${b1.toString(2)}, ${b2.toString(2)}")
    //            println("+++ full chunk = ${fullChunk.toString(2)}")

            val shift = 16 - (c*chunkSize + chunkSize - dataIndex*8)
    //            println("+++ shift: $shift")

            val chunkMask = (255 shr (8 - chunkSize) and 0xff) shl shift
    //            println("+++ chunk mask: ${chunkMask.toString(2)}")

            val b = (fullChunk and chunkMask) shr shift
    //            println("+++ chunk $c = ${b.toString(2)}")

            pos += chunkSize
            b.toByte()
        }

    //        print("+++ data = ")
    //        input.forEach {
    //            print("$it ")
    //        }
    //        println()
    //
    //        print("+++ chunks = ")
    //        chunks.forEach {
    //            print("$it ")
    //        }
    //        println()
    //
    //        print("+++ data = ")
    //        input.forEach {
    //            print("${it.toString(2)} ")
    //        }
    //        println()
    //
    //        print("+++ chunks = ")
    //        chunks.forEach {
    //            print("${it.toString(2)} ")
    //        }
    //        println()

        var result = ""
        (0 until chunks.size - pad*8/chunkSize).forEach {
            result += alphabet.getOrElse(chunks[it].toInt()) { '=' }
        }

        if (alphabet.contains("="))
    //            result += '='
            (0 until pad*8/chunkSize).forEach {
                result += '='
            }

    //        val shifter = 255 shl (8 - chunkSize) and 0xFF
    //        println("+++ shifter: ${shifter.toString(2)}")
    //
    //        blocks.forEach { block ->
    //            println("+++ block: ${block.contentToString()}")
    //            val numChunks = blockSize*8/chunkSize
    //            (0 until numChunks).forEach { c ->
    //                println("++++++ chunk: $c")
    //                val bStart = c*chunkSize
    //                val bEnd = bStart + chunkSize
    //                println("++++++ bits: $bStart to $bEnd")
    //                val iStart = bStart/8
    //                val iEnd = bEnd/8
    //                println("++++++ chunk index: $iStart to $iEnd")
    //                val bits = block[iStart] + block[iEnd]
    //            }
    //        }

    //        println("+++++++++++++++++++++++++ encodeBase32 end +++++++++++++++++++++++++")
    //        println("++++  result = $result")
        return result
    }