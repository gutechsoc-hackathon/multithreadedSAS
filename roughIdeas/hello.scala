import scala.collection.parallel.mutable.ParHashMap

class Node(value: Int, id: Int){
    var v = value
    var i = id
    override def toString(): String = v toString
}

object testParHashMap{
    def visit(entry: Tuple2[Int, Node]){
        entry._2.v += 1
    }
    def main(args: Array[String]){
        val hm = new ParHashMap[Int, Node]()
        for (i <- 1 to 10){
            var node = new Node(0, i)
            hm.put(node.i, node)
        }

        println("========== BEFORE ==========")
        hm.foreach{println}

        hm.foreach{visit}

        println("========== AFTER ==========")
        hm.foreach{println}

    }
}
