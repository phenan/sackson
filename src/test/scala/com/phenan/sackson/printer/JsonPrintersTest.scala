package com.phenan.sackson.printer

import com.fasterxml.jackson.core.{JsonFactory, JsonGenerator}
import com.phenan.sackson.path.JsonPath
import org.scalatest.wordspec.AnyWordSpec

import java.io.ByteArrayOutputStream

class JsonPrintersTest extends AnyWordSpec {
  private val jsonFactory = new JsonFactory()

  private def runPrinter[T](printer: JsonPrinter[T], value: T): String = {
    val stream = new ByteArrayOutputStream()
    val generator = jsonFactory.createGenerator(stream)
    val options = JsonPrinter.Options()
    printer.run(generator, JsonPath.Root, options)(value)
    generator.flush()
    val result = stream.toString
    stream.close()
    result
  }

  import JsonPrinters._

  "boolean" should {
    "print true" in {
      assert(runPrinter(boolean, true) == "true")
    }
    "print false" in {
      assert(runPrinter(boolean, false) == "false")
    }
  }

  private val singleFieldStruct = struct {
    optional("b", boolean) *:
      nil
  }

  "singleFieldStruct" should {
    "print empty object for None" in {
      assert(runPrinter(singleFieldStruct, Tuple(None)) == """{}""")
    }
    "print single field object for Some(true)" in {
      assert(runPrinter(singleFieldStruct, Tuple(Some(true))) == """{"b":true}""")
    }
  }

  private val structWith2Fields = struct {
    required("b1", boolean) *:
      optional("b2", boolean) *:
      nil
  }

  "structWith2Fields" should {
    "print single field object for (true, None)" in {
      assert(runPrinter(structWith2Fields, (true, None)) == """{"b1":true}""")
    }
    "print two fields object for (true, Some(false))" in {
      assert(runPrinter(structWith2Fields, (true, Some(false))) == """{"b1":true,"b2":false}""")
    }
  }
}
