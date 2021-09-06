package com.phenan.sackson.printer

import com.fasterxml.jackson.core.JsonGenerator
import com.phenan.sackson.path.JsonPath

object PrimitivePrinters {
  object BooleanPrinter extends JsonPrinter[Boolean] {
    override private[printer] def run (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options): Boolean => Unit = {
      generator.writeBoolean
    }
  }

  object IntPrinter extends JsonPrinter[Int] {
    override private[printer] def run(generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options): Int => Unit = {
      generator.writeNumber
    }
  }

  object StringPrinter extends JsonPrinter[String] {
    override private[printer] def run(generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options): String => Unit = {
      generator.writeString
    }
  }
}
