package com.phenan.sackson.parser

import com.fasterxml.jackson.core.JsonParser as JacksonParser
import com.phenan.sackson.path.JsonPath

sealed trait JsonFieldParser[T] {
  def fieldName: String
  def parser: JsonParser[T]
}

object JsonFieldParser {
  case class Required [T] (fieldName: String, valueParser: JsonParser[T]) extends JsonFieldParser[T] {
    override def parser: JsonParser[T] = valueParser
  }
  case class Optional [T] (fieldName: String, valueParser: JsonParser[T]) extends JsonFieldParser[Option[T]] {
    val parser: JsonParser[Option[T]] = valueParser.map(Some(_))
  }
}
