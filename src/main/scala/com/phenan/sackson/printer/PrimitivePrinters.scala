package com.phenan.sackson.printer

import com.fasterxml.jackson.core.JsonGenerator
import com.phenan.sackson.path.JsonPath

object PrimitivePrinters {
  object BooleanPrinter extends JsonPrinter[Boolean] {
    override def run (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options): Boolean => Unit = {
      generator.writeBoolean
    }
  }
}
