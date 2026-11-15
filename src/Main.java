import controller.PutusanController;
import controller.CSVReader;
import model.Putusan;
import struktur.DynamicArray;
import view.MainFrame;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        // 1. SETUP LOOK AND FEEL & FONT GLOBAL
        try {
            // Gunakan Look and Feel sistem operasi
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Trik agar font lebih besar dan tajam (Anti-Aliasing)
            FontUIResource fontBaru = new FontUIResource(new Font("SansSerif", Font.PLAIN, 14));
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof FontUIResource) {
                    UIManager.put(key, fontBaru);
                }
            }
        } catch (Exception e) {
            System.err.println("Gagal mengatur Look and Feel: " + e.getMessage());
        }

        // 2. BACA DATA DARI CSV
        System.out.println("Sedang membaca file CSV...");
        CSVReader reader = new CSVReader();
        
        // Pastikan path ini sesuai dengan posisi file CSV kamu
        DynamicArray<Putusan> dataAwal = reader.readCSV("src/data/putusan-50.csv");
        
        if (dataAwal == null) {
            System.err.println("Gagal memuat data CSV. Pastikan file ada di: src/data/putusan-50.csv");
            System.exit(1);
        }
        
        System.out.println("Berhasil memuat " + dataAwal.size() + " data putusan.");

        // 3. INISIALISASI CONTROLLER
        PutusanController controller = new PutusanController(dataAwal);

        // 4. JALANKAN UI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(controller);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null); // Tampil di tengah layar
        });
    }
}