# Android Security Sandbox

A sandbox app with some tools and code to help you to better secure your Android apps.

[Related presentation - pt-BR](https://speakerdeck.com/rafaeltoledo/seguranca-no-android-1)

## This sample includes
1. Obfuscation using Proguard and `android-proguard-optimize` rules
2. Encrypted database storage using [SQLCipher](https://www.zetetic.net/sqlcipher/sqlcipher-for-android)
3. Encrypted key-value storage using [Hawk](https://github.com/orhanobut/hawk), powered by [Conceal](https://facebook.github.io/conceal/)
4. Device checking using [SafetyNet](https://developers.google.com/android/reference/com/google/android/gms/safetynet/SafetyNet)
5. Root detection using [RootBeer](https://github.com/scottyab/rootbeer)
6. HTTP pinning using [OkHttp](https://github.com/square/okhttp/wiki/HTTPS)
7. Other environment checks, like *Debug*, *Emulator*, *Installation Source*, and *Tampering*
