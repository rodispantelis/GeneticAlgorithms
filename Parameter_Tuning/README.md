# Α parameter tuning genetic algorithm.

Use it to optimize the parameters that other programs use on runtime.

In the profile and settings files you may store the settings for running the tuning procedure in various files.
The program under study should be prepared so as to run from the command line like:

java -jar clique_ga.jar var1 var2 var3

where var1 var2 var3 are the parameters that we care to examine.

The program should produce a single output that the tuning algorithm will use as fitness.
