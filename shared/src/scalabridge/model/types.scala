package scalabridge.model

import cats.syntax.either._
import io.circe.Decoder.Result
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.circe.{_, given}

import java.time.ZonedDateTime

object types:
  final case class Title(value: String)
  object Title:
    given Encoder[Title] = Encoder[String].contramap(_.value)
    given Decoder[Title] = Decoder[String].emap(v => Title(v).asRight)

  final case class Description(value: String)
  object Description:
    given Encoder[Description] = Encoder[String].contramap(_.value)
    given Decoder[Description] =
      Decoder[String].emap(v => Description(v).asRight)

  final case class Author(value: String)
  object Author:
    given Encoder[Author] = Encoder[String].contramap(_.value)
    given Decoder[Author] = Decoder[String].emap(v => Author(v).asRight)

  final case class Timestamp(value: ZonedDateTime)
  object Timestamp:
    given Encoder[Timestamp] = Encoder[ZonedDateTime].contramap(_.value)
    given Decoder[Timestamp] =
      Decoder[ZonedDateTime].emap(v => Timestamp(v).asRight)


  final case class ToDo(
      title: Title,
      description: Description,
      author: Author,
      timestamp: Option[Timestamp]
  )
  object ToDo:
    given Codec[ToDo] = deriveCodec

end types
