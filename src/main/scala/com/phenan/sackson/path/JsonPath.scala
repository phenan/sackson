package com.phenan.sackson.path

sealed trait JsonPath

object JsonPath {
  case object Root extends JsonPath
  case class Field (name: String) extends JsonPath
}
