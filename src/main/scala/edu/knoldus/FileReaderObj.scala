package edu.knoldus
import java.io.File

import zio.{Has, Layer, URIO, ZIO, ZLayer}

import scala.io.{BufferedSource, Source}

object FileReaderObj {
  type FileReader = Has[FileReader.Service]

  object FileReader {
    trait Service {
      def read(file: File): ZIO[Any, Throwable, String]
    }

    val any: ZLayer[FileReader, Nothing, FileReader] =
      ZLayer.requires[FileReader]

    val live: Layer[Nothing, Has[Service]] = ZLayer.succeed {
      new Service {
        override def read(file: File): ZIO[Any, Throwable, String] = {
          def acquire(file: File) = ZIO(Source.fromFile(file))
          def release(reader: BufferedSource) = URIO(reader.close())
          acquire(file).bracket(release) { reader =>
            ZIO(reader.getLines().mkString)
          }
        }
      }
    }
  }
  def read(file: => File): ZIO[FileReader, Throwable, String] =
    ZIO.accessM(_.get.read(file))
}
