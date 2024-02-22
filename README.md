<h1 align="center">Types</h1>

<h4 align="center">Java library to parse or iterate any object type.</h4>

<p align="center">
    <a href="https://saic.one/discord">
        <img src="https://img.shields.io/discord/974288218839191612.svg?style=flat-square&label=discord&logo=discord&logoColor=white&color=7289da"/>
    </a>
    <a href="https://www.codefactor.io/repository/github/saicone/types">
        <img src="https://img.shields.io/codefactor/grade/github/saicone/types?style=flat-square&logo=codefactor&logoColor=white&label=codefactor&color=00b16a"/>
    </a>
    <a href="https://github.com/saicone/types">
        <img src="https://img.shields.io/github/languages/code-size/saicone/types?logo=github&logoColor=white&style=flat-square"/>
    </a>
    <a href="https://jitpack.io/#com.saicone/types">
        <img src="https://img.shields.io/github/v/tag/saicone/types?style=flat-square&logo=jitpack&logoColor=white&label=JitPack&color=brigthgreen"/>
    </a>
    <a href="https://javadoc.saicone.com/types/">
        <img src="https://img.shields.io/badge/JavaDoc-Online-green?style=flat-square"/>
    </a>
</p>

Types library convert any object type into required object type, also can iterate hover any java object.

```java
String str = "1234";
int number = Types.INTEGER.parse(str);

double[] array = new double[] { 10.3, 8.4, 5.0 };
List<Float> list = ValueType.of(array).asList(Types.FLOAT);

Map<String, String> from = Map.of("1234", "true", "55", "false", "10", "true");
Map<Integer, Boolean> to = new TypeOf<Map<Integer, Boolean>>(){}.parse(from);
```

## Dependency

How to add Types library in your project.

<details>
  <summary>build.gradle</summary>

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.saicone.types:types:1.0'
}
```

</details>

<details>
  <summary>build.gradle.kts</summary>

```kotlin
repositories {
  maven("https://jitpack.io")
}

dependencies {
  compileOnly("com.saicone.types:types:1.0")
}
```

</details>

<details>
  <summary>pom.xml</summary>

```xml
<repositories>
  <repository>
    <id>Jitpack</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.saicone.types</groupId>
    <artifactId>types</artifactId>
    <version>1.0</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```

</details>

## Supported types

A "supported" type (for example) doesn't mean you can magically convert a `Map<String, Integer>` into `Boolean`,
every object type that you want to parse must have a minimum sense with the conversion or `null` will be return,
for example a `"1.4"` can be converted into any `Number` type.

Also, any supported (primitive) object type can be returned as array value of its type.

### Primitives

Since primitive types cannot be explicitly `null`, they will be returned with a fallback value.

<details>
  <summary>Click to show</summary>

* `char` or `'\0'`
* `boolean` or `Boolean.FALSE`
* `byte` or `Byte.MIN_VALUE`
* `short` or `Short.MIN_VALUE`
* `int` or `Integer.MIN_VALUE`
* `float` or `Float.MIN_VALUE`
* `long` or `Long.MIN_VALUE`
* `double` or `Double.MIN_VALUE`

</details>

### Objects

Well known Java objects and the accepted types to properly parse them.

<details>
  <summary>Click to show</summary>

* `java.lang.Object`
* `java.lang.String`
* `java.lang.Character`
* `java.lang.Boolean`
* `java.lang.Number`
* `java.lang.Byte`
* `java.lang.Short`
* `java.lang.integer`
* `java.lang.Float`
* `java.lang.Long`
* `java.lang.Double`
* `java.math.BigInteger`
    1. `String`
    2. `Number`
* `java.math.BigDecimal`
    1. `String`
    2. `Number`
* `java.lang.Class<?>`
    1. `String`
* `java.util.UUID`
    1. `String`
    2. 4-length `int[]`
* `java.net.URI`
    1. `String`
    2. `URL`
    3. `File`
    4. `Path`
* `java.net.URL`
    1. `String`
    2. `URI`
    3. `File`
    4. `Path`
* `java.io.File`
    1. `String` separated by `/`
    2. `String[]`
* `java.nio.file.Path`
    1. `String` separated by `/`
    2. `String[]`
* `java.time.LocalDate`
    1. Epoch day `Long`
    2. 2-length `Number[]` (year, dayOfYear)
    3. 3-length `Number[]` (year, month, day)
    4. ISO-8601 `String`
* `java.time.LocalTime`
    1. Seconds of day `Long`
    2. 2-length `Number[]` (hour, minute)
    3. 3-length `Number[]` (hour, minute, second)
    4. 4-length `Number[]` (hour, minute, second, nanoOfSecond)
    5. `String` formatted as `hour:minute:second.nanoOfSecond`, examples: `"10:30"`, `"10:40:05"`, `"09:08:21.35"`
* `java.time.LocalDateTime`
    1. 5-length `Number[]` (year, month, day, hour, minute)
    2. 6-length `Number[]` (year, month, day, hour, minute, second)
    3. 7-length `Number[]` (year, month, day, hour, minute, second, nanoOfSecond)
    4. ISO-8601 `String` separated by `T` with time formatted as `hour:minute:second.nanoOfSecond`

</details>

### Generic

Typical Java objects with parameters.

<details>
  <summary>Click to show</summary>

* `java.lang.Enum<?>`
    1. Name `String` (case-insensitive)
    2. Ordinal `Number`
* `java.util.Collection<E>` - Can be any Java object that implements `Collection`
* `java.util.Map<K, V>` - Can be any Java object that implements `Map`

(The type parameters `E`, `K` and `V` can be any supported type)

</details>

### Functional

Functional Java objects to handle any type parser in a flexible way.

<details>
  <summary>Click to show</summary>

* `java.util.Optional<E>` - Contains parsed value, empty optional type is return when parse fails or throw exception
* `java.util.concurrent.CompletableFuture<E>` - Maintain parse function into non-null return value (may throw NullPointerException)

(The type parameter `E` can be any supported type)

</details>

## Usage

How to use Types library.

### Conversion

How to convert object types.

### Iterate

How to iterate into any object value.

### Register

How to register your own types.