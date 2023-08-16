# multimult
A One-Stop-Shop for Binary to Text Encoding

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