package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogger {
    // List untuk menyimpan log
    private static List<String> logList = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Method untuk menambah log baru
    public static void log(String aktivitas) {
        String waktu = LocalDateTime.now().format(formatter);
        String pesan = "[" + waktu + "] " + aktivitas;
        logList.add(0, pesan); // Tambahkan di urutan paling atas (terbaru)
    }

    // Method untuk mengambil semua log
    public static List<String> getLogs() {
        return logList;
    }
    
    // Method untuk menghapus semua log
    public static void clearLogs() {
        logList.clear();
    }
}