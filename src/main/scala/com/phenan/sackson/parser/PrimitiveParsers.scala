package com.phenan.sackson.parser

import com.fasterxml.jackson.core.{JsonToken, JsonParser => JacksonParser}
import com.phenan.sackson.path.JsonPath

object PrimitiveParsers {
  object BooleanParser extends JsonParser[Boolean] {
    override private[parser] def run(parser: JacksonParser, path: JsonPath, options: JsonParser.Options): Either[JsonParseError, Boolean] = {
      val currentToken = parser.currentToken()
      if (currentToken == JsonToken.VALUE_NUMBER_INT) {
        val int = parser.getIntValue
        parser.nextToken()
        Right(int != 0)
      } else if (currentToken == JsonToken.VALUE_TRUE) {
        parser.nextToken()
        Right(true)
      } else if (currentToken == JsonToken.VALUE_FALSE) {
        parser.nextToken()
        Right(false)
      } else {
        Left(JsonParseError("boolean", currentToken, path))
      }
    }
  }

  object IntParser extends JsonParser[Int] {
    override private[parser] def run(parser: JacksonParser, path: JsonPath, options: JsonParser.Options): Either[JsonParseError, Int] = {
      val currentToken = parser.currentToken()
      if (currentToken == JsonToken.VALUE_NUMBER_INT) {
        val int = parser.getIntValue
        parser.nextToken()
        Right(int)
      } else if (currentToken == JsonToken.VALUE_STRING) {
        val string = parser.getText.nn
        parser.nextToken()
        string.toIntOption.toRight(JsonParseError("int", currentToken, path))
      } else {
        Left(JsonParseError("int", currentToken, path))
      }
    }
  }
}
