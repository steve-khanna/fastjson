# SENG 401 Project: Group 16. Repo #2: FastJson
***
### Members: Steve Khanna, Abid Al Labib, David Macababayao, Ragya Mittal, Cobe Reinbold, Long Tran
***
## System description
FastJSON is a library to convert instances of Java Objects into their equivalent JSON representation and vice versa. The library takes into account inheritance when performing any conversions so that the final result is well formatted Java/JSON

## Design Patterns
Implementing two new design patterns for this software system was not an easy task, since the system code is already well written. We noticed that the code base uses lots of design patterns (mostly singleton) in conjuction with each other implying that specific design choices were made during its evolution. However, we have found a couple of sections that we could implement design patterns on.

Our previous experience with the first repository allowed us to easily indentify areas where we could implement design patterns learned in class.

The 2 design patterns we implemented are the **Singleton** and the **Bridge pattern**.
***
## 1. Singleton Pattern (Worked on by Steve Khanna, Long Tran, and Ragya Mittal)
The Singleton pattern allows for classes to only be instantiated once and that object is the only instance of the class used on all of the class' application. Furthermore, there should only be one class creating the instance of this object.

### What prompted us to choose this:
#### Context:
When sifting through the code, we noticed that many Deserialization classes had static variables called instance. This meant that the instance was created at execution and persisted throughout the lifetime of the program. Given that only one instance existed at any given time this was a perfect candidate for the Singleton pattern. A helpful confirmation of this was provided to us by our first repository which also had deserializers implemented using the singleton pattern. Given that both deserializers worked in essentially the same way, we could use the singleton pattern to refactor the code base.

#### Details:
We saw that the code has the singleton method implemented on a lot of classes but their implementation is quite different from the implementation taught in lectures. The original singleton implementation did not have a condition which created a new instance of the object if the current static variable was null. We added this extra if statement to make the code more readable and closer to how singletons were implemented in class. We recognize that the architecture of the system remains unchanged by this refactoring; however, we wanted better code legibility as the current architecture of the system was already well thought out.

### Implementation and Refactoring

#### Classes Changed
* **MapDeserializer**
    * Updated the code to make the singleton implementation more legible.
***
## 2. Bridge Pattern (Worked on by Steve Khanna)
The Bridge pattern allows to decouple an abstraction from its implementation so two can be changed easily.

### What prompted us to choose this:
#### Context:
During execution, fastJSON uses a lot of utilities to correctly convert a data type to it's equivalent Java/JSON representation. For example, if a Java object contained a value that is greater than the value of the Maximum Integer in the standard Java library, converting the instance into it's equivalent JSON by just reading the values in the register would yield an incorrect result. These utilities allow fastJSON to correctly identify these data types and return the correct result. Given that this is such a tedious, and algorithm intensive task; we decided to use the Bridge Pattern to decouple the algorithm from the function. This was done for two main reasons: 1. The final code would be a lot easier to read and would follow better convention as the functions won't be ~400 lines long; and 2. To allow developers to update the code and add additional support for related classes (for example: RyuBigInteger or RyuBigDecimal) which would follow the same algorithm as RyuDouble.

#### Details:
The RyuDouble class contains a method toString() that would convert a given double into its string equivalent. The toString() method does its conversion in multiple different steps that uses algorithms. We decided that some of these steps could be decoupled from the method. This would make the process independent from the method so that new algorithms could be easily integrated into the process. This also brings modularity into the code, in which other classes could also use the same algorithms on similar processes.

### Implementation and Refactoring

#### Classes Added
* **RyuDoubleAlgorithm**
    * Contains the steps that were decoupled from the class method (used in RyuDouble).
* **BridgeStruct**
    * Contains the data structure needed by the processes in the toString() method.

#### Classes Changed
* **RyuDouble**
    * Modularized the method toString()'s steps/processes

***

# fastjson

[![Build Status](https://travis-ci.org/alibaba/fastjson.svg?branch=master)](https://travis-ci.org/alibaba/fastjson)
[![Codecov](https://codecov.io/gh/alibaba/fastjson/branch/master/graph/badge.svg)](https://codecov.io/gh/alibaba/fastjson/branch/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.alibaba/fastjson/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.alibaba/fastjson/)
[![GitHub release](https://img.shields.io/github/release/alibaba/fastjson.svg)](https://github.com/alibaba/fastjson/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/alibaba/fastjson) 
[![QualityGate](https://quality-gate.com/backend/api/timeline?branchName=master&projectName=alibaba_fastjson)](https://quality-gate.com/dashboard/branches/7816#overview)

Fastjson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object. Fastjson can work with arbitrary Java objects including pre-existing objects that you do not have source-code of.

### Fastjson Goals
 * Provide the best performance on the server-side and android client
 * Provide simple toJSONString() and parseObject() methods to convert Java objects to JSON and vice-versa
 * Allow pre-existing unmodifiable objects to be converted to and from JSON
 * Extensive support of Java Generics
 * Allow custom representations for objects
 * Support arbitrarily complex objects (with deep inheritance hierarchies and extensive use of generic types)

![fastjson](logo.jpg "fastjson")

## Documentation

- [Documentation Home](https://github.com/alibaba/fastjson/wiki)
- [Contributing Code](https://github.com/nschaffner/fastjson/blob/master/CONTRIBUTING.md)
- [Frequently Asked Questions](https://github.com/alibaba/fastjson/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)

## Benchmark

https://github.com/eishay/jvm-serializers/wiki

## Download

- [maven][1]
- [the latest JAR][2]

[1]: https://repo1.maven.org/maven2/com/alibaba/fastjson/
[2]: https://search.maven.org/remote_content?g=com.alibaba&a=fastjson&v=LATEST

## Maven

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.73</version>
</dependency>
```

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.1.72.android</version>
</dependency>
```

## Gradle via JCenter

``` groovy
compile 'com.alibaba:fastjson:1.2.73'
```

``` groovy
compile 'com.alibaba:fastjson:1.1.72.android'
```

Please see this [Wiki Download Page][Wiki] for more repository info.

[Wiki]: https://github.com/alibaba/fastjson/wiki#download

### *License*

Fastjson is released under the [Apache 2.0 license](license.txt).

```
Copyright 1999-2020 Alibaba Group Holding Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at the following link.

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
