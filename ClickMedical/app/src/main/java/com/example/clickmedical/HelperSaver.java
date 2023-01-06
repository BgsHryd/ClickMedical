package com.example.clickmedical;

public class HelperSaver {
    public static RumahSakit RS;
    public static Kamar kamar;
    public static int imageKamar;

    public static Kamar getKamar() {
        return kamar;
    }
    public static RumahSakit getRumahSakit() {
        return RS;
    }
    public static int getImageKamar() {
        return imageKamar;
    }
    public static void setImageKamar(int imageKamar) {
        HelperSaver.imageKamar = imageKamar;
    }
    public static void setKamar(Kamar kamar) {
        HelperSaver.kamar = kamar;
    }
    public static void setRS(RumahSakit RS) {
        HelperSaver.RS = RS;
    }
}
