Code for blog -

-Without GC logs
java split.StringSplitTestApp

-With GC logs to see GC activity
java  -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps split.StringSplitTestApp
