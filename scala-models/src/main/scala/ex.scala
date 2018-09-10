import scalation.linalgebra._
import scalation.columnar_db._
import scalation.plot._
import scalation.analytics.Regression
import scalation.util._

object Model1 extends App{
    println(scalation.DATA_DIR)
    val table = MatrixD(scalation.DATA_DIR+"dataset1.csv")
    println(table)
    //println(table.sigma[Int]("year", _ == 1970))
    
    //val mat = table.toMatriD(_,_)
    //val rg = Regression(mat)

    println("\n")
}
