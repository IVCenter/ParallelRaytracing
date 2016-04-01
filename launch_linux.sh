echo "Launching..."
java -cp ./bin -XX:+AggressiveOpts -XX:CompileThreshold=1 -XX:+UseFastAccessorMethods system.Launcher
