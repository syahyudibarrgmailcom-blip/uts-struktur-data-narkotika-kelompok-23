package controller;
import struktur.DynamicArray;
import model.Putusan;

public class HasilImpor {
    public DynamicArray<Putusan> dataBerhasil;
    public int jumlahGagal;
    
    public HasilImpor(DynamicArray<Putusan> data, int gagal) {
        this.dataBerhasil = data;
        this.jumlahGagal = gagal;
    }
}