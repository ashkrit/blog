package scalaobjects

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object MemoryAllocationApp {

  def main(args: Array[String]): Unit = {

    val allocateType = args(0)
    val count = args(1).toInt

    val allocator = allocateType match {
      case "case" => new CaseClassAllocator
      case "tuple" => new TupleAllocator
    }

    val bucket = ArrayBuffer[Object]()
    usedMemory
    lotsOfGC
    val memory = usedMemory

    for (item <- 0 to count) {
      bucket += allocator.create("GOOG", "NYSE", item, Random.nextDouble())
    }

    lotsOfGC
    val totalMemory = usedMemory - memory
    println(s"Type ${allocateType} took ${totalMemory / 1024} KB for ${count} object")
  }

  def usedMemory: Long = Runtime.getRuntime.totalMemory - Runtime.getRuntime.freeMemory

  def lotsOfGC(): Unit = {
    for (_ <- 0 to 20) {
      System.gc()
      Thread.sleep(100)
    }
  }

}


trait ObjectAllocator {
  def create(symbol: String, exchange: String, qty: Int, price: Double): Object
}

class CaseClassAllocator extends ObjectAllocator {
  override def create(symbol: String, exchange: String, qty: Int, price: Double): Object = Trade(symbol, exchange, qty, price)
}

class TupleAllocator extends ObjectAllocator {
  override def create(symbol: String, exchange: String, qty: Int, price: Double): Object = (symbol, exchange, qty, price)
}