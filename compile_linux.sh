shopt -s globstar
echo "Compiling..."
javac -d ./bin ./**/*.java
jar -cf ./Nightsky.jar ./bin
echo "Compiling done."
