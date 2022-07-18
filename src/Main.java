import java.util.*;

public class Main {
    private static Map<Character, Integer> romanNumbersMap = Map.of(
                'I', 1,
                'V', 5,
                'X', 10);
    private static  List<Character> operators = Arrays.asList('+', '-', '/', '*');

    public static void main(String[] args) throws Exception {
        String result = calc(new Scanner(System.in).nextLine());
        if(!"null".equals(result)) {
            System.out.println(result);
        }
    }

    public static String calc(String input) throws Exception {
        String[] inputData = parseInputString(input);
        if(isNumbersArabic(inputData[0])){
            return getResult(inputData);
        }else {
            return convertArabicToRoman(Integer.parseInt(getResult(inputData)));
        }

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

    private static List<Integer> getIntNumbers(String intString) throws Exception {
        List<Integer> intNumbers = new ArrayList<>();
        if(intString.contains("10")){
            intNumbers.add(10);
            intString = intString.replace("10", "");
        }
        intString.chars()
                .forEach(i -> {
                    int res = Character.getNumericValue(i);
                    if(res != -1){
                        intNumbers.add(res);
                    }
                });

        if (intNumbers.size() > 2){
            throw new Exception("Wrong mathematical operation format - two operand and one operator required");
        }

        for (int number: intNumbers) {
            if(number < 1 || number > 10){
                throw new Exception("Numbers should be in range from 1 to 10");
            }
        }

        return intNumbers;
    }

    private static List<String> getRomanNumbers(String romanString) throws Exception {
        List<Character> firstRomanNumber = new ArrayList<>();
        List<Character> secondRomanNumber = new ArrayList<>();

        romanString = romanString.replace("IV","IIII");
        romanString = romanString.replace("IX","VIIII");

        romanString.chars()
                .takeWhile(c -> c != ' ' && !operators.contains((char)c))
                .filter(c -> romanNumbersMap.containsKey((char)c))
                .forEach(c -> firstRomanNumber.add((char)c));
        romanString.chars()
                .skip(firstRomanNumber.size())
                .dropWhile(c -> (char)c == ' ' && !operators.contains((char)c))
                .filter(c -> romanNumbersMap.containsKey((char)c))
                .forEach(c -> secondRomanNumber.add((char)c));

        StringBuilder firstNumber = new StringBuilder();
        for (Character c: firstRomanNumber) {
            firstNumber.append(c);
        }

        StringBuilder secondNumber = new StringBuilder();
        for (Character c: secondRomanNumber) {
            secondNumber.append(c);
        }

        if((firstNumber.toString().startsWith("X") && firstNumber.length() > 1) ||
            (secondNumber.toString().startsWith("X") && secondNumber.length() > 1)){
            throw new Exception("Numbers should be in range from I to X");
        }

        return List.of(firstNumber.toString(), secondNumber.toString());
    }
    private static String getResult(String[] inputData) throws Exception {
        int firstNumber;
        int secondNumber;
        String operation;
        boolean isArabicNumbers = isNumbersArabic(inputData[0]);

        if (isArabicNumbers) {
            firstNumber = Integer.parseInt(inputData[0]);
            secondNumber = Integer.parseInt(inputData[2]);
            operation = inputData[1];
        }else {
            firstNumber = convertRomanToArabic(inputData[0]);
            secondNumber = convertRomanToArabic(inputData[2]);
            operation = inputData[1];
        }

        switch (operation) {
            case "+":
                return String.valueOf(firstNumber + secondNumber);

            case "-":
                if(!isArabicNumbers && firstNumber < secondNumber){
                    throw new Exception("Roman numeral system has no negative numbers");
                }
                return String.valueOf(firstNumber - secondNumber);

            case "/":
                return String.valueOf(firstNumber / secondNumber);

            case "*":
                return String.valueOf(firstNumber * secondNumber);

            default:
                return null;
        }
    }

    private static boolean isNumbersArabic(String input){
        return input.matches("(.*)\\d(.*)");
    }

    private static int convertRomanToArabic(String romanNumber){
        return romanNumber.chars()
                .map(r -> romanNumbersMap.get((char)r))
                .sum();
    }

    private static String convertArabicToRoman(int number){
        String[] tens = {"", "X", "XX"};
        String[] units = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        String result = tens[(number % 100) / 10] + units[number % 10];
        if(result.length() > 0){
            return result;
        }
        else {
            return "nulla";
        }
    }
}