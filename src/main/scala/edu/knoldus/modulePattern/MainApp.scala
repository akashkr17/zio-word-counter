package edu.knoldus.modulePattern
import java.util.UUID

import edu.knoldus.modulePattern.DatabaseService._
import zio.{ExitCode, URIO}

object MainApp extends zio.App {
  val program = for {
    user <- getUser(UUID.randomUUID().toString)
    _ <- LoggerService.log(s"Hello $user")
  } yield ()
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.provideLayer(LoggerService.Logger.live ++ DatabaseService.Database.live).exitCode
}
