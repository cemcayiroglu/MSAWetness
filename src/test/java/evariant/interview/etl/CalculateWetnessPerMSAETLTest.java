package evariant.interview.etl;

import evariant.interview.model.MSAWetness;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ccayirog on 12/16/2015.
 */
public class CalculateWetnessPerMSAETLTest {

    @Test
    public void testCalculateWetnessPerMSAETL() throws Exception {
        List<MSAWetness> calculate = CalculateWetnessPerMSAETL.calculate("src/test/resources/states.csv",
                "src/test/resources/CountyMSA.txt", "src/test/resources/wbanmasterlist.psv", "src/test/resources/201505precip.txt");
        assertEquals("Check list size", 286, calculate.size());
        assertEquals("Check fist msa", "houston-the woodlands-sugar land, tx metropolitan statistical area", calculate.get(0).getMsa());
        assertEquals("Check second msa", "dallas-fort worth-arlington, tx metropolitan statistical area", calculate.get(1).getMsa());
    }

}
