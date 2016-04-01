shopt -s globstar
echo "Compiling..."
javac -d ./bin ./src/**.java
jar -cf ./Nightsky.jar ./bin
echo "Compiling done."
