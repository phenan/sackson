package com.phenan.sackson.printer

import com.fasterxml.jackson.core.JsonGenerator
import com.phenan.sackson.path.JsonPath

sealed trait JsonFieldPrinter[T] {
  def run (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options): T => Unit
}

object JsonFieldPrinter {
  case class Required [T] (fieldName: String, valuePrinter: JsonPrinter[T]) extends JsonFieldPrinter[T] {
    override def run (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options): T => Unit = { value =>
      generator.writeFieldName(fieldName)
      valuePrinter.run(generator, path, options)(value)
    }
  }
  case class Optional [T] (fieldName: String, valuePrinter: JsonPrinter[T]) extends JsonFieldPrinter[Option[T]] {
    override def run (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options): Option[T] => Unit = {
      _.foreach { value =>
        generator.writeFieldName(fieldName)
        valuePrinter.run(generator, path, options)(value)
      }
    }
  }
}
