package dk.bettingai.marketsimulator

import ISimulator._
import scala.io._
import dk.bettingai.marketsimulator.trader._
import dk.bettingai.marketsimulator.risk._
import java.io.File
import dk.bettingai.marketsimulator.trader.ITrader._
import dk.bettingai.marketsimulator.betex.api._
import scala.collection._
import immutable.TreeMap

/**This trait represents a simulator that processes market events, analyses traders  and returns analysis reports.
 * 
 * @author korzekwad
 *
 */
object ISimulator {

  /**
   * @param userId The betting exchange user id for registered trader.
   * @param trader Registered trader.
   *
   */
  case class RegisteredTrader(userId: Int, val trader: ITrader) extends ITrader {

    override def init(ctx: ITraderContext) = trader.init(ctx)
    def execute(ctx: ITraderContext) = trader.execute(ctx)
    override def after(ctx: ITraderContext) = trader.after(ctx)
  }
}
trait ISimulator {

  /** Processes market events, analyses traders and returns analysis reports.
   * 
   * @param marketDataContains market events that the market simulation is executed for. Key - marketId, value - market events
   * @param traders Traders to analyse, all they are analysed on the same time, so they compete against each other
   * @param p Progress listener. Value between 0% and 100% is passed as an function argument.
   */
  def runSimulation(marketData: TreeMap[Long, File], traders: List[ITrader], p: (Int) => Unit): SimulationReport

  /**Registers new trader and return trader context. 
   * This context can be used to trigger some custom traders that are registered manually by a master trader, 
   * e.g. when testing some evolution algorithms for which more than one trader is required.
   * @return trader context
   */
  def registerTrader(market: IMarket): ITraderContext
}