package ce_examples

import cats.effect.{IO, IOApp}

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object TwoThreadsFourFibers extends IOApp.Simple with PrintThread with Runtime {

  def ec: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(2))

  def countdown(id: String)(i: Int): IO[Unit] =
    for {
      _ <- IO(Thread.sleep(200)) // --> throttle a bit
      _ <- printThread(id)
      _ <- if (id == "A" && i == 2) IO(println("A CEDES")) >> IO.cede
      else IO.unit
      _ <- if (i == 1) IO(println(s"$id DONE"))
      else countdown(id)(i - 1)
    } yield ()

  override def run: IO[Unit] =
    for {
      fiber1 <- countdown("A")(4).start
      fiber2 <- countdown("B")(4).start
      fiber3 <- countdown("C")(4).start
      fiber4 <- countdown("D")(4).start
      _ <- fiber1.join
      _ <- fiber2.join
      _ <- fiber3.join
      _ <- fiber4.join
    } yield ()

}
