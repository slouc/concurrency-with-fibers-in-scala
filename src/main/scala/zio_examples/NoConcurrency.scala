package zio_examples

import zio.console.Console
import zio.{ExitCode, URIO, ZEnv}

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object NoConcurrency extends zio.App with PrintThread with Runtime {

  def ec: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

  def countdown(id: String)(i: Int): URIO[Console, Unit] =
    for {
      _ <- printThread(id)
      _ <- if (i == 1) URIO.unit else countdown(id)(i - 1)
    } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    for {
      _ <- countdown("A")(5)
      _ <- countdown("B")(5)
    } yield ExitCode.success


}
