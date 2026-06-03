public class Main {
    public static void main(String[] args){
       CustomLong a = new CustomLong(100L);
       CustomLong b = new CustomLong(20L);
        System.out.println(a.plus(b).getValue());
        System.out.println(a.minus(b).getValue());
        System.out.println(a.times(b).getValue());
        System.out.println(a.div(b).getValue());
    }
}
