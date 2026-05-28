public class exam2 {
    public static void main(String[] args) {
        System.out.println("List the intergers with 5 to 7 digits that sastify: ");

        for (int i = 10000; i <= 9999999; i++) {
            if (isPalindrome(i) && areAllDigitsPrime(i) && isSumOfDigitsPrime(i) && isPrime(i)) {
                System.out.print(i + " ");
            }
        }
    }

    public static boolean isPalindrome(int n) {
        String str = String.valueOf(n);
        String reverseStr = new StringBuilder(str).reverse().toString();
        return str.equals(reverseStr);
    }

    public static boolean areAllDigitsPrime(int n) {
        while (n > 0) {
            int digit = n % 10;
            if (digit != 2 && digit != 3 && digit != 5 && digit != 7) {
                return false;
            }
            n /= 10;
        }
        return true;
    }

    public static boolean isSumOfDigitsPrime(int n) {
        int sum = sumOfDigits(n);
        return isPrime(sum);
    }
    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
    public static int sumOfDigits(int n) {
        int sum = 0;
        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }
}