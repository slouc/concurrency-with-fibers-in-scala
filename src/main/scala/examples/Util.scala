package examples

import cats.effect.{IO, IOApp}
import cats.effect.unsafe.{IORuntime, IORuntimeConfig}

import scala.concurrent.ExecutionContext

trait Runtime { self: IOApp =>

  def ec: ExecutionContext

  override implicit val runtime: IORuntime =
    IORuntime(
      ec,
      IORuntime.createDefaultBlockingExecutionContext()._1,
      IORuntime.createDefaultScheduler()._1,
      () => ()
    )
}

trait PrintThread {
  def printThread(s: String): IO[Unit] =
    IO(println(s"[${Thread.currentThread.getName}] $s"))
}
