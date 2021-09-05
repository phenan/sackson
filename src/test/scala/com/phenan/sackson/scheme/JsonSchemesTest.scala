package com.phenan.sackson.scheme

import com.phenan.sackson.parser.{JsonParseError, JsonParser}
import com.phenan.sackson.printer.JsonPrinter
import org.scalatest.wordspec.AnyWordSpec

class JsonSchemesTest extends AnyWordSpec {
  import JsonSchemes._

  private val singleFieldStruct = struct {
    optional("b", boolean) *:
      nil
  }

  "singleFieldStruct.parser" should {
    "parse empty object as (None)" in {
      assert(singleFieldStruct.parser.parseString("""{}""") == Right(Tuple(None)))
    }
    "parse single field object as single value tuple" in {
      assert(singleFieldStruct.parser.parseString("""{"b":true}""") === Right(Tuple(Some(true))))
    }
  }

  "singleFieldStruct.printer" should {
    "print empty object for None" in {
      assert(singleFieldStruct.printer.printString(Tuple(None)) == """{}""")
    }
    "print single field object for Some(true)" in {
      assert(singleFieldStruct.printer.printString(Tuple(Some(true))) == """{"b":true}""")
    }
  }

  private val structWith2Fields = struct {
    required("b1", boolean) *:
      optional("b2", boolean) *:
      nil
  }

  "structWith2Fields.parser" should {
    "parse single field object as (true, None)" in {
      assert(structWith2Fields.parser.parseString("""{"b1":true}""") == Right((true, None)))
    }
    "parse two fields object as (true, Some(false))" in {
      assert(structWith2Fields.parser.parseString("""{"b1":true,"b2":false}""") == Right((true, Some(false))))
    }
  }

  "structWith2Fields.printer" should {
    "print single field object for (true, None)" in {
      assert(structWith2Fields.printer.printString((true, None)) == """{"b1":true}""")
    }
    "print two fields object for (true, Some(false))" in {
      assert(structWith2Fields.printer.printString((true, Some(false))) == """{"b1":true,"b2":false}""")
    }
  }
}
