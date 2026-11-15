package controller;

import model.Putusan;
import struktur.DynamicArray;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CSVWriter {
    public void writeCSV(DynamicArray<Putusan> data, String filePath) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            // Tulis Header
            pw.println("caseId,nomorPerkara,pengadilan,tanggalPutusan,terdakwaKode,jenisNarkotika,beratBarangBuktiGram,pasalDakwaan,pasalTerbukti,tuntutanBulan,vonisBulan,dendaRupiah,barangBukti,keadaanMemberatkan,keadaanMeringankan,sourcePages");
            
            for (int i = 0; i < data.size(); i++) {
                Putusan p = data.get(i);
                pw.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\",%d,%d,%d,\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    p.getCaseId(),
                    p.getNomorPerkara(),
                    p.getPengadilan(),
                    p.getTanggalPutusan().toString(),
                    p.getTerdakwaKode(),
                    p.getJenisNarkotika(),
                    p.getBeratBarangBuktiGram(),
                    p.getPasalDakwaan(),
                    p.getPasalTerbukti(),
                    p.getTuntutanBulan(),
                    p.getVonisBulan(),
                    p.getDendaRupiah(),
                    p.getBarangBukti(),
                    p.getKeadaanMemberatkan(),
                    p.getKeadaanMeringankan(),
                    p.getSourcePages()
                );
            }
        } catch (Exception e) {
            System.out.println("Error menyimpan CSV: " + e.getMessage());
        }
    }
}