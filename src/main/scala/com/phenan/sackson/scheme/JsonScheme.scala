package com.phenan.sackson.scheme

import com.phenan.sackson.parser.JsonParser
import com.phenan.sackson.printer.JsonPrinter

trait JsonScheme [T] { self =>
  def parser: JsonParser[T]
  def printer: JsonPrinter[T]
  def schema: JsonPrinter[Unit]

  import JsonSchemes._

  def imap [U] (f: T => U, g: U => T): JsonScheme[U] = new JsonScheme[U] {
    override def parser: JsonParser[U] = self.parser.map(f)
    override def printer: JsonPrinter[U] = self.printer.contraMap(g)
    override def schema: JsonPrinter[Unit] = self.schema
  }

  def const (value: T): JsonScheme[Unit] = new JsonScheme[Unit] {
    override def parser: JsonParser[Unit] = self.parser.const(value)
    override def printer: JsonPrinter[Unit] = self.printer.const(value)
    override def schema: JsonPrinter[Unit] = ???/*struct(
      required("enum") -> array(self).const(Seq(value))
    ).printer*/
  }
}
