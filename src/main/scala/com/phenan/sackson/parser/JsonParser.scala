package com.phenan.sackson.parser

import com.fasterxml.jackson.core.{JsonFactory, JsonParser as JacksonParser}
import com.phenan.sackson.path.JsonPath

import scala.util.Using

trait JsonParser [T] { self =>
  private[parser] def run (parser: JacksonParser, path: JsonPath, options: JsonParser.Options): Either[JsonParseError, T]

  private[parser] def run (parser: JacksonParser, options: JsonParser.Options): Either[JsonParseError, T] = {
    run(parser, JsonPath.Root, options)
  }

  def parseString (string: String, options: JsonParser.Options = JsonParser.Options(), factory: JsonParser.JacksonParserFactory = JsonParser.defaultFactory): Either[JsonParseError, T] = {
    factory.withParserFromString(string) { parser =>
      run(parser, options)
    }
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
    def withParserFromString[T](string: String)(f: JacksonParser => T): T = {
      Using(jsonFactory.createParser(string).nn) { parser =>
        parser.nextToken()
        f(parser)
      }.get
    }
  }

  lazy val defaultFactory: JacksonParserFactory = new JacksonParserFactory(new JsonFactory())
}
