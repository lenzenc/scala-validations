package com.payit

import com.payit.validations.rules.{StringRules, GeneralRules, OrderingRules}

package object validations extends GeneralRules with OrderingRules with StringRules {

}
