package com.phenan.sackson.scheme

import com.phenan.sackson.parser.{JsonParser, PrimitiveParsers}
import com.phenan.sackson.printer.{JsonPrinter, PrimitivePrinters}

object PrimitiveSchemes {
  import JsonSchemes._

  object BooleanScheme extends JsonScheme[Boolean] {
    override def parser: JsonParser[Boolean] = PrimitiveParsers.BooleanParser
    override def printer: JsonPrinter[Boolean] = PrimitivePrinters.BooleanPrinter
    override def schema: JsonPrinter[Unit] = struct(
      required("type", string.const("boolean"))
      *: nil
    ).printer.contraMap((_: Unit) => Tuple(()))
  }

  object IntScheme extends JsonScheme[Int] {
    override def parser: JsonParser[Int] = PrimitiveParsers.IntParser
    override def printer: JsonPrinter[Int] = PrimitivePrinters.IntPrinter
    override def schema: JsonPrinter[Unit] = struct(
      required("type", string.const("number"))
      *: nil
    ).printer.contraMap((_: Unit) => Tuple(()))
  }

  object StringScheme extends JsonScheme[String] {
    override def parser: JsonParser[String] = PrimitiveParsers.StringParser
    override def printer: JsonPrinter[String] = PrimitivePrinters.StringPrinter
    override def schema: JsonPrinter[Unit] = struct(
      required("type", string.const("string"))
        *: nil
    ).printer.contraMap((_: Unit) => Tuple(()))
  }
}
