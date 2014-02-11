#Link to Blog - http://ashkrit.blogspot.sg/2013/07/which-memory-is-faster-heap-or.html

source ~/test-jdk8.sh 

java -version

javac *.java

#SIZE=50000000
SIZE=50000000


java -Xms2g -Xmx2g TestMemoryAllocator HEAP $SIZE

java -Xms2g -Xmx2g TestMemoryAllocator BB $SIZE

java -Xms2g -Xmx2g TestMemoryAllocator DBB $SIZE

java -Xms2g -Xmx2g TestMemoryAllocator OFFHEAP $SIZE


