package dk.bettingai.marketsimulator.betex

import org.junit._
import Assert._
import java.util.Date
import dk.bettingai.marketsimulator.betex.api._
import IBet.BetTypeEnum._
import IBet.BetStatusEnum._

class MarketTestGetBestPrices {
	
	/** 
	 *  Tests for getBestPrices.
	 * 
	 * */
	@Test(expected=classOf[IllegalArgumentException]) def testGetBestPricesForNotExistingRunner  {
		val market = new Market(1,"Match Odds","Man Utd vs Arsenal",1,new Date(2000),List(new Market.Runner(11,"Man Utd"),new Market.Runner(12,"Arsenal")))

		market.getBestPrices(13)
	}

	@Test def testGetBestPricesBothToBackAndToLayAreAvailable {
		val market = new Market(1,"Match Odds","Man Utd vs Arsenal",1,new Date(2000),List(new Market.Runner(11,"Man Utd"),new Market.Runner(12,"Arsenal")))

		market.placeBet(100,122,13,2.1,LAY,11)
		market.placeBet(101,121,3,2.2,LAY,11)
		market.placeBet(102,122,5,2.2,LAY,11)
		market.placeBet(103,121,8,2.4,BACK,11)
		market.placeBet(104,122,25,2.5,BACK,11)

		val bestPrices = market.getBestPrices(11)
		assertEquals(2.2,bestPrices._1,0)
		assertEquals(2.4,bestPrices._2,0)
	}

	@Test def testGetBestPricesBothToBackAndToLayAreAvailablePlusBetOnOtherRunner {
		val market = new Market(1,"Match Odds","Man Utd vs Arsenal",1,new Date(2000),List(new Market.Runner(11,"Man Utd"),new Market.Runner(12,"Arsenal")))

		market.placeBet(100,122,13,2.1,LAY,11)
		market.placeBet(101,121,3,2.2,LAY,11)
		market.placeBet(102,122,5,2.2,LAY,11)
		market.placeBet(103,121,8,2.4,BACK,11)
		market.placeBet(104,122,25,2.5,BACK,11)

		market.placeBet(105,122,13,2.3,LAY,12)
		market.placeBet(106,122,25,2.3,BACK,12)

		val bestPrices = market.getBestPrices(11)
		assertEquals(2.2,bestPrices._1,0)
		assertEquals(2.4,bestPrices._2,0)
	}

	@Test def testGetBestPricesBothToBackAndToLayAreAvailablePlusSettledBets {
		val market = new Market(1,"Match Odds","Man Utd vs Arsenal",1,new Date(2000),List(new Market.Runner(11,"Man Utd"),new Market.Runner(12,"Arsenal")))

		market.placeBet(100,122,13,2.1,LAY,11)
		market.placeBet(101,121,3,2.2,LAY,11)
		market.placeBet(102,122,5,2.2,LAY,11)
		market.placeBet(103,121,8,2.4,BACK,11)
		market.placeBet(104,122,25,2.5,BACK,11)

		/**Matching bets*/
		market.placeBet(105,121,8,2.4,LAY,11)
		market.placeBet(106,122,8,2.2,BACK,11)

		val bestPrices = market.getBestPrices(11)
		assertEquals(2.1,bestPrices._1,0)
		assertEquals(2.5,bestPrices._2,0)
	}

	@Test def testGetBestPricesToBackPriceIsNotAvailable {
		val market = new Market(1,"Match Odds","Man Utd vs Arsenal",1,new Date(2000),List(new Market.Runner(11,"Man Utd"),new Market.Runner(12,"Arsenal")))

		market.placeBet(103,121,8,2.4,BACK,11)
		market.placeBet(104,122,25,2.5,BACK,11)

		val bestPrices = market.getBestPrices(11)
		assertEquals(Double.NaN,bestPrices._1,0)
		assertEquals(2.4,bestPrices._2,0)
	}

	@Test def testGetBestPricesToLayPriceIsNotAvailable {
		val market = new Market(1,"Match Odds","Man Utd vs Arsenal",1,new Date(2000),List(new Market.Runner(11,"Man Utd"),new Market.Runner(12,"Arsenal")))

		market.placeBet(100,122,13,2.1,LAY,11)
		market.placeBet(101,121,3,2.2,LAY,11)
		market.placeBet(102,122,5,2.2,LAY,11)

		val bestPrices = market.getBestPrices(11)
		assertEquals(2.2,bestPrices._1,0)
		assertEquals(Double.NaN,bestPrices._2,0)
	}

	@Test def testGetBestPricesNoBestPricesAvailable {
		val market = new Market(1,"Match Odds","Man Utd vs Arsenal",1,new Date(2000),List(new Market.Runner(11,"Man Utd"),new Market.Runner(12,"Arsenal")))

		val bestPrices = market.getBestPrices(11)
		assertEquals(Double.NaN,bestPrices._1,0)
		assertEquals(Double.NaN,bestPrices._2,0)
	}

}