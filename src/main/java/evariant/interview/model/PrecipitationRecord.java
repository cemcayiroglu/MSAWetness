package evariant.interview.model;

import java.text.ParseException;

/**
 * Created by ccayirog on 12/16/2015.
 */
public class PrecipitationRecord {

    private String wban;

    private String yearMonthDay;

    private int hour;

    private double precipitation;

    private String precipitationFlag;


    private PrecipitationRecord(String wban, String yearMonthDay, int hour, double precipitation, String precipitationFlag) {
        this.wban = wban;
        this.yearMonthDay = yearMonthDay;
        this.hour = hour;
        this.precipitation = precipitation;
        this.precipitationFlag = precipitationFlag;
    }

    private static final String SEPARATOR = ",";

    public static PrecipitationRecord parse(String row) throws ParseException {
        String[] fields = row.split(SEPARATOR);
        if (fields.length != 5) {
            throw new ParseException("can not parse: " + row, 0);
        }
        String precipitationTrim = fields[3].trim();
        double precipitation = Double.parseDouble((precipitationTrim.equals("T") || precipitationTrim.isEmpty() ? "0" : precipitationTrim));
        return new PrecipitationRecord(fields[0].trim(), fields[1].trim(), Integer.parseInt(fields[2].trim()), precipitation, fields[4].trim());
    }

    public boolean rained() {
        return this.precipitation > 0D;
    }

    public boolean isDay() {
        return this.hour > 7;
    }

    public String getWban() {
        return wban;
    }

    public String getYearMonthDay() {
        return yearMonthDay;
    }

    public int getHour() {
        return hour;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public String getPrecipitationFlag() {
        return precipitationFlag;
    }

    @Override
    public String toString() {
        return "PrecipitationRecord{" +
                "wban='" + wban + '\'' +
                ", yearMonthDay='" + yearMonthDay + '\'' +
                ", hour='" + hour + '\'' +
                ", precipitation='" + precipitation + '\'' +
                ", precipitationFlag='" + precipitationFlag + '\'' +
                '}';
    }
}
