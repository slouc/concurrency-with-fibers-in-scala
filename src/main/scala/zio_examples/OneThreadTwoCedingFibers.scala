package zio_examples

import zio.console.Console
import zio.{ExitCode, URIO, ZEnv, ZIO}

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object OneThreadTwoCedingFibers extends zio.App with RuntimeUtils {

  def ec: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

  def countdown(id: String)(i: Int): URIO[Console, Unit] =
    for {
      _ <- printThread(id)
      _ <- ZIO.yieldNow
      _ <- if (i == 1) URIO.unit else countdown(id)(i - 1)
    } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    for {
      fiber1 <- countdown("A")(5).fork
      fiber2 <- countdown("B")(5).fork
      _ <- printThread("main")
      _ <- fiber1.join
      _ <- fiber2.join
    } yield  ExitCode.success

}

