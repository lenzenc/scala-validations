package com.payit

import com.payit.validations.rules.{Rule, StringRules, GeneralRules, OrderingRules}

package object validations extends GeneralRules with OrderingRules with StringRules {

  case class ValidationProp[T, V](key: String, prop: (T) => V, parentKey: ParentKey = ParentKey())

  implicit class RuleOpts[T, V](validationProp: ValidationProp[T, V]) {
    def is(validationRules: Rule[V]*): RuleSet[T, V] = new RuleSet[T, V] {
      val parentKey = validationProp.parentKey
      val key = validationProp.key
      val mapper = validationProp.prop
      val rules = validationRules
    }
  }

  implicit class ValidationsOpts[T, V <: Validations](validationProp: ValidationProp[T, V]) {
    def isValid: ValidationsRuleSet[T, V] = new ValidationsRuleSet[T, V](
      validationProp.key,
      validationProp.prop
    )
  }

}
