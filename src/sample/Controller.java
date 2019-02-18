package sample;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.script.ScriptException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Controller implements Initializable {
    //Declaring internal variables.....
    private BigDecimal left;
    private String selectedOperator;
    private boolean numberInputting;

    @FXML
    private JFXTextField textField;
    @FXML
    private JFXTextField textField2;


    //Constructor for setting default values
    public Controller() {
        this.left = BigDecimal.ZERO;
        this.selectedOperator = "";
        this.numberInputting = false;
    }



//************EVENT HANDLING METHOD - START*****************//

    //Creating EventListener for the buttons and creating entries for each button input
    @FXML
    public void operateButton(ActionEvent ae) throws ScriptException {
        //initialize the buttons to get button-source of click/press
        JFXButton button = (JFXButton) ae.getSource();
        String buttonText = button.getText();



        //**************HANDLING CLEAR BUTTONS*************//

                //Clearing the entire textField i.e. clearing the entire calculator and setting defaults
                if (buttonText.equals("C")) {
                    selectedOperator="";
                    left = BigDecimal.ZERO;
                    numberInputting = false;
                    textField.clear();
                    textField2.clear();
                    return;
                }

                //clearing a single entry entered into the textField
                if (buttonText.equals("CE")) {
                    //Clear top field if CE is pressed again after bottom field is empty
                    if(textField.getText().equals("") && !textField2.getText().equals("")){
                        textField2.clear();
                        selectedOperator="";
                        left = BigDecimal.ZERO;
                        numberInputting = false;
                        return;
                    }
                    //Clear both fields if CE is pressed again and if top field contains "="
                    else if(textField2.getText().contains("=")){
                        textField.clear();
                        textField2.clear();
                        selectedOperator="";
                        left = BigDecimal.ZERO;
                        numberInputting = false;
                        return;
                    }
                    // Clear only part of Top Field to match CE entry on bottom field
                    else {
                        StringBuilder builder = new StringBuilder();
                        int x = 0;
                        String old = textField2.getText();
                        String remove = textField.getText();
                        Matcher m = Pattern.compile(remove).matcher(old);
                        while (m.find()) {
                            builder.append(old.substring(x, m.start()));
                            x = m.end();
                        }
                        textField2.setText(builder.toString());
                        textField.setText("");
                    }
                    return;
                }

                //deleting single button inputs
                if (buttonText.equals("⌫")) {
                    //Clear top field if it has an "equals" sign when clearing bottom field completely
                    int index2 = textField2.getLength();
                    if(textField2.getText().contains("=")) {
                        textField.clear();
                        textField2.clear();
                        selectedOperator="";
                        left = BigDecimal.ZERO;
                        numberInputting = false;
                        return;
                    }
                    //If both fields are clear
                    else if(textField.getText().equals("") && textField2.getText().equals("")){
                        return;
                    }
                    //Clear entire top field on a second press of del button if bottom field is clear
                    else if(textField.getText().equals("") && !textField2.getText().equals("")){
                        textField2.clear();
                        selectedOperator="";
                        left = BigDecimal.ZERO;
                        numberInputting = false;
                        return;
                    }
                    //reset calculator if something is solved and del is pressed
                    else if(textField2.getText(index2 - 1, index2).matches("[-×÷+%]")){
                        textField.clear();
                        textField2.clear();
                        selectedOperator="";
                        left = BigDecimal.ZERO;
                        numberInputting = false;
                        return;
                    }

                    //Clear single entries from both fields
                    else {
                        if(textField.getText().equals("") && textField2.getText().equals("")){
                            return;
                        }
                        textField.setText(textField.getText(0, textField.getLength() - 1));
                        textField2.setText(textField2.getText(0, textField2.getLength() - 1));

                    }
                    return;
                }
        //**************HANDLING CLEAR BUTTONS - END*************//




        //**************HANDLING NUMERAL, OPERATOR & EQUALS BUTTONS - START****************//

                //taking the numerals and decimal pointer
                if (buttonText.matches("[0-9\\.]")) {
                        //clear top field when an expression is solved and a numeral button is pressed
                        if (textField2.getText().contains("=") || textField2.getText().contains("Enter a Digit First!")) {
                            textField2.clear();
                        }
                        //check on multiple decimal point inputs
                        if (buttonText.matches("\\.")) {
                            if (textField.getText().contains(".")) return;
                        }
                        //handle bottom field on numeral button press
                        if (!numberInputting) {
                            numberInputting = true;
                            textField.clear();
                        }
                        textField.appendText(buttonText);
                        textField2.appendText(buttonText);
                        return;
                }

                //setting the operator that the user uses
                if (buttonText.matches("[-×÷+%]")) {
                        //clear top field if a solved expression is present and take last entry from bottom field
                        if (textField2.getText().contains("=")){
                            textField2.clear();
                            textField2.setText(textField.getText());
                        }
                        //don't take input if top field expression has % sign
                        if(textField2.getText().contains("%")){
                            return;
                        }
                        //don't take input if calc is just started or cleared
                        if(textField.getText().equals("")){
                            return;
                        }

                        selectedOperator = buttonText;
                        numberInputting = false;

                        //Replace old operator & display the new pressed operator 'only' in the top field
                        int index = textField2.getLength();
                        if(index!=1) {
                            String substring = textField2.getText(index - 1, index);
                            if (substring.matches("[-×÷+]")) {
                                textField2.setText(textField2.getText(0, index - 1));
                                textField2.appendText(selectedOperator);
                                return;
                            }
                        }
                        //Evaluating an expression already present inside top field
                        String s = textField2.getText();
                        if(s.contains("+") || s.contains("-") || s.contains("×") || s.contains("÷")){
                            left = Model.evaluate(textField2.getText()); //calling external evaluation method
                        }else{
                            left = new BigDecimal(textField.getText());
                        }
                        textField2.appendText(selectedOperator);
                        return;
                }


                //calculating entered entries as per the operator set and showing the result in textfield
                if (buttonText.equals("=")) {
                    //allow user to input equals button once
                    if(textField2.getText().contains("=")){
                        return;
                    }
                    //Don't output anything if the last entry into top field is "%"
                    if((textField2.getText(textField2.getLength() -1, textField2.getLength()).equals("%"))){
                        return;
                    }
                    //don't take input if calc is just started or cleared
                    if(textField.getText().equals("")){
                        return;
                    }

                    final BigDecimal right = numberInputting ? new BigDecimal(textField.getText()) : left;
                    BigDecimal originalLeft = left;
                    left = Model.calculate(selectedOperator, left, right);
                    //Remove trailing zeroes after decimal and if required the decimal too
                    String s = left.toString();
                    s = s.contains(".") ? s.replaceAll("0*$","").replaceAll("\\.$","") : s;
                    //Check if both numbers are equal or right is not entered
                    if (left.equals(right) & !numberInputting){
                        textField.setText(s);
                        textField2.appendText(right+" = "+textField.getText());

                    }else{
                        if (textField2.getText().contains("%")){    //Handling the display output if expression has a % operator
                            textField.setText(s);
                            textField2.setText("Percentage: ("+originalLeft+"/"+right+")*100="+s);
                        }else {
                            textField.setText(s);
                            textField2.appendText(" = " + textField.getText());
                        }
                    }
                    selectedOperator="";
                    left = BigDecimal.ZERO;
                    numberInputting = false;
                    return;
                }
        //**************HANDLING NUMERAL, OPERATOR & EQUALS BUTTONS - END****************//


        //**********HANDLING TOP ROW BUTTONS - START**************//

                if (buttonText.contains("√") || buttonText.contains("x²") || buttonText.contains("x³") || buttonText.contains("1/x")){
                    int index = textField2.getLength();
                    if (textField.getText().equals("")) {
                        textField.clear();
                        textField2.setText("Enter a Digit First!");  //prompt user to enter a digit first
                    }
                    //check if users last input was an operator
                    else if(textField2.getText(index - 1, index).matches("[-×÷+%]") ||
                                textField2.getText().contains("%")) {
                    }
                    //continue with the calculation if above conditions are cleared
                    else {
                        if(!textField2.getText().contains("=")) left = Model.evaluate(textField2.getText());  //calling external evaluation method
                        else  left = new BigDecimal(textField.getText());
                        BigDecimal display = left;

                        if(buttonText.contains("√")) left = new BigDecimal(Math.sqrt(left.doubleValue()));
                        else if(buttonText.contains("x²"))  left = left.multiply(left);
                        else if(buttonText.contains("x³")) left = left.multiply(left).multiply(left);
                        else left = BigDecimal.ONE.divide(left, 3, RoundingMode.HALF_UP);

                        left = left.setScale(3, RoundingMode.HALF_UP);
                        String result = left.toString();
                        result = result.contains(".") ? result.replaceAll("0*$","").replaceAll("\\.$","") : result;
                        textField.setText(result);

                        if(buttonText.contains("√")) textField2.setText("√"+display+" = "+textField.getText());
                        else if(buttonText.contains("x²"))  textField2.setText(display+"² = "+textField.getText());
                        else if(buttonText.contains("x³")) textField2.setText(display+"³ = "+textField.getText());
                        else textField2.setText("1/"+display+" = "+textField.getText());

                        numberInputting = false;
                    }
                }
        ////**********HANDLING TOP ROW BUTTONS - END**************//
    }
//************EVENT HANDLING METHOD - END*****************//

    @Override
    public void initialize (URL url, ResourceBundle resources){

    }


}