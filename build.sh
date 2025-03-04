#!/bin/bash

mkdir -p build

javac -d build $(find src/java -name "*.java")
echo "Main-Class: pokemon.App" >build/MANIFEST.MF
jar cfm build/pokemon.jar build/MANIFEST.MF -C build .

echo "Build successful. JAR file is located at build/pokemon.jar"
