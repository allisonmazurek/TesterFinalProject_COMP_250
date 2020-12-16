package finalproject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class SearchEngineTest {
    SearchEngine actualEngine;
    SearchEngine expectedEngine;

    @BeforeEach
    void setup() {
        try {
            actualEngine = new SearchEngine("Test1.xml");
            actualEngine.crawlAndIndex("A");
        }
        catch (Exception e) {
            System.out.println("Exception Found: " + e.toString() + "\n");
            e.printStackTrace();
        }

        expectedEngine = new SearchEngine();
        expectedEngine.internet.addVertex("A");
        expectedEngine.internet.addVertex("B");
        expectedEngine.internet.addVertex("C");
        expectedEngine.internet.addVertex("D");
        expectedEngine.internet.addEdge("A", "B");
        expectedEngine.internet.addEdge("A", "C");
        expectedEngine.internet.addEdge("B", "A");
        expectedEngine.internet.addEdge("B", "C");
        expectedEngine.internet.addEdge("B", "D");
        expectedEngine.internet.addEdge("C", "A");
        expectedEngine.internet.addEdge("D", "C");
        expectedEngine.internet.setPageRank("A", 1.2702546296296298);
        expectedEngine.internet.setPageRank("B", 0.8194444444444444);
        expectedEngine.internet.setPageRank("C", 1.2745949074074074);
        expectedEngine.internet.setPageRank("D", 0.6357060185185185);
    }

    @AfterEach
    void tearDown() {
        for (String s : expectedEngine.internet.getVertices()){
            actualEngine.internet.setPageRank(s, 1.0);
        }
    }

    @Test
    void crawlAndIndexSizeAnVerticesEqual() {
        assertAll(
                () -> assertTrue(verifyEqual(expectedEngine.internet.getVertices(), actualEngine.internet.getVertices()) && expectedEngine.internet.getVertices().size() == actualEngine.internet.getVertices().size())
        );

    }

    @Test
    void crawlAndIndexInEdgesCorrect() {
        assertAll(
                () -> assertTrue(verifyEqual(expectedEngine.internet.getEdgesInto("A"), actualEngine.internet.getEdgesInto("A")) &&
                        verifyEqual(expectedEngine.internet.getEdgesInto("B"), actualEngine.internet.getEdgesInto("B")) &&
                        verifyEqual(expectedEngine.internet.getEdgesInto("C"), actualEngine.internet.getEdgesInto("C")) &&
                        verifyEqual(expectedEngine.internet.getEdgesInto("D"), actualEngine.internet.getEdgesInto("D")))
        );

    }

    @Test
    void crawlAndIndexOutEdgesCorrect() {
        assertAll(
                () -> assertTrue(verifyEqual(expectedEngine.internet.getNeighbors("A"), actualEngine.internet.getNeighbors("A")) &&
                        verifyEqual(expectedEngine.internet.getNeighbors("B"), actualEngine.internet.getNeighbors("B")) &&
                        verifyEqual(expectedEngine.internet.getNeighbors("C"), actualEngine.internet.getNeighbors("C")) &&
                        verifyEqual(expectedEngine.internet.getNeighbors("D"), actualEngine.internet.getNeighbors("D")))
        );

    }

    public boolean verifyEqual(ArrayList<String> list1, ArrayList<String> list2) {
        int c = 0;
        for (String s : list1) {
            if (!s.equals(list2.get(c))) return false;
            c++;
        }
        return true;
    }


    @Test
    void assignPageRanks() {
        actualEngine.assignPageRanks(0.01);
        assertAll(
                () -> assertTrue(roundAvoid(actualEngine.internet.getPageRank("A"), 3) == roundAvoid(expectedEngine.internet.getPageRank("A"), 3) &&
                        roundAvoid(actualEngine.internet.getPageRank("B"), 3) == roundAvoid(expectedEngine.internet.getPageRank("B"),3) &&
                        roundAvoid(actualEngine.internet.getPageRank("C"), 3) == roundAvoid(expectedEngine.internet.getPageRank("C"),3) &&
                        roundAvoid(actualEngine.internet.getPageRank("D"), 3) == roundAvoid(expectedEngine.internet.getPageRank("D"),3))
        );
    }

    @Test
    void ComputeRanks() {

    }

    public static double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    @Test
    void computeRanksFiveTimes() {
        actualEngine.computeRanks(actualEngine.internet.getVertices());
        actualEngine.computeRanks(actualEngine.internet.getVertices());
        actualEngine.computeRanks(actualEngine.internet.getVertices());
        actualEngine.computeRanks(actualEngine.internet.getVertices());
        actualEngine.computeRanks(actualEngine.internet.getVertices());
        assertAll(
                () -> assertTrue(roundAvoid(actualEngine.internet.getPageRank("A"), 3) == roundAvoid(expectedEngine.internet.getPageRank("A"), 3) &&
                        roundAvoid(actualEngine.internet.getPageRank("B"), 3) == roundAvoid(expectedEngine.internet.getPageRank("B"),3) &&
                        roundAvoid(actualEngine.internet.getPageRank("C"), 3) == roundAvoid(expectedEngine.internet.getPageRank("C"),3) &&
                        roundAvoid(actualEngine.internet.getPageRank("D"), 3) == roundAvoid(expectedEngine.internet.getPageRank("D"),3))
        );
    }

    @Test
    void computeRanksOnceTimes() {
        actualEngine.computeRanks(actualEngine.internet.getVertices());
        assertAll(
                () -> assertTrue(roundAvoid(actualEngine.internet.getPageRank("A"), 3) == roundAvoid(1.1666666666666665, 3) &&
                        roundAvoid(actualEngine.internet.getPageRank("B"), 3) == roundAvoid(0.75,3) &&
                        roundAvoid(actualEngine.internet.getPageRank("C"), 3) == roundAvoid(1.4166666666666665,3) &&
                        roundAvoid(actualEngine.internet.getPageRank("D"), 3) == roundAvoid(0.6666666666666666,3))
        );
    }

    @Test
    void getResults() {
       actualEngine.assignPageRanks(0.01);
        assertAll(
                () -> assertEquals(new ArrayList<String>(Arrays.asList("C", "B", "D")), actualEngine.getResults("3740770036"))
        );
    }
}