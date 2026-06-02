import java.util.*;
import java.math.*;

import static java.lang.Math.sqrt;

public class Main {

    public static boolean isPrime(int n){
        if(n == 1 || n== 0) return false;
        for(int i =2; i<=sqrt(n); i++){
            if(n%i==0) return false;
        }
        return true;
    }

    public static boolean digitIsPrime(int n) {

        while (n > 10) {
            int m = n % 10;
            if (!isPrime(m)) return false;
            else n /= 10;
        }
        return true;
    }

    public static boolean isPalindrome (int n){
        String a = Integer.toString(n);
        String s = new StringBuilder(a).reverse().toString();
        return s.equals(a);
    }

    public static int sum (int n){
        int sum = 0;
        while(n>0){
            sum+=n%10;
            n/=10;
        }
        return sum;
    }

    public static void main(String[] args) {
        for(int i = 10000; i<=9999999; i++) {
            if (isPrime(i) && digitIsPrime(i) && isPrime(sum(i)) && isPalindrome(i)) {
                System.out.print(i + " ");
            }
        }
    }
}