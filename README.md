_Note: This is a fully working application with the latest ICU library, however the code has not been updated since August 2009_

# ICU Transliteration Application
## Introduction
The ICU Transliteration Application, originally developed for The Sikher Project (http://www.sikher.com), is a Java application that can transliterate large amounts of Unicode text rapidly through the ICU API (http://site.icu-project.org/). Transliteration can be done between 33 major Unicode scripts including Latin, Arabic, Armenian, Cyrillic, Devanagari, Gurmukhi and Hebrew.

![](http://www.sikher.com/wp-content/downloads/gallery/icu-transliteration/icu-transliteration.jpg)

## Features
* Contains the latest ICU library version 57.1 released 23rd of March 2016
* Full source released under the MIT license
* Cross-platform application supporting Unicode
* Can transliterate 2000 lines per second via the ICU API
* Input and output language script selection between Accents, Arabic, Armenian, Bengali, Cyrillic, Devanagari, Digit, Fullwidth, Georgian, Greek, Gujarati, Gurmukhi/Punjabi, Halfwidth, Han, Hangul, Hebrew, Hiragana, Jamo, Kannada, Katakana, Latin, Malayalam, Name, NumericPinyin, Oryia, Pinyin, Publishing, Simplified, Syriac, Tamil, Telugu, Thaana and Thai.
* Ability to import from and export to a text file, with no file size limit

## Pre-requisites
* You should have [Java](https://java.com/en/download/) or OpenJDK installed. And that's about it!

## Installation
Just use `git` to clone the ssh version:

    git clone git@github.com:sikher/icu-transliteration-application.git

**Or** use `git` to clone the https version:

    git clone https://github.com/sikher/icu-transliteration-application.git

**Or** download the .zip archive and unzip it to the folder `gruntjs-skeleton`:

    https://github.com/sikher/icu-transliteration-application.git

## Running the Application

Navigate to the directory where you downloaded the application and double-click `Translit_Application.jar`

**OR** in a Terminal window type the following command:

    java -jar "Translit_Application.jar"

## Upgrading the ICU Library

Should you want to upgrade (or downgrade) the library just follow these 3 easy steps:

1. Go to the website, http://site.icu-project.org/download & select the version of the ICU library you want to upgrade to under the column **ICU4J** (since we want the Java variant of the library)
2. You should see several files. The one you want will be in the format `icu4j-{version}.jar`. Please ignore the other `icu4j-{version}-src.jar` & `icu4j-{version}-docs.jar` files.
3. Download the `icu4j-{version}.jar` file to the ICU Transliteration Application's `lib` folder, delete the previous file `icu4j-4_2_1.jar`, and rename this new library, whatever it's version may be, to the old file's name: `icu4j-4_2_1.jar`

Note: The library must be named **icu4j-4_2_1.jar** because the ICU Transliteration Application tries to look for the library under this name each time

Additionally, upgrading does not necessarily mean the input and output language options inside the app would automatically get updated. For this, several source files would need to be updated and the application re-compiled. We currently cannot provide support on how to do this.
