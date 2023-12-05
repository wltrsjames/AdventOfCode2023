import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String fileInput = FileUtils.readFile("src/resources/Day1-1Input.txt");
        List<String> inputItems = Arrays.stream(fileInput.split("\r\n")).toList();

        List<Integer> codes = inputItems.stream().map(inputItem -> {
            String replaceNumberWords = wordToDigit(inputItem);
            String removedLetters = replaceNumberWords.replaceAll("[^0-9]+", "");
            char firstDigit = removedLetters.charAt(0);
            char lastDigit = removedLetters.charAt(removedLetters.length() - 1);

            return Integer.valueOf(Character.toString(firstDigit) + Character.toString(lastDigit));
        }).toList();

        System.out.println(codes.size());

        Integer sumOfCodes = codes.stream().reduce(0, Integer::sum);

        System.out.println(sumOfCodes);
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms");
    }

    private static String wordToDigit(String code) {
        String[] digitWords = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};

        List<NumberDigitLocation> numberDigitLocations = Arrays.stream(digitWords)
                .filter(code::contains)
                .flatMap(digitWord -> {
                    int index = code.indexOf(digitWord);
                    List<NumberDigitLocation> numberDigitLocations2 = new ArrayList<>();

                    while (index >= 0) {
                        numberDigitLocations2.add(new NumberDigitLocation(digitWord, index));
                        index = code.indexOf(digitWord, index + 1);
                    }
                    return numberDigitLocations2.stream();
                })
                .sorted((o1, o2) -> Integer.compare(o2.digitLocation, o1.digitLocation)).toList();

        String codeNumberReplaced = code;

        for (NumberDigitLocation numberDigitLocation : numberDigitLocations) {
            String startUptoNumber = codeNumberReplaced.substring(0, numberDigitLocation.digitLocation);
            String numberString = getNumberForString(numberDigitLocation.digitString);
            String endAfterNumber = codeNumberReplaced.substring(numberDigitLocation.digitLocation + numberDigitLocation.digitString.length() - numberDetectionOffset(codeNumberReplaced.substring(numberDigitLocation.digitLocation), numberDigitLocation.digitString.length()));

            codeNumberReplaced = startUptoNumber + numberString + endAfterNumber;
        }

        return codeNumberReplaced;
    }

    private static int numberDetectionOffset(String afterString, int expectedLength) {
        String[] numberSplit = afterString.split("[0-9]");
        int numberOffset = numberSplit.length > 1 ? expectedLength - numberSplit[0].length() : 0;
        return numberOffset;
    }

    private static String getNumberForString(String numberValue) {
        return switch (numberValue) {
            case "one" -> "1";
            case "two" -> "2";
            case "three" -> "3";
            case "four" -> "4";
            case "five" -> "5";
            case "six" -> "6";
            case "seven" -> "7";
            case "eight" -> "8";
            case "nine" -> "9";
            default -> throw new IllegalStateException("Unexpected value: " + numberValue);
        };
    }

    private record NumberDigitLocation(String digitString, int digitLocation) {
        private NumberDigitLocation(String digitString, int digitLocation) {
            this.digitString = digitString;
            this.digitLocation = digitLocation;
        }
    }
}