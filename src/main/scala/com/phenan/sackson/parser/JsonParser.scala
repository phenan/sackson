package com.phenan.sackson.parser

import com.fasterxml.jackson.core.{JsonParser => JacksonParser}
import com.phenan.sackson.path.JsonPath

trait JsonParser [T] { self =>
  def run (parser: JacksonParser, path: JsonPath, options: JsonParser.Options): Either[JsonParseError, T]

  def const (value: T): JsonParser[Unit] = (parser: JacksonParser, path: JsonPath, options: JsonParser.Options) => {
    self.run(parser, path, options).flatMap { result =>
      if (result == value) Right(())
      else Left(JsonParseError(s"constant $value", parser.currentToken(), path))
    }
  }

  def map [U] (f: T => U): JsonParser[U] = (parser: JacksonParser, path: JsonPath, options: JsonParser.Options) => {
    self.run(parser, path, options).map(f)
  }
}

object JsonParser {
  case class Options ()
}
