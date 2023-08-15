package com.funkatronics.multibase

import com.funkatronics.encoders.Base58
import com.funkatronics.encoders.BaseN

import com.funkatronics.encoders.Binary as Base2Enc
import com.funkatronics.encoders.Base64 as Base64Enc

sealed class MultiBase(val prefix: Char, val alphabet: String) {
    object Base2 : MultiBase('0', "01")
    object Base8 : MultiBase('7', "01234567")
    object Base10 : MultiBase('9', "0123456789")
    object Base16 : MultiBase('f', "0123456789abcdef")
    object Base16Upper : MultiBase('F', "0123456789ABCDEF")
    object Base32 : MultiBase('b', "abcdefghijklmnopqrstuvwxyz234567")
    object Base32Upper : MultiBase('B', "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567")
    object Base32Pad : MultiBase('c', "abcdefghijklmnopqrstuvwxyz234567=")
    object Base32UpperPad : MultiBase('C', "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567=")
    object Base32Hex : MultiBase('v', "0123456789abcdefghijklmnopqrstuv")
    object Base32HexUpper : MultiBase('V', "0123456789ABCDEFGHIJKLMNOPQRSTUV")
    object Base32HexPad : MultiBase('t', "0123456789abcdefghijklmnopqrstuv=")
    object Base32HexUpperPad : MultiBase('T', "0123456789ABCDEFGHIJKLMNOPQRSTUV=")
    object Base32Z : MultiBase('h', "ybndrfg8ejkmcpqxot1uwisza345h769")
    object Base36 : MultiBase('k', "0123456789abcdefghijklmnopqrstuvwxyz")
    object Base36Upper : MultiBase('K', "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    object Base58Flickr : MultiBase('Z', "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ")
    object Base58Btc : MultiBase('z', "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz")
    object Base64 : MultiBase('m', "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")
    object Base64Url : MultiBase('u', "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_")
    object Base64Pad : MultiBase('M', "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=")
    object Base64UrlPad : MultiBase('U', "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_=")

    fun encode(input: ByteArray) = Companion.encode(this, input)

    companion object {
        fun encode(base: MultiBase, input: ByteArray): String = base.prefix + when (base) {
            is Base2 -> Base2Enc.encodeToString(input)//Base2N.encode(base.alphabet, 2, input)
            is Base8 -> BaseN.encode(base.alphabet, input)
            is Base10 -> BaseN.encode(base.alphabet, input)
            is Base16 -> BaseN.encode(base.alphabet, input)
            is Base16Upper -> BaseN.encode(base.alphabet, input)
            is Base32 -> BaseN.encode(base.alphabet, input)
            is Base32Upper -> BaseN.encode(base.alphabet, input)
            is Base32Pad -> BaseN.encode(base.alphabet, input)
            is Base32UpperPad -> BaseN.encode(base.alphabet, input)
            is Base32Hex -> BaseN.encode(base.alphabet, input)
            is Base32HexUpper -> BaseN.encode(base.alphabet, input)
            is Base32HexPad -> BaseN.encode(base.alphabet, input)
            is Base32HexUpperPad -> BaseN.encode(base.alphabet, input)
            is Base32Z -> BaseN.encode(base.alphabet, input)
            is Base36 -> BaseN.encode(base.alphabet, input)
            is Base36Upper -> BaseN.encode(base.alphabet, input)
            is Base58Flickr -> BaseN.encode(base.alphabet, input)
            is Base58Btc -> Base58.encodeToString(input)
            is Base64 -> Base64Enc.getEncoder(withoutPadding = true).encodeToString(input)
            is Base64Url -> Base64Enc.getUrlEncoder(withoutPadding = true).encodeToString(input)
            is Base64Pad -> Base64Enc.encodeToString(input)
            is Base64UrlPad -> Base64Enc.getUrlEncoder().encodeToString(input)
        }

        fun decode(input: String): ByteArray = when (input.first()) {
            Base2.prefix -> Base2Enc.decode(input.drop(1))//Base2N.decode(Base2.alphabet, 256, input.drop(1))
            Base8.prefix -> BaseN.decode(Base8.alphabet, input.drop(1))
            Base10.prefix -> BaseN.decode(Base10.alphabet, input.drop(1))
            Base16.prefix -> BaseN.decode(Base16.alphabet, input.drop(1))
            Base16Upper.prefix -> BaseN.decode(Base16Upper.alphabet, input.drop(1))
            Base32.prefix -> BaseN.decode(Base32.alphabet, input.drop(1))
            Base32Upper.prefix -> BaseN.decode(Base32Upper.alphabet, input.drop(1))
            Base32Pad.prefix -> BaseN.decode(Base32Pad.alphabet, input.drop(1))
            Base32UpperPad.prefix -> BaseN.decode(Base32UpperPad.alphabet, input.drop(1))
            Base32Hex.prefix -> BaseN.decode(Base32Hex.alphabet, input.drop(1))
            Base32HexUpper.prefix -> BaseN.decode(Base32HexUpper.alphabet, input.drop(1))
            Base32HexPad.prefix -> BaseN.decode(Base32HexPad.alphabet, input.drop(1))
            Base32HexUpperPad.prefix -> BaseN.decode(Base32HexUpperPad.alphabet, input.drop(1))
            Base32Z.prefix -> BaseN.decode(Base32Z.alphabet, input.drop(1))
            Base36.prefix -> BaseN.decode(Base36.alphabet, input.drop(1))
            Base36Upper.prefix -> BaseN.decode(Base36Upper.alphabet, input.drop(1))
            Base58Flickr.prefix -> BaseN.decode(Base58Flickr.alphabet, input.drop(1))
            Base58Btc.prefix -> Base58.decode(input.drop(1))
            Base64.prefix -> Base64Enc.decode(input.drop(1))
            Base64Url.prefix -> Base64Enc.getUrlDecoder().decode(input.drop(1))
            Base64Pad.prefix -> Base64Enc.decode(input.drop(1))
            Base64UrlPad.prefix -> Base64Enc.getUrlDecoder().decode(input.drop(1))
            else -> throw IllegalStateException("Unsupported encoding encountered: No decoder available for prefix ${input.first()}")
        }
    }
}