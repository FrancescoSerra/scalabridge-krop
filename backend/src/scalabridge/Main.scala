package scalabridge

import cats.syntax.all.*
import krop.all.{_, given}
import scalabridge.model.types.*
import scalabridge.routes.Routes
import scalabridge.routes.Routes.*
import scalabridge.views.html

import java.time.ZonedDateTime
import scala.collection.mutable

object Main {
  val name = "scalabridge"
  private val todos = mutable.Map[Int, ToDo]()

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
  val updateTodo = updateTodoRoute.handle((id,body) =>
    todos.updateWith(id)(existing => existing.map(_.copy(
      title = body.title,
      description = body.description,
      author = body.author
    ))
  ))
  val updateTodoOnlySameAuthor = updateOnlyIfSameAuthorRoute.handle((author,id,body) =>
    todos.get(id).flatMap(todo =>
      if (todo.author.value == author)
        todos.updateWith(id)(existing => existing.map(_.copy(
          title = body.title,
          description = body.description,
          author = body.author
        ))).map(_.asRight[String])
      else "Boo!! Not same author!!".asLeft[ToDo].some
    )
  )
  val getPersonForms = getPersonForm.handle(() => Person("Francesco", 45))
  val createPersonForms = createPersonForm.handle(person => s"${person.name} ${person.age}")

  val assets =
    Routes.assets.passthrough

  val application: Application =
    homeRoute
      .orElse(getTodo)
      .orElse(postTodo)
      .orElse(getTodos)
      .orElse(deleteTodo)
      .orElse(updateTodo)
      .orElse(updateTodoOnlySameAuthor)
      .orElse(assets)
      .orElse(getPersonForms)
      .orElse(createPersonForms)
      .orElse(Application.notFound)

  @main def run(): Unit =
    ServerBuilder.default
      .withApplication(application)
      .run()
}
