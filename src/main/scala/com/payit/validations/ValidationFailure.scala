package com.payit.validations

case class ValidationFailure(
  parentKey: ParentKey,
  key: String,
  ruleKey: String,
  message: String,
  params: Seq[Any] = Seq.empty)
