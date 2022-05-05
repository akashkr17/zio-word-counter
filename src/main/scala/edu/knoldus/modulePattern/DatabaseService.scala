package edu.knoldus.modulePattern

import zio.{Has, Layer, Task, UIO, ZIO, ZLayer}
import zio.console.{Console, putStrLn}

case class User(id: String,name: String)

object DatabaseService {
  type Database = Has[Database.Service]

  object Database {
    trait Service {
      def getUser(id: String): Task[User]
    }

    val any: ZLayer[Database, Nothing, Database] =
      ZLayer.requires[Database]

    val live: Layer[Nothing, Has[Service]] = ZLayer.succeed {
      (id: String) => {
        Task(User(id,"Akash"))
      }
    }
  }
  def getUser(id: => String): ZIO[Database, Throwable, User] =
    ZIO.accessM(_.get.getUser(id))
}
