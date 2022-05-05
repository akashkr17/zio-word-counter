package edu.knoldus

import edu.knoldus.FileReaderObj._
import edu.knoldus.WordCounterObj._
import zio.clock._
import zio.console.Console
import zio.{ExitCode, URIO, ZIO}

object ZLayerApp0 extends zio.App {
  val env = Console.live ++ Clock.live ++ (FileReader.live >>> WordCounter.live)

  def printThread = s"[${Thread.currentThread().getName}]"
  val filePaths = List("file.txt", "file2.txt", "file3.txt", "file4.txt")
  val fileReaderOne: ZIO[WordCounter, Nothing, Unit] = for {
    data <- counter(filePaths).debug(printThread).fork
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    fileReaderOne.provideSomeLayer(env).exitCode

}

// output:
// [info] running ZLayersApp
// Welcome to ZIO!
// done: v = 10
