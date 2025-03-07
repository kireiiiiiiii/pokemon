@echo off
setlocal

echo Checking Java compiler...
if not exist "%JAVA_HOME%\bin\javac.exe" (
    echo Error: Java compiler not found. Ensure JDK is installed and JAVA_HOME is set.
    exit /b 1
)

echo Creating build directory...
if not exist build (
    echo Running: mkdir build
    mkdir build
    echo Build directory created.
) else (
    echo Build directory already exists.
)

echo Checking Java files...
dir /s /b "src\main\java\*.java"
if %errorlevel% neq 0 (
    echo Error: No Java files found.
    exit /b 1
)

echo Compiling Java files...
dir /s /b "src\main\java\*.java" > filelist.txt
javac -d build @"filelist.txt"
del filelist.txt

if %errorlevel% neq 0 (
    echo Error: Compilation failed.
    exit /b 1
)

echo Creating JAR file...
echo Main-Class: pokemon.App > "build\MANIFEST.MF"
jar cfm "build\pokemon.jar" "build\MANIFEST.MF" -C build .

if %errorlevel% neq 0 (
    echo Error: Failed to create JAR file.
    exit /b 1
)

echo Build successful.
