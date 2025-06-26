package scalabridge.model

import cats.syntax.either.*
import io.circe.Decoder.Result
import io.circe.Encoder
import io.circe.Decoder
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import io.circe.{*, given}
import krop.route.{DecodeFailure, StringCodec}

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
    
    
  final case class Person(
      name: String,
      age: Int
  )
  
  
  sealed trait Answer
  object Answer {
    given Encoder[Answer] = Encoder.encodeBoolean.contramap {
      case Yes => true
      case No => false
    }
    
    given Decoder[Answer] = Decoder.decodeBoolean.map {
      case true => Yes
      case false => No
    }
    
    val stringCodec: StringCodec[Answer] = new StringCodec[Answer] {
      val name: String = "<Answer>"
      
      def decode(value: String): Either[DecodeFailure, Answer] =
        value.toBooleanOption.map {
          case true => Yes
          case false => No
        }.toRight(DecodeFailure(value, name))
        
      def encode(value: Answer): String = value match {
        case Yes => true.toString
        case No => false.toString
      }
    }
  }
  case object Yes extends Answer
  case object No extends Answer
  
  

end types
