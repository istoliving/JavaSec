Dropwizard self-validating feature enabling attackers to inject arbitrary Java EL expressions, leading to RCE vulnerability.

vulnerable code snippet 
```java
// com.example.helloworld.core.Person#validateFullName

@SelfValidation
public void validateFullName(ViolationCollector col) {
    if (fullName.contains("$")) {
        col.addViolation("Full name contains invalid characters:  " + fullName);
    }
}

// io.dropwizard.validation.selfvalidating.ViolationCollector#addViolation
public void addViolation(String msg) {
    this.violationOccurred = true;
    this.context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
}

```


issue reproduction


<img width="991" alt="image" src="https://user-images.githubusercontent.com/55024146/194807970-43943312-9068-4779-b0c5-8ae8beee662b.png">
