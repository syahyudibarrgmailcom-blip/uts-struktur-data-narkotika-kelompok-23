package controller;

import model.Putusan;
import struktur.DynamicArray;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;

public class CSVReader {
    public DynamicArray<Putusan> readCSV(String filePath) {
        DynamicArray<Putusan> data = new DynamicArray<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Lewati header
                }
                
                // Split CSV dengan aman (mengabaikan koma di dalam tanda kutip)
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                
                if (values.length >= 16) {
                    // Bersihkan tanda kutip (")
                    for (int i = 0; i < values.length; i++) {
                        values[i] = values[i].replaceAll("^\"|\"$", "").trim();
                    }
                    
                    try {
                        Putusan p = new Putusan(
                            values[0], // caseId
                            values[1], // nomorPerkara
                            values[2], // pengadilan
                            LocalDate.parse(values[3]), // tanggalPutusan
                            values[4], // terdakwaKode
                            values[5], // jenisNarkotika
                            Double.parseDouble(values[6].replace(",", ".")), // beratBarangBukti
                            values[7], // pasalDakwaan
                            values[8], // pasalTerbukti
                            Integer.parseInt(values[9]), // tuntutan
                            Integer.parseInt(values[10]), // vonis
                            Long.parseLong(values[11]), // denda
                            values[12], // barangBukti
                            values[13], // memberatkan
                            values[14], // meringankan
                            values[15]  // sourcePages
                        );
                        data.add(p);
                    } catch (Exception e) {
                        System.out.println("Gagal parsing baris: " + line);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error membaca file CSV: " + e.getMessage());
        }
        
        return data;
    }
}