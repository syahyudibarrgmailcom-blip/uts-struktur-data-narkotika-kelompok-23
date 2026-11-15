package model;

import java.time.LocalDate;

public class Putusan {
    // 1. Atribut sesuai Panduan UTS Halaman 2
    private String caseId;                  // Kode unik internal kelompok
    private String nomorPerkara;            // Nomor perkara pada putusan
    private String pengadilan;              // Nama Pengadilan Negeri
    private LocalDate tanggalPutusan;       // Tanggal putusan diucapkan
    private String terdakwaKode;            // Kode anonim, bukan nama lengkap
    private String jenisNarkotika;          // Jenis zat atau daftar jenis zat
    private double beratBarangBuktiGram;    // Berat netto
    private String pasalDakwaan;            // Pasal pada dakwaan yang relevan
    private String pasalTerbukti;           // Pasal yang dinyatakan terbukti
    private int tuntutanBulan;              // Tuntutan pidana penjara dalam bulan
    private int vonisBulan;                 // Pidana penjara dalam bulan
    private long dendaRupiah;               // Pidana denda
    private String barangBukti;             // Ringkasan barang bukti utama
    private String keadaanMemberatkan;      // Ringkasan dari pertimbangan
    private String keadaanMeringankan;      // Ringkasan dari pertimbangan
    private String sourcePages;             // Halaman sumber per kelompok atribut

    // 2. Constructor Lengkap (Untuk membaca dari CSV)
    public Putusan(String caseId, String nomorPerkara, String pengadilan, LocalDate tanggalPutusan,
                   String terdakwaKode, String jenisNarkotika, double beratBarangBuktiGram,
                   String pasalDakwaan, String pasalTerbukti, int tuntutanBulan, int vonisBulan,
                   long dendaRupiah, String barangBukti, String keadaanMemberatkan,
                   String keadaanMeringankan, String sourcePages) {
        this.caseId = caseId;
        this.nomorPerkara = nomorPerkara;
        this.pengadilan = pengadilan;
        this.tanggalPutusan = tanggalPutusan;
        this.terdakwaKode = terdakwaKode;
        this.jenisNarkotika = jenisNarkotika;
        this.beratBarangBuktiGram = beratBarangBuktiGram;
        this.pasalDakwaan = pasalDakwaan;
        this.pasalTerbukti = pasalTerbukti;
        this.tuntutanBulan = tuntutanBulan;
        this.vonisBulan = vonisBulan;
        this.dendaRupiah = dendaRupiah;
        this.barangBukti = barangBukti;
        this.keadaanMemberatkan = keadaanMemberatkan;
        this.keadaanMeringankan = keadaanMeringankan;
        this.sourcePages = sourcePages;
    }

    // 3. Getter dan Setter (Wajib ada agar bisa diakses oleh UI dan Controller)
    
    public String getCaseId() { return caseId; }
    public void setCaseId(String caseId) { this.caseId = caseId; }

    public String getNomorPerkara() { return nomorPerkara; }
    public void setNomorPerkara(String nomorPerkara) { this.nomorPerkara = nomorPerkara; }

    public String getPengadilan() { return pengadilan; }
    public void setPengadilan(String pengadilan) { this.pengadilan = pengadilan; }

    public LocalDate getTanggalPutusan() { return tanggalPutusan; }
    public void setTanggalPutusan(LocalDate tanggalPutusan) { this.tanggalPutusan = tanggalPutusan; }

    public String getTerdakwaKode() { return terdakwaKode; }
    public void setTerdakwaKode(String terdakwaKode) { this.terdakwaKode = terdakwaKode; }

    public String getJenisNarkotika() { return jenisNarkotika; }
    public void setJenisNarkotika(String jenisNarkotika) { this.jenisNarkotika = jenisNarkotika; }

    public double getBeratBarangBuktiGram() { return beratBarangBuktiGram; }
    public void setBeratBarangBuktiGram(double beratBarangBuktiGram) { this.beratBarangBuktiGram = beratBarangBuktiGram; }

    public String getPasalDakwaan() { return pasalDakwaan; }
    public void setPasalDakwaan(String pasalDakwaan) { this.pasalDakwaan = pasalDakwaan; }

    public String getPasalTerbukti() { return pasalTerbukti; }
    public void setPasalTerbukti(String pasalTerbukti) { this.pasalTerbukti = pasalTerbukti; }

    public int getTuntutanBulan() { return tuntutanBulan; }
    public void setTuntutanBulan(int tuntutanBulan) { this.tuntutanBulan = tuntutanBulan; }

    public int getVonisBulan() { return vonisBulan; }
    public void setVonisBulan(int vonisBulan) { this.vonisBulan = vonisBulan; }

    public long getDendaRupiah() { return dendaRupiah; }
    public void setDendaRupiah(long dendaRupiah) { this.dendaRupiah = dendaRupiah; }

    public String getBarangBukti() { return barangBukti; }
    public void setBarangBukti(String barangBukti) { this.barangBukti = barangBukti; }

    public String getKeadaanMemberatkan() { return keadaanMemberatkan; }
    public void setKeadaanMemberatkan(String keadaanMemberatkan) { this.keadaanMemberatkan = keadaanMemberatkan; }

    public String getKeadaanMeringankan() { return keadaanMeringankan; }
    public void setKeadaanMeringankan(String keadaanMeringankan) { this.keadaanMeringankan = keadaanMeringankan; }

    public String getSourcePages() { return sourcePages; }
    public void setSourcePages(String sourcePages) { this.sourcePages = sourcePages; }
    
    // 4. Override toString() (Opsional, untuk nge-print data saat testing di console)
    @Override
    public String toString() {
        return "Putusan{" +
                "caseId='" + caseId + '\'' +
                ", nomorPerkara='" + nomorPerkara + '\'' +
                ", pengadilan='" + pengadilan + '\'' +
                ", jenisNarkotika='" + jenisNarkotika + '\'' +
                ", vonisBulan=" + vonisBulan +
                '}';
    }
}