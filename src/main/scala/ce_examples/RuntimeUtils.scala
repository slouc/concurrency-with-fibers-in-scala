package ce_examples

import cats.effect.unsafe.{IORuntime, IORuntimeConfig}
import cats.effect.{IO, IOApp}

import scala.concurrent.ExecutionContext

trait RuntimeUtils { self: IOApp =>

  def ec: ExecutionContext

  override implicit val runtime: IORuntime =
    IORuntime(
      ec,
      IORuntime.createDefaultBlockingExecutionContext()._1,
      IORuntime.createDefaultScheduler()._1,
      () => (),
      IORuntimeConfig()
    )

  def printThread(s: String): IO[Unit] =
    IO(println(s"[${Thread.currentThread.getName}] $s")) // not IO.println to avoid ceding
}