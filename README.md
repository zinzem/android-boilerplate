# Android Boilerplate

Creating a new project can be a bit annoying as it does not come with everything you would expect.
This boilerplate repo comes with minimal dependencies like Koin, Okhttp or Mockkk. It also has basic multimodule split.

Now this would not be usefull without the `script/boilerplate.sh` script:
```
script/boilerplate.sh MyApp com.myapp.android
```
This script will copy all the folders and files (except `script/`) into a new `myapp` folder and take care of the changes required like replace all package names, change the app name to `MyApp`, the applicationId to `com.myapp.android` and more. 
