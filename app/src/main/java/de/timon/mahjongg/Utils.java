package de.timon.mahjongg;

public class Utils {
    public static String addPlusSign(int number) {
        if (number > 0) {
            return "+" + number;
        } else {
            return String.valueOf(number);
        }
    }

    public static boolean is4Players() {
        return ApplicationGlobals.playerCount == 4;
    }
}
