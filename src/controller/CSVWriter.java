package controller;

import model.Putusan;
import struktur.DynamicArray;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    public static boolean simpanKeCSV(DynamicArray<Putusan> data, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Case ID,Nomor Perkara,Pengadilan,Tanggal,Terdakwa,Jenis Narkotika,Vonis (Bulan),Denda (Rp)\n");

            for (int i = 0; i < data.size(); i++) {
                Putusan p = data.get(i);
                writer.append(p.getCaseId()).append(",")
                      .append(p.getNomorPerkara()).append(",")
                      .append(p.getPengadilan()).append(",")
                      .append(p.getTanggalPutusan().toString()).append(",")
                      .append(p.getTerdakwaKode()).append(",")
                      .append(p.getJenisNarkotika()).append(",")
                      .append(String.valueOf(p.getVonisBulan())).append(",")
                      .append(String.valueOf(p.getDendaRupiah())).append("\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Gagal ekspor: " + e.getMessage());
            return false;
        }
    }
}