package com.payit.validations.rules

case class RuleViolation(ruleKey: String, message: String, params: Seq[Any] = Seq.empty)
