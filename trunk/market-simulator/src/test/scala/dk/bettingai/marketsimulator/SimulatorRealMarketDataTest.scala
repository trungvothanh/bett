package dk.bettingai.marketsimulator

import org.junit._
import Assert._
import scala.io._
import java.io._
import dk.bettingai.marketsimulator.betex.api._
import dk.bettingai.marketsimulator.betex._
import dk.bettingai.marketsimulator.marketevent._
import dk.bettingai.marketsimulator.trader._
import dk.bettingai.marketsimulator.betex.Market._
import java.util.Date
import scala.collection._
import immutable.TreeMap
import ISimulator._
import SimulatorTest._

class SimulatorRealMarketDataTest {

  private val betex = new Betex()
  private val traders = new SimpleTrader() :: Nil
  private val simulator = new Simulator(betex, 0)

  /**
   * Test scenarios for runSimulation - analysing single trader only.
   * */
  @Test
  def testOneTraderRealData {
    val marketEventsFile10 = new File("src/test/resources/marketRealDataTwoMarkets/101655610.csv")
    val marketEventsFile20 = new File("src/test/resources/marketRealDataTwoMarkets/101655622.csv")

    /**Run market simulation.*/
    val marketReports = simulator.runSimulation(TreeMap(101655610l -> marketEventsFile10, 101655622l -> marketEventsFile20), traders, (progress: Int) => {}).marketReports

    assertEquals(2, marketReports.size)

    /**Check report for the first market.*/
    assertEquals(1, marketReports(0).traderReports.size)
    assertMarketReport(101655610, "1m Hcap", "/GB/Muss 22nd Aug", marketReports(0))
    assertEquals(-463.572, marketReports(0).traderReports(0).marketExpectedProfit.marketExpectedProfit, 0.001)
    assertEquals(12393, marketReports(0).traderReports(0).matchedBetsNumber)
    assertEquals(863, marketReports(0).traderReports(0).unmatchedBetsNumber)

    /**Check report for the second market.*/
    assertEquals(1, marketReports(1).traderReports.size)
    assertMarketReport(101655622, "1m Hcap", "/GB/Muss 22nd Aug - 2", marketReports(1))
    assertEquals(-194.699, marketReports(1).traderReports(0).marketExpectedProfit.marketExpectedProfit, 0.001)
    assertEquals(11256, marketReports(1).traderReports(0).matchedBetsNumber)
    assertEquals(672, marketReports(1).traderReports(0).unmatchedBetsNumber)

    assertEquals(2, traders.head.initCalledTimes.get)
    assertEquals(2, traders.head.afterCalledTimes.get)
  }

  /**
   * Test scenarios for runSimulation - analysing multiple traders.
   * */
  @Test
  def testTwoTradersRealData {
    val marketEventsFile10 = new File("src/test/resources/marketRealDataTwoMarkets/101655610.csv")
    val marketEventsFile20 = new File("src/test/resources/marketRealDataTwoMarkets/101655622.csv")

    val twoTraders = new SimpleTrader() :: new SimpleTrader() :: Nil
    /**Run market simulation.*/
    val marketReports = simulator.runSimulation(TreeMap(101655610l -> marketEventsFile10, 101655622l -> marketEventsFile20), twoTraders, (progress: Int) => {}).marketReports

    assertEquals(2, marketReports.size)

    /**Check report for the first market.*/
    val marketReport1 = marketReports(0)
    assertEquals(2, marketReport1.traderReports.size)
    assertMarketReport(101655610, "1m Hcap", "/GB/Muss 22nd Aug", marketReport1)
    assertTraderReport(-386.316, 12437, 719, marketReport1.traderReports(0))
    assertEquals(RegisteredTrader(3, twoTraders(0)), marketReport1.traderReports(0).trader)
    assertTraderReport(-402.047, 12379, 752, marketReport1.traderReports(1))
    assertEquals(RegisteredTrader(4, twoTraders(1)), marketReport1.traderReports(1).trader)
    assertEquals(2, twoTraders(0).initCalledTimes.get)
    assertEquals(2, twoTraders(0).afterCalledTimes.get)

    /**Check report for the second market.*/
    val marketReport2 = marketReports(1)
    assertEquals(2, marketReport2.traderReports.size)
    assertMarketReport(101655622, "1m Hcap", "/GB/Muss 22nd Aug - 2", marketReport2)
    assertTraderReport(-172.720, 11238, 595, marketReport2.traderReports(0))
    assertEquals(RegisteredTrader(3, twoTraders(0)), marketReport2.traderReports(0).trader)
    assertTraderReport(-176.796, 11220, 610, marketReport2.traderReports(1))
    assertEquals(RegisteredTrader(4, twoTraders(1)), marketReport2.traderReports(1).trader)

    assertEquals(2, twoTraders(1).initCalledTimes.get)
    assertEquals(2, twoTraders(1).afterCalledTimes.get)
  }
}