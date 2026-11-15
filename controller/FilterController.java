package controller;

import model.Putusan;
import struktur.DynamicArray;

public class FilterController {
    // Fungsi untuk mencari berdasarkan Nomor Perkara
    public DynamicArray<Putusan> cariBerdasarkanNomor(DynamicArray<Putusan> data, String keyword) {
        DynamicArray<Putusan> hasil = new DynamicArray<>();
        String keywordLower = keyword.toLowerCase();
        
        for (int i = 0; i < data.size(); i++) {
            Putusan p = data.get(i);
            if (p.getNomorPerkara().toLowerCase().contains(keywordLower)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    // Fungsi untuk mencari berdasarkan Jenis Narkotika
    public DynamicArray<Putusan> cariBerdasarkanNarkotika(DynamicArray<Putusan> data, String keyword) {
        DynamicArray<Putusan> hasil = new DynamicArray<>();
        String keywordLower = keyword.toLowerCase();
        
        for (int i = 0; i < data.size(); i++) {
            Putusan p = data.get(i);
            if (p.getJenisNarkotika().toLowerCase().contains(keywordLower)) {
                hasil.add(p);
            }
        }
        return hasil;
    }
}