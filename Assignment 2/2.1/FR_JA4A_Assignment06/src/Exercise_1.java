import java.util.Random;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Exercise_1 {
    public static void main(String[] args){
        int[] arr = new int[10];
        Random random = new Random();
        System.out.println("--- MANG SO NGUYEN NGAU NHIEN ---");
        for (int i = 0; i<10;i++){
            arr[i] = random.nextInt(100);
            System.out.println("Index "+i+"4: "+arr[i]);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nVui long nhap vao index de xem gia tri: ");
        try{
                int index = scanner.nextInt();
                int value = arr[index];
                System.out.println("-> Gia tri tai index "+ index +" la: " +value);
        }catch (InputMismatchException e){
            System.out.println("Loi: Du lieu dau vao khong hop le, Vui long nhap mot so nguyen.");
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Loi: Index ban nhap vao nam ngoai pham vi cua mang (chi nhap tu 0 den 9)");
        } catch (Exception e){
            System.out.println("Da xay ra loi khong xac dinh: "+ e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
