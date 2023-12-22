-dontwarn java.lang.invoke.*	

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule	
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {	
  **[] $VALUES;	
  public *;	
}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {	
    volatile <fields>;	
}

-dontwarn kotlin.time.ClockMark
-dontwarn kotlin.time.MonoClock

-dontwarn org.slf4j.impl.StaticLoggerBinder

# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}	
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}	

# OkHttp
-keepattributes Signature	
-keepattributes *Annotation*	
-keep class okhttp3.** { *; }	
-keep interface okhttp3.** { *; }	
-dontwarn okhttp3.**	

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**	

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit	

# Application models
-keep class com.cinema.entract.app.model.** { *; }	
-keep class com.cinema.entract.data.model.** { *; }	
-keep class com.cinema.entract.remote.model.** { *; }
