package com.payit.validations

import com.payit.validations.rules.{Rule, GeneralRules}
import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class RuleSetSpec extends Specification with ValidationMatchers with GeneralRules {

  case class ValidateObj(v1: String = "Value1", v2: String = "Value2")

  ".apply" >> {
    "when validation is successful" >> {
      "it should return Success" >> {
        val model = ValidateObj()
        val ruleSet = buildRuleSet(Seq(Required()), model)
        ruleSet(model) should beSuccessful
      }
      "it should return expected ValidateObj object" >> {
        val model = ValidateObj()
        val ruleSet = buildRuleSet(Seq(Required()), model)
        ruleSet(model) should beSuccessful(model)
      }
    }
    "when validation fails" >> {
      "it should return Failure" >> {
        val model = ValidateObj()
        val ruleSet = buildRuleSet(Seq(MaxLength(2)), model)
        ruleSet(model) should beFailing(Seq(ValidationFailure(
          ParentKey("parent"),
          "v1",
          "maxlength",
          "maximum is 2 characters",
          Seq(2))))
      }
      "it should return correct ValidationFailure" >> {
        val failures = failureRun(Seq(MaxLength(2)))
        failures.map(_.ruleKey) must contain(exactly("maxlength"))
      }
      "for 2 rules" >> {
        "it should return correct 2 ValidationFailure" >> {
          val failures = failureRun(Seq(MaxLength(2), StartsWith("failed")))
          failures.map(_.ruleKey) must contain(exactly("maxlength", "startswith"))
        }
      }
      "when there are multiple of the same rule types" >> {
        "it should return only 1 ValidationFailure" >> {
          val failures = failureRun(Seq(MaxLength(2), MaxLength(2)))
          failures.map(_.ruleKey) must contain(exactly("maxlength"))
        }
      }
      "when there are multiple and 2 are of the same type" >> {
        "it should return only 2 ValidationFailure" >> {
          val failures = failureRun(Seq(MaxLength(2), MaxLength(2), StartsWith("failed")))
          failures.map(_.ruleKey) must contain(exactly("maxlength", "startswith"))
        }
      }
    }
  }

  private def successRun(ruleSeq: Seq[Rule[String]], model: ValidateObj = ValidateObj()): ValidateObj = {

    val result = buildRuleSet(ruleSeq, model)(model)
    result should beSuccessful
    result match {
      case Success(m) => m
      case _ => sys.error("match should of be successful!")
    }

  }

  private def failureRun(ruleSeq: Seq[Rule[String]], model: ValidateObj = ValidateObj()): Seq[ValidationFailure] = {

    val result = buildRuleSet(ruleSeq, model)(model)
    result should beFailing
    result match {
      case Failed(f) => f
      case _ => sys.error("match should of be a failure!")
    }

  }

  private def buildRuleSet(ruleSeq: Seq[Rule[String]], model: ValidateObj = ValidateObj()): RuleSet[ValidateObj, String] = {
    new RuleSet[ValidateObj, String] {
      val parentKey = ParentKey("parent")
      val key = "v1"
      val mapper: (ValidateObj) => String = { m => m.v1 }
      val rules = ruleSeq
    }
  }

}
