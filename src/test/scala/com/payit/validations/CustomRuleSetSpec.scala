package com.payit.validations

import com.payit.validations.rules.RuleViolation
import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

import scala.collection._

class CustomRuleSetSpec extends Specification with ValidationMatchers {

  val NoFailures: (String, mutable.ListBuffer[RuleViolation]) => Unit = { (value, failures) => }
  val WithFailures: (String, mutable.ListBuffer[RuleViolation]) => Unit = { (value, failures) =>
    failures += RuleViolation("min", "Min Value", Seq(1))
  }

  ".apply" >> {
    "when constructor function adds NO RuleViolations" >> {
      "it should be successful" >> {
        CustomRuleSet[String]("testing", NoFailures)("testValue") should beSuccessful("testValue")
      }
    }
    "when constructor function adds RuleViolations" >> {
      "it should fail with expected failure" >> {
        CustomRuleSet[String]("testing", WithFailures)("testValue") should beFailing.like{ case failures =>
          failures must containTheSameElementsAs(Seq(ValidationFailure(ParentKey(), "testing", "min", "Min Value", Seq(1))))
        }
      }
    }
  }

}
