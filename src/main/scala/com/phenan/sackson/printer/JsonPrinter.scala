package com.phenan.sackson.printer

import com.fasterxml.jackson.core.{JsonFactory, JsonGenerator}
import com.phenan.sackson.path.JsonPath

import java.io.ByteArrayOutputStream
import scala.util.Using

trait JsonPrinter[T] { self =>
  private[printer] def run (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options): T => Unit

  private[printer] def run (generator: JsonGenerator, options: JsonPrinter.Options): T => Unit = {
    run(generator, JsonPath.Root, options)
  }

  def printString(value: T, options: JsonPrinter.Options = JsonPrinter.defaultOptions, factory: JsonPrinter.JacksonGeneratorFactory = JsonPrinter.defaultFactory): String = {
    factory.withStringGenerator { generator =>
      run(generator, options)(value)
    }
  }

  def const (value: T): JsonPrinter[Unit] = (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options) => { _ =>
    self.run(generator, path, options)(value)
  }

  def contraMap [U] (f: U => T): JsonPrinter[U] = (generator: JsonGenerator, path: JsonPath, options: JsonPrinter.Options) => {
    self.run(generator, path, options).compose(f)
  }
}

object JsonPrinter {
  case class Options ()

  class JacksonGeneratorFactory (jsonFactory: JsonFactory) {
    def withStringGenerator(f: JsonGenerator => Unit): String = {
      Using.Manager { use =>
        val stream = use(new ByteArrayOutputStream())
        val generator = use(jsonFactory.createGenerator(stream).nn)
        f(generator)
        generator.flush()
        stream.toString
      }.get
    }
  }

  lazy val defaultFactory: JacksonGeneratorFactory = new JacksonGeneratorFactory(new JsonFactory())

  val defaultOptions: Options = Options()
}
