import java.util.*;
public class Main {
    public static void main(String [] args){
        Scanner scanner = new Scanner(System.in);
        ArrayList<Integer> l = new ArrayList<>();
        //Input the range of array
        int n = scanner.nextInt();
        //Input the elements of array
        for(int i = 0; i<n; i++){
            int index = scanner.nextInt();
            l.add(index);
        }
        //Input the inserted element
        int element = scanner.nextInt();
        l.add(element);
        //sort the array
        l.sort(Integer::compareTo);
        for(int e : l){
            System.out.print(e+" ");
        }
    }
}
