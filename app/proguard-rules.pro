

#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

-keep class cn.ifreedomer.com.softmanager.bean.**{*;}
-keep class cn.ifreedomer.com.softmanager.model.**{*;}
-keep class cn.ifreedomer.com.softmanager.widget.**{*;}
-keep class com.zzzmode.appopsx.**{*;}
#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

-keep class com.bumptech.glide.**{*;}
-keep class com.google.gson.**{*;}
-keep class butterknife.**{*;}
-keep class com.umeng.analytics.**{*;}
-keep class com.zhy.**{*;}
-keep class okhttp3.**{*;}
-keep class okio.**{*;}
-keep class retrofit2.**{*;}
-keep class com.squareup.**{*;}
-keep class com.tbruyelle.**{*;}
-keep class org.hamcrest.**{*;}
-keep class io.reactivex.**{*;}
-keep class com.jakewharton.**{*;}
-keep class com.jakewharton.**{*;}
-keep class com.sun.mail.**{*;}

-keep class com.sun.mail.**{*;}
-keep class com.alipay.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}
-keep class org.apache.harmony.**{*;}
-keep class myjava.awt.datatransfer.**{*;}
-keep class com.sun.activation.**{*;}
-keep class javax.activation.**{*;}

#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------



#-------------------------------------------------------------------------





#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-dontwarn android.support.v4.**
-keep class android.support.v4.**  { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment




-optimizationpasses 5
#
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
#
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
#
# 包含有类名->混淆后类名的映射关系
-verbose
#
# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses
-ignorewarnings  # 忽略警告，避免打包时某些警告出现

#
# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify
#
# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses
#
# 避免混淆泛型
-keepattributes Signature
#
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
#
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*

-dontwarn
-dontnote

-ignorewarnings  # 忽略警告，避免打包时某些警告出现
