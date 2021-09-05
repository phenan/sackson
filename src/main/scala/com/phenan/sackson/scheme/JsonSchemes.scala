package com.phenan.sackson.scheme

object JsonSchemes {
  val boolean: JsonScheme[Boolean] = PrimitiveSchemes.BooleanScheme
  //val int: JsonScheme[Int] = ???
  //val double: JsonScheme[Double] = ???
  //val string: JsonScheme[String] = ???

  def array[T](scheme: JsonScheme[T]): JsonScheme[Seq[T]] = ???

  def required [T] (fieldName: String, printer: JsonScheme[T]): JsonFieldScheme[T] = JsonFieldScheme.Required(fieldName, printer)
  def optional [T] (fieldName: String, printer: JsonScheme[T]): JsonFieldScheme[Option[T]] = JsonFieldScheme.Optional(fieldName, printer)

  def struct [T <: Tuple] (printers: FieldSchemes[T]): JsonScheme[T] = new JsonObjectScheme[T](printers.fields)

  class FieldSchemes [T <: Tuple] (val fields: Tuple.Map[T, JsonFieldScheme]) extends AnyVal {
    def *: [U] (printer: JsonFieldScheme[U]): FieldSchemes[U *: T] = new FieldSchemes[U *: T](printer *: fields)
  }

  def nil: FieldSchemes[EmptyTuple] = new FieldSchemes(EmptyTuple)
}
