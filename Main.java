package JavaCalculator;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

/*
Hi! Welcome to my Java Calculator.

It currently supports +, -, *, / as operators. 
It also supports complex nested parentheses.
As for decimals, those are also supported.

It returns exceptions for any syntax errors related to parentheses and operators.
It also returns exceptions for unsupported inputs (e.g. letters, or other alpha-
    numeric codes outside of valid algebraic-nonvariable expressions).

The calculator does not support variables, exponents, modulo, shift operators, etc.
The calculator also does not utilize half-adders or any machine/low-level code needed to 
    evaluate the operators from scratch.

The only thing that might sometimes be glitchy is returning slightly innaccurate answers
    (e.g. 3.154*2 - 6 = 0.3079999999... when the answer should just be 0.308)
    This has to do with the nature of Java Doubles, but aside from that, the calculator
    has very good accuracy.

I kept all functions on this file because VSCode allows me to close all the functions
    I am not currently working in. Had that not been the case, I would have just made 
    separate files for the functions.

    
I watched a video by Ting Zhang (aka happygirlzt) to explain her thought process behind
    solving a similar problem, when I got stuck using stacks and recursion. She used queues
    and recursion, I found that some slight differences in implementation made a huge 
    difference. Using switch statements also just make life a lot easier. My code is therefore 
    similar to hers, but I made modifications to account for decimals, and I also developed the 
    code to return exceptions for invalid inputs or syntax errors as well as the calculator's
    ability to continously interact with the terminal for valid inputs.

    Here is the video I watched.
    https://www.youtube.com/watch?v=C_jxn1hTn6Q
    Her GitHub: https://github.com/happygirlzt
*/


public class Main{
    String s = "";
    public static void main(String[] args) throws Exception{
        //make an instance of main
        Main myInstance = new Main();
        //set up scanner to receive input
        //I have a while loop and commented out the close Scanner function so
        //that the user may continuously interact with the terminal testing expressions
        while(true){
        Scanner inputCalc = new Scanner(System.in);
        String s = inputCalc.nextLine();
        myInstance.s = "";
        myInstance.returnAnswer(s);
        }
        //inputCalc.close();

    }

    public void returnAnswer(String x) throws Exception{
        //remove all white space
        x = x.replace(" ", "");
        //return Exceptions for various error types, fixes negative sign redundancies
        x = SignCheck(x);
        x = CharacterCheck(x);
        x = ParenthesesCheck(x);
        //calculates the expression
        double ret = Calculate(x);
        //prints out final result
        System.out.println(ret);
    }
    public String SignCheck(String x) throws Exception{
        StringBuilder sb = new StringBuilder();
        if(x.charAt(0) == '*' && x.charAt(0) == '/' && x.charAt(0) == '+'){
            try{
                throw new Exception("syntax error: cannot begin with a plus, multiplication, or division sign");
            }
            catch(Exception e){
                throw e;
            }
        }
        int inRow = 0;
        int negRow = 0;
        for(int i = 0; i < x.length(); i++){
            sb.append(x.charAt(i));
            if(x.charAt(i) >= '0'){
                inRow = 0;
                negRow = 0;
            }
            if(x.charAt(i) >= '*' && x.charAt(i) <= '/') {
                inRow++;
                if(x.charAt(i) == '-') negRow++;
            }
            if(x.charAt(i) == '('){
                if(x.charAt(i+1) == '*' || x.charAt(i+1) == '/' || x.charAt(i+1) == '+'){
                    try{
                        throw new Exception("syntax error: cannot have a non-negative sign immediately after open parenthesis");
                    }
                    catch(Exception e){
                        throw e;
                    }
                }
            }
            if(x.charAt(i) == ')'){
                if(x.charAt(i-1) == '*' || x.charAt(i-1) == '/' || x.charAt(i-1) == '+' || x.charAt(i-1)== '-'){
                    try{
                        throw new Exception("syntax error: cannot have a sign immediately before closed parenthesis");
                    }
                    catch(Exception e){
                        throw e;
                    }
                }
            }
            if(inRow >= 2){
                if(x.charAt(i) != '-'){
                    try{
                        throw new Exception("syntax error: cannot have two or more signs in a row unless a negative is after an operator.");
                    }
                    catch(Exception e){
                        throw e;
                    }
                }
                if(inRow >= 3){
                    try{
                        throw new Exception("syntax error: can never have three or more operators in a row");
                    }
                    catch(Exception e){
                        throw e;
                    }
                }
            }
            //turn double minus negatives into plus
            if(negRow == 2){
                sb.setLength(sb.length()-1);
                sb.append('+');
            }
        }
        return sb.toString();
    }
    public String CharacterCheck(String x) throws Exception{
    
        for(int i = 0; i < x.length(); i++){
            if(!(x.charAt(i) <= '9' && x.charAt(i) >= '(' && x.charAt(i) != ',')){
                try{
                    throw new Exception("input error: detected invalid character");
                }
                catch(Exception e){
                    throw e;
                }
            }
            if(x.charAt(i)== '.' && x.charAt(i+1) == '.'){
                try{
                    throw new Exception("syntax error: cannot have more than one decimal point in a row");
                }
                catch(Exception e){
                    throw e;
                }
            }
            if(x.charAt(i) == '.'){
                if(i == x.length()-1){
                    try{
                        throw new Exception("syntax error: cannot end with a decimal");
                    }
                    catch(Exception e){
                        throw e;
                    }
                }
                if(x.charAt(i+1) < '0' || x.charAt(i+1) > '9'){
                    try{
                        throw new Exception("syntax error: decimal needs a digit to the right of it");
                    }
                    catch(Exception e){
                        throw e;
                    }
                }
            }
        }
        return x;
    }
    public String ParenthesesCheck(String x) throws Exception{
        int left = 0;
        int right = 0;
        for(int i = 0; i < x.length(); i++){
            if(x.charAt(i) == ')'){
                right++;
            }
            if(x.charAt(i) == '('){
                left++;
            }
            if(right > left){
                try{
                    throw new Exception("syntax error: right parenthesis detected before left");
                }
                catch(Exception e){
                    throw e;
                }
            }
        }
        if(left != right){
            try{
                throw new Exception("syntax error: missing parenthesis");
            }
            catch(Exception e){
                throw e;
            }
        }
        return x;
    }
    
    public double Calculate(String pass){
        Queue<Character> q = new ArrayDeque<>();
        for(char c : pass.toCharArray()){
            if(c != ' '){
                q.offer(c);
            }
        }
        q.offer(' ');
        return dfs(q);
    }
    //the recursive dfs helper function helps to evaluate nested parentheses
    public double dfs(Queue<Character> q){
        double curr = 0, prev = 0, sum = 0;
        char prevOp = '+';
        boolean amDecimal = false;
        double div = 1.00;
        while(!q.isEmpty()){
            char c = q.poll();
            if(c >= '0' && c <= '9' && amDecimal){
                div *= 10;
                curr += (c - '0')/div;
                
            }
            else if(c >= '0' && c <= '9' && !amDecimal){
                div = 1.00;
                curr = curr * 10.00 + c - '0';
                
            } else if(c == '.'){
                amDecimal = true;

            } else if(c == '('){
                curr = dfs(q);
                
            }
            else{
                amDecimal = false;
                
                switch (prevOp){
                    case '+':
                        sum += prev;
                        prev = curr;
                        break;
                    case '-':
                        sum += prev;
                        prev = -curr;
                        break;
                    case '*':
                        prev *= curr;
                        break;
                    case '/':
                        prev /= curr;
                        break;
                }
                if(c== ')') break;
                prevOp = c;
                curr = 0;
            }   
        }
        return prev + sum;
    }
}