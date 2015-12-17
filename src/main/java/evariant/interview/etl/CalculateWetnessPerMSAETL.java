package evariant.interview.etl;

import com.google.common.util.concurrent.AtomicDouble;
import com.opencsv.CSVParser;
import evariant.interview.model.*;
import evariant.interview.stream.UtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by ccayirog on 12/16/2015.
 */
public class CalculateWetnessPerMSAETL {

    private static Logger logger = LoggerFactory.getLogger(CalculateWetnessPerMSAETL.class);


    public static List<MSAWetness> calculate(String states, String countyPerMSA, String wbanMaster, String precipitationFile) throws Exception {
        // read the short version of states for data enrichment
        Map<String, String> stateMap = readStateShort(states);

        // read counties of MSAs
        Map<CountyKey, County> msaCountyMap = readMSA(countyPerMSA, stateMap);

        // read wbans of MSAs
        Map<String, WBAN> wbanMap = readWBAN(wbanMaster, msaCountyMap);

        // read total Precipitation per WBAN
        Map<WBAN, AtomicDouble> wbanIdTotalPrecipMap = readPercip(precipitationFile, wbanMap);

        // check for multiple wbans per county
        Map<County, Set<AtomicDouble>> multiWBANs = getMultiWBANs(msaCountyMap, wbanIdTotalPrecipMap);

        //calculate avg precipitation per count
        Map<County, Double> avgPrecipPerCounty = new HashMap<>();
        multiWBANs.entrySet().stream().forEach(r -> avgPrecipPerCounty.put(r.getKey(), takeAvg(r.getValue())));

        // calculate msa wetness
        Map<String, AtomicDouble> msaWetness = new HashMap<>();
        avgPrecipPerCounty.entrySet().stream().forEach(r ->
                {
                    if (!msaWetness.containsKey(r.getKey().getMsa())) {
                        msaWetness.put(r.getKey().getMsa(), new AtomicDouble());
                    }
                    msaWetness.get(r.getKey().getMsa()).addAndGet(r.getKey().getPopulation() * r.getValue());
                }
        );
        List<MSAWetness> msaWetnessList = new ArrayList<>();
        //sort MSA by wetness
        msaWetness.entrySet().stream().forEach(r -> msaWetnessList.add(new MSAWetness(r.getKey(), r.getValue().get())));
        Collections.sort(msaWetnessList, Collections.reverseOrder());
        return msaWetnessList;
    }


    private static double takeAvg(Set<AtomicDouble> atomicDoubles) {
        double total = 0D;
        for (AtomicDouble atomicDouble : atomicDoubles) {
            total += atomicDouble.get();
        }
        return total / atomicDoubles.size();
    }


    private static Map<County, Set<AtomicDouble>> getMultiWBANs(Map<CountyKey, County> msaCountyMap, Map<WBAN, AtomicDouble> wbanIdTotalPrecipMap) {
        Map<County, Set<AtomicDouble>> countySetMap = new HashMap<>();
        wbanIdTotalPrecipMap.keySet().stream().forEach(
                r -> {
                    if (!countySetMap.containsKey(msaCountyMap.get(r.getCountyKey()))) {
                        countySetMap.put(msaCountyMap.get(r.getCountyKey()), new HashSet<>());
                    }
                    countySetMap.get(msaCountyMap.get(r.getCountyKey())).add(wbanIdTotalPrecipMap.get(r));
                }
        );
        return countySetMap;
    }

    private static Map<WBAN, AtomicDouble> readPercip(String path, Map<String, WBAN> wbanMap) throws IOException {
        Map<WBAN, AtomicDouble> wbanIdTotalPrecipMap = new HashMap<>();
        BufferedReader br = getBufferedReaderWithoutHeader(path);
        br.lines().
                map(catchException(PrecipitationRecord::parse)).
                filter(notNull).
                filter(PrecipitationRecord::rained).
                filter(PrecipitationRecord::isDay).
                filter(r -> wbanMap.containsKey(r.getWban())).
                forEach(r ->
                {
                    if (!wbanIdTotalPrecipMap.containsKey(wbanMap.get(r.getWban()))) {
                        wbanIdTotalPrecipMap.put(wbanMap.get(r.getWban()), new AtomicDouble(0D));
                    }
                    wbanIdTotalPrecipMap.get(wbanMap.get(r.getWban())).addAndGet(r.getPrecipitation());
                });
        br.close();
        return wbanIdTotalPrecipMap;
    }


    private static Map<String, WBAN> readWBAN(String path, Map<CountyKey, County> msaCountyMap) throws IOException {
        CSVParser csvParser = new CSVParser('|', '"', '\n', false, false);
        Map<String, WBAN> wbanMap = new HashMap<>();
        BufferedReader br = getBufferedReaderWithoutHeader(path);
        br.lines().
                map(catchException(r -> WBAN.parse(r, csvParser))).
                filter(notNull).
                filter(r -> msaCountyMap.containsKey(r.getCountyKey())).
                forEach(r -> wbanMap.put(r.getWbanId(), r));
        br.close();
        return wbanMap;
    }

    private static Map<String, String> readStateShort(String path) throws IOException {
        CSVParser csvParser = new CSVParser(',');
        Map<String, String> stateMap = new HashMap<>();
        BufferedReader br = getBufferedReaderWithoutHeader(path);
        br.lines().map(catchException(csvParser::parseLine)).
                forEach(r -> stateMap.put(r[0].toLowerCase(), r[1].toLowerCase()));
        br.close();
        return stateMap;
    }

    private static Map<CountyKey, County> readMSA(String path, Map<String, String> stateShortMap) throws IOException {
        CSVParser csvParser = new CSVParser(',');
        Map<CountyKey, County> msaMap = new HashMap<>();
        BufferedReader br = getBufferedReaderWithoutHeader(path);
        br.lines().
                map(catchException(r -> County.parse(r, csvParser, stateShortMap))).
                filter(notNull).
                filter(County::isMsa).
                forEach(r -> msaMap.put(r.getCountyKey(), r));
        br.close();
        return msaMap;
    }

    private static BufferedReader getBufferedReaderWithoutHeader(String path) throws IOException {
        InputStream inputStream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        br.readLine();
        return br;
    }


    public static final Predicate<Object> notNull = o -> o != null;

    public static <T, R> Function<T, R> catchException(UtilException.Function_WithExceptions<T, R> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        };
    }

}
