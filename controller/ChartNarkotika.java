package controller;

import model.Putusan;
import struktur.DynamicArray;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Komponen Panel untuk menampilkan Bar Chart Jumlah Putusan per Jenis Narkotika.
 */
public class ChartNarkotika extends JPanel {

    // Menyimpan data (Jenis Narkotika -> Jumlah Kasus)
    private Map<String, Integer> dataChart = new LinkedHashMap<>();
    
    // Palet warna untuk batang chart
    private final Color[] PALETTE = {
            new Color(59, 130, 246),   // Biru
            new Color(34, 197, 94),    // Hijau
            new Color(249, 115, 22),   // Oranye
            new Color(236, 72, 153),   // Pink
            new Color(168, 85, 247),   // Ungu
            new Color(234, 179, 8),    // Kuning
            new Color(20, 184, 166),   // Teal
    };

    public ChartNarkotika() {
        setBackground(Color.WHITE);
    }

    /**
     * Method untuk mengisi data chart dari Controller.
     * Panggil method ini setiap kali data putusan berubah.
     */
    public void setData(DynamicArray<Putusan> semuaPutusan) {
        dataChart.clear();
        
        for (int i = 0; i < semuaPutusan.size(); i++) {
            Putusan p = semuaPutusan.get(i);
            String jenis = p.getJenisNarkotika();
            
            // Handle jika data kosong/null
            if (jenis == null || jenis.trim().isEmpty()) {
                jenis = "Tidak Diketahui";
            }
            
            jenis = jenis.trim();
            // Tambahkan hitungan
            dataChart.put(jenis, dataChart.getOrDefault(jenis, 0) + 1);
        }
        
        // Panggil repaint agar Swing menggambar ulang komponen ini dengan data baru
        repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Aktifkan Anti-aliasing agar gambar lebih halus
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // ==== JIKA DATA KOSONG ====
        if (dataChart.isEmpty()) {
            g2.setColor(Color.GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
            String msg = "Belum ada data putusan untuk dianalisis";
            int msgW = g2.getFontMetrics().stringWidth(msg);
            g2.drawString(msg, (getWidth() - msgW) / 2, getHeight() / 2);
            return;
        }

        // ==== PENGATURAN AREA GRAFIK ====
        int padLeft = 60;   // Jarak dari kiri (untuk label Y)
        int padRight = 30;  // Jarak dari kanan
        int padTop = 40;    // Jarak dari atas (untuk judul nilai)
        int padBottom = 80; // Jarak dari bawah (untuk label X)

        int chartW = getWidth() - padLeft - padRight;
        int chartH = getHeight() - padTop - padBottom;

        // Cari nilai maksimum untuk menentukan skala Y
        int maxVal = 0;
        for (int v : dataChart.values()) {
            maxVal = Math.max(maxVal, v);
        }
        if (maxVal == 0) maxVal = 1;
        
        // Bulatkan nilai max agar garis grid rapi (misal max 7 jadi 10)
        int gridLines = 5;
        int step = (int) Math.ceil(maxVal / (double) gridLines);
        maxVal = step * gridLines;

        // ==== GAMBAR GARIS GRID & LABEL SUMBU Y ====
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        FontMetrics fm = g2.getFontMetrics();
        
        for (int i = 0; i <= gridLines; i++) {
            int y = padTop + chartH - (chartH * i / gridLines);
            
            // Garis Grid Horizontal
            g2.setColor(new Color(235, 235, 235));
            g2.drawLine(padLeft, y, padLeft + chartW, y);
            
            // Label Angka di Sumbu Y
            g2.setColor(Color.GRAY);
            String label = String.valueOf(step * i);
            g2.drawString(label, padLeft - fm.stringWidth(label) - 8, y + 4);
        }

        // ==== GAMBAR SUMBU X DAN Y ====
        g2.setColor(new Color(180, 180, 180));
        g2.drawLine(padLeft, padTop, padLeft, padTop + chartH); // Garis Y
        g2.drawLine(padLeft, padTop + chartH, padLeft + chartW, padTop + chartH); // Garis X

        // ==== GAMBAR BATANG (BAR) ====
        int n = dataChart.size();
        int totalGap = (n + 1) * 15; // Jarak antar batang
        int barW = Math.max(30, (chartW - totalGap) / n); // Lebar batang minimal 30

        int i = 0;
        for (Map.Entry<String, Integer> entry : dataChart.entrySet()) {
            String jenis = entry.getKey();
            int nilai = entry.getValue();

            // Hitung tinggi dan posisi batang
            int barH = (int) ((double) nilai / maxVal * chartH);
            int x = padLeft + 15 + i * (barW + 15);
            int y = padTop + chartH - barH;

            // Gambar Batang (Rounded Rectangle)
            g2.setColor(PALETTE[i % PALETTE.length]);
            g2.fillRoundRect(x, y, barW, barH, 10, 10);

            // Gambar Nilai di atas batang
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            String valStr = String.valueOf(nilai);
            g2.drawString(valStr, x + (barW - g2.getFontMetrics().stringWidth(valStr)) / 2, y - 8);

            // Gambar Label X (Jenis Narkotika)
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            String labelX = jenis;
            // Jika teks terlalu panjang, potong dan beri titik-titik
            if (labelX.length() > 12) {
                labelX = labelX.substring(0, 11) + "…";
            }
            int labelW = g2.getFontMetrics().stringWidth(labelX);
            g2.drawString(labelX, x + (barW - labelW) / 2, padTop + chartH + 20);

            i++;
        }

        // ==== JUDUL SUMBU Y ====
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("Jumlah Kasus", 5, padTop - 15);
    }
}