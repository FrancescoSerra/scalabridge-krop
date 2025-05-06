package scalabridge

import krop.all.{_, given}
import scalabridge.model.types._
import scalabridge.routes.Routes
import scalabridge.routes.Routes._
import scalabridge.views.html

import java.time.ZonedDateTime
import scala.collection.mutable

object Main {
  val name = "scalabridge"
  val todos = mutable.Map[Int, ToDo]()

  val homeRoute =
    home.handle(() => html.base(name, html.home(name)).toString)

  val getTodo = getTodoRoute.handle(id => todos.get(id))
  val postTodo = postTodoRoute.handle { todo =>
    val keys = todos.keys.toList
    val max = if (keys.isEmpty) 1 else keys.max + 1
    todos.update(
      max,
      todo.copy(
        timestamp = todo.timestamp.orElse(
          Some(
            Timestamp(
              ZonedDateTime.now()
            )
          )
        )
      )
    )
  }
  val getTodos = getAllTodosRoute.handle(() => todos)
  val deleteTodo = deleteTodoRoute.handle(id => todos.remove(id))

  val assets =
    Routes.assets.passthrough

  val application: Application =
    homeRoute
      .orElse(getTodo)
      .orElse(postTodo)
      .orElse(getTodos)
      .orElse(deleteTodo)
      .orElse(assets)
      .orElse(Application.notFound)

  @main def run(): Unit =
    ServerBuilder.default
      .withApplication(application)
      .run()
}
