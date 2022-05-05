package edu.knoldus.modulePattern

import edu.knoldus.modulePattern.DatabaseService.Database
import edu.knoldus.modulePattern.LoggerService.Logger
import zio.{Has, Layer, Task, UIO, ZIO, ZLayer}
import zio.console.{Console, putStrLn}


object UserRepo {
  type Users = Has[Users.Service]

  object Users {
    trait Service {
      def getUser(id: String): Task[Unit]
    }

    val any: ZLayer[Users, Nothing, Users] =
      ZLayer.requires[Users]

    val live: Any = ZLayer.fromServices[LoggerService.Logger.Service,DatabaseService.Database.Service] { (logger, database) => new Service {
      override def getUser(id: String): Task[Unit]= {
        for {
          user <- database.getUser(id)
          _ <- logger.log(s"Hello $user")
        } yield ()
      }
    }

    }
  }
  def getUser(line: => String): ZIO[Users, Throwable, Unit] =
    ZIO.accessM(_.get.getUser(line))

}
