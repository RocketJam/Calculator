package com.example.megacalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    TextView result;

    private static int numCount = 0;

    private static double lastNumber = 0;

    private static Button opB ; // for button color

    private static Button prevB;

    private static boolean transition = false;

    private static Stack<Double> stack;

    private static Stack<String> opStack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        result = findViewById(R.id.Result);

        String temp = convert(lastNumber);

        result.setText(temp);

        stack = new Stack<>();

        opStack = new Stack<>();

        //stack.push(lastNumber);

    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putDouble("lastNum",lastNumber);

        super.onSaveInstanceState(savedInstanceState);
    }

    public void equaltsOp(View view){

        String opName = buttonValue(view);

        if(transition != true){

            stack.push(lastNumber);

        }

        Log.v("Default" , opStack.isEmpty() +"");

        if(opStack.isEmpty() != true && transition != true){

            Log.v("Default" , stack.size() + "  is size before");

            cntrOp(opName);
        }

    }

    public void operation(View view){

        if(transition != true){

            stack.push(lastNumber);

        }


        String opName = buttonValue(view);

        Log.v("Default", opStack.isEmpty() + "  is it?");

        if(opStack.isEmpty() != true){

            Log.v("Default", opStack.isEmpty() + "  is it?");

            if(transition == true){
                opStack.pop();

                opStack.push(opName);
            }
            else{
                cntrOp(opName);
            }

        }
        else{
            opStack.push(opName);

            currentOp(opB);

            numCount = 0;

            transition = true;
        }


    }

    private void cntrOp(String opName){


        switch (opStack.pop()){

            case "+":{

                Log.v("Default" , stack.size()+"   its size");

                double num2 = stack.pop();

                Log.v("Default" , num2 + "");

                double num1 = stack.pop();

                Log.v("Default" , num1 + "");

                double res =num1 + num2;

                Log.v("Default" , res + "");

                stack.push(res);

                pushOp(opName);

                Log.v("Default" , stack.peek()+"");

                String display = convert(res);

                result.setText(display);

                transition = true;

                numCount = 0;

                break;
            }

            case "-" :{

                Log.v("Default" , stack.size()+"   its size");

                double num2 = stack.pop();

                Log.v("Default" , num2 + "");

                double num1 = stack.pop();

                Log.v("Default" , num1 + "");

                double res =num1 - num2;

                Log.v("Default" , res + "");

                stack.push(res);

                Log.v("Default" , stack.peek()+"");

               pushOp(opName);

                String display = convert(res);

                result.setText(display);

                transition = true;

                numCount = 0;

                break;
            }

            case "/" :{

                Log.v("Default" , stack.size()+"   its size");

                double num2 = stack.pop();

                Log.v("Default" , num2 + "");

                double num1 = stack.pop();

                Log.v("Default" , num1 + "");

                DecimalFormat format = new DecimalFormat("#.00");

                String display = "";

                double res = 0 ;

                if(num2 != 0){
                     res=num1 / num2;

                    res = Double.parseDouble(format.format(res));

                    Log.v("Default" , res + "");

                    stack.push(res);

                    Log.v("Default" , stack.peek()+"");

                    pushOp(opName);

                    display = convert(res);
                }

                else{

                    pushOp("=");

                    stack.push(0.0);

                     display= "Error";



                }


                result.setText(display);

                transition = true;

                numCount = 0;

                break;
            }


            case "*" :{

                Log.v("Default" , stack.size()+"   its size");

                double num2 = stack.pop();

                Log.v("Default" , num2 + "");

                double num1 = stack.pop();

                Log.v("Default" , num1 + "");

                DecimalFormat format = new DecimalFormat("#.00");


                double res =num1 * num2;

                res = Double.parseDouble(format.format(res));

                Log.v("Default" , res + "");

                stack.push(res);

                Log.v("Default" , stack.peek()+"");

                pushOp(opName);

                String display = convert(res);

                result.setText(display);

                transition = true;

                numCount = 0;

                break;
            }
        }
    }

    public void pushOp(String opName){

        Log.v("Default" , opName + "  op name" );


        if(!opName.equals("=") ){
            opStack.push(opName);
        }
    }

    // displays and puts the number character into the text view
    public void putNumber(View view){

        String line = "";

        if(transition){
            line = "0";

            transition = false;
        }
        else{
            line = result.getText().toString();
        }

        String name = buttonValue(view);

        line = logicComponent(name,line);

        double number = Double.parseDouble(line);

        lastNumber = number;

        //line = convert(lastNumber);
        Log.v("Default", convert(lastNumber) + "");
        Log.v("Default",number + "");
        Log.v("Default",line + "  Line");

        result.setText(line);

    }


    // determines the character that is typed in from the button
    public String buttonValue (View view){
        Button pressed = (Button) view ;

        Log.v("Default", pressed.getId()+"");
        if(opB == null){
            opB = pressed;
            prevB = pressed;
        }
        else{
            prevB = opB;
            opB = pressed;
        }


        String name = pressed.getText().toString();

        return name;
    }


    // identifies what to do with the number or character. This function doesnt handle operations : - , +
    public String logicComponent(String newVal , String line){
        String finalVal = line;

        double number = Double.parseDouble(finalVal);

        switch(newVal){

            case "+/-":{

                finalVal = signValid(finalVal);

                break;
            }

            case ".":{

                finalVal = isDotValid(finalVal,newVal);

                break;
            }

            default:{
                if(numCount < 7){
                    finalVal = numWay(finalVal,newVal);

                    if(!finalVal.equals("0"))
                    increaseCount();
                }

                break;
            }

        }
        return finalVal;
    }


    // identifying whether decimal dot is relevant
    private String isDotValid(String finalVal, String newVal){
        if(!finalVal.contains(".")){

            increaseCount();

            return finalVal = finalVal + newVal;
        }
        else {
            return finalVal;
        }
    }


    // increases amount of characters in the string
    private void increaseCount(){
        numCount++;
    }


    // this function shows decides what exactly happens when the number is pressed
    private String numWay(String finalVal, String newVal){

        String pureNum = finalVal;

        String sign = "";

        if(finalVal.charAt(0) == '-'){
            Log.v("Default",finalVal.charAt(0)+"");
            pureNum = finalVal.substring(1);
            sign= "-";
        }

        if(numCount == 0 && pureNum.charAt(0) == '0'){
            return pureNum = sign + newVal;
        }

        Log.v("Default",sign + "  This is the sign");

        return  finalVal = sign + pureNum + newVal;
    }


    // this function adds or removes the sigh
    private String signValid(String finalVal){

        if(finalVal.charAt(0) == '-'){
            String positiveStr = finalVal.substring(1);

            finalVal = positiveStr;

            return finalVal;
        }

        return finalVal = '-' + finalVal;

    }


    //
    private void currentOp(Button currentB ){

    }


    // this function called when AC button is pressed
    public void resetAll(View view){
        lastNumber = 0 ;

        result.setText("0");

        numCount = 0;

        stack.clear();

        opStack.clear();

        transition = false;
    }


    // this unit determines whether the number has redundant decimals
    private String convert(Double currentNum){

        Integer temp = currentNum.intValue();

        if(temp.doubleValue() == currentNum){
            return temp + "";
        }

        return currentNum + "";
    }


}