package view;

import controller.PutusanController;
import controller.StatistikController;
import controller.HasilAgregasi;
import controller.FilterController;
import model.Putusan;
import struktur.DynamicArray;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class MainFrame extends JFrame {
    private PutusanController controller;
    
    // Panel utama untuk CardLayout (Navigasi halaman)
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel; // Simpan referensi sidebar untuk reset warna tombol

    // Warna Tema
    private final Color PRIMARY_COLOR = new Color(59, 130, 246); // Biru
    private final Color SIDEBAR_BG = new Color(248, 250, 252); // Abu sangat muda
    private final Color CARD_BG = Color.WHITE;
    private final Color BORDER_COLOR = new Color(226, 232, 240);

    public MainFrame(PutusanController controller) {
        this.controller = controller;
        
        setTitle("Sistem Pengelolaan Data Putusan Narkotika");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inisialisasi CardLayout untuk area konten
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        // 1. BUAT SEMUA PANEL (Kartu)
        mainContentPanel.add(createDashboardPanel(), "Dashboard");
        mainContentPanel.add(createDataPutusanPanel(), "DataPutusan");
        mainContentPanel.add(createTambahPutusanPanel(), "TambahPutusan");
        mainContentPanel.add(createFilterPanel(), "Filter"); // <-- Fitur Filter dimasukkan ke sini
        mainContentPanel.add(createPlaceholderPanel("Analisis Data", "Grafik dan Analisis akan tampil di sini."), "Analisis");
        mainContentPanel.add(createPlaceholderPanel("Riwayat Aktivitas", "Log aktivitas akan tampil di sini."), "Riwayat");
        mainContentPanel.add(createPlaceholderPanel("Ekspor Data", "Fitur Ekspor CSV akan tampil di sini."), "Ekspor");

        // 2. PANEL KIRI (SIDEBAR)
        sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);

        // 3. TAMBAHKAN MAIN CONTENT PANEL KE FRAME
        add(mainContentPanel, BorderLayout.CENTER);
        
        // Tampilkan Dashboard sebagai default
        cardLayout.show(mainContentPanel, "Dashboard");
    }

    // =========================================================================
    // 1. PANEL SIDEBAR (KIRI)
    // =========================================================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 720));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        // Header Sidebar
        JPanel headerSidebar = new JPanel();
        headerSidebar.setLayout(new BoxLayout(headerSidebar, BoxLayout.Y_AXIS));
        headerSidebar.setBackground(SIDEBAR_BG);
        headerSidebar.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        JLabel iconScale = new JLabel("⚖️"); 
        iconScale.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconScale.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel title = new JLabel("Case Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subTitle = new JLabel("Narcotics Decision System");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subTitle.setForeground(Color.GRAY);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerSidebar.add(iconScale);
        headerSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        headerSidebar.add(title);
        headerSidebar.add(subTitle);
        sidebar.add(headerSidebar);

        // Menu Sidebar
        String[] menus = {"1. Dashboard", "2. Data Putusan", "3. Tambah Putusan", 
                          "4. Filter dan Pencarian", "5. Analisis Data", "6. Riwayat Aktivitas", "7. Ekspor Data"};
        String[] cardNames = {"Dashboard", "DataPutusan", "TambahPutusan", "Filter", "Analisis", "Riwayat", "Ekspor"};

        for (int i = 0; i < menus.length; i++) {
            String menu = menus[i];
            String cardName = cardNames[i];
            
            JButton btn = new JButton(menu);
            btn.setMaximumSize(new Dimension(230, 45));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setFont(new Font("SansSerif", Font.BOLD, 13));
            
            // Warna default (putih) atau aktif (biru muda)
            btn.setBackground(i == 0 ? new Color(219, 234, 254) : Color.WHITE);
            btn.setForeground(i == 0 ? PRIMARY_COLOR : Color.DARK_GRAY);
            
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // --- AKSI KLIK TOMBOL MENU ---
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Pindah panel sesuai yang diklik
                    cardLayout.show(mainContentPanel, cardName);
                    
                    // Reset warna semua tombol
                    for (Component c : sidebarPanel.getComponents()) {
                        if (c instanceof JButton) {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.DARK_GRAY);
                        }
                    }
                    // Highlight tombol yang diklik
                    btn.setBackground(new Color(219, 234, 254));
                    btn.setForeground(PRIMARY_COLOR);
                }
            });

            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        sidebar.add(Box.createVerticalGlue());

        // Footer Sidebar
        JLabel footer = new JLabel("<html><b>● Sistem Siap Digunakan</b><br><small>Universitas Muhammadiyah Malang</small></html>");
        footer.setForeground(new Color(22, 163, 74));
        footer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidebar.add(footer);

        return sidebar;
    }

    // =========================================================================
    // 2. PANEL DASHBOARD
    // =========================================================================
    private JPanel createDashboardPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel lblDashboard = new JLabel("<html><h1 style='margin:0;'>Dashboard</h1><p style='margin:0; color:gray;'>Ringkasan data putusan pengadilan narkotika</p></html>");
        headerPanel.add(lblDashboard, BorderLayout.WEST);
        
        JLabel lblUser = new JLabel("📅 Selasa, 21 Oktober 2025  |  👥 Kelompok B");
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblUser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        headerPanel.add(lblUser, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- CENTER (Cards + Table) ---
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);

        // Hitung Statistik menggunakan Controller
        StatistikController statCtrl = new StatistikController();
        DynamicArray<Putusan> data = controller.getAllData();
        HasilAgregasi hasil = statCtrl.hitungStatistik(data);
        
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));

        // 1. CARD PANEL (Kotak Ringkasan)
        JPanel cardPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.add(createCard("📄", String.valueOf(hasil.totalPerkara), "Total Putusan", new Color(239, 246, 255), PRIMARY_COLOR));
        cardPanel.add(createCard("⚖️", String.format("%.0f Bulan", hasil.rataRataVonis), "Rata-rata Vonis", new Color(240, 253, 244), new Color(22, 163, 74)));
        cardPanel.add(createCard("💰", nf.format(hasil.totalDenda).replace(",00", ""), "Total Denda", new Color(250, 245, 255), new Color(147, 51, 234)));
        cardPanel.add(createCard("💊", "3", "Jenis Narkotika", new Color(255, 247, 237), new Color(234, 88, 12)));
        centerPanel.add(cardPanel, BorderLayout.NORTH);

        // 2. CHART & TABLE SPLIT
        JPanel chartAndTablePanel = new JPanel(new BorderLayout(0, 20));
        chartAndTablePanel.setBackground(Color.WHITE);

        // --- BAGIAN GRAFIK (Mockup Placeholder) ---
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setBackground(Color.WHITE);
        
        // Placeholder Bar Chart
        JPanel barChartPanel = new JPanel(new BorderLayout());
        barChartPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        barChartPanel.add(new JLabel("<html><h3>📊 Statistik Vonis Penjara</h3></html>"), BorderLayout.NORTH);
        JPanel barPlaceholder = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(96, 165, 250));
                int[] heights = {50, 100, 130, 160, 120, 90};
                for (int i = 0; i < heights.length; i++) {
                    g.fillRect(50 + (i * 60), 200 - heights[i], 40, heights[i]);
                }
            }
        };
        barChartPanel.add(barPlaceholder, BorderLayout.CENTER);

        // Placeholder Pie Chart
        JPanel pieChartPanel = new JPanel(new BorderLayout());
        pieChartPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        pieChartPanel.add(new JLabel("<html><h3>🍩 Distribusi Jenis Narkotika</h3></html>"), BorderLayout.NORTH);
        JPanel piePlaceholder = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(96, 165, 250)); g.fillArc(50, 50, 150, 150, 0, 144);
                g.setColor(new Color(74, 222, 128)); g.fillArc(50, 50, 150, 150, 144, 126);
                g.setColor(new Color(251, 146, 60)); g.fillArc(50, 50, 150, 150, 270, 90);
                g.setColor(Color.WHITE); g.fillOval(85, 85, 80, 80);
            }
        };
        pieChartPanel.add(piePlaceholder, BorderLayout.CENTER);

        chartsPanel.add(barChartPanel);
        chartsPanel.add(pieChartPanel);

        // --- TABEL DATA TERBARU ---
        String[] columns = {"No", "Nomor Perkara", "Pengadilan", "Jenis Narkotika", "Vonis (Bulan)", "Denda (Rp)", "Tanggal Putusan"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        int limit = Math.min(hasil.totalPerkara, 5);
        for (int i = 0; i < limit; i++) {
            Putusan p = data.get(i);
            model.addRow(new Object[]{
                i + 1, p.getNomorPerkara(), p.getPengadilan(), p.getJenisNarkotika(), 
                p.getVonisBulan(), String.format("%,d", p.getDendaRupiah()), p.getTanggalPutusan().toString()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER_COLOR), "📚 Data Terbaru", 0, 0, new Font("SansSerif", Font.BOLD, 14), Color.DARK_GRAY));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(800, 250));

        chartAndTablePanel.add(chartsPanel, BorderLayout.CENTER);
        chartAndTablePanel.add(tablePanel, BorderLayout.SOUTH);
        
        centerPanel.add(chartAndTablePanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    // =========================================================================
    // 3. PANEL DATA PUTUSAN (TABEL UTAMA)
    // =========================================================================
    private JPanel createDataPutusanPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("<html><h1 style='margin:0;'>Data Putusan</h1></html>");
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"No", "Nomor Perkara", "Pengadilan", "Terdakwa", "Jenis Narkotika", "Vonis (Bulan)", "Denda (Rp)", "Tanggal"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        DynamicArray<Putusan> data = controller.getAllData();
        for (int i = 0; i < data.size(); i++) {
            Putusan p = data.get(i);
            model.addRow(new Object[]{
                i + 1, p.getNomorPerkara(), p.getPengadilan(), p.getTerdakwaKode(), p.getJenisNarkotika(), 
                p.getVonisBulan(), String.format("%,d", p.getDendaRupiah()), p.getTanggalPutusan().toString()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // 4. PANEL TAMBAH PUTUSAN (FORM INPUT)
    // =========================================================================
    private JPanel createTambahPutusanPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Judul
        JLabel title = new JLabel("Form Tambah Putusan");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        // Komponen Input
        JTextField txtCaseId = new JTextField("P00X");
        JTextField txtNomorPerkara = new JTextField();
        JTextField txtPengadilan = new JTextField("Pengadilan Negeri Jakarta Utara");
        JTextField txtTerdakwa = new JTextField();
        JTextField txtJenisNarkotika = new JTextField();
        JTextField txtTanggal = new JTextField(LocalDate.now().toString());
        JTextField txtVonis = new JTextField();
        JTextField txtDenda = new JTextField();

        // Susunan Form
        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Case ID:"), gbc);
        gbc.gridx = 1; panel.add(txtCaseId, gbc);

        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Nomor Perkara:"), gbc);
        gbc.gridx = 1; panel.add(txtNomorPerkara, gbc);

        gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Pengadilan:"), gbc);
        gbc.gridx = 1; panel.add(txtPengadilan, gbc);

        gbc.gridy = 4; gbc.gridx = 0; panel.add(new JLabel("Inisial Terdakwa:"), gbc);
        gbc.gridx = 1; panel.add(txtTerdakwa, gbc);

        gbc.gridy = 5; gbc.gridx = 0; panel.add(new JLabel("Jenis Narkotika:"), gbc);
        gbc.gridx = 1; panel.add(txtJenisNarkotika, gbc);

        gbc.gridy = 6; gbc.gridx = 0; panel.add(new JLabel("Tanggal Putusan (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; panel.add(txtTanggal, gbc);

        gbc.gridy = 7; gbc.gridx = 0; panel.add(new JLabel("Vonis Penjara (Bulan):"), gbc);
        gbc.gridx = 1; panel.add(txtVonis, gbc);

        gbc.gridy = 8; gbc.gridx = 0; panel.add(new JLabel("Denda (Rp):"), gbc);
        gbc.gridx = 1; panel.add(txtDenda, gbc);

        // Tombol Simpan
        JButton btnSimpan = new JButton("Simpan Data");
        btnSimpan.setBackground(PRIMARY_COLOR);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        gbc.gridy = 9; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(btnSimpan, gbc);

        // Aksi Ketika Tombol Simpan Diklik
        btnSimpan.addActionListener(e -> {
            try {
                // 1. Ambil data dari form
                String caseId = txtCaseId.getText();
                String nomor = txtNomorPerkara.getText();
                String pengadilan = txtPengadilan.getText();
                String terdakwa = txtTerdakwa.getText();
                String narkotika = txtJenisNarkotika.getText();
                LocalDate tanggal = LocalDate.parse(txtTanggal.getText());
                int vonis = Integer.parseInt(txtVonis.getText());
                long denda = Long.parseLong(txtDenda.getText());

                // 2. Masukkan ke dalam Constructor Putusan (16 Parameter)
                Putusan putusanBaru = new Putusan(
                        caseId, 
                        nomor, 
                        pengadilan, 
                        tanggal, 
                        terdakwa, 
                        narkotika, 
                        0.0,               // beratBarangBuktiGram (Default 0.0)
                        "-",               // pasalDakwaan (Default)
                        "-",               // pasalTerbukti (Default)
                        0,                 // tuntutanBulan (Default)
                        vonis,             // vonisBulan (Dari form)
                        denda,             // dendaRupiah (Dari form)
                        "-",               // barangBukti (Default)
                        "-",               // keadaanMemberatkan (Default)
                        "-",               // keadaanMeringankan (Default)
                        "-"                // sourcePages (Default)
                ); 
                
                // 3. Tambahkan ke controller
                controller.tambahData(putusanBaru); 

                JOptionPane.showMessageDialog(panel, "Data berhasil disimpan ke memori!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                // 4. Kosongkan form
                txtNomorPerkara.setText(""); txtTerdakwa.setText(""); txtJenisNarkotika.setText("");
                txtVonis.setText(""); txtDenda.setText("");

                // 5. Pindah ke halaman Data Putusan agar bisa lihat hasilnya
                cardLayout.show(mainContentPanel, "DataPutusan");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Gagal menyimpan! Pastikan format angka dan tanggal benar.\nError: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // =========================================================================
    // 4.5 PANEL FILTER DAN PENCARIAN
    // =========================================================================
    private JPanel createFilterPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // --- HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        JLabel title = new JLabel("<html><h1 style='margin:0;'>Filter dan Pencarian</h1></html>");
        headerPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- BAGIAN ATAS: FORM PENCARIAN ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_COLOR), "🔍 Cari Data Putusan", 
            0, 0, new Font("SansSerif", Font.BOLD, 14), Color.DARK_GRAY));

        JLabel lblKategori = new JLabel("Cari Berdasarkan:");
        String[] kategori = {"Nomor Perkara", "Jenis Narkotika"};
        JComboBox<String> cbKategori = new JComboBox<>(kategori);
        
        JTextField txtKeyword = new JTextField(25);
        JButton btnCari = new JButton("Cari");
        btnCari.setBackground(PRIMARY_COLOR);
        btnCari.setForeground(Color.WHITE);
        btnCari.setFont(new Font("SansSerif", Font.BOLD, 12));

        JButton btnReset = new JButton("Reset");
        btnReset.setBackground(Color.LIGHT_GRAY);

        searchPanel.add(lblKategori);
        searchPanel.add(cbKategori);
        searchPanel.add(txtKeyword);
        searchPanel.add(btnCari);
        searchPanel.add(btnReset);
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // --- BAGIAN BAWAH: TABEL HASIL ---
        String[] columns = {"No", "Nomor Perkara", "Pengadilan", "Terdakwa", "Jenis Narkotika", "Vonis (Bulan)", "Denda (Rp)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // --- LOGIKA PENCARIAN ---
        FilterController filterCtrl = new FilterController();

        // Fungsi bantuan untuk mengisi tabel
        Runnable isiTabelAwal = () -> {
            model.setRowCount(0); // Hapus data lama
            DynamicArray<Putusan> semuaData = controller.getAllData();
            for (int i = 0; i < semuaData.size(); i++) {
                Putusan p = semuaData.get(i);
                model.addRow(new Object[]{
                    i + 1, p.getNomorPerkara(), p.getPengadilan(), p.getTerdakwaKode(), 
                    p.getJenisNarkotika(), p.getVonisBulan(), String.format("%,d", p.getDendaRupiah())
                });
            }
        };

        // Tampilkan semua data saat panel pertama kali dibuka
        isiTabelAwal.run();

        // Aksi Tombol Cari
        btnCari.addActionListener(e -> {
            String keyword = txtKeyword.getText();
            String pilihan = (String) cbKategori.getSelectedItem();
            
            DynamicArray<Putusan> hasilPencarian;
            if ("Nomor Perkara".equals(pilihan)) {
                hasilPencarian = filterCtrl.cariBerdasarkanNomor(controller.getAllData(), keyword);
            } else {
                hasilPencarian = filterCtrl.cariBerdasarkanNarkotika(controller.getAllData(), keyword);
            }

            // Tampilkan hasil ke tabel
            model.setRowCount(0);
            for (int i = 0; i < hasilPencarian.size(); i++) {
                Putusan p = hasilPencarian.get(i);
                model.addRow(new Object[]{
                    i + 1, p.getNomorPerkara(), p.getPengadilan(), p.getTerdakwaKode(), 
                    p.getJenisNarkotika(), p.getVonisBulan(), String.format("%,d", p.getDendaRupiah())
                });
            }
            
            if (hasilPencarian.size() == 0) {
                JOptionPane.showMessageDialog(mainPanel, "Data tidak ditemukan.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Aksi Tombol Reset
        btnReset.addActionListener(e -> {
            txtKeyword.setText("");
            isiTabelAwal.run(); // Kembalikan ke tampilan awal
        });

        return mainPanel;
    }

    // =========================================================================
    // 5. PANEL PLACEHOLDER (SEMENTARA UNTUK MENU LAIN)
    // =========================================================================
    private JPanel createPlaceholderPanel(String title, String message) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitle = new JLabel("<html><h1 style='margin:0;'>" + title + "</h1></html>");
        panel.add(lblTitle, BorderLayout.NORTH);
        
        JLabel lblMessage = new JLabel(message, SwingConstants.CENTER);
        lblMessage.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblMessage.setForeground(Color.GRAY);
        panel.add(lblMessage, BorderLayout.CENTER);
        
        return panel;
    }

    // =========================================================================
    // 6. FUNGSI BANTUAN: CARD STATISTIK
    // =========================================================================
    private JPanel createCard(String icon, String value, String title, Color bgColor, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        
        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setOpaque(true);
        lblIcon.setBackground(bgColor);
        lblIcon.setForeground(iconColor);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        lblIcon.setPreferredSize(new Dimension(50, 50));
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(CARD_BG);
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 22));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTitle.setForeground(Color.GRAY);

        textPanel.add(lblValue);
        textPanel.add(lblTitle);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }
}