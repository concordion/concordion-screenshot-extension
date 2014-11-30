How to contribute
================
We welcome contributions to Concordion extensions. This document lists the guidelines for contributing.

Proposing a change
-----------------------------
Firstly, you should create an issue for your enhancement request. You may also want to discuss the change with the [concordion-dev list](https://groups.google.com/forum/#!forum/concordion-dev) before implementation.

New feature proposals should be described with acceptance tests. For features not yet implemented, please add the `@Unimplemented` annotation.

Implementing a change
----------------------------------
Concordion uses a "Fork & Pull" model for collaborative development. If you have changes that you would like us to consider for introduction to Concordion, you will need to:
 1. [fork](https://help.github.com/articles/fork-a-repo) the repository, 
 1. commit and push your changes to your forked project, and 
 1. send us a [pull request](https://help.github.com/articles/using-pull-requests) referencing the URL of the issue that you created.


Development Rules
-----------------
* Don't break existing behaviour. Backward compatibility is extremely important.
* New feature proposals should be described with acceptance tests and discussed with the [concordion-dev list](https://groups.google.com/forum/#!forum/concordion-dev) before implementation.
* Follow the style and conventions of the existing code (basically Sun's conventions). In particular:
  - Use 4 spaces (not tabs) 
  - Line width of 120 characters
  - Always use braces after "if" statements.
* All code changes should have automated tests of some sort.
* Never check a failing test into the repository. (Though you can check-in unimplemented acceptance test HTML for new feature ideas). 

Building
--------
This project ships with the Gradle wrapper.

Useful tasks include:

`./gradlew clean test`
    - to compile all and run all tests (Concordion output is written to ./build/reports/spec)

`./gradlew install`
    - to build the jar file and install to the local Maven repository. (The concordion-screenshot-extension-demo project incudes a `dev.gradle` file with instructions on including the local Maven repository when finding the dependencies).

`./gradlew tasks`
    - to list all tasks