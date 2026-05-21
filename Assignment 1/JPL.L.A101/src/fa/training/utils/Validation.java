package fa.training.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Calendar;

public class Validation {

    private static final Scanner scanner = new Scanner(System.in);

    //Kiểm tra định dạng ISBN
    public static boolean isIsbnValid(String isbn) {
        // Regex: Bắt đầu và kết thúc bằng chuỗi chứa số và gạch ngang, tổng độ dài 10-17
        String isbnRegex = "^[0-9\\-]{10,17}$";
        return Pattern.matches(isbnRegex, isbn);
    }

     //lấy chuỗi ISBN hợp lệ từ người dùng
    public static String getValidIsbn() {
        String isbn;
        while (true) {
            System.out.print("Enter ISBN (10-17 characters, digits and '-' only, e.g.,ex: 678-3-16-1486): ");
            isbn = scanner.nextLine().trim();
            if (isIsbnValid(isbn)) {
                return isbn;
            } else {
                System.err.println("Invalid ISBN format! Please try again.");
            }
        }
    }

    //ép kiểu và kiểm tra dữ liệu số nguyên (int) an toàn

    public static int getInt(String message) {
        int result;
        while (true) {
            try {
                System.out.print(message);
                result = Integer.parseInt(scanner.nextLine().trim());
                if (result <= 0) {
                    System.err.println("Number must be greater than 0.");
                    continue;
                }
                return result;
            } catch (NumberFormatException e) {
                System.err.println("Invalid input! Please enter a valid integer number.");
            }
        }
    }

    //ép kiểu và kiểm tra định dạng Ngày tháng (Date)

    public static Date getDate(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        while (true) {
            try {
                System.out.print(message + " (dd/MM/yyyy): ");
                String dateStr = scanner.nextLine().trim();
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                System.err.println("Invalid date format! Please use dd/MM/yyyy.");
            }
        }
    }

    //lấy chuỗi thông thường, không được để trống

    public static String getString(String message) {
        String result;
        while (true) {
            System.out.print(message);
            result = scanner.nextLine().trim();
            if (result.isEmpty()) {
                System.err.println("Input cannot be empty. Please try again.");
            } else {
                return result;
            }
        }
    }
    //nhập ngày tháng và ép buộc năm phải khớp với năm xuất bản đã nhập
    public static Date getDateMatchingYear(String message, int expectedYear) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        while (true) {
            try {
                System.out.print(message + " (dd/MM/yyyy): ");
                String dateStr = scanner.nextLine().trim();
                Date date = sdf.parse(dateStr);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int yearFromDate = calendar.get(Calendar.YEAR);

                if (yearFromDate == expectedYear) {
                    return date;
                } else {
                    System.err.println("Mismatch Error! The year in Publication Date (" + yearFromDate +
                            ") must match the Publication Year (" + expectedYear + "). Please try again.");
                }
            } catch (ParseException e) {
                System.err.println("Invalid date format! Please use dd/MM/yyyy.");
            }
        }
    }
}