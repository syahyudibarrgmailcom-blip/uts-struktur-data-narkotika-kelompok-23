package controller;
import model.Putusan;
import struktur.DynamicArray;

public class HasilCari {
    public DynamicArray<Putusan> dataDitemukan;
    public int jumlahLangkah; // Sesuai UTS: melaporkan jumlah langkah pencarian
    
    public HasilCari(DynamicArray<Putusan> data, int langkah) {
        this.dataDitemukan = data;
        this.jumlahLangkah = langkah;
    }
}