package zio_examples

import zio.console.{Console, _}
import zio.{ExitCode, URIO, ZEnv, ZIO}

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object TwoThreadsFourFibers extends zio.App with RuntimeUtils {

  def ec: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))

  def countdown(id: String)(i: Int): URIO[Console, Unit] =
    for {
      _ <- ZIO.succeed(Thread.sleep(200)) // --> throttle a bit
      _ <- printThread(id)
      _ <- if (id == "A" && i == 2) putStrLn("A YIELDS") *> ZIO.yieldNow
      else URIO.unit
      _ <- if (i == 1) putStrLn(s"$id DONE")
      else countdown(id)(i - 1)
    } yield ()

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    for {
      fiber1 <- countdown("A")(4).fork
      fiber2 <- countdown("B")(4).fork
      fiber3 <- countdown("C")(4).fork
      fiber4 <- countdown("D")(4).fork
      _ <- fiber1.join
      _ <- fiber2.join
      _ <- fiber3.join
      _ <- fiber4.join
    } yield ExitCode.success

}

