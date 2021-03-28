package zio_examples

import zio.URIO
import zio.console._
import zio.internal.Platform

import scala.concurrent.ExecutionContext

trait RuntimeUtils {

  self: zio.App =>

  def ec: ExecutionContext

  override val platform = Platform.fromExecutionContext(ec)

  def printThread(s: String): URIO[Console, Unit] =
    putStrLn(s"[${Thread.currentThread.getName}] $s")
}