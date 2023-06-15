if [  $# -lt 2 ]
then
	echo "Usage: $0 [app name] [package name]"
	exit 1
fi

appname=$1
appnamel=$(echo "$1" | tr '[:upper:]' '[:lower:]')
packagename=$2
packagepath=$(echo "$packagename" | sed -e "s/\./\//g")

# Handle root
mkdir "$appnamel"
cp -r gradle/ "$appnamel"/gradle
cp .gitignore "$appnamel"/
cp build.gradle "$appnamel"/
cp gradle.properties "$appnamel"/
cp gradlew "$appnamel"/
cp gradlew.bat "$appnamel"/
cp gradle.properties "$appnamel"/
echo "# $appname" >> "$appnamel"/README.md
cp settings.gradle "$1"/

# Handle app/src/debug/
mkdir -p "$appnamel"/app/src/debug/res/values/
cp app/src/debug/res/values/strings.xml "$appnamel"/app/src/debug/res/values/strings.xml
sed -i '' "s/Boilerplate/$appname/g" "$appnamel"/app/src/debug/res/values/strings.xml

# Handle app/src/main/
mkdir -p "$appnamel"/app/src/main
cp app/src/main/AndroidManifest.xml "$appnamel"/app/src/main/AndroidManifest.xml
cp -r app/src/main/res/ "$appnamel"/app/src/main/res/
sed -i '' "s/Boilerplate/$appname/g" "$appnamel"/app/src/main/res/values/strings.xml
mkdir -p "$appnamel"/app/src/main/java/"$packagepath"
cp -r app/src/main/java/com/boilerplate/android/ "$appnamel"/app/src/main/java/"$packagepath"/
grep -rl com.boilerplate.android "$appnamel"/app/src/main/ | xargs sed -i '' "s/com.boilerplate.android/$packagename/g"
cp app/.gitignore "$appnamel"/app/.gitignore
cp app/build.gradle "$appnamel"/app/build.gradle
cp app/proguard-rules.pro "$appnamel"/app/proguard-rules.pro

# Handle app/src/test/
mkdir -p "$appnamel"/app/src/test/java/"$packagepath"

# Handle app/src/androidTest/
mkdir -p "$appnamel"/app/src/androidTest/java/"$packagepath"

# Handle core/src/main/
mkdir -p "$appnamel"/core/src/main
cp core/src/main/AndroidManifest.xml "$appnamel"/core/src/main/AndroidManifest.xml
cp -r core/src/main/res/ "$appnamel"/core/src/main/res/
mkdir -p "$appnamel"/core/src/main/java/"$packagepath"
cp -r core/src/main/java/com/boilerplate/android/ "$appnamel"/core/src/main/java/"$packagepath"/
grep -rl com.boilerplate.android "$appnamel"/core/src/main/ | xargs sed -i '' "s/com.boilerplate.android/$packagename/g"
cp core/.gitignore "$appnamel"/core/.gitignore
cp core/build.gradle "$appnamel"/core/build.gradle
cp core/proguard-rules.pro "$appnamel"/core/proguard-rules.pro

# Handle core/src/test/
mkdir -p "$appnamel"/core/src/test/java/"$packagepath"
