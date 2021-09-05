package com.phenan.sackson.parser

import com.fasterxml.jackson.core.{JsonFactory, JsonParser as JacksonParser}
import com.phenan.sackson.path.JsonPath
import org.scalatest.wordspec.AnyWordSpec

class JsonParsersTest extends AnyWordSpec {
  private val jsonFactory = new JsonFactory()

  private def runParser[T](parser: JsonParser[T], string: String): Either[JsonParseError, T] = {
    val jsonParser = jsonFactory.createParser(string).nn
    jsonParser.nextToken()
    parser.run(jsonParser, JsonPath.Root, JsonParser.Options())
  }

  import JsonParsers._

  "boolean" should {
    "parse true" in {
      assert(runParser(boolean, "true") == Right(true))
    }
    "parse false" in {
      assert(runParser(boolean, "false") == Right(false))
    }
    "parse 1 as true" in {
      assert(runParser(boolean, "1") == Right(true))
    }
    "parse 0 as false" in {
      assert(runParser(boolean, "0") == Right(false))
    }
  }

  private val singleFieldStruct = struct {
    optional("b", boolean) *:
      nil
  }

  "singleFieldStruct" should {
    "parse empty object as (None)" in {
      assert(runParser(singleFieldStruct, """{}""") == Right(Tuple(None)))
    }
    "parse single field object as single value tuple" in {
      assert(runParser(singleFieldStruct, """{"b":true}""") === Right(Tuple(Some(true))))
    }
  }

  private val structWith2Fields = struct {
    required("b1", boolean) *:
      optional("b2", boolean) *:
      nil
  }

  "structWith2Fields" should {
    "parse single field object as (true, None)" in {
      assert(runParser(structWith2Fields, """{"b1":true}""") == Right((true, None)))
    }
    "parse two fields object as (true, Some(false))" in {
      assert(runParser(structWith2Fields, """{"b1":true,"b2":false}""") == Right((true, Some(false))))
    }
  }
}
