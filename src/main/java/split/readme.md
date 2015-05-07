Code for blog - http://ashkrit.blogspot.com/2015/05/experiment-with-string-split.html

-Without GC logs <BR>

java split.StringSplitTestApp

-With GC logs to see GC activity <BR>
java  -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps split.StringSplitTestApp
