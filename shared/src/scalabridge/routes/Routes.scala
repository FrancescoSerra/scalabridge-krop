package scalabridge.routes

import cats.syntax.all.*
import krop.all.*
import scalabridge.model.types.*
import org.http4s.Status.*

import scala.collection.mutable

object Routes {
  val home =
    new Route(
      Request.get(Path.root),
      Response.ok(Entity.html)
    )

  // This route serves static assets, such as Javascript or stylesheets, from
  // your resource directory.
  val assets =
    new Route(
      Request.get(Path / "assets" / Param.separatedString("/")),
      Response.staticResource("scalabridge/assets/")
    )

  val getTodoRoute = new Route(
    Request.get(Path / "todos" / Param.int),
    Response.ok(Entity.jsonOf[ToDo].map(_.some)).orNotFound
  )
  val postTodoRoute = new Route(
    Request.post(Path / "todos").withEntity(Entity.jsonOf[ToDo]),
    Response.ok(Entity.unit)
  )
  val getAllTodosRoute = new Route(
    Request.get(Path / "todos"),
    Response.ok(Entity.jsonOf[mutable.Map[Int, ToDo]])
  )
  val deleteTodoRoute = Route(
    Request.delete(Path / "todos" / Param.int),
    Response.ok(Entity.unit)
  )
  val updateTodoRoute = new Route(
    Request.patch(Path / "todos" / Param.int).withEntity(Entity.jsonOf[ToDo]),
    Response.ok(Entity.jsonOf[ToDo]).orNotFound
  )
  val updateOnlyIfSameAuthorRoute = new Route(
    Request.patch(Path / "todos" / Param.string / Param.int).withEntity(Entity.jsonOf[ToDo]),
    Response.ok(Entity.jsonOf[ToDo]).orElse(Response.status(BadRequest, Entity.text)).orNotFound
  )

  val getPersonForm = new Route(
    Request.get(Path / "person"),
    Response.ok(Entity.formOf[Person])
  )

  val createPersonForm = new Route(
    Request.post(Path / "person").withEntity(Entity.formOf[Person]),
    Response.ok(Entity.text)
  )
}
