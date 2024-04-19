# KnightCodeSkeleton

This project encapsulates the directory structure required for the final project in CS322. Additionally, it contains an ant build.xml file you can use to buid/compile/clean your project if you wish. Refer to the build.xml file for the build/clean targets.

This project simulates a real-world compiler build. Using a defined grammar, and ANTLR generated files from that grammar, this project works on extending a Visitor, a strategy used by many compilers. The Visitor overrides methods to provide basic functionality to a user, such as printing, arithmetic, comparisons, and more. This compiler works with a language called KnightCode. KnightCode is a language derived from the "defined grammar" I mentioned earlier. In addition to the visitor, there is a kcc file that "kicks off" the compiler. Run this kcc file and input the names of the desired input and output files to use the compiler.

(I was unable to get the desired outputs from the test files, but I was able to get the compiler to compile. I added detailed notes in the files I worked on, especially in the MyVisitor.java file)

