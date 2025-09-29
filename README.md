# multimult
A One-Stop-Shop for Binary to Text Encoding

[![badge-latest-release]][url-latest-release]
[![badge-license]][url-license]
[![badge-kotlin]][url-kotlin]

## Usage

### Base64
```kotlin
val bytesToEncode = "my string".encodeToByteArray()

// encode 
val encoded: String = Base64.encodeToString(bytesToEncode)

// decode
val decoded = Base64.decodeToString(encoded)
```

### Base32
```kotlin
val bytesToEncode = "my string".encodeToByteArray()

// encode 
val encoded: String = Base32.encodeToString(bytesToEncode)

// decode
val decoded = Base32.decodeToString(encoded)
```

### Base58
```kotlin
val bytesToEncode = "my string".encodeToByteArray()

// encode 
val encoded: String = Base58.encodeToString(bytesToEncode)

// decode
val decoded = Base58.decodeToString(encoded)
```

MultiBase.encode(MultiBase.Base8, 


### MultiBase
```kotlin
val bytesToEncode = "my string".encodeToByteArray()
val base = MultiBase.Base8 // choose your base

// encode 
val encoded: String = MultiBase.encode(base, bytesToEncode)

// decode
val decoded = MultiBase.decode(encoded)
```

<!-- TAG_VERSION -->
[badge-latest-release]: https://img.shields.io/badge/dynamic/json.svg?url=https://api.github.com/repos/Funkatronics/multimult/releases/latest&query=tag_name&label=release&color=blue
[badge-license]: https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat

<!-- TAG_DEPENDENCIES -->
[badge-kotlin]: https://img.shields.io/badge/kotlin-2.1.21-blue.svg?logo=kotlin

[url-latest-release]: https://github.com/Funkatronics/multimult/releases/latest
[url-license]: https://www.apache.org/licenses/LICENSE-2.0.txt
[url-kotlin]: https://kotlinlang.org