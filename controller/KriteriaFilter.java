package controller;

public class KriteriaFilter {
    private String jenisNarkotika;
    private int minVonis;
    private int maxVonis;

    public KriteriaFilter(String jenisNarkotika, int minVonis, int maxVonis) {
        this.jenisNarkotika = jenisNarkotika;
        this.minVonis = minVonis;
        this.maxVonis = maxVonis;
    }

    public String getJenisNarkotika() { return jenisNarkotika; }
    public int getMinVonis() { return minVonis; }
    public int getMaxVonis() { return maxVonis; }
}