package com.phenan.sackson.parser

object JsonParsers {
  val boolean: JsonParser[Boolean] = PrimitiveParsers.BooleanParser
  val int: JsonParser[Int] = PrimitiveParsers.IntParser
  //val double: JsonParser[Double] = ???
  //val string: JsonParser[String] = ???

  def array[T](scheme: JsonParser[T]): JsonParser[Seq[T]] = ???

  def required [T] (fieldName: String, parser: JsonParser[T]): JsonFieldParser[T] = JsonFieldParser.Required(fieldName, parser)
  def optional [T] (fieldName: String, parser: JsonParser[T]): JsonFieldParser[Option[T]] = JsonFieldParser.Optional(fieldName, parser)

  def struct [T <: Tuple] (parsers: FieldParsers[T]): JsonParser[T] = new JsonObjectParser[T](parsers.fields)

  class FieldParsers [T <: Tuple] (val fields: Tuple.Map[T, JsonFieldParser]) extends AnyVal {
    def *: [U] (parser: JsonFieldParser[U]): FieldParsers[U *: T] = new FieldParsers[U *: T](parser *: fields)
  }

  def nil: FieldParsers[EmptyTuple] = new FieldParsers(EmptyTuple)
}
