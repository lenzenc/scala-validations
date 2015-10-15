package com.payit.validations

import com.payit.validations.rules.RuleViolation

import scala.collection._

trait Validations { obj =>

  protected var validator: Validator[this.type] = new Validator[this.type] {
    override val parentKey = ParentKey(validationKey)
    val ruleSets = Seq.empty
  }

  def isValid: Validated[Map[String, Seq[ValidationFailure]], this.type] = validator(obj)

  protected def validations(ruleSets: RuleSet[this.type, _]*) = {
    validator = createValidator(ruleSets)
  }

  protected def prop[V](key: String, prop: (this.type) => V, parentKey: ParentKey = ParentKey()) = {
    ValidationProp[this.type, V](key, prop, parentKey)
  }

  protected def validate(
    key: String,
    f: (this.type, mutable.ListBuffer[RuleViolation]) => Unit): CustomRuleSet[this.type] =
  {
    new CustomRuleSet[this.type](key, f)
  }

  protected def validationKey = obj.getClass().getSimpleName

  protected def createValidator(validationRuleSets: Seq[RuleSet[this.type, _]]) = new Validator[this.type] {
    override val parentKey = ParentKey(validationKey)
    val ruleSets = validationRuleSets
  }

}
