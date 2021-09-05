package com.phenan.sackson.scheme

import com.phenan.sackson.parser.{JsonFieldParser, JsonObjectParser, JsonParser}
import com.phenan.sackson.printer.{JsonFieldPrinter, JsonObjectPrinter, JsonPrinter}
import com.phenan.sackson.util.TupleUtil

class JsonObjectScheme [T <: Tuple] (fields: Tuple.Map[T, JsonFieldScheme]) extends JsonScheme[T] {
  override def parser: JsonParser[T] = {
    val fieldParsers = TupleUtil.tupleMapTransform[T, JsonFieldScheme, JsonFieldParser](fields) {
      [t] => (scheme: JsonFieldScheme[t]) => scheme.parser
    }
    new JsonObjectParser[T](fieldParsers)
  }

  override def printer: JsonPrinter[T] = {
    val fieldPrinters = TupleUtil.tupleMapTransform[T, JsonFieldScheme, JsonFieldPrinter](fields) {
      [t] => (scheme: JsonFieldScheme[t]) => scheme.printer
    }
    new JsonObjectPrinter[T](fieldPrinters)
  }

  override def schema: JsonPrinter[Unit] = {
    ???
  }
}
