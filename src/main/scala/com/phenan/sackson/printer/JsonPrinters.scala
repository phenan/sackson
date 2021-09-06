package com.phenan.sackson.printer

import com.fasterxml.jackson.core.JsonGenerator
import com.phenan.sackson.path.JsonPath

object JsonPrinters {
  val boolean: JsonPrinter[Boolean] = PrimitivePrinters.BooleanPrinter
  val int: JsonPrinter[Int] = PrimitivePrinters.IntPrinter
  //val double: JsonPrinter[Double] = ???
  val string: JsonPrinter[String] = PrimitivePrinters.StringPrinter

  def array[T](scheme: JsonPrinter[T]): JsonPrinter[Seq[T]] = ???

  def required [T] (fieldName: String, printer: JsonPrinter[T]): JsonFieldPrinter[T] = JsonFieldPrinter.Required(fieldName, printer)
  def optional [T] (fieldName: String, printer: JsonPrinter[T]): JsonFieldPrinter[Option[T]] = JsonFieldPrinter.Optional(fieldName, printer)

  def struct [T <: Tuple] (printers: FieldPrinters[T]): JsonPrinter[T] = new JsonObjectPrinter[T](printers.fields)

  class FieldPrinters [T <: Tuple] (val fields: Tuple.Map[T, JsonFieldPrinter]) extends AnyVal {
    def *: [U] (printer: JsonFieldPrinter[U]): FieldPrinters[U *: T] = new FieldPrinters[U *: T](printer *: fields)
  }
  
  def nil: FieldPrinters[EmptyTuple] = new FieldPrinters(EmptyTuple)
}
