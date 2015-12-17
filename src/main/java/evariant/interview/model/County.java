package evariant.interview.model;

import com.opencsv.CSVParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by ccayirog on 12/16/2015.
 */
public class County {

    private CountyKey countyKey;
    private int population;
    private String msa;

    public County(CountyKey countyKey, int population, String msa) {
        this.countyKey = countyKey;
        this.population = population;
        this.msa = msa;
    }

    public static County parse(String row, CSVParser csvParser, Map<String, String> stateShortMap) throws ParseException, IOException {
        String[] fields = csvParser.parseLine(row);
        if (fields.length != 7) {
            throw new ParseException("can not parse: " + row, 0);
        }
        String name = fields[1].replace(" County", "").toLowerCase();
        String state = fields[2].toLowerCase();
        int population = Integer.parseInt(fields[4].replace(",", ""));
        String msa = fields[5].toLowerCase();
        return new County(new CountyKey(name, stateShortMap.get(state)), population, msa);
    }

    public CountyKey getCountyKey() {
        return countyKey;
    }

    public boolean isMsa() {
        return !msa.isEmpty();
    }

    public int getPopulation() {
        return population;
    }

    public String getMsa() {
        return msa;
    }
}
