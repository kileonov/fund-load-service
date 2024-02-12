## Fund Load Service
This is a fund load service, which decides either it's possible to load customer fund or not.

## How it works
When application fully starts up, it sends `ReadyEvent`, which is handled by `ApplicationEventListener`. 
Then it reads data from `input.txt` and processes line by line. The final result is written to `output.txt`.

## Architecture
Kind of DDD implemented with combination of OOP & Functional programming

## How to run
`./gradlew clean bootRun`


## How to test
`./gradlew clean test` 
- Unit tests
- Integration test, verifies computed and expected output.txt files are the same

## Technology used
- Kotlin
- Spring Boot
- H2
- Jet Brains Exposed ORM
- gradle

## Improvements for the further consideration, not necessary for now:
- Chain of responsibility pattern for fund loading validation in `FundService`
- MultiModule gradle project (configuration, domain, logic)
- MapStruct

