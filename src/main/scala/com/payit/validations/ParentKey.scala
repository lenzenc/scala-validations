package com.payit.validations

case class ParentKey(keys: String*) {

  override def toString = s"ParentKey(${keys.mkString(".")})"

  def hasKeys: Boolean = keys.nonEmpty

  def ++(parentKey: ParentKey): ParentKey = ParentKey((parentKey.keys ++ keys):_*)

}
