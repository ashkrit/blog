package scalaobjects

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

trait MemoryAccess {
  def create(): Unit

  def calculate(): Double
}


object MemoryAccessTestApp {

  def main(args: Array[String]): Unit = {

    val allocateType = args(0)
    val count = args(1).toInt

    val allocator = allocateType match {
      case "case" => new CaseClassAccess(count)
      case "tuple" => new TupleClassAccess(count)
      case "java" => new JavaClassAccess(count)
    }


    allocator.create()
    var totalValue = 0d
    Range(0, 20).foreach((_) => {
      val value = allocator.calculate()
      totalValue += value
    })

    println(totalValue)

  }

}

class CaseClassAccess(size: Int) extends MemoryAccess {

  val buffer = ArrayBuffer[Trade]()

  override def create(): Unit = {
    for (item <- 0 to size) {
      buffer.append(Trade("GOOG", "NYSE", item, Random.nextDouble()))
    }
    println("Allocation Done")

  }

  override def calculate(): Double = {
    val start = System.currentTimeMillis()
    var totalValue = 0.0d
    for (item <- 0 to size) {
      totalValue += buffer(item).price
    }
    val total = System.currentTimeMillis() - start

    println(s" ${this.getClass.getName} Time taken for ${size} objects is ${total} ms")
    totalValue

  }
}

class JavaClassAccess(size: Int) extends MemoryAccess {

  val buffer = ArrayBuffer[JavaTrade]()

  override def create(): Unit = {
    for (item <- 0 to size) {
      buffer.append(new JavaTrade("GOOG", "NYSE", item, Random.nextDouble()))
    }
    println("Allocation Done")

  }

  override def calculate(): Double = {
    val start = System.currentTimeMillis()
    var totalValue = 0.0d
    for (item <- 0 to size) {
      totalValue += buffer(item).price
    }
    val total = System.currentTimeMillis() - start

    println(s" ${this.getClass.getName} Time taken for ${size} objects is ${total} ms")
    totalValue

  }
}


class TupleClassAccess(size: Int) extends MemoryAccess {

  val bucket = ArrayBuffer[(String, String, Int, Double)]()

  override def create(): Unit = {
    for (item <- 0 to size) {
      bucket.append(("GOOG", "NYSE", item, Random.nextDouble()))
    }
    println("Allocation Done")

  }

  override def calculate(): Double = {
    val start = System.currentTimeMillis()
    var totalValue = 0.0d
    for (item <- 0 to size) {
      totalValue += bucket(item)._4
    }
    val total = System.currentTimeMillis() - start

    println(s" ${this.getClass.getName} Time taken for ${size} objects is ${total} ms")
    totalValue
  }
}
