package com.payit.validations

trait Validator[T] extends (T => Validated[Map[String, Seq[ValidationFailure]], T]) {

  def parentKey: ParentKey = ParentKey()

  def ruleSets: Seq[RuleSet[T, _]]

  def apply(obj: T): Validated[Map[String, Seq[ValidationFailure]], T] = {
    var failures = Map[String, Seq[ValidationFailure]]()
    ruleSets.foreach { ruleSet =>
      ruleSet(obj) match {
        case Success(_) => Nil
        case Failed(f) => failures = (failures.values.flatMap(f => f) ++ f).groupBy{f =>
          buildKey(f.key, f.parentKey)}.mapValues(_.map(f => f).toSeq.distinct)
      }
    }

    if (failures.isEmpty) Success(obj) else Failed(failures)
  }

  protected def buildKey(key: String, parentKey: ParentKey): String = (parentKey.keys :+ key).mkString(".")
  
}