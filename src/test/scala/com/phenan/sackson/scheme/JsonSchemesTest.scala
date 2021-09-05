package com.phenan.sackson.scheme

import com.fasterxml.jackson.core.JsonFactory
import com.phenan.sackson.parser.{JsonParseError, JsonParser}
import com.phenan.sackson.path.JsonPath
import com.phenan.sackson.printer.JsonPrinter
import org.scalatest.wordspec.AnyWordSpec

import java.io.ByteArrayOutputStream

class JsonSchemesTest extends AnyWordSpec {
  private val jsonFactory = new JsonFactory()

  private def runParser[T](parser: JsonParser[T], string: String): Either[JsonParseError, T] = {
    val jsonParser = jsonFactory.createParser(string).nn
    jsonParser.nextToken()
    parser.run(jsonParser, JsonPath.Root, JsonParser.Options())
  }

  private def runPrinter[T](printer: JsonPrinter[T], value: T): String = {
    val stream = new ByteArrayOutputStream()
    val generator = jsonFactory.createGenerator(stream).nn
    val options = JsonPrinter.Options()
    printer.run(generator, JsonPath.Root, options)(value)
    generator.flush()
    val result = stream.toString
    stream.close()
    result
  }

  import JsonSchemes._

  private val singleFieldStruct = struct {
    optional("b", boolean) *:
      nil
  }

  "singleFieldStruct.parser" should {
    "parse empty object as (None)" in {
      assert(runParser(singleFieldStruct.parser, """{}""") == Right(Tuple(None)))
    }
    "parse single field object as single value tuple" in {
      assert(runParser(singleFieldStruct.parser, """{"b":true}""") === Right(Tuple(Some(true))))
    }
  }

  "singleFieldStruct.printer" should {
    "print empty object for None" in {
      assert(runPrinter(singleFieldStruct.printer, Tuple(None)) == """{}""")
    }
    "print single field object for Some(true)" in {
      assert(runPrinter(singleFieldStruct.printer, Tuple(Some(true))) == """{"b":true}""")
    }
  }

  private val structWith2Fields = struct {
    required("b1", boolean) *:
      optional("b2", boolean) *:
      nil
  }

  "structWith2Fields.parser" should {
    "parse single field object as (true, None)" in {
      assert(runParser(structWith2Fields.parser, """{"b1":true}""") == Right((true, None)))
    }
    "parse two fields object as (true, Some(false))" in {
      assert(runParser(structWith2Fields.parser, """{"b1":true,"b2":false}""") == Right((true, Some(false))))
    }
  }

  "structWith2Fields.printer" should {
    "print single field object for (true, None)" in {
      assert(runPrinter(structWith2Fields.printer, (true, None)) == """{"b1":true}""")
    }
    "print two fields object for (true, Some(false))" in {
      assert(runPrinter(structWith2Fields.printer, (true, Some(false))) == """{"b1":true,"b2":false}""")
    }
  }
}
