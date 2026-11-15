package view;

import controller.PutusanController;
import controller.CSVWriter;
import controller.PanelAnalisisData;
import controller.ActivityLogger;
import model.Putusan;
import struktur.DynamicArray;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class MainFrame extends JFrame {
    private PutusanController controller;
    
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private JPanel sidebarPanel; 
    
    private PanelAnalisisData panelAnalisis; 

    // Warna Tema
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private final Color SIDEBAR_BG = new Color(248, 250, 252);
    private final Color BORDER_COLOR = new Color(226, 232, 240);

    public MainFrame(PutusanController controller) {
        this.controller = controller;
        
        setTitle("Sistem Pengelolaan Data Putusan Narkotika");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);

        // 1. BUAT SEMUA PANEL
        mainContentPanel.add(createDashboardPanel(), "Dashboard");
        mainContentPanel.add(createDataPutusanPanel(), "DataPutusan");
        mainContentPanel.add(createTambahPutusanPanel(), "TambahPutusan");
        mainContentPanel.add(createFilterPanel(), "Filter");
        
        panelAnalisis = new PanelAnalisisData(controller);
        mainContentPanel.add(panelAnalisis, "Analisis"); 
        
        mainContentPanel.add(createRiwayatPanel(), "Riwayat");
        mainContentPanel.add(createEksporPanel(), "Ekspor"); // <--- FITUR EKSPOR

        sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
        
        cardLayout.show(mainContentPanel, "Dashboard");
    }

    // =========================================================================
    // 1. PANEL SIDEBAR
    // =========================================================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 720));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

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
            btn.setBackground(i == 0 ? new Color(219, 234, 254) : Color.WHITE);
            btn.setForeground(i == 0 ? PRIMARY_COLOR : Color.DARK_GRAY);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainContentPanel, cardName);
                    if (cardName.equals("Analisis")) {
                        panelAnalisis.refreshData();
                    }
                    for (Component c : sidebarPanel.getComponents()) {
                        if (c instanceof JButton) {
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.DARK_GRAY);
                        }
                    }
                    btn.setBackground(new Color(219, 234, 254));
                    btn.setForeground(PRIMARY_COLOR);
                }
            });

            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        sidebar.add(Box.createVerticalGlue());
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

        JLabel lblDashboard = new JLabel("<html><h1 style='margin:0;'>Dashboard</h1><p style='margin:0; color:gray;'>Ringkasan data putusan pengadilan narkotika</p></html>");
        mainPanel.add(lblDashboard, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setBackground(Color.WHITE);

        String[] columns = {"No", "Nomor Perkara", "Pengadilan", "Jenis Narkotika", "Vonis (Bulan)", "Denda (Rp)", "Tanggal Putusan"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        DynamicArray<Putusan> data = controller.getAllData();
        
        int limit = Math.min(data.size(), 10);
        for (int i = 0; i < limit; i++) {
            Putusan p = data.get(i);
            model.addRow(new Object[]{
                i + 1, p.getNomorPerkara(), p.getPengadilan(), p.getJenisNarkotika(), 
                p.getVonisBulan(), String.format("%,d", p.getDendaRupiah()), p.getTanggalPutusan()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(35);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER_COLOR), "📚 Data Terbaru", 0, 0, new Font("SansSerif", Font.BOLD, 14), Color.DARK_GRAY));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    // =========================================================================
    // 3. PANEL DATA PUTUSAN
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
                p.getVonisBulan(), String.format("%,d", p.getDendaRupiah()), p.getTanggalPutusan()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(35);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // 4. PANEL TAMBAH PUTUSAN
    // =========================================================================
    private JPanel createTambahPutusanPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Form Tambah Putusan");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        JTextField txtCaseId = new JTextField("P" + (controller.getAllData().size() + 1));
        JTextField txtNomorPerkara = new JTextField();
        JTextField txtPengadilan = new JTextField("Pengadilan Negeri Malang");
        JTextField txtTerdakwa = new JTextField();
        JTextField txtJenisNarkotika = new JTextField();
        JTextField txtTanggal = new JTextField(LocalDate.now().toString());
        JTextField txtVonis = new JTextField();
        JTextField txtDenda = new JTextField();

        gbc.gridwidth = 1;
        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Case ID:"), gbc);
        gbc.gridx = 1; panel.add(txtCaseId, gbc);

        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Nomor Perkara:"), gbc);
        gbc.gridx = 1; panel.add(txtNomorPerkara, gbc);

        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Pengadilan:"), gbc);
        gbc.gridx = 1; panel.add(txtPengadilan, gbc);

        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Inisial Terdakwa:"), gbc);
        gbc.gridx = 1; panel.add(txtTerdakwa, gbc);

        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Jenis Narkotika:"), gbc);
        gbc.gridx = 1; panel.add(txtJenisNarkotika, gbc);

        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Tanggal Putusan (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; panel.add(txtTanggal, gbc);

        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Vonis Penjara (Bulan):"), gbc);
        gbc.gridx = 1; panel.add(txtVonis, gbc);

        gbc.gridy++; gbc.gridx = 0; panel.add(new JLabel("Denda (Rp):"), gbc);
        gbc.gridx = 1; panel.add(txtDenda, gbc);

        JButton btnSimpan = new JButton("Simpan Data");
        btnSimpan.setBackground(PRIMARY_COLOR);
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        gbc.gridy++; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(btnSimpan, gbc);

        btnSimpan.addActionListener(e -> {
            try {
                String caseId = txtCaseId.getText();
                String nomor = txtNomorPerkara.getText();
                String pengadilan = txtPengadilan.getText();
                String terdakwa = txtTerdakwa.getText();
                String narkotika = txtJenisNarkotika.getText();
                LocalDate tanggal = LocalDate.parse(txtTanggal.getText());
                int vonis = Integer.parseInt(txtVonis.getText());
                long denda = Long.parseLong(txtDenda.getText());

                Putusan putusanBaru = new Putusan(
                        caseId, nomor, pengadilan, tanggal, terdakwa, narkotika, 
                        0.0, "-", "-", 0, vonis, denda, "-", "-", "-", "-"
                ); 
                
                controller.tambahData(putusanBaru); 
                ActivityLogger.log("Menambahkan data putusan baru: " + nomor); // LOG
                JOptionPane.showMessageDialog(panel, "Data berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                panelAnalisis.refreshData();
                
                txtNomorPerkara.setText(""); txtTerdakwa.setText(""); txtJenisNarkotika.setText("");
                txtVonis.setText(""); txtDenda.setText("");
                
                cardLayout.show(mainContentPanel, "DataPutusan");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Gagal menyimpan! Pastikan format benar.\nError: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // =========================================================================
    // 5. PANEL FILTER
    // =========================================================================
    private JPanel createFilterPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("<html><h1 style='margin:0;'>Filter dan Pencarian</h1></html>");
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        
        JComboBox<String> cbKategori = new JComboBox<>(new String[]{"Nomor Perkara", "Jenis Narkotika"});
        JTextField txtKeyword = new JTextField(25);
        JButton btnCari = new JButton("Cari");
        JButton btnReset = new JButton("Reset");

        searchPanel.add(new JLabel("Cari Berdasarkan:"));
        searchPanel.add(cbKategori);
        searchPanel.add(txtKeyword);
        searchPanel.add(btnCari);
        searchPanel.add(btnReset);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"No", "Nomor Perkara", "Pengadilan", "Terdakwa", "Jenis Narkotika", "Vonis (Bulan)", "Denda (Rp)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(35);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        Runnable isiTabelAwal = () -> {
            model.setRowCount(0); 
            DynamicArray<Putusan> semuaData = controller.getAllData();
            for (int i = 0; i < semuaData.size(); i++) {
                Putusan p = semuaData.get(i);
                model.addRow(new Object[]{
                    i + 1, p.getNomorPerkara(), p.getPengadilan(), p.getTerdakwaKode(), 
                    p.getJenisNarkotika(), p.getVonisBulan(), String.format("%,d", p.getDendaRupiah())
                });
            }
        };
        isiTabelAwal.run();

        btnCari.addActionListener(e -> {
            String keyword = txtKeyword.getText().toLowerCase();
            String pilihan = (String) cbKategori.getSelectedItem();
            
            model.setRowCount(0);
            DynamicArray<Putusan> semuaData = controller.getAllData();
            int no = 1;
            for (int i = 0; i < semuaData.size(); i++) {
                Putusan p = semuaData.get(i);
                boolean cocok = false;
                if ("Nomor Perkara".equals(pilihan) && p.getNomorPerkara().toLowerCase().contains(keyword)) {
                    cocok = true;
                } else if ("Jenis Narkotika".equals(pilihan) && p.getJenisNarkotika().toLowerCase().contains(keyword)) {
                    cocok = true;
                }

                if (cocok) {
                    model.addRow(new Object[]{
                        no++, p.getNomorPerkara(), p.getPengadilan(), p.getTerdakwaKode(), 
                        p.getJenisNarkotika(), p.getVonisBulan(), String.format("%,d", p.getDendaRupiah())
                    });
                }
            }
            ActivityLogger.log("Mencari data: " + keyword); // LOG
        });

        btnReset.addActionListener(e -> {
            txtKeyword.setText("");
            isiTabelAwal.run();
        });

        return mainPanel;
    }

    // =========================================================================
    // 6. PANEL RIWAYAT AKTIVITAS
    // =========================================================================
    private JPanel createRiwayatPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("<html><h1 style='margin:0;'>Riwayat Aktivitas</h1><p style='margin:0; color:gray;'>Catatan aktivitas pengguna di dalam sistem</p></html>");
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"No", "Waktu & Aktivitas"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        
        JButton btnClear = new JButton("Hapus Semua Riwayat");
        btnClear.setBackground(new Color(239, 68, 68));
        btnClear.setForeground(Color.WHITE);
        btnClear.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        btnClear.addActionListener(e -> {
            int konfirmasi = JOptionPane.showConfirmDialog(panel, "Apakah Anda yakin ingin menghapus semua riwayat?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                ActivityLogger.clearLogs();
                model.setRowCount(0);
            }
        });
        
        bottomPanel.add(btnClear);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        table.addAncestorListener(new javax.swing.event.AncestorListener() {
            @Override
            public void ancestorAdded(javax.swing.event.AncestorEvent event) {
                model.setRowCount(0);
                int no = 1;
                for (String log : ActivityLogger.getLogs()) {
                    model.addRow(new Object[]{no++, log});
                }
            }
            @Override
            public void ancestorRemoved(javax.swing.event.AncestorEvent event) {}
            @Override
            public void ancestorMoved(javax.swing.event.AncestorEvent event) {}
        });

        return panel;
    }

    // =========================================================================
    // 7. PANEL EKSPOR DATA
    // =========================================================================
    private JPanel createEksporPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("<html><h1>Ekspor Data</h1><p>Simpan semua data putusan ke dalam format CSV.</p></html>");
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(title, gbc);

        JButton btnEkspor = new JButton("Ekspor ke CSV");
        btnEkspor.setBackground(PRIMARY_COLOR);
        btnEkspor.setForeground(Color.WHITE);
        btnEkspor.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        gbc.gridy = 1;
        panel.add(btnEkspor, gbc);

        btnEkspor.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Simpan File CSV");
            fileChooser.setSelectedFile(new java.io.File("Data_Putusan_Narkotika.csv"));
            
            int userSelection = fileChooser.showSaveDialog(panel);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                boolean sukses = CSVWriter.simpanKeCSV(controller.getAllData(), path);
                
                if (sukses) {
                    JOptionPane.showMessageDialog(panel, "Data berhasil diekspor ke:\n" + path, "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    ActivityLogger.log("Mengekspor data putusan ke CSV: " + path); // LOG
                } else {
                    JOptionPane.showMessageDialog(panel, "Gagal mengekspor data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }
}