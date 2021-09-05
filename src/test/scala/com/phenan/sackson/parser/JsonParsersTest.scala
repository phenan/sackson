package com.phenan.sackson.parser

import org.scalatest.wordspec.AnyWordSpec

class JsonParsersTest extends AnyWordSpec {
  import JsonParsers._

  "boolean" should {
    "parse true" in {
      assert(boolean.parseString("true") == Right(true))
    }
    "parse false" in {
      assert(boolean.parseString("false") == Right(false))
    }
    "parse 1 as true" in {
      assert(boolean.parseString("1") == Right(true))
    }
    "parse 0 as false" in {
      assert(boolean.parseString("0") == Right(false))
    }
  }

  private val singleFieldStruct = struct {
    optional("b", boolean) *:
      nil
  }

  "singleFieldStruct" should {
    "parse empty object as (None)" in {
      assert(singleFieldStruct.parseString("""{}""") == Right(Tuple(None)))
    }
    "parse single field object as single value tuple" in {
      assert(singleFieldStruct.parseString("""{"b":true}""") === Right(Tuple(Some(true))))
    }
  }

  private val structWith2Fields = struct {
    required("b1", boolean) *:
      optional("b2", boolean) *:
      nil
  }

  "structWith2Fields" should {
    "parse single field object as (true, None)" in {
      assert(structWith2Fields.parseString("""{"b1":true}""") == Right((true, None)))
    }
    "parse two fields object as (true, Some(false))" in {
      assert(structWith2Fields.parseString("""{"b1":true,"b2":false}""") == Right((true, Some(false))))
    }
  }
}
