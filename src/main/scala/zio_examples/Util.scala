package zio_examples

import zio.URIO
import zio.console._
import zio.internal.Platform

import scala.concurrent.ExecutionContext

trait Runtime {
  self: zio.App =>

  def ec: ExecutionContext

  override val platform = Platform.fromExecutionContext(ec)

}

trait PrintThread {
  def printThread(s: String): URIO[Console, Unit] =
    putStrLn(s"[${Thread.currentThread.getName}] $s")
}
