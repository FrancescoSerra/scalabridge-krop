package scalabridge.routes.get

import krop.route.*
import scalabridge.model.types.*

import scala.collection.mutable

object todos:
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

end todos
