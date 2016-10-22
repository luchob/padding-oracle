# padding-oracle

##Introduction

This is a small java application that demonstrates a successfull _Padding Oracle_ attack.

The rough idea is that a text encrypted with a block cipher (e.g. AES, 3DES, etc) in CBC mode of operation and PKCS7 padding may be decrypted without knowledge of the secret key. The only thing that is necessary is the so called _Padding oracle_. A padding oracle leaks data about whether the padding of an encrypted message is correct or not. Just that.

The application encryts a text message and tries to decrypt it. The bundle contains a padding oracle.

More details about padding oracle attacks are available [here|https://en.wikipedia.org/wiki/Padding_oracle_attack].
There was missing information about some implementation cornercases - please see [here|http://crypto.stackexchange.com/questions/40800/is-the-padding-oracle-attack-deterministic].

## How to run the application

### Clone the repository

On a Windows machine, for exmple:

```
mkdir po
cd po
git clone https://github.com/luchob/padding-oracle.git .
```

### Run the application

Type `gralew run`. You should see output similar to the following:

```
gradlew run
:compileJava
:processResources UP-TO-DATE
:classes
:run
15:39:06.823 [main] DEBUG eu.balev.poracle.Main - Going to try to encrypt a given text with AES/CBC/PKCS5Padding and decrypt it withouth knowledge of the key with a padding oracle attack.
15:39:07.273 [main] DEBUG eu.balev.poracle.Main - Length of the text to encrypt is 78 bytes.
15:39:07.273 [main] DEBUG eu.balev.poracle.Main - Length of the encrypted text is 80. Spread over 5 blocks of 16 bytes.
15:39:07.723 [main] DEBUG eu.balev.poracle.Main - Original  text is: Hi, I'm Lucho! This is a test plain text, encrypted with AES/CBC/PKCS5Padding.
15:39:07.723 [main] DEBUG eu.balev.poracle.Main - Decrypted text is: Hi, I'm Lucho! This is a test plain text, encrypted with AES/CBC/PKCS5Padding.

BUILD SUCCESSFUL

Total time: 5.903 secs
```

## How to explore the application

The application can be easily explored and experimented with in the Eclipse IDE. The application comes with a gradle wrapper and eclipse plugin. The eclipse plugin is able to generate Eclipse project files. To genreate Eclipse project files run:

```
gradlew eclipse
```

After that import the application as existing Eclipse project. There is single class `Main` which is an entry point to the app. If another IDE is used please import the java sources as appropriate for it.

## Notes

The performance of the application can be improved in the future by:

* paralelizing the decryption operations
* improving the ambiguity resolution whenever there are padding byte conflicts

Please note that this is just an educational application and not all of the best coding practices are taken into account when creating it.

## Bug reports

Bug reports and comments are welcome!
