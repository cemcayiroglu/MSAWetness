package evariant.interview;

import evariant.interview.etl.CalculateWetnessPerMSAETL;
import evariant.interview.model.MSAWetness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Created by ccayirog on 12/15/2015.
 */
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        if (args.length != 4) {
            throw new IllegalArgumentException();
        }
        //calculate wetness
        List<MSAWetness> calculate = CalculateWetnessPerMSAETL.calculate(args[0], args[1], args[2], args[3]);
        //printing the result
        calculate.stream().forEach(System.out::println);

    }


}






