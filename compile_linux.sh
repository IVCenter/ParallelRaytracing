shopt -s globstar
echo "Compiling..."
mkdir ./bin
javac -d ./bin ./**/*.java
jar -cf ./Nightsky.jar ./bin
echo "Compiling done."
