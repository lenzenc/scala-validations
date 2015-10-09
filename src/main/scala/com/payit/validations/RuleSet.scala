package com.payit.validations

import com.payit.validations.rules.Rule

trait RuleSet[T, V] extends (T => Validated[Seq[ValidationFailure], T]) {
  def parentKey: ParentKey
  def key: String
  def mapper: (T) => V
  def rules: Seq[Rule[V]]

  def apply(obj: T): Validated[Seq[ValidationFailure], T] = {
    var failures = Seq[ValidationFailure]()
    rules.foreach { rule =>
      rule(mapper(obj)) match {
        case Failed(f) => {
          if (!failures.map(_.ruleKey).contains(f.ruleKey)) failures = failures :+ ValidationFailure(
            parentKey,
            key,
            f.ruleKey,
            f.message,
            f.params)
        }
        case _ => Nil
      }
    }
    if (failures.isEmpty) Success(obj) else Failed(failures)
  }

}
