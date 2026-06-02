import java.util.*;
public class Exercise_3 {
    public static void main(String[] args){
        int[] arr = new int[10];
        Scanner scanner = new Scanner(System.in);
        System.out.println("Vui long nhap vao mot mang:");
        for(int i=0;i<10;i++){
            arr[i] = scanner.nextInt();
        }
        System.out.println("Nhap gia tri index ma ban muon: ");
        try{
            int index = scanner.nextInt();
            System.out.println("gia tri cua day so tai vi tri "+index+" la: "+arr[index]);
        } catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Loi: "+ e.getMessage());
            System.out.println("Loi: gia tri cua index khong hop le, vui long nhap gia tri tu 0 - 9");
        } catch (InputMismatchException e){
            System.out.println("Loi:Du lieu dau vao khong hop le, vui long nhap so nguyen");
        }
        finally{
            scanner.close();
        }
    }
}
