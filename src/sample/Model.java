package sample;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Model {

    //************EXTERNAL CALCULATION METHOD - START*****************//
    //Method for Calculation of Addition, Substraction, Multiplication, Division, Percentage
    public static BigDecimal calculate (String operator, BigDecimal left, BigDecimal right){
        switch (operator) {
            case "+":
                return left.add(right);
            case "-":
                return left.subtract(right);
            case "×":
                return left.multiply(right);
            case "÷":
                return left.divide(right, 3, RoundingMode.HALF_UP);
            case "%":
                return (left.divide(right, 3 , RoundingMode.HALF_UP)).multiply(new BigDecimal(100));
        }
        return right;
    }
    //************EXTERNAL CALCULATION METHOD - END*****************//



    //************EXTERNAL EXPRESSION EVALUATION METHOD - START*****************//
    //Method for calculating long expressions entered by user before pressing equals
    public static BigDecimal evaluate (String expression) throws ScriptException {
        expression = expression.replace("×", "*");
        expression = expression.replace("÷", "/");
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        return new BigDecimal(engine.eval(expression).toString());
    }
    //************EXTERNAL EXPRESSION EVALUATION METHOD - END*****************//

}
