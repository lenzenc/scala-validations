package com.payit.validations.rules

import com.payit.validations.{Success, Validated}
import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class RuleSpec extends Specification with ValidationMatchers {

  val FakeRule = new Rule[String] {
    def apply(value: String): Validated[RuleViolation, String] = Success(value)
  }

  ".succeeded" >> {
    "when passed a value" >> {
      "it should result in Success with the value that was passed" >> {
        FakeRule.succeeded("testing") should beSuccessful("testing")
      }
    }
  }

  ".failed" >> {
    "when passed key, message and param values" >> {
      "it should result in Failed with the values that were passes" >> {
        FakeRule.failed("key", "msg", Seq("test")) should beFailing(RuleViolation("key", "msg", Seq("test")))
      }
    }
    "when passed with only key and message" >> {
      "it should result in Failed with the values that were passes" >> {
        FakeRule.failed("key", "msg") should beFailing(RuleViolation("key", "msg", Seq.empty))
      }
    }
  }

}
