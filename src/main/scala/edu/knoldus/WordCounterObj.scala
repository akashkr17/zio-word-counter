package edu.knoldus.WordCounterObj

import java.io.File

import edu.knoldus.FileReaderObj.fileReader._
import zio.{Has, ZIO, ZLayer}

object WordCounterObj {
  type WordCounter = Has[WordCounter.Service]

  object WordCounter {
    trait Service {

      def counter(str: List[String]): ZIO[Any, Throwable, Map[String, Int]]
    }

    val any: ZLayer[WordCounter, Nothing, WordCounter] =
      ZLayer.requires[WordCounter]

    val live: ZLayer[FileReader, Nothing, WordCounter] = ZLayer.fromService {
      (moduleA: FileReader.Service) =>
        new Service {

          override def counter(
              str: List[String]): ZIO[Any, Throwable, Map[String, Int]] = {

            //  str.

            for {
              res: Seq[String] <- ZIO.foreach(str)(data =>
                moduleA.read(new File(data)))
              res3: Map[String, Int] <- ZIO {
                res
                  .to(LazyList)
                  .flatMap(_.split("\\W+"))
                  .groupMapReduce(identity)(_ => 1)(_ + _)
              }
            } yield res3
            //          map {
            //            data: String =>  data.split(" ")
            //              .toList
            //              .flatMap(_.split("\\W+"))
            //              .groupMapReduce(identity)(_ => 1)(_ + _)
            //            //                  .groupBy(x => x)
            //            //                  .view.mapValues(_.size)
            //          }

          }
        }
    }
  }

  def counter(
      str: List[String]): ZIO[WordCounter, Throwable, Map[String, Int]] =
    ZIO.accessM(_.get.counter(str))
}
