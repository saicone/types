<h1 align="center">Types</h1>

<h4 align="center">Java library to parse, iterate or wrap any object type.</h4>

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

Types library convert any object type into required object type and iterate any java object.

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

Also, can wrap objects as other types.

```java
// Original String list
List<String> stringList = List.of("1", "2", "3", "4");

// A view of original list as Integer list
List<Integer> integerList = new WrappedList<>(stringList, TypeWrapper.of(Types.String, Types.INTEGER));

// So this
if (integerList.contains(3)) {
    // ...
}
// Is the equivalent of
if (stringList.contains("3")) {
    // ...
}
```

## Dependency

How to implement Types library in your project.

<details>
  <summary>build.gradle</summary>

```groovy
plugins {
    id 'com.gradleup.shadow' version '8.3.4'
}

repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.saicone:types:1.3.0'
}

jar.dependsOn (shadowJar)

shadowJar {
    // Relocate types
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
    id("com.gradleup.shadow") version "8.3.4"
}

repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.saicone:types:1.3.0")
}

tasks {
    jar {
        dependsOn(tasks.shadowJar)
    }

    shadowJar {
        // Relocate types
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
        <version>1.3.0</version>
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
                <!-- Relocate types -->
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
* `java.math.BigDecimal`
* `java.lang.Class<?>`
  1. Qualified name, for example `some.package.for.MyClass`
  2. File path, for example `some/package/for/MyClass.class`
  3. Descriptor, for example `Lsome/package/for/MyClass;`
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
> * Enum, by extracting ordinal value
> * Boolean `true = 1 | false = 0`
> * Decimal, for example `35`
> * Binary `0[bB][0-1]`, for example `0b11010`
> * Hex `0[xX][0-9A-Fa-f]`, for example `0x46`
> * Hex color `#[0-9A-Fa-f]`, for example `#46`
> * Octal `0[0-7]`, for example `075`
> 
> And also detect:
> * Leading signs `+ -`
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

By default, Types library can convert 6 data types using 4 different methods.

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

### Wrap

Type wrapping is a lazy object conversion that work in different dimensions using two types.

**Type A:** Is the base type of object, you can get it by `unwrap` the type B.
**Type B:** Is the type of object that `wrap` type A, in other words, to show A as B.

**One dimension wrapper:**

A one dimension `TypeWrapper` can be created using one type parser to `wrap` or `unwrap` any representation of a type.

```java
TypeParser<Integer> parser = Types.INTEGER;

// Wrapper that only wrap objects
TypeWrapper<Integer, Integer> onlyWrap = TypeWrapper.wrap(parser);

// Wrapper that only unwrap objects
TypeWrapper<Integer, Integer> onlyUnwrap = TypeWrapper.unwrap(parser);


// Example
List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4));

// By only wrapping values you got a list that pass values to type wrapper for outgoing operations
// Which is useless in this case since the wrapper doesn't make a value transformation
List<Integer> integerList = new WrappedList<>(list, onlyWrap);

// This value was processed on wrapper, int this case any change was applied
int i = integerList.get(0);

// By only unwrapping values you got a list that accept any kind of object that can be converted to Integer on incoming operations
// Which is useful to take advantage of type parsing
List<Integer> integerList = new WrappedList<>(list, onlyUnwrap);

// In this case, the following operation is valid since any String is converted to Integer
if (integerList.contains("2")) {
    integerList.remove("1");
}
```

**Two dimension wrapper with cast:**

A two dimension `TypeWrapper` can be created using one type parser to `wrap` type A or `unwrap` type B and expect that the other type must be obtained using a regular cast.

```java
TypeParser<Long> parserA = Types.LONG;
TypeParser<Integer> parserB = Types.INTEGER;

// Wrapper that only wrap type A to show it as B
// Meaning that you only need to provide a type parser to convert A to B
TypeWrapper<Long, Integer> onlyWrap = TypeWrapper.wrap(parserB);

// Wrapper that only unwrap type B to return it as A
// Meaning that you only need to provide a type parser to convert B to A
TypeWrapper<Long, Integer> onlyUnwrap = TypeWrapper.unwrap(parserA);


// Example
List<Long> longList = new ArrayList<>(List.of(1L, 2L, 3L, 4L));

// By only wrapping values you got a list that show values as Integer for outgoing operations
// And try to cast incoming values as Long for outgoing operations
List<Integer> integerList = new WrappedList<>(longList, onlyWrap);

// This value was converted from Long
int i = integerList.get(0);

// By only unwrapping values you got a list that try to cast outgoing values as Integer for incoming operations
// And accepts Long-representation values for incoming operations (for example, an Integer)
List<Integer> integerList = new WrappedList<>(longList, onlyUnwrap);

// In this case the list accept any Long representation as well
if (integerList.contains(2L)) {
    integerList.remove("1L");
}
```

**Two dimension wrapper:**

A two dimension `TypeWrapper` can be created using one type parser to `wrap` type A and other type parser to `unwrap` type B.

```java
TypeParser<Integer> parserA = Types.INTEGER;
TypeParser<String> parserB = Types.STRING;

// A wrapper that make Integers look like Strings by wrapping any String representation
// And make String look like Integers by unwrapping any Integer representation
TypeWrapper<Integer, String> wrapper = TypeWrapper.of(parserA, parserB);


// Example
List<Integer> integerList = new ArrayList<>(List.of(1, 2, 3, 4));

// By providing the wrapper, the Integer list will act as a String list
List<String> stringList = new WrappedList<>(integerList, wrapper);

if (integerList.contains("2")) {
    integerList.remove("1");
}
```

### Register

How to register your own types.

```java
// Register
TypeParser<MyObject> parser = (object) -> {
    // Convert into MyObject...
};
Types.put(MyObject.class, parser);

// Unregister
Types.remove(MyObject.class);

// Get
TypeParser<MyObject> parser = Types.of(MyObject.class);
```
