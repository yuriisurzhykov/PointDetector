# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepclassmembers class * extends com.yuriisurzhykov.pointdetector.uicomponents.list.AbstractViewHolder {
    public <init>();
    public<init>(android.view.View);
}

-keepattributes Signature

-keepclasseswithmembers class com.yuriisurzhykov.pointdetector.data.cache.entities.** {
    public ** component1();
    <fields>;
}
-keepclassmembers class com.google.firebase.database.GenericTypeIndicator { *; }
-keep class * extends com.google.firebase.database.GenericTypeIndicator { *; }
-keepclassmembers class * extends com.google.firebase.database.GenericTypeIndicator {
    <init>(...);
}
-keep class com.google.firebase.database.GenericTypeIndicator { *; }
-keep @kotlinx.serialization.Serializable class * {*;}

# Keep Dependency Injection Framework related classes and methods
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.annotation.** { *; }
# Keep empty constructors with @Inject annotation
-keepclassmembers,allowobfuscation @javax.inject.Inject class * {
    <init>(...);
}