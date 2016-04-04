shopt -s globstar
echo "Compiling..."
mkdir ./bin
javac -d ./bin $(find ./src -type f -name "*.java")
jar -cf ./Nightsky.jar -C ./bin .
echo "Compiling done."
