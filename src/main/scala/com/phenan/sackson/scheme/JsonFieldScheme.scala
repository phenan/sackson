package com.phenan.sackson.scheme

import com.phenan.sackson.parser.JsonFieldParser
import com.phenan.sackson.printer.JsonFieldPrinter

sealed trait JsonFieldScheme[T] {
  def parser: JsonFieldParser[T]
  def printer: JsonFieldPrinter[T]
}

object JsonFieldScheme {
  case class Required [T] (fieldName: String, valueScheme: JsonScheme[T]) extends JsonFieldScheme[T] {
    override def parser: JsonFieldParser[T] = JsonFieldParser.Required(fieldName, valueScheme.parser)
    override def printer: JsonFieldPrinter[T] = JsonFieldPrinter.Required(fieldName, valueScheme.printer)
  }
  
  case class Optional [T] (fieldName: String, valueScheme: JsonScheme[T]) extends JsonFieldScheme[Option[T]] {
    override def parser: JsonFieldParser[Option[T]] = JsonFieldParser.Optional(fieldName, valueScheme.parser)
    override def printer: JsonFieldPrinter[Option[T]] = JsonFieldPrinter.Optional(fieldName, valueScheme.printer)
  }
}
