package com.phenan.sackson.parser

import com.fasterxml.jackson.core.{JsonFactory, JsonParser as JacksonParser}
import com.phenan.sackson.path.JsonPath

trait JsonParser [T] { self =>
  private[parser] def run (parser: JacksonParser, path: JsonPath, options: JsonParser.Options): Either[JsonParseError, T]

  private[parser] def run (parser: JacksonParser, options: JsonParser.Options): Either[JsonParseError, T] = {
    run(parser, JsonPath.Root, options)
  }

  def parseString (string: String, options: JsonParser.Options = JsonParser.Options(), factory: JsonParser.JacksonParserFactory = JsonParser.defaultFactory): Either[JsonParseError, T] = {
    run(factory.fromString(string), options)
  }

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

  class JacksonParserFactory (jsonFactory: JsonFactory) {
    def fromString(string: String): JacksonParser = {
      val parser = jsonFactory.createParser(string).nn
      parser.nextToken()
      parser
    }
  }

  lazy val defaultFactory: JacksonParserFactory = new JacksonParserFactory(new JsonFactory())
}
