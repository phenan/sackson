package com.phenan.sackson.parser

import com.fasterxml.jackson.core.{JsonParseException, JsonToken, JsonParser as JacksonParser}
import com.phenan.sackson.path.JsonPath
import com.phenan.sackson.util.TupleUtil

import scala.annotation.tailrec

class JsonObjectParser [T <: Tuple] (fields: Tuple.Map[T, JsonFieldParser]) extends JsonParser[T] {
  private val fieldParserList: List[JsonFieldParser[Any]] = fields.toList.asInstanceOf[List[JsonFieldParser[Any]]]
  private val fieldMap: Map[String, (JsonFieldParser[Any], Int)] = {
    fieldParserList.zipWithIndex.map { case (parser, index) => parser.fieldName -> (parser, index) }.toMap
  }

  override def run(parser: JacksonParser, path: JsonPath, options: JsonParser.Options): Either[JsonParseError, T] = {
    val currentToken = parser.currentToken()
    if (currentToken != JsonToken.START_OBJECT) {
      Left(JsonParseError("Object", currentToken, path))
    } else {
      parser.nextToken()
      readRep(prepareFieldValueArray, parser, path, options)
    }
  }

  private def prepareFieldValueArray = {
    fieldParserList.view.map {
      case JsonFieldParser.Optional(_, _) => None
      case JsonFieldParser.Required(_, _) => null
    }.toArray[Any | Null]
  }

  private def buildObjectFromArray(array: Array[Any | Null], token: JsonToken | Null, path: JsonPath): Either[JsonParseError, T] = {
    if (array.contains(null)) {
      val fieldNames = array.zip(fieldParserList).collect { case (null, parser) => parser.fieldName }
      Left(JsonParseError(s"field name (missing: ${fieldNames.mkString(", ")})", token, path))
    } else {
      Right(Tuple.fromArray(array).asInstanceOf[T])
    }
  }

  @tailrec
  private def readRep(fieldValueArray: Array[Any | Null], parser: JacksonParser, path: JsonPath, options: JsonParser.Options): Either[JsonParseError, T] = {
    val currentToken = parser.currentToken()
    if (currentToken == JsonToken.END_OBJECT) {
      parser.nextToken()
      buildObjectFromArray(fieldValueArray, currentToken, path)
    } else if (parser.currentToken() == JsonToken.FIELD_NAME) {
      val fieldName = parser.currentName().nn
      parser.nextToken()
      fieldMap.get(fieldName) match {
        case Some((fieldParser, index)) =>
          fieldParser.parser.run(parser, path.field(fieldName), options) match {
            case Left(error) =>
              Left(error)
            case Right(value) =>
              fieldValueArray(index) = value
              readRep(fieldValueArray, parser, path, options)
          }
        case None =>
          skipFieldValue(parser, path.field(fieldName), options) match {
            case Left(error) =>
              Left(error)
            case Right(_) =>
              readRep(fieldValueArray, parser, path, options)
          }
      }
    } else {
      Left(JsonParseError(s"field name or end of object", currentToken, path))
    }
  }

  private def skipFieldValue(parser: JacksonParser, path: JsonPath, options: JsonParser.Options) = {
    parser.currentToken() match {
      case JsonToken.START_ARRAY =>
        skipChildren("array", parser, path, options)
      case JsonToken.START_OBJECT =>
        skipChildren("object", parser, path, options)
      case JsonToken.VALUE_STRING | JsonToken.VALUE_NUMBER_INT | JsonToken.VALUE_NUMBER_FLOAT | JsonToken.VALUE_TRUE | JsonToken.VALUE_FALSE | JsonToken.VALUE_NULL =>
        parser.nextToken()
        Right(())
      case unexpected =>
        Left(JsonParseError("array, object, string, int, float, boolean, or null", unexpected, path))
    }
  }

  private def skipChildren(kind: String, parser: JacksonParser, path: JsonPath, options: JsonParser.Options) = {
    val currentToken = parser.currentToken()
    try {
      parser.skipChildren()
      parser.nextToken()
      Right(())
    } catch {
      case e: JsonParseException =>
        Left(JsonParseError(kind, currentToken, path))
    }
  }
}
