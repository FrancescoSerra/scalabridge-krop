package scalabridge.routes

import krop.all._
import krop.route._
import scalabridge.model.types.ToDo

import scala.collection.mutable

object Routes {
  val home =
    Route(
      Request.get(Path.root),
      Response.ok(Entity.html)
    )

  // This route serves static assets, such as Javascript or stylesheets, from
  // your resource directory.
  val assets =
    Route(
      Request.get(Path / "assets" / Param.separatedString("/")),
      Response.staticResource("scalabridge/assets/")
    )

  val getTodoRoute = Route(
    Request.get(Path / "todos" / Param.int),
    Response.ok(Entity.jsonOf[Option[ToDo]])
  )
  val postTodoRoute = Route(
    Request.post(Path / "todos").withEntity(Entity.jsonOf[ToDo]),
    Response.ok(Entity.unit)
  )
  val getAllTodosRoute = Route(
    Request.get(Path / "todos"),
    Response.ok(Entity.jsonOf[mutable.Map[Int, ToDo]])
  )
  val deleteTodoRoute = Route(
    Request.delete(Path / "todos" / Param.int),
    Response.ok(Entity.unit)
  )
}
