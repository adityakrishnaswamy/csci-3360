import scalation.linalgebra._
import scalation.columnar_db._
import scalation.plot._
import scalation.analytics.Regression
import scalation.util._

object Model1 extends App{
    println(scalation.DATA_DIR)
    val table = Relation(scalation.DATA_DIR+"dataset1.csv","table",-1,"IDDDD",",",null)
    val mat = table.toMatriD(List.range(0,table.cols))

    //val x = mat(mat.range1,2 to mat.dim2).+^:(mat.col(0))
    val cols = Array(0,1,2,3,4)
    val x = mat.selectCols(Array(0,2,3,4))
    val y = mat.col(cols(1))

    var rg = new Regression(x,y)
    rg.train().eval()
    println("coefficients w/ all features = " + rg.coefficient)
    println("r2 w/ all features = "+rg.fit()(0))

    //Set of column indeces for forward/backward selection
    var colSet = scala.collection.mutable.Set(0,1,2,3)

    //Forward-Backward elim
    println(backward_elim(rg,rg.backwardElim,colSet)) 

    val new_x = mat.selectCols(Array(0,2))
    rg = new Regression(new_x,y)
    rg.train().eval()
    colSet = scala.collection.mutable.Set(0)   
    println(forward_sel(rg,rg.forwardSel,colSet))


    def backward_elim(rg: Regression, elim_func: scala.collection.mutable.Set[Int] => (Int,VectoD,VectoD), cols_set:scala.collection.mutable.Set[Int]):Double = {
        val initial_r2 = rg.fit()(0)
        var best_r2 = initial_r2
        var result = elim_func(cols_set);
        var r2 = result._3(0)
        println("Current r2 = "+r2 + " vs best_r2 = "+ best_r2)
        if(best_r2 > r2){
            return best_r2
        } else {
            return backward_elim(rg,elim_func,cols_set-result._1)
        }
    }

    def forward_sel(rg: Regression, elim_func: scala.collection.mutable.Set[Int] => (Int,VectoD,VectoD), cols_set:scala.collection.mutable.Set[Int]): Double = {
        val initial_r2 = rg.fit()(0)
        var best_r2 = initial_r2
        var result = elim_func(cols_set);
        var r2 = result._3(0)
        println("Current r2 = "+r2 + " vs best_r2 = "+ best_r2)
        if(best_r2 > r2){
            return best_r2
        } else {
            return forward_sel(rg,elim_func,cols_set+result._1)
        }
    }

}

