package controller;

import model.Putusan;
import struktur.DynamicArray;

public class StatistikController {
    public HasilAgregasi hitungStatistik(DynamicArray<Putusan> data) {
        int total = data.size();
        if (total == 0) {
            return new HasilAgregasi(0, 0, 0);
        }

        double totalVonis = 0;
        long totalDenda = 0;

        for (int i = 0; i < total; i++) {
            Putusan p = data.get(i);
            totalVonis += p.getVonisBulan();
            totalDenda += p.getDendaRupiah();
        }

        double rataRata = totalVonis / total;
        return new HasilAgregasi(total, rataRata, totalDenda);
    }
}