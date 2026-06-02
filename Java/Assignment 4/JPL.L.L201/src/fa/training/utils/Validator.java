package fa.training.utils;

public class Validator {
    public static boolean isValidId(String ID, String prefix) {
        if (ID == null || !ID.matches(prefix + "\\d{5}")) {
            return false;
        }
        return true;
    }
    public static boolean isValidModel(String model){
        return model != null && model.length() <= 40;
    }

    public static boolean isValidPlaneType(String type){
        return type.equals("CAG") || type.equals("LGR") || type.equals("PRV");
    }

    public static boolean isValidHelicopterWeight(double empty, double max){
        return max <= 1.5 * empty;
    }
}
