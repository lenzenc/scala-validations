package com.payit.validations.rules

import com.payit.validations.Validated

trait StringRules {

  case class StartsWith(str: String) extends Rule[String] {
    def apply(value: String): Validated[RuleViolation, String] = value match {
      case s if s != null && s.startsWith(str) => succeeded(value)
      case _ => failed("startswith", s"should start with $value", Seq(value))
    }
  }

}
