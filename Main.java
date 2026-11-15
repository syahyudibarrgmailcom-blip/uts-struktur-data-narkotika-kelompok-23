import controller.PutusanController;
import controller.CSVReader;
import model.Putusan;
import struktur.DynamicArray;
import view.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Gagal mengatur Look and Feel: " + e.getMessage());
        }

        System.out.println("Sedang membaca file CSV...");
        CSVReader reader = new CSVReader();
        
        // Pastikan path ini sesuai dengan posisi file CSV kamu
        DynamicArray<Putusan> dataAwal = reader.readCSV("src/data/putusan-50.csv");
        
        System.out.println("Berhasil memuat " + dataAwal.size() + " data putusan.");

        PutusanController controller = new PutusanController(dataAwal);

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(controller);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}