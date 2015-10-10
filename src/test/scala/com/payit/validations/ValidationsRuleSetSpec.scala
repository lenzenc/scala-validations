package com.payit.validations

import org.specs2.mutable.Specification
import org.specs2.validation.ValidationMatchers

class ValidationsRuleSetSpec extends Specification with ValidationMatchers {

  case class ChildObj(name: String, failure: Map[String, Seq[ValidationFailure]] = Map()) extends Validations {
    override def isValid: Validated[Map[String, Seq[ValidationFailure]], this.type ] = {
      if (failure.isEmpty) Success(this) else Failed(failure)
    }
  }
  case class ParentObj(childObj: ChildObj) {
    def withChild(childObj: ChildObj) = copy(childObj = childObj)
  }

  val ruleSet = new ValidationsRuleSet[ParentObj, ChildObj]("childObj", { p => p.childObj })

  "#constructor" >> {
    "it should set the parentKey to the key value" >> {
      ruleSet.parentKey must_== ParentKey(ruleSet.key)
    }
  }

  val Failure = ValidationFailure(ParentKey(), "name", "max", "msg", Seq(100))
  val Child = ChildObj("child")
  val FailedChildNoParent = ChildObj("child", Map("" -> Seq(Failure)))
  val FailedChildWithParent = ChildObj("child", Map("" -> Seq(Failure.withParentKey(ParentKey("parent")))))
  val Parent = ParentObj(Child)


  ".apply" >> {
    "when Success" >> {
      "it should return the object value under validation" >> {
        ruleSet(Parent) must beSuccessful(Parent)
      }
    }
    "when Failed" >> {
      "when failures from Validations#isValid have a ParentKey" >> {
        "it should prepend this rule sets parent key value to the given failure's parent key" >> {
          ruleSet(Parent.withChild(FailedChildWithParent)) must beFailing.like { case failures =>
            failures must containTheSameElementsAs(Seq(Failure.withParentKey(ParentKey("parent", "childObj"))))
          }
        }
      }
      "when failures from Validations#isValid has no ParentKey" >> {
        "it should set the parent key value on the returned failure that matches this rule sets key value" >> {
          ruleSet(Parent.withChild(FailedChildNoParent)) must beFailing.like { case failures =>
            failures must containTheSameElementsAs(Seq(Failure.withParentKey(ParentKey("childObj"))))
          }
        }
      }
    }
  }

}
