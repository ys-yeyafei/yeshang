# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\SDK/tools/proguard/proguard-android.txt
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

#指定代码的压缩级别
    -optimizationpasses 5

    #包明不混合大小写
    -dontusemixedcaseclassnames

    #不去忽略非公共的库类
    -dontskipnonpubliclibraryclasses

     #优化  不优化输入的类文件
    -dontoptimize

     #预校验
    -dontpreverify

     #混淆时是否记录日志
    -verbose

     # 混淆时所采用的算法
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

    #保护注解
    -keepattributes *Annotation*

    # 保持哪些类不被混淆
    -keep public class * extends android.app.Fragment
    -keep public class * extends android.app.Activity
    -keep public class * extends android.app.Application
    -keep public class * extends android.app.Service
    -keep public class * extends android.content.BroadcastReceiver
    -keep public class * extends android.content.ContentProvider
    -keep public class * extends android.app.backup.BackupAgentHelper
    -keep public class * extends android.preference.Preference
    -keep public class com.android.vending.licensing.ILicensingService
    #如果有引用v4包可以添加下面这行
    -keep public class * extends android.support.v4.app.Fragment
    -keep public class * extends android.app.Dialog
    -keep public class * extends android.view


     #宝付
     -keep public class com.baofoo.sdk.vip.BaofooPayActivity{*;}



    #忽略警告
    -ignorewarning

    ##记录生成的日志数据,gradle build时在本项目根目录输出##

    #apk 包内所有 class 的内部结构
    -dump class_files.txt
    #未混淆的类和成员
    -printseeds seeds.txt
    #列出从 apk 中删除的代码
    -printusage unused.txt
    #混淆前后的映射
    -printmapping mapping.txt


    #保留一个完整的包
    -keep class cn.ysgroup.ysdai.Beans.** {
        *;
     }


    #如果引用了v4或者v7包
    -dontwarn android.support.**
    -keep class android.support.**{*;}       #不进行混淆
    -keep interface android.support.**{*;}


    -keep public class * extends android.view.View {
        public <init>(android.content.Context);
        public <init>(android.content.Context, android.util.AttributeSet);
        public <init>(android.content.Context, android.util.AttributeSet, int);
        public void set*(...);
    }

    #保持 native 方法不被混淆
    -keepclasseswithmembernames class * {
        native <methods>;
    }

    #保持自定义控件类不被混淆
    -keepclasseswithmembers class * {
        public <init>(android.content.Context, android.util.AttributeSet);
    }

    #保持自定义控件类不被混淆
    -keepclassmembers class * extends android.app.Activity {
       public void *(android.view.View);
    }

    #保持 Parcelable 不被混淆
    -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
    }

    #保持 Serializable 不被混淆
    -keepnames class * implements java.io.Serializable

    #保持 Serializable 不被混淆并且enum 类也不被混淆
    -keepclassmembers class * implements java.io.Serializable {
        static final long serialVersionUID;
        private static final java.io.ObjectStreamField[] serialPersistentFields;
        !static !transient <fields>;
        !private <fields>;
        !private <methods>;
        private void writeObject(java.io.ObjectOutputStream);
        private void readObject(java.io.ObjectInputStream);
        java.lang.Object writeReplace();
        java.lang.Object readResolve();
    }


    -keepclassmembers class * {
        public void *ButtonClicked(android.view.View);
    }

    #不混淆资源类
    -keepclassmembers class **.R$* {
        public static <fields>;
    }


    ####################gson##################
    -keepattributes Signature
    # Gson specific classes
    -keep class sun.misc.Unsafe { *; }
    # Application classes that will be serialized/deserialized over Gson
    -keep class com.google.gson.examples.android.model.** { *; }
    ####################fastjson##################
    -keep class com.alibaba.fastjson.** { *; }
    -dontwarn com.alibaba.fastjson.**
    ####################butterknife##################
    -keepclassmembers class ** {
        public void onEvent*(**);
    }
   ####################butterknife##################
      -keep class butterknife.** { *; }
      -dontwarn butterknife.internal.**
      -keep class **$$ViewBinder { *; }

      -keepclasseswithmembernames class * {
          @butterknife.* <fields>;
      }

      -keepclasseswithmembernames class * {
          @butterknife.* <methods>;
      }

    ####################volley##################
    -keep class com.android.volley.** {*;}
    -keep class com.android.volley.toolbox.** {*;}
    -keep class com.android.volley.Response$* { *; }
    -keep class com.android.volley.Request$* { *; }
    -keep class com.android.volley.RequestQueue$* { *; }
    -keep class com.android.volley.toolbox.HurlStack$* { *; }
    -keep class com.android.volley.toolbox.ImageLoader$* { *; }
    -keep public class cn.ysgroup.ysdai.Util.PostStringRequest { *;}
    -keep public class cn.ysgroup.ysdai.Util.PostStringWithCookieRequest { *;}

    ####################umeng##################

    -dontoptimize
    -dontwarn android.webkit.WebView
    -dontwarn com.umeng.**
    -dontwarn
    -keepattributes Exceptions,InnerClasses,Signature
    -keepattributes SourceFile,LineNumberTable

    -keep public interface com.umeng.socialize.**
    -keep public interface com.umeng.socialize.sensor.**
    -keep public interface com.umeng.scrshot.**

    -keep public class com.umeng.socialize.* {*;}
    -keep public class javax.**
    -keep public class android.webkit.**

    -keep class com.umeng.scrshot.**
    -keep public class com.tencent.** {*;}
    -keep class com.umeng.socialize.sensor.**

    -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

    -keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

    -keep public class [your_pkg].R$*{
        public static final int *;
    }
    -keepclassmembers class * {
        public <init> (org.json.JSONObject);
     }
     -keepclassmembers enum * {
         public static **[] values();
         public static ** valueOf(java.lang.String);
     }

    ####################zxing#####################
    -keep class com.google.zxing.** {*;}
    -dontwarn com.google.zxing.**
    ################httpmime/httpcore##########
    -keep class org.apache.http.** {*;}
    -dontwarn org.apache.http.**
     ################bugly##########
    -dontwarn com.tencent.bugly.**
    -keep public class com.tencent.bugly.**{*;}

    -dontoptimize
    -dontpreverify

    -dontwarn cn.jpush.**
    -keep class cn.jpush.** { *; }