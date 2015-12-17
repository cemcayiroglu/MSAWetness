package evariant.interview.model;

/**
 * Created by ccayirog on 12/16/2015.
 */
public class MSAWetness implements Comparable<MSAWetness> {
    private String msa;
    private Double wetness;

    public MSAWetness(String msa, Double wetness) {
        this.msa = msa;
        this.wetness = wetness;
    }

    @Override
    public int compareTo(MSAWetness o) {
        return this.wetness.compareTo(o.wetness);
    }

    public String getMsa() {
        return msa;
    }

    public void setMsa(String msa) {
        this.msa = msa;
    }

    public Double getWetness() {
        return wetness;
    }

    public void setWetness(Double wetness) {
        this.wetness = wetness;
    }

    @Override
    public String toString() {
        return "MSAWetness{" +
                "msa='" + msa + '\'' +
                ", wetness=" + wetness +
                '}';
    }
}
