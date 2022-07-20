import java.util.*;

public class Main {
    private static final Map<Character, Integer> romanNumbersMap = Map.of(
                'I', 1,
                'V', 5,
                'X', 10,
                'L', 50,
                'C', 100);
    private static final List<Character> operators = Arrays.asList('+', '-', '/', '*');

    public static void main(String[] args) throws Exception {
        String result = calc(new Scanner(System.in).nextLine());
        if(!"null".equals(result)) {
            System.out.println(result);
        }
    }

    public static String calc(String input) throws Exception {
        String[] inputData = parseInputString(input);

        return getResult(inputData);
    }

    private static String[] parseInputString(String input) throws Exception {
        Character operator = getOperator(input);
        boolean isArabicNumbersPresent = isNumbersArabic(input);
        boolean isRomanNumbersPresent = input.chars()
                .filter(c -> romanNumbersMap.containsKey((char)c))
                .count() > 0;

        if (isRomanNumbersPresent && isArabicNumbersPresent){
            throw new Exception("Impossible to use different numeral systems at the same time");
        } else if (isArabicNumbersPresent) {
            List<Integer> intNumbers = getIntNumbers(input);
            return new String[]{intNumbers.get(0).toString(), operator.toString(), intNumbers.get(1).toString()};
        } else if (isRomanNumbersPresent) {
            List<String> romanNumbers = getRomanNumbers(input);
            return new String[]{romanNumbers.get(0), operator.toString(), romanNumbers.get(1)};
        }else {
            return null;
        }
    }

    private static Character getOperator(String input) throws Exception {
        long operatorCount = input.chars()
                .filter(c -> operators.contains((char)c))
                .count();
        if(operatorCount == 0){
            throw new Exception("This string is not a mathematical operation");
        } else if (operatorCount > 1) {
            throw new Exception("Wrong mathematical operation format - two operand and one operator required");
        }

        return (char)input.chars()
                .filter(c -> operators.contains((char) c))
                .boxed()
                .toList()
                .get(0)
                .intValue();
    }

    private static List<Integer> getIntNumbers(String input) throws Exception {
        List<Integer> intNumbers = new ArrayList<>();

        String[] inputSplitArray = input.split(" ");
        for (String s: inputSplitArray) {
            Integer number = parseInt(s);
            if(number != null){
                intNumbers.add(number);
            }
        }

        if (intNumbers.size() > 2){
            throw new Exception("Wrong mathematical operation format - two operand and one operator required");
        }

        return intNumbers;
    }

    private static List<String> getRomanNumbers(String romanString) throws Exception {
        List<String> romanNumbers = new ArrayList<>();

        romanString = romanString.replace("IV","IIII");
        romanString = romanString.replace("IX","VIIII");

        String[] inputSplitArray = romanString.split(" ");

        for (String s:inputSplitArray) {
            if(!operators.contains(s.charAt(0))){
                romanNumbers.add(s);
            }
        }

        if(romanNumbers.size() > 2){
            throw new Exception("Wrong mathematical operation format - two operand and one operator required");
        }

        return romanNumbers;
    }

    private static String getResult(String[] inputData) throws Exception {
        int firstNumber;
        int secondNumber;
        String operation;
        boolean isArabicNumbers = isNumbersArabic(inputData[0]);

        if (isArabicNumbers) {
            firstNumber = Integer.parseInt(inputData[0]);
            secondNumber = Integer.parseInt(inputData[2]);
        }else {
            firstNumber = convertRomanToArabic(inputData[0]);
            secondNumber = convertRomanToArabic(inputData[2]);
        }
        operation = inputData[1];

        if(firstNumber < 1 || firstNumber > 10 || secondNumber < 1 || secondNumber > 10){
            throw new Exception("Numbers should be in range from 1 to 10");
        }

        return switch (operation) {
            case "+" -> isArabicNumbers ?
                    String.valueOf(firstNumber + secondNumber) : convertArabicToRoman(firstNumber + secondNumber);
            case "-" -> isArabicNumbers ?
                    String.valueOf(firstNumber - secondNumber) : convertArabicToRoman(firstNumber - secondNumber);
            case "/" -> isArabicNumbers ?
                    String.valueOf(firstNumber / secondNumber) : convertArabicToRoman(firstNumber / secondNumber);
            case "*" -> isArabicNumbers ?
                    String.valueOf(firstNumber * secondNumber) : convertArabicToRoman(firstNumber * secondNumber);
            default -> null;
        };
    }

    private static boolean isNumbersArabic(String input){
        return input.matches("(.*)\\d(.*)");
    }

    private static int convertRomanToArabic(String romanNumber){
        romanNumber = romanNumber.replace("IV","IIII");
        romanNumber = romanNumber.replace("IX","VIIII");

        return romanNumber.chars()
                .map(r -> romanNumbersMap.get((char)r))
                .sum();
    }

    private static String convertArabicToRoman(int number) throws Exception {
        if(number < 0){
            throw new Exception("Roman numeral system has no negative numbers");
        }

        String[] hundreds = {"", "C"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] units = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        String result = hundreds[(number % 1000) / 100] + tens[(number % 100) / 10] + units[number % 10];
        if(result.length() > 0){
            return result;
        }
        else {
            throw new Exception("Roman numeral system has no 0");
        }
    }

    private static Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}