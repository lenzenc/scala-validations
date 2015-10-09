package org.specs2.validation

import com.payit.validations.{Failed, Success, Validated}
import org.specs2.mutable.Specification

import org.specs2.matcher._
import org.specs2.text.Quote._
import org.specs2.execute.{ Failure, Result }

trait ValidationMatchers extends Specification { outer =>

  def beSuccessful[T](t: => T) =
    new Matcher[Validated[_, T]] {
      def apply[S <: Validated[_, T]](value: Expectable[S]) = {
        val expected = t
        result(
          value.value == Success(t),
          value.description + " is Success with value" + q(expected),
          value.description + " is not Success with value" + q(expected),
          value
        )
      }
    }

  def beSuccessful[T] = new SuccessValidationMatcher[T]

  class SuccessValidationMatcher[T] extends Matcher[Validated[_, T]] {
    def apply[S <: Validated[_, T]](value: Expectable[S]) = {
      result(
        value.value.isSuccess,
        value.description + " is Success",
        value.description + " is not Success",
        value
      )
    }

    def like(f: PartialFunction[T, MatchResult[_]]) = this and partialMatcher(f)

    private def partialMatcher(f: PartialFunction[T, MatchResult[_]]) = new Matcher[Validated[_, T]] {
      def apply[S <: Validated[_, T]](value: Expectable[S]) = {
        val res: Result = value.value match {
          case Success(t) if f.isDefinedAt(t)  => f(t).toResult
          case Success(t) if !f.isDefinedAt(t) => Failure("function undefined")
          case other                            => Failure("no match")
        }
        result(
          res.isSuccess,
          value.description + " is Success[T] and " + res.message,
          value.description + " is Success[T] but " + res.message,
          value
        )
      }
    }
  }

  def successful[T](t: => T) = beSuccessful(t)
  def successful[T] = beSuccessful

  def beFailing[T](t: => T) = new Matcher[Validated[T, _]] {
    def apply[S <: Validated[T, _]](value: Expectable[S]) = {
      val expected = t
      result(
        value.value == Failed(t),
        value.description + " is Failure with value" + q(expected),
        value.description + " is not Failure with value" + q(expected),
        value
      )
    }
  }

  def beFailing[T] = new FailureMatcher[T]
  class FailureMatcher[T] extends Matcher[Validated[T, _]] {
    def apply[S <: Validated[T, _]](value: Expectable[S]) = {
      result(
        value.value.isFailure,
        value.description + " is Failure",
        value.description + " is not Failure",
        value
      )
    }

    def like(f: PartialFunction[T, MatchResult[_]]) = this and partialMatcher(f)

    private def partialMatcher(f: PartialFunction[T, MatchResult[_]]) = new Matcher[Validated[T, _]] {
      def apply[S <: Validated[T, _]](value: Expectable[S]) = {
        val res: Result = value.value match {
          case Failed(t) if f.isDefinedAt(t)  => f(t).toResult
          case Failed(t) if !f.isDefinedAt(t) => Failure("function undefined")
          case other                        => Failure("no match")
        }
        result(
          res.isSuccess,
          value.description + " is Failure[T] and " + res.message,
          value.description + " is Failure[T] but " + res.message,
          value
        )
      }
    }
  }

  def failing[T](t: => T) = beFailing(t)
  def failing[T] = beFailing

}
object ValidationMatchers extends ValidationMatchers
