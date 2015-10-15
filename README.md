# Scala Validations
----
Scala Validations is a pure Scala validation framework that is/was largely inspired by the ActiveRecord::Validations framework in Ruby on Rails.  The intent of this framework is to be flexible enough to be used in many different use cases given a typical SOA type service stack.  Providing controller level parameter validation to service interface and domain model validations. 

# Example / Usage
The follow is a simple example showing how to get started using this library.

First, you will need to import the following;

    import com.payit.validations._
    
Then create some objects and define "validation" rules for those.
    
    case class Address(name: String, street: String) extends Validations {
      validations(
        prop("name", { a => a.name }).is(Required(), MaxLength(100)),
        prop("street", { a => a.street }).is(Required(), MaxLength(100))
      )
    }
    case class Contact(firstName: String, lastName: String, address: Address) extends Validations {
      validations(
        prop("firstName", { c => c.firstName }).is(Required(), MaxLength(100)),
        prop("lastName", { c => c.lastName }).is(Required(), MaxLength(100)),
        prop("address", { c => c.address }).isValid
      )
    }
    case class Customer(name: String, contact: Contact) extends Validations {
      validations(
        prop("name", { c => c.name }).is(Required(), MinLength(4)),
        prop("contact", { c => c.contact }).isValid
      )
    }    
    
As you can see in the example the classes that define "validations" extend the Validations trait which allows you to define a list of validation rule as well as exposed a "isValid" method that is used to run validation rule on a given instance.

Now if I create the following instances of Customer and call the "isValid" method this is the output I would expect;

    val ValidCustomer = Customer("Customer A", Contact("Bob", "Smith", Address("Home", "214 Some Street")))
    val InValidCustomer = Customer("C", Contact("Bob", "", Address("", "214 Some Street")))

    println(ValidCustomer.isValid)
    println(InValidCustomer.isValid)
    
Results:
    
    Success(Customer(Customer A,Contact(Bob,Smith,Address(Home,214 Some Street))))
    Failed(Map(
        name -> List(ValidationFailure(ParentKey(),name,minlength,minimum is 4 characters,List(4))), 
        contact.lastName -> List(ValidationFailure(ParentKey(contact),lastName,blank,must not be blank,List())), 
        address.contact.name -> List(ValidationFailure(ParentKey(address.contact),name,blank,must not be blank,List()))
    ))
    
## Custom Validations
Scala Validations comes with a handful of out of the box validation rules, however there are always use cases where you need to extend or create custom validation rules.
    
Depending on the use case there are 2 different ways of creating custom validation rules.    
### Reusable
If you have a use case where you need to reuse a custom validation rule across objects then the best thing to do is create your own class extending Rule, see below for an example;

    case class MyCustomRule extends Rule[String] {
      def apply(value: String): Validated[RuleViolation, String] = value match {
        case x if x == "test" => success(value)
        case _ => failed("ruleKey", "Custom Message")
      }
    }
    
Then in a class that extends "Validations" you can use your new custom validation rule;
    
    case class Person(name: String) extends Validations {
      validations(
        prop("name", { p => p.name }).is(MyCustomRule())
      )
    }

### Inline
The other use case where you might need a custom rule is when a validation rule is something scoped to a class, meaning it doesn't need to be reused or in a case there multiple properties of a class need to be used to validation a rule.

Here is an example of creating a custom inline validation rule;

    case class Person(name: String) extends Validations {
      validations(
        validate("name", { (person, failures) =>
          if (person.name != "test") failures += RuleViolation("ruleKey", "Custom Message")
        })
      )
    }

Of course to clean this example up you could also define a private method that represents the signature of the second parameter and just pass that.