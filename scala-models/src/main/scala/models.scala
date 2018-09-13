import scalation.linalgebra._
import scalation.columnar_db._
import scalation.plot._
import scalation.analytics.Regression
import scalation.util._

object Model1 extends App{
    println(scalation.DATA_DIR)
    val table = Relation(scalation.DATA_DIR+"dataset1.csv","table",-1,"IDDDD",",",null)
    val mat = table.toMatriD(List.range(0,table.cols))

    val x = mat(mat.range1,2 to mat.dim2).+^:(mat.col(0))
    val y = mat.col(1)

    val rg = new Regression(x,y)
    rg.train().eval()
    println("coefficient = " + rg.coefficient)
    var set = scala.collection.mutable.Set(0,1,2,3)

    //Forward-Backward elim
    // println(rg.backwardElim(set))

    //Plotting example
    Plot(mat.col(0).toInt,mat.col(4).toInt,_title="Title")
    Plot(mat.col(0).toInt,rg.predict(x).toInt,_title="Title")
}
