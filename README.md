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
for (int i : IterableType.<Integer>ofAny(number)) {
    // do something
}

double[] array = new double[] { 10.3, 8.4, 5.0 };
List<Float> list = ValueType.of(array).asList(Types.FLOAT);
for (double d : IterableType.of(array)) {
    // do something
}

Map<String, String> from = Map.of("1234", "true", "55", "false", "10", "true");
Map<Integer, Boolean> to = new TypeOf<Map<Integer, Boolean>>(){}.parse(from);
for (Map.Entry<Integer, Boolean> entry : IterableType.of(to)) {
    // do something
}
```

## Dependency

How to implement Types library in your project.

<details>
  <summary>build.gradle</summary>

```groovy
plugins {
    id 'com.github.johnrengelman.shadow' version '8.1.1'
}

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.saicone:types:1.2'
}

jar.dependsOn (shadowJar)

shadowJar {
    // Relocate types (DO NOT IGNORE THIS)
    relocate 'com.saicone.types', project.group + '.libs.types'
    // Exclude unused classes (optional)
    minimize()
}
```

</details>

<details>
  <summary>build.gradle.kts</summary>

```kotlin
plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.saicone:types:1.2")
}

tasks {
    jar {
        dependsOn(tasks.shadowJar)
    }

    shadowJar {
        // Relocate types (DO NOT IGNORE THIS)
        relocate("com.saicone.types", "${project.group}.libs.types")
        // Exclude unused classes (optional)
        minimize()
    }
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
        <groupId>com.saicone</groupId>
        <artifactId>types</artifactId>
        <version>1.2</version>
        <scope>compile</scope>
    </dependency>
</dependencies>

<build>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.3.0</version>
        <configuration>
            <relocations>
                <!-- Relocate types (DO NOT IGNORE THIS) -->
                <relocation>
                    <pattern>com.saicone.types</pattern>
                    <shadedPattern>${project.groupId}.libs.types</shadedPattern>
                </relocation>
            </relocations>
            <!-- Exclude unused classes (optional) -->
            <minimizeJar>true</minimizeJar>
        </configuration>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>shade</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
</build>
```

</details>

## Supported types

A "supported" type (for example) doesn't mean you can magically convert a `Map<String, Integer>` into `Boolean`,
every object type that you want to parse must have a minimum sense with the conversion or `null` will be return,
for example a `"1.4"` can be converted into any `Number` type.

> [!NOTE]
> Any supported (primitive) object type can be returned as array value of its type.

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
  1. 36-length `String` (with dashes) `00000000-0000-0000-0000-000000000000`
  2. 32-length `String` (without dashes) `00000000000000000000000000000000`
  3. 4-length array (converted to int) `[000000000, 000000000, 000000000, 000000000]`
  4. 2-length array (converted to long) `[mostSigBits, leastSigBits]`
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
  2. 2-length array (converted to int) `[year, dayOfYear]`
  3. 3-length array (converted to int) `[year, month, day]`
  4. ISO-8601 `String`
* `java.time.LocalTime`
  1. Seconds of day `Long`
  2. 2-length array (converted to int) `[hour, minute]`
  3. 3-length array (converted to int) `[hour, minute, second]`
  4. 4-length array (converted to int) `[hour, minute, second, nanoOfSecond]`
  5. `String` formatted as `hour:minute:second.nanoOfSecond`, examples: `"10:30"`, `"10:40:05"`, `"09:08:21.35"`
* `java.time.LocalDateTime`
  1. Epoch seconds `Long`
  2. 5-length array (converted to int) `[year, month, day, hour, minute]`
  3. 6-length array (converted to int) `[year, month, day, hour, minute, second]`
  4. 7-length array (converted to int) `[year, month, day, hour, minute, second, nanoOfSecond]`
  5. ISO-8601 `String` separated by `T` with time formatted as `hour:minute:second.nanoOfSecond`

</details>

> [!NOTE]
> Any Number type can be parsed from:
> * Boolean `true = 1 | false = 0`
> * Decimal, for example `35`
> * Binary `0[bB][0-1]`, for example `0b11010`
> * Hex `0[xX][0-9A-Fa-f]`, for example `0x46`
> * Hex color `#[0-9A-Fa-f]`, for example `#46`
> * Octal `0[0-7]`, for example `075`
> 
> And detect:
> * Leading sings `+ -`
> * Unsigned suffix `u`
> * Number suffixes `b B s S i I f F l L d D`

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

By default, Types library can convert 5 data types using 4 different methods.

* TypeParser - Make your own implementation of type conversion.
* Types - Same as TypeParser, but every parser is cached.
* ValueType - Encapsulated object with methods to convert with Types or create one-time use TypeParser.
* TypeOf - Same as Types, but computes automatically the required type object parser (including generic objects).

**Single objects**:

```java
String str = "1234";

// Using TypeParser
TypeParser<Double> parser = (object) -> Double.parseDouble(String.valueOf(object));
double number = parser.parse(str);

// Using Types (Same as TypeParser, but extract first element of any iterable or array object)
int number = Types.INTEGER.parse(str);

// Using ValueType
float number = ValueType.of(str).asFloat();

// Using TypeOf
TypeOf<Long> type = new TypeOf<Long>(){};
long number = type.parse(str);
```

**Collections**:

```java
int number = 1234;

// Using TypeParser
TypeParser<List<Integer>> parser = TypeParser.collection(Types.INTEGER, ArrayList::new);
List<Integer> list = parser.parse(number);

// Using Types
List<Double> list = Types.DOUBLE.list(number);

// Using ValueType
List<Float> list = ValueType.of(number).asList(Types.FLOAT);

// Using TypeOf
TypeOf<List<Long>> type = new TypeOf<List<Long>>(){};
List<Long> list = type.parse(number);
```

**Object Arrays**:

```java
String str = "1234";

// Using TypeParser
TypeParser<String> parser = TypeParser.of(String.class, String::valueOf);
String[] array = parser.array(str);

// Using Types
Integer[] array = Types.INTEGER.array(str);

// Using ValueType
String[] array = ValueType.of(str).asList(Types.STRING);

// Using TypeOf
TypeOf<Integer[]> type = new TypeOf<Integer[]>(){};
Integer[] array = type.parse(str);
```

**Primitive Arrays**:

```java
String str = "1234";

// Using TypeParser
TypeParser<Integer> parser = TypeParser.of(int.class, object -> Integer.parseInt(String.valueOf(object)));
int[] array = parser.array(str);

// Using Types
float[] array = Types.of(float.class).array(str);

// Using ValueType
double[] array = ValueType.of(str).asArray(Types.of(double.class));

// Using TypeOf
TypeOf<long[]> type = new TypeOf<long[]>(){};
long[] array = type.parse(str);
```

**Enums**:

```java
String str = "VALUE_NAME";

// Using TypeParser
TypeParser<MyEnum> parser = TypeParser.enumeration(MyEnum.class);
MyEnum value = parser.parse(str);

// Using ValueType
MyEnum value = ValueType.of(str).asEnum(MyEnum.class);

// Using TypeOf
TypeOf<MyEnum> type = new TypeOf<MyEnum>(){};
MyEnum value = type.parse(str);
```

**Maps**:

```java
Map<String, String> map = new HashMap<>();
map.put("1234", "true");
map.put("55", "false");
map.put("12", "true")

// Using TypeParser
TypeParser<Map<Integer, Boolean>> parser = TypeParser.map(Types.INTEGER, Types.BOOLEAN, HashMap::new);
Map<Integer, Boolean> value = parser.parse(map);

// Using ValueType
Map<Integer, Boolean> value = ValueType.of(map).asMap(Types.INTEGER, Types.BOOLEAN, new HashMap<>());

// Using TypeOf
TypeOf<Map<Integer, Boolean>> type = new TypeOf<Map<Integer, Boolean>>(){};
Map<Integer, Boolean> value = type.parse(map);
```

> [!TIP]
> If you create a `TypeParser` or use `TypeOf` is suggested to save as `static final` field.

### Iterate

How to iterate any object value.

```java
// Single object
String value = "text";
for (String str : IterableType.<String>ofAny(value)) {
    // do something
}

// Array / Collection
Integer[] value = new Integer[] { 1, 2, 3, 4 };
int[] value = new int[] { 1, 2, 3, 4 };
List<Integer> value = new ArrayList<>();
for (int i : IterableType.of(value)) {
    // do something
}

// Map
Map<String, Integer> value = new HashMap<>();
for (Map.Entry<String, Integer> entry : IterableType.of(value)) {
    // do something
}
```

### Register

How to register your own types.

```java
// Register
TypeParser<MyObject> parser = (object) -> {
    // Convert into MyObject...
};
Types.add(MyObject.class, parser);

// Unregister
Types.remove(MyObject.class);

// Get
TypeParser<MyObject> parser = Types.of(MyObject.class);
```
