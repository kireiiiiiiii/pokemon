#!/bin/bash

set -e

mkdir -p build

javac -d build $(find src/main/java -name "*.java")
echo "Main-Class: pokemon.App" >build/MANIFEST.MF
jar cfm build/pokemon.jar build/MANIFEST.MF -C build .

echo "Build successful. JAR file is located at build/pokemon.jar"
