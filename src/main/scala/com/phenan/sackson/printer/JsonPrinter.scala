package com.phenan.sackson.printer

import com.fasterxml.jackson.core.JsonGenerator
import com.phenan.sackson.path.JsonPath

trait JsonPrinter[T] { self =>
  def run (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options): T => Unit

  def const (value: T): JsonPrinter[Unit] = (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options) => { _ =>
    self.run(generator, path, options)(value)
  }

  def contraMap [U] (f: U => T): JsonPrinter[U] = (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options) => {
    self.run(generator, path, options).compose(f)
  }
}

object JsonPrinter {
  case class Options ()
}
