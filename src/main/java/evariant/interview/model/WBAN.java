package evariant.interview.model;

import com.opencsv.CSVParser;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by ccayirog on 12/16/2015.
 */
public class WBAN {
    private String wbanId;
    private CountyKey countyKey;

    public WBAN(String wbanId, CountyKey county) {
        this.wbanId = wbanId;
        this.countyKey = county;
    }

    public static WBAN parse(String row, CSVParser csvParser) throws ParseException, IOException {
        String[] fields = csvParser.parseLine(row);
        if (fields.length != 19) {
            throw new ParseException("can not parse: " + row, 0);
        }
        String wbanId = fields[1];
        String state = fields[3].toLowerCase();
        String countyName = fields[4].toLowerCase();
        return new WBAN(wbanId, new CountyKey(countyName, state));
    }

    public String getWbanId() {
        return wbanId;
    }

    public CountyKey getCountyKey() {
        return countyKey;
    }

    @Override
    public String toString() {
        return "WBAN{" +
                "wbanId='" + wbanId + '\'' +
                ", countyKey=" + countyKey +
                '}';
    }
}
