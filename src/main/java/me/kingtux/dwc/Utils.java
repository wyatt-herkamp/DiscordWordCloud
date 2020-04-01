package me.kingtux.dwc;

public class Utils {
    public static boolean isInt(String radius) {
        try {
            Integer.parseInt(radius);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
