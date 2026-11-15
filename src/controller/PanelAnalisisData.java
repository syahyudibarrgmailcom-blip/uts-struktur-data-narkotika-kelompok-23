package controller;

import model.Putusan;
import struktur.DynamicArray;

import javax.swing.*;
import java.awt.*;

public class PanelAnalisisData extends JPanel {

    private PutusanController controller;
    private ChartNarkotika chartNarkotika;

    // Variabel untuk Label Ringkasan Statistik
    private JLabel lblTotalPutusan;
    private JLabel lblRataVonis;
    private JLabel lblRataTuntutan;

    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color CARD_BG = Color.WHITE;

    public PanelAnalisisData(PutusanController controller) {
        this.controller = controller;
        
        // Setup Layout Utama
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // 1. HEADER (Judul Halaman)
        JLabel title = new JLabel(
                "<html><h1 style='margin:0;'>Analisis Data</h1>" +
                "<p style='margin:0; color:gray;'>Visualisasi dan statistik putusan narkotika</p></html>");
        add(title, BorderLayout.NORTH);

        // 2. AREA TENGAH (Berisi Kartu Statistik + Grafik)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);

        // --- A. PANEL KARTU STATISTIK (Atas) ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0)); // 1 baris, 3 kolom
        statsPanel.setBackground(Color.WHITE);

        lblTotalPutusan = new JLabel("0");
        lblRataVonis = new JLabel("0 bln");
        lblRataTuntutan = new JLabel("0 bln");

        statsPanel.add(createCard("📄", lblTotalPutusan, "Total Putusan", new Color(239, 246, 255), new Color(59, 130, 246)));
        statsPanel.add(createCard("⚖️", lblRataVonis, "Rata-rata Vonis", new Color(240, 253, 244), new Color(22, 163, 74)));
        statsPanel.add(createCard("📊", lblRataTuntutan, "Rata-rata Tuntutan", new Color(250, 245, 255), new Color(147, 51, 234)));

        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // --- B. PANEL GRAFIK (Bawah) ---
        JPanel cardChart = new JPanel(new BorderLayout(0, 10));
        cardChart.setBackground(CARD_BG);
        cardChart.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel chartTitle = new JLabel("📊 Jumlah Putusan per Jenis Narkotika");
        chartTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        chartTitle.setForeground(Color.DARK_GRAY);

        chartNarkotika = new ChartNarkotika();

        cardChart.add(chartTitle, BorderLayout.NORTH);
        cardChart.add(chartNarkotika, BorderLayout.CENTER);

        centerPanel.add(cardChart, BorderLayout.CENTER);

        // Masukkan centerPanel ke panel utama
        add(centerPanel, BorderLayout.CENTER);

        // 3. ISI DATA SAAT PERTAMA KALI DIBUKA
        refreshData();
    }

    /**
     * Method ini dipanggil untuk mengambil data terbaru dari controller,
     * lalu mengupdate grafik dan angka statistik.
     */
    public void refreshData() {
        DynamicArray<Putusan> data = controller.getAllData();
        
        // 1. Update Grafik Batang
        chartNarkotika.setData(data);

        // 2. Hitung Statistik
        int total = data.size();
        double totalVonis = 0;
        double totalTuntutan = 0;

        for (int i = 0; i < total; i++) {
            totalVonis += data.get(i).getVonisBulan();
            totalTuntutan += data.get(i).getTuntutanBulan();
        }

        double rataVonis = (total > 0) ? (totalVonis / total) : 0;
        double rataTuntutan = (total > 0) ? (totalTuntutan / total) : 0;

        // 3. Update Text pada Label
        lblTotalPutusan.setText(String.valueOf(total));
        lblRataVonis.setText(String.format("%.1f bln", rataVonis));
        lblRataTuntutan.setText(String.format("%.1f bln", rataTuntutan));
    }

    /**
     * Helper untuk membuat desain Kartu Statistik
     */
    private JPanel createCard(String icon, JLabel valueLabel, String title, Color bgColor, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));

        // Ikon
        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setOpaque(true);
        lblIcon.setBackground(bgColor);
        lblIcon.setForeground(iconColor);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIcon.setPreferredSize(new Dimension(50, 50));

        // Teks (Angka & Judul)
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(CARD_BG);

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        valueLabel.setForeground(Color.DARK_GRAY);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTitle.setForeground(Color.GRAY);

        textPanel.add(valueLabel);
        textPanel.add(lblTitle);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
}