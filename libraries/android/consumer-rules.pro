-keep class * implements androidx.viewbinding.ViewBinding {
    public static *** bind(android.view.View);
    public static *** inflate(android.view.LayoutInflater);
    public static *** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
}

-keep class androidx.lifecycle.DefaultLifecycleObserver

-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}
