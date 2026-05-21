import java.util.*;

public class Exercise_2 {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Chuong trinh chia hai so thuc ---");
        try {
            System.out.println("Nhap vao so bi chia a: ");
            double a = scanner.nextDouble();
            System.out.println("Nhap vao so chia b: ");
            double b = scanner.nextDouble();

            double result = divide(a,b);
            System.out.println("-> ket qua cua phep chia la: "+result);
        } catch(InputMismatchException e){
            System.out.println("Loi: Du lieu dau vao khong hop le, vui long nhap so thuc");
        } catch (ArithmeticException e){
            System.out.println("Loi: "+ e.getMessage());
            System.out.println("Chuong trinh ket thuc vi phep chia khong hop le");
        } catch (Exception e){
            System.out.println("Loi khong xac dinh: "+ e.getMessage());
        } finally{
            scanner.close();
        }
    }

    public static double divide(double a, double b){
        if(b==0){
            throw new ArithmeticException("So chia khong the bang 0.");
        }
        return a/b;
    }
}
