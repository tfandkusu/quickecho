# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/toya/app/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepattributes Signature

# onEventメソッドの名前を変えない
-keepclassmembers class ** {
    public void onEvent*(***);
}
-keepnames class jp.bellware.echo.view.setting.SettingActivityAlias
-keepnames class jp.bellware.echo.view.memo.SoundMemoActivityAlias

-keep public class androidx.navigation.fragment.** {
  public *;
}
