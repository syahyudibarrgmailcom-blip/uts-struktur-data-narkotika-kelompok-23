package controller;

import model.Putusan;
import struktur.DynamicArray;

public class PutusanController {
    // Variabel untuk menyimpan data
    private DynamicArray<Putusan> daftarPutusan;

    public PutusanController(DynamicArray<Putusan> dataAwal) {
        this.daftarPutusan = dataAwal;
    }

    // Method untuk mengambil semua data (dipakai di MainFrame)
    public DynamicArray<Putusan> getAllData() {
        return this.daftarPutusan;
    }

    // Method untuk menambah data baru (dipakai saat klik tombol Simpan)
    public void tambahData(Putusan p) {
        this.daftarPutusan.add(p);
    }
}