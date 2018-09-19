import scalation.linalgebra._
import scalation.columnar_db._
import scalation.plot._
import scalation.analytics.Regression
import scalation.util._
import scala.collection.mutable.Set
import scalation.stat.StatVector.corr

/**
* App for running Project1
*/
object Models extends App{
    println(scalation.DATA_DIR)
    val table = Relation(scalation.DATA_DIR+"dataset1.csv","table",-1,"IDDDD",",",null)
    var mat = table.toMatriD(List.range(0,table.cols))

    //Include a column of all one's for intercept
    //This can be removed
    mat = VectorD.one (mat.dim1) +^: mat
    
    //Select columns (Intercept,year,GDP,Interest_RATE,WPI) as input variables
    val x = mat.selectCols(Array(0,1,3,4,5))
    //Select Money_Printed as output variable
    val y = mat.col(2)

    /////// Model 1 ///////
    var rg = new Regression(x,y)
    rg.train().eval()
    //Display quality of fit values
    banner("Model 1 QoF Information")
    rg.fitMap foreach {x => println(x._1 + ":" + x._2)}
    rg.summary()

    ////// Model 2 - Without WPI ///////
    //Select columns excluding WPI
    val new_x = mat.selectCols(Array(0,1,3,4))
    rg = new Regression(new_x,y)
    rg.train().eval()
    val b = rg.coefficient
    val stdErr = rg.residual
    banner("Model 2 QoF Information")
    rg.fitMap foreach {x => println(x._1 + ":" + x._2)}
    println(rg.summary(b))

    ////// Forward/Backward sel/elim on Model 2 ///////
    ///// Code found in scalalation source
    banner("Forward Selection")
    val cols_i = Set(0)
    for (i <- 1 until new_x.dim2){
        val (x_,b_,fit_) = rg.forwardSel(cols_i)
        println (s"forward model: add x = $x_ with b = $b_ \n fit = $fit_")
        cols_i += x_
    }

    banner("Backward Elim Test")
    val cols = Set(0) ++ Array.range(1,new_x.dim2)
    for(i <- 1 until new_x.dim2){
        val(x,b,fit) = rg.backwardElim(cols)
        println(s"backward model: remove x=$x with b=$b \n fit=$fit")
        cols-=x
    }


    //Collinearity
    banner ("Collinearity Tests")
    println("corr(x) = "+corr(new MatrixD(new_x)))
    println(rg.vif)


    //Plotting predictions
    val t = VectorD.range(0,mat.dim1)
    val yp = rg.predict(new_x)
    new Plot(t,y,yp,"regression")
}

