package com.payit.validations

import org.specs2.mutable.Specification

class ParentKeySpec extends Specification {

  ".toString" >> {
    "when no keys exist" >> {
      "it should return expected values and format" >> {
        ParentKey().toString must_== "ParentKey()"
      }
    }
    "when there is only one key" >> {
      "it should return expected values and format" >> {
        ParentKey("key1").toString must_== "ParentKey(key1)"
      }
    }
    "when there are two keys" >> {
      "it should return expected values and format" >> {
        ParentKey("key1", "key2").toString must_== "ParentKey(key1.key2)"
      }
    }
    "when there are more than two keys" >> {
      "it should return expected values and format" >> {
        ParentKey("key1", "key2", "key3").toString must_== "ParentKey(key1.key2.key3)"
      }
    }
  }

  ".hasKeys" >> {
    "when there are no keys" >> {
      "it should return false" >> {
        ParentKey().hasKeys must beFalse
      }
    }
    "when there are keys" >> {
      "it should return true" >> {
        ParentKey("key").hasKeys must beTrue
      }
    }
  }

  ".++" >> {
    "when there are no keys on instance and passed ParentKey with a key" >> {
      "it should return expected ParentKey" >> {
        ParentKey() ++ ParentKey("key1") must_== ParentKey("key1")
      }
    }
    "when there are keys on instance and passed ParentKey with no keys" >> {
      "it should return expected ParentKey" >> {
        ParentKey("key1") ++ ParentKey() must_== ParentKey("key1")
      }
    }
    "when there are keys on instance and passed ParentKey with a key" >> {
      "it should return expected ParentKey" >> {
        ParentKey("key1") ++ ParentKey("key2") must_== ParentKey("key2", "key1")
      }
    }
  }

}
