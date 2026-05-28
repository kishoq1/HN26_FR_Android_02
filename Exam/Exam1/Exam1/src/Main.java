import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("input n: ");
        int n = scanner.nextInt();

        if (n <= 0) {
            System.out.println("plese input a positive n!");
            return;
        }

        System.out.println(" Sum of the digits of n: " + sumOfDigits(n));

        System.out.print("Factorize n into prime factors: ");
        factorizePrimeFactors(n);
        System.out.println();

        System.out.print("The divisors of n: ");
        listDivisors(n);
        System.out.println();

        System.out.print("The prime divisors of n: ");
        listPrimeDivisors(n);
        System.out.println();

        scanner.close();
    }

    public static int sumOfDigits(int n) {
        int sum = 0;
        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }
        return sum;
    }

    public static void factorizePrimeFactors(int n) {
        for (int i = 2; i <= n; i++) {
            while (n % i == 0) {
                System.out.print(i + (n == i ? "" : " * "));
                n /= i;
            }
        }
    }

    public static void listDivisors(int n) {
        for (int i = 1; i <= n; i++) {
            if (n % i == 0) {
                System.out.print(i + " ");
            }
        }
    }

    public static void listPrimeDivisors(int n) {
        for (int i = 1; i <= n; i++) {
            if (n % i == 0 && isPrime(i)) {
                System.out.print(i + " ");
            }
        }
    }

    public static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}