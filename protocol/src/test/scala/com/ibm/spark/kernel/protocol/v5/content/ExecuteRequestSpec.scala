/*
 * Copyright 2014 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.spark.kernel.protocol.v5.content

import org.scalatest.{Matchers, FunSpec}
import play.api.data.validation.ValidationError
import play.api.libs.json._
import com.ibm.spark.kernel.protocol.v5._

class ExecuteRequestSpec extends FunSpec with Matchers {
  val executeRequestJson: JsValue = Json.parse("""
  {
    "code": "<STRING>",
    "silent": false,
    "store_history": false,
    "user_expressions": {},
    "allow_stdin": false
  }
  """)

  val executeRequest: ExecuteRequest = ExecuteRequest(
    "<STRING>", false, false, UserExpressions(), false
  )

  describe("ExecuteRequest") {
    describe("implicit conversions") {
      it("should implicitly convert from valid json to a executeRequest instance") {
        // This is the least safe way to convert as an error is thrown if it fails
        executeRequestJson.as[ExecuteRequest] should be (executeRequest)
      }

      it("should also work with asOpt") {
        // This is safer, but we lose the error information as it returns
        // None if the conversion fails
        val newExecuteRequest = executeRequestJson.asOpt[ExecuteRequest]

        newExecuteRequest.get should be (executeRequest)
      }

      it("should also work with validate") {
        // This is the safest as it collects all error information (not just first error) and reports it
        val executeRequestResults = executeRequestJson.validate[ExecuteRequest]

        executeRequestResults.fold(
          (invalid: Seq[(JsPath, Seq[ValidationError])]) => println("Failed!"),
          (valid: ExecuteRequest) => valid
        ) should be (executeRequest)
      }

      it("should implicitly convert from a executeRequest instance to valid json") {
        Json.toJson(executeRequest) should be (executeRequestJson)
      }
    }
  }
}

