package com.payit.validations

class ValidationsRuleSet[T, V <: Validations](val key: String, val mapper: (T) => V) extends RuleSet[T, V] {

  val parentKey: ParentKey = ParentKey(key)
  val rules = Seq.empty

  override def apply(obj: T): Validated[Seq[ValidationFailure], T] = {
    mapper(obj).isValid match {
      case Success(_) => Success(obj)
      case Failed(failureMap) => Failed(failureMap.values.flatMap(_.map{ f =>
        f.withParentKey(parentKey ++ f.parentKey)}).toSeq)
    }
  }

}
