package ce_examples

import java.util.concurrent.Executors

import cats.effect.{IO, IOApp}

import scala.concurrent.ExecutionContext

object ThreeThreadsTwoFibers extends IOApp.Simple with RuntimeUtils {

  def ec: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(3))

  def countdown(id: String)(i: Int): IO[Unit] =
    for {
      _ <- printThread(id)
      _ <- if (i == 1) IO.unit else countdown(id)(i - 1)
    } yield ()

  override def run: IO[Unit] =
    for {
      fiber1 <- countdown("A")(5).start
      fiber2 <- countdown("B")(5).start
      _ <- printThread("main")
      _ <- fiber1.join
      _ <- fiber2.join
    } yield ()

  //  Expected output:
  //
  //  [pool-1-thread-2] A
  //  [pool-1-thread-3] B
  //  [pool-1-thread-1] main
  //  [pool-1-thread-2] A
  //  [pool-1-thread-2] A
  //  [pool-1-thread-2] A
  //  [pool-1-thread-2] A
  //  [pool-1-thread-3] B
  //  [pool-1-thread-3] B
  //  [pool-1-thread-3] B
  //  [pool-1-thread-3] B

}