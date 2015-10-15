package com.payit.validations

import com.payit.validations.rules.{RuleViolation, Rule}

import scala.collection._

class CustomRuleSet[T](
  val key: String,
  f: (T, mutable.ListBuffer[RuleViolation]) => Unit,
  val parentKey: ParentKey = ParentKey()) extends RuleSet[T, T]
{

  val rules: Seq[Rule[T]] = Seq.empty
  val mapper: (T) => T = { t => t }

  override def apply(obj: T): Validated[scala.Seq[ValidationFailure], T] = {
    val failures = mutable.ListBuffer[RuleViolation]()
    f(obj, failures)
    if (failures.nonEmpty) Failed(failures.map(buildFailure(_))) else Success(obj)
  }
}

object CustomRuleSet {

  def apply[T](key: String, f: (T, mutable.ListBuffer[RuleViolation]) => Unit, parentKey: ParentKey = ParentKey()) =
    new CustomRuleSet[T](key, f, parentKey)

}
