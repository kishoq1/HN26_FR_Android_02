import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MyString {
    private final String text;

    public MyString(String text) {
        this.text = text;
    }

    // 1. Tính tổng các số trong chuỗi
    public void calculateSumOfNumbers() {
        int sum = 0;
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            sum += Integer.parseInt(matcher.group());
        }
        System.out.println("- Tong cac so trong chuoi: " + sum);
    }

    // 2. Tìm và in ra ký tự có mã ASCII nhỏ nhất
    public void printSmallestASCII() {
        if (text == null || text.isEmpty()) {
            System.out.println("- Chuoi rong, khong co ky tu de tim.");
            return;
        }
        char minChar = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < minChar) {
                minChar = text.charAt(i);
            }
        }
        System.out.println("- Ky tu co ma ASCII nho nhat la: '" + minChar + "' (Ma ASCII: " + (int)minChar + ")");
    }

    // 3. Tìm và in ra các chữ cái in hoa
    public void printCapitalLetters() {
        System.out.print("- Cac chu cai in hoa trong chuoi: ");
        boolean hasCapital = false;
        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                System.out.print(c + " ");
                hasCapital = true;
            }
        }
        if (!hasCapital) {
            System.out.print("Khong co chu cai in hoa nao.");
        }
        System.out.println();
    }

    public void capitalizeFirstLetters() {
        if (text == null || text.isEmpty()) return;

        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        System.out.println("- Chuoi viet hoa chu cai dau: " + result.toString().trim());
    }

    // 5. In ra chuỗi đảo ngược
    public void printReversedString() {
        String reversed = new StringBuilder(text).reverse().toString();
        System.out.println("- Chuoi dao nguoc: " + reversed);
    }
}

public class Exercise_2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nhap vao mot chuoi bat ky:");
        String testString = scanner.nextLine();
        System.out.println("--- KET QUA EXERCISE 2 ---");
        System.out.println("Chuoi ban dau: " + testString + "\n");
        MyString myStr = new MyString(testString);

        myStr.calculateSumOfNumbers();
        myStr.printSmallestASCII();
        myStr.printCapitalLetters();
        myStr.capitalizeFirstLetters();
        myStr.printReversedString();
    }
}