package com.phenan.sackson.parser

import com.fasterxml.jackson.core.JsonToken
import com.phenan.sackson.path.JsonPath

case class JsonParseError (expected: String, currentToken: JsonToken | Null, path: JsonPath)
