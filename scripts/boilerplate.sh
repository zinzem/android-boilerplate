if [  $# -lt 2 ]
then
	echo "Usage: $0 [app name] [package name]"
	exit 1
fi

appname=$1
appnamel=$(echo "$1" | tr '[:upper:]' '[:lower:]')
packagename=$2
packagepath=$(echo "$packagename" | sed -e "s/\./\//g")
targetroot=$(dirname "$0")/..

# Handle root
mkdir "$appnamel"
cp -r "$targetroot"/gradle/ "$appnamel"/gradle
cp "$targetroot"/.gitignore "$appnamel"/
cp "$targetroot"/build.gradle "$appnamel"/
cp "$targetroot"/gradle.properties "$appnamel"/
cp "$targetroot"/gradlew "$appnamel"/
cp "$targetroot"/gradlew.bat "$appnamel"/
cp "$targetroot"/gradle.properties "$appnamel"/
echo "# $appname" >> "$appnamel"/README.md
cp "$targetroot"/settings.gradle "$appnamel"/

# Handle app/src/debug/
mkdir -p "$appnamel"/app/src/debug/res/values/
cp "$targetroot"/app/src/debug/res/values/strings.xml "$appnamel"/app/src/debug/res/values/strings.xml
sed -i '' "s/Boilerplate/$appname/g" "$appnamel"/app/src/debug/res/values/strings.xml

# Handle app/src/main/
mkdir -p "$appnamel"/app/src/main
cp "$targetroot"/app/src/main/AndroidManifest.xml "$appnamel"/app/src/main/AndroidManifest.xml
cp -r "$targetroot"/app/src/main/res/ "$appnamel"/app/src/main/res/
sed -i '' "s/Boilerplate/$appname/g" "$appnamel"/app/src/main/res/values/strings.xml
mkdir -p "$appnamel"/app/src/main/java/"$packagepath"
cp -r "$targetroot"/app/src/main/java/com/boilerplate/android/ "$appnamel"/app/src/main/java/"$packagepath"/
grep -rl com.boilerplate.android "$appnamel"/app/src/main/ | xargs sed -i '' "s/com.boilerplate.android/$packagename/g"
cp "$targetroot"/app/.gitignore "$appnamel"/app/.gitignore
cp "$targetroot"/app/build.gradle "$appnamel"/app/build.gradle
cp "$targetroot"/app/proguard-rules.pro "$appnamel"/app/proguard-rules.pro

# Handle app/src/test/
mkdir -p "$appnamel"/app/src/test/java/"$packagepath"

# Handle app/src/androidTest/
mkdir -p "$appnamel"/app/src/androidTest/java/"$packagepath"

# Handle core/src/main/
mkdir -p "$appnamel"/core/src/main
cp "$targetroot"/core/src/main/AndroidManifest.xml "$appnamel"/core/src/main/AndroidManifest.xml
cp -r "$targetroot"/core/src/main/res/ "$appnamel"/core/src/main/res/
mkdir -p "$appnamel"/core/src/main/java/"$packagepath"
cp -r "$targetroot"/core/src/main/java/com/boilerplate/android/ "$appnamel"/core/src/main/java/"$packagepath"/
grep -rl com.boilerplate.android "$appnamel"/core/src/main/ | xargs sed -i '' "s/com.boilerplate.android/$packagename/g"
cp "$targetroot"/core/.gitignore "$appnamel"/core/.gitignore
cp "$targetroot"/core/build.gradle "$appnamel"/core/build.gradle
cp "$targetroot"/core/proguard-rules.pro "$appnamel"/core/proguard-rules.pro

# Handle core/src/test/
mkdir -p "$appnamel"/core/src/test/java/"$packagepath"
