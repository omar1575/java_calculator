package gui;

import engine.CalculatorEngine;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import operations.Addition;
import operations.Division;
import operations.Multiplication;
import operations.Subtraction;

import java.util.Objects;

/**
 * @Author Omar Mahmoud
 */
public class CalculatorManager {

    private static final CalculatorEngine calculatorEngine = new CalculatorEngine();
    private static final Addition addition = new Addition();
    private static final Subtraction subtraction = new Subtraction();
    private static final Multiplication multiplication = new Multiplication();
    private static final Division division = new Division();

    private double currentNumber = 0;
    private String currentOperator = "";
    private boolean waitingForOperand = false;

    public void handleNumberClick (Button button, Label calculatorDisplay) {
        String number = button.getText();
        appendToDisplay(number, calculatorDisplay);
    }

    private void appendToDisplay (String number, Label calculatorDisplay) {
        if (waitingForOperand) {
            calculatorDisplay.setText(number);
            waitingForOperand = false;
        } else {
            String current = calculatorDisplay.getText();
            if (current.equals("0")) {
                calculatorDisplay.setText(number);
            } else {
                calculatorDisplay.setText(current + number);
            }
        }
    }

    public void handleOperatorClick (Button button, Label calculatorDisplay) {
        String operator = button.getText();
        processOperator(operator, calculatorDisplay);
    }

    private void processOperator (String operator, Label calculatorDisplay) {
        double number = Double.parseDouble(calculatorDisplay.getText());

        if (!currentOperator.isEmpty() && !waitingForOperand) {
            double result = performCalculation(currentNumber, number, operator);
            calculatorDisplay.setText(Double.toString(result));
            currentNumber = result;
        } else {
            currentNumber = number;
        }

        currentOperator = operator;
        waitingForOperand = true;
    }

    private double performCalculation (double currentNumber, double number, String operator) {
        return switch (operator) {
            case "+" -> calculatorEngine.calculate(addition, currentNumber, number);
            case "-" -> calculatorEngine.calculate(subtraction, currentNumber, number);
            case "X" -> calculatorEngine.calculate(multiplication, currentNumber, number);
            case "/" -> calculatorEngine.calculate(division, currentNumber, number);
            default -> number;
        };
    }

    public void handleEqualsClick (Label calculatorDisplay) {
        if (!currentOperator.isEmpty() && !waitingForOperand) {
            double secondNumber = Double.parseDouble(calculatorDisplay.getText());
            double result = performCalculation(currentNumber, secondNumber, currentOperator);
            calculatorDisplay.setText(Double.toString(result));
            currentOperator = "";
            waitingForOperand = true;
        }
    }

    public void handleClearClick (Label calculatorDisplay) {
        calculatorDisplay.setText("0");
        currentNumber = 0;
        currentOperator = "";
        waitingForOperand = false;
    }

    public void handleKeyPressed(KeyEvent keyEvent, Label calculatorDisplay) {
        String text = keyEvent.getText();
        KeyCode code = keyEvent.getCode();

        switch (text) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
                appendToDisplay(text, calculatorDisplay);
                break;
            case "+":
                processOperator("+", calculatorDisplay);
                break;
            case "-":
                processOperator("-", calculatorDisplay);
                break;
            case "*":
                processOperator("X", calculatorDisplay);
                break;
            case "/":
                processOperator("/", calculatorDisplay);
                break;
            case "=":
                handleEqualsClick(calculatorDisplay);
                break;
            default:
                break;
        }

        if (Objects.requireNonNull(code) == KeyCode.DELETE) {
            handleClearClick(calculatorDisplay);
        }
    }
}
