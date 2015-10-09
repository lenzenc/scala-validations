package com.payit.validations.rules

import com.payit.validations.{Failed, Success, Validated}

trait Rule[T] extends (T => Validated[RuleViolation, T]) {

  def succeeded(value: T): Validated[RuleViolation, T] = Success(value)

  def failed(ruleKey: String, message: String, params: Seq[Any] = Seq.empty): Validated[RuleViolation, T] = {
    Failed(RuleViolation(ruleKey, message, params))
  }

}
