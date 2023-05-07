package main;

import jdk.jshell.spi.ExecutionControl;
import junit.framework.TestSuite;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.util.*;

public class TruthTables {
    public static void main(String[] args) {
        //an interactive window, that shows the truth table for a given number of variables
        //the user can input the number of variables and if they are true or false
        //the number of variables is always 3 or less
        //the program will then output the truth table for the given variables
        //the user can then change the values of the variables and the truth table will update and show the name of the gate
        // like "and", "or", "not", "xor", "nand", "nor", "xnor"

        //the user can also input a boolean expression and the program will output the truth table for that expression

        //new JFrame
        JFrame frame = new JFrame("Truth Tables");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);

        //new JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //new JLabel
        JLabel boolean_label = new JLabel("Enter the boolean expression");
        //new JTextField
        JTextField boolean_expression = new JTextField(); //example: (a and b) or (c and d), a and b, a or b, a xor b, a nand b, a nor b, a xnor b, not a


        frame.add(panel);
    }
}

class ExpressionConverter {
    private static boolean hasHigherPrecedence(String op1, String op2) {
        int op1Precedence = getPrecedence(op1);
        int op2Precedence = getPrecedence(op2);
        return op1Precedence >= op2Precedence;
    }

    public static String convertToPrefixNotation(String expression) {
        // Create a stack to store operators
        Stack<String> operatorStack = new Stack<>();
        // Create a list to store the output in prefix notation
        Stack<String> outputList = new Stack<>();

        // Split the expression into parts
        String[] parts = expression.split("\\s+");

        // Iterate over the parts
        for (String part : parts) {
            // If the part is an operand, push it onto the outputList
            if (isOperand(part)) {
                outputList.push(part);
            }
            // If the part is an operator, pop and process the operators from the operatorStack while the stack is not empty,
            // the top element is not a left parenthesis, and the precedence of the top element is greater than or equal to the precedence of the current operator
            else if (isOperator(part)) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(") && operatorPrecedence(operatorStack.peek()) >= operatorPrecedence(part)) {
                    String poppedOperator = operatorStack.pop();
                    String operand2 = outputList.pop();
                    String operand1 = outputList.pop();
                    String newExpression = poppedOperator + " " + operand1 + " " + operand2;
                    outputList.push(newExpression);
                }
                operatorStack.push(part);
            }
            // If the part is a left parenthesis, push it onto the operatorStack
            else if (part.equals("(")) {
                operatorStack.push(part);
            }
            // If the part is a right parenthesis, pop and process operators from the operatorStack until a left parenthesis is encountered
            else if (part.equals(")")) {
                while (!operatorStack.peek().equals("(")) {
                    String poppedOperator = operatorStack.pop();
                    String operand2 = outputList.pop();
                    String operand1 = outputList.pop();
                    String newExpression = poppedOperator + " " + operand1 + " " + operand2;
                    outputList.push(newExpression);
                }
                // Pop the left parenthesis from the operatorStack
                operatorStack.pop();
            }
        }

        // Pop and process any remaining operators from the operatorStack
        while (!operatorStack.isEmpty()) {
            String poppedOperator = operatorStack.pop();
            String operand2 = outputList.pop();
            String operand1 = outputList.pop();
            String newExpression = poppedOperator + " " + operand1 + " " + operand2;
            outputList.push(newExpression);
        }

        // The top item on the outputList should be the prefix notation of the expression

        for(String s : outputList) {
            System.out.println(s);
        }

        return outputList.pop();
    }

    public static void main(String[] args) {
        String expression1 = "a and b";
        String prefixNotation1 = convertToPrefixNotation(expression1);
        System.out.println("Final Output: " + prefixNotation1 + "\n");

        String expression2 = "(a and b) or (c and d)";
        String prefixNotation2 = convertToPrefixNotation(expression2);
        System.out.println("Final Output: " + prefixNotation2 + "\n");

        String expression3 = "(a and b) or c";
        String prefixNotation3 = convertToPrefixNotation(expression3);
        System.out.println("Final Output: " + prefixNotation3 + "\n");
    }





    // Helper method to determine if a part is an operand (i.e. a variable)

    private static int operatorPrecedence(String operator) {
        switch (operator) {
            case "not":
                return 3;
            case "and":
            case "nand":
                return 2;
            case "or":
            case "nor":
            case "xor":
            case "xnor":
                return 1;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }


    // Helper method to get the precedence of an operator
    private static int getPrecedence(String operator) {
        switch (operator) {
            case "not":
                return 3;
            case "and":
            case "nand":
                return 2;
            case "or":
            case "nor":
            case "xor":
            case "xnor":
                return 1;
            default:
                return -1;
        }
    }




    // Helper method to determine if a part is an operand (i.e. a variable)
    private static boolean isOperand(String part) {
        return part.matches("[a-z]+");
    }

    // Helper method to determine if a part is an operator
    private static boolean isOperator(String part) {
        return part.matches("and|or|not|xor|nand|nor|xnor");
    }


    public static ExpressionPart convertToExpressionTree(String expression) {
        // Split the expression into parts
        String[] parts = expression.split(" ");

        // Create a stack to store operands and operators
        Stack<ExpressionPart> stack = new Stack<>();

        // Iterate over the parts of the expression
        for (int i = parts.length - 1; i >= 0; i--) {
            String part = parts[i];

            // If the part is an operand, create a new Operand object and push it onto the stack
            if (isOperand(part)) {
                Operand operand = new Operand(part, false);
                stack.push(operand);
            }
            // If the part is an operator, create a new Operator object, pop the top two items from the stack as its left and right children, and push the new Operator object onto the stack
            else if (isOperator(part)) {
                Operator operator = new Operator(part);
                ExpressionPart left = stack.pop();
                ExpressionPart right = stack.pop();
                operator.setLeft(left);
                operator.setRight(right);
                stack.push(operator);
            }
        }

        // The top item on the stack should be the root of the expression tree
        ExpressionPart root = stack.pop();

        if (root == null) {
            return null;
        } else if (root instanceof Operand) {
            // If the root is an operand, return it directly
            return root;
        } else if (root instanceof Operator) {
            // If the root is an operator, return it with its left and right children as the subtrees
            Operator operator = (Operator) root;
            operator.setLeft(convertToExpressionTree(((ExpressionPart) operator.getLeft()).getName()));
            operator.setRight(convertToExpressionTree(((ExpressionPart) operator.getRight()).getName()));
            return operator;
        }

        return null;
    }


}

abstract class ExpressionPart {
    String name;
    boolean isOperand;

    ExpressionPart(String name, boolean isOperand) {
        this.name = name;
        this.isOperand = isOperand;
    }

    public String getName() {
        return name;
    }

    public boolean isOperand() {
        return isOperand;
    }
}

class Operand extends ExpressionPart {
    boolean value;

    Operand(String name, boolean value) {
        super(name, true);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}

class Operator extends ExpressionPart {
    ExpressionPart left;
    ExpressionPart right;

    Operator(String name) {
        super(name, false);
    }

    public void setLeft(ExpressionPart left) {
        this.left = left;
    }

    public void setRight(ExpressionPart right) {
        this.right = right;
    }

    public ExpressionPart getLeft() {
        return left;
    }

    public ExpressionPart getRight() {
        return right;
    }

    public int getPrecedence() {
        // TODO: Implement method to return precedence of operator
        return 0;
    }
}

class ExpressionPartTree {
    ExpressionPart left;
    ExpressionPart right;
    ExpressionPart root;

    public ExpressionPartTree(ExpressionPart left, ExpressionPart right, ExpressionPart root) {
        this.left = left;
        this.right = right;
        this.root = root;
    }

    public ExpressionPart getLeft() {
        return left;
    }

    public ExpressionPart getRight() {
        return right;
    }

    public ExpressionPart getRoot() {
        return root;
    }
}








class LogicalExpressionParser {

    public static void main(String[] args) {
        reverseNumber(1234500078);
        System.out.println(factorial(4));
        System.out.println(factorial(0));
        System.out.println(factorial(Integer.MIN_VALUE));
        System.out.println(factorial(Integer.MAX_VALUE));
        System.out.println(e(Integer.MAX_VALUE));
        System.out.println(e(0));
        if(e(2)==2.5)System.out.println("yes");
        System.out.println(e(1));
        System.out.println(e(2));
        System.out.println(e(3));
    }

    public static double e(int n){
        double result = 0;
        int fact;

        for(int i = 0; i<=n; i++){
            fact = factorial(i);
            if(fact>Integer.MAX_VALUE/10)break;
            result += 1 / (double) fact;
        }
        return result;
    }

    public static int factorial(int n){
        int result = 1;
        for(int i=1; i<=n; i++){
            result *= i;
            if(result > Integer.MAX_VALUE/10)break;
        }
        return result;
    }

    public static void reverseNumber(int n){
        boolean small = n<10;
        boolean big = n/10 >= Integer.MAX_VALUE/100;

        if(big){
            System.out.println(n%10);
            n -= n%10;
            System.out.println(n%100/10);
            n -= n%100;
            n /= 100;
        }

        int result_partial = n;
        int result = 0;
        int potenz = 0;

        if(small){
            System.out.println(n);
        }

        else {
            for (int i = 10; i <= n*10; i *= 10) {
                result = result_partial % i;

                if (result % i != 0) {
                    System.out.println((result_partial % i) * 10 / i);
                }

                else {
                    potenz = 0;
                    while (!(1 >= result % i)) {
                        potenz++;
                        System.out.println((int) 0);
                        i *= 10;
                    }
                    System.out.println((int)((result % i) / Math.pow(10, potenz)));
                }
                result_partial -= result_partial % i;
            }
        }
    }


    //schreib mir eine Funktion für die Leibnizserie mit den gegebenen Funktionen in der Klasse
    public static double leibniz(int n){
        double result = 0;
        for(int i = 0; i<n; i++){
            result += Math.pow(-1, i) / (2*i+1);
        }
        return result;
    }
}