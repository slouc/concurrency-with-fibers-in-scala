package examples

import java.util.concurrent.Executors

import cats.effect.{IO, IOApp}

import scala.concurrent.ExecutionContext

object NoConcurrency extends IOApp.Simple with PrintThread with Runtime {

  def ec: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

  def countdown(id: String)(i: Int): IO[Unit] =
    for {
      _ <- printThread(id)
      _ <- if (i == 1) IO.unit else countdown(id)(i - 1)
    } yield ()

  override def run(): IO[Unit] =
    for {
      _ <- countdown("A")(5)
      _ <- countdown("B")(5)
    } yield ()

}
