package examples

import java.util.concurrent.Executors

import cats.effect.{IO, IOApp}

import scala.concurrent.ExecutionContext

object OneThreadTwoFibers extends IOApp.Simple with PrintThread with Runtime {

  def ec: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

  def countdown(id: String)(i: Int): IO[Unit] =
    for {
      _ <- printThread(id)
      _ <- if (i == 1) IO.unit else countdown(id)(i - 1)
    } yield ()

  override def run(): IO[Unit] =
    for {
      fiber1 <- countdown("A")(5).start
      fiber2 <- countdown("B")(5).start
      _ <- printThread("main")
      _ <- fiber1.join
      _ <- fiber2.join
    } yield ()

}
