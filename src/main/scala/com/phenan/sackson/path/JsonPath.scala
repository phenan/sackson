package com.phenan.sackson.path

sealed trait JsonPath {
  def field(fieldName: String): JsonPath = JsonPath.Field(this, fieldName)
}

object JsonPath {
  case object Root extends JsonPath
  case class Field (parent: JsonPath, name: String) extends JsonPath
}
