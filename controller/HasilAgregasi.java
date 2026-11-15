package controller;

public class HasilAgregasi {
    public int totalPerkara;
    public double rataRataVonis;
    public long totalDenda;
    
    public HasilAgregasi(int totalPerkara, double rataRataVonis, long totalDenda) {
        this.totalPerkara = totalPerkara;
        this.rataRataVonis = rataRataVonis;
        this.totalDenda = totalDenda;
    }
}