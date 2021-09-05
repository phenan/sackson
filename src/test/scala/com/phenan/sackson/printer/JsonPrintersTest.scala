package com.phenan.sackson.printer

import com.phenan.sackson.path.JsonPath
import org.scalatest.wordspec.AnyWordSpec

class JsonPrintersTest extends AnyWordSpec {
  import JsonPrinters._

  "boolean" should {
    "print true" in {
      assert(boolean.printString(true) == "true")
    }
    "print false" in {
      assert(boolean.printString(false) == "false")
    }
  }

  "int" should {
    "print 0" in {
      assert(int.printString(0) == "0")
    }
    "print negative integer" in {
      assert(int.printString(-448) == "-448")
    }
  }

  private val singleFieldStruct = struct {
    optional("b", boolean) *:
      nil
  }

  "singleFieldStruct" should {
    "print empty object for None" in {
      assert(singleFieldStruct.printString(Tuple(None)) == """{}""")
    }
    "print single field object for Some(true)" in {
      assert(singleFieldStruct.printString(Tuple(Some(true))) == """{"b":true}""")
    }
  }

  private val structWith2Fields = struct {
    required("b1", boolean) *:
      optional("b2", boolean) *:
      nil
  }

  "structWith2Fields" should {
    "print single field object for (true, None)" in {
      assert(structWith2Fields.printString((true, None)) == """{"b1":true}""")
    }
    "print two fields object for (true, Some(false))" in {
      assert(structWith2Fields.printString((true, Some(false))) == """{"b1":true,"b2":false}""")
    }
  }
}
