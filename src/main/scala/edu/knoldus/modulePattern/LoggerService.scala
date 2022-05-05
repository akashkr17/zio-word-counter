package edu.knoldus.modulePattern

import java.io.{File, IOException}

import zio._
import zio.console._

import scala.io.{BufferedSource, Source}

object LoggerService {
  type Logger = Has[Logger.Service]

  object Logger {
    trait Service {
      def log(line: String): UIO[Unit]
    }

    val any: ZLayer[Logger, Nothing, Logger] =
      ZLayer.requires[Logger]

    val live: Layer[Nothing, Has[Service]] = ZLayer.succeed {
      (line: String) => {
        putStrLn(line).provideLayer(Console.live).orDie
      }
    }
  }
  def log(line: => String): ZIO[Logger, Throwable, Unit] =
    ZIO.accessM(_.get.log(line))
}
