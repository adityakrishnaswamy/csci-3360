import scalation.linalgebra._
import scalation.columnar_db._
import scalation.plot._
import scalation.analytics.Regression
import scalation.util._
import scala.collection.mutable.Set

object Model1 extends App{
    println(scalation.DATA_DIR)
    val table = Relation(scalation.DATA_DIR+"dataset1.csv","table",-1,"IDDDD",",",null)
    val mat = table.toMatriD(List.range(0,table.cols))
    
    val x = mat.selectCols(Array(0,2,3,4))
    val y = mat.col(1)

    /////// Model 1 ///////

    var rg = new Regression(x,y)
    rg.train().eval()
    //Display quality of fit values
    println("QoF Information = "+rg.fitMap)

    ////// Model 2 - Without WPI ///////
    val new_x = mat.selectCols(Array(0,2,3))
    rg = new Regression(new_x,y)
    rg.train().eval()
    println("QoF Information = "+rg.fitMap)

    ////// Forward/Backward sel/elim on Model 2 ///////
    //I don't really know how to interpret these return values
    //the documentation says backwardElim and forwardSel return the index
    //of the feature to add or remove from the model 
    println("Reduced fit w/ backwardSel: "+rg.backwardElim(Set(0)++Array.range(1,new_x.dim2)))
    println("Reduce fit w/ forwardSel: "+rg.forwardSel(Set(0)--Array.range(1,new_x.dim2)))
}

