ParallelRaytracing
==================

*Note: Currently in the process of overhauling the Integration and Material systems.  Unfortunately there is no backwards compatibility.
*Note: Commit 735 is the last version of the old Materials system.

ParallelRaytracing is a highly scalable path-tracer written in Java.  Some of its features include: the ability to scale seamlessly from a single node (any device running the software) to any number of nodes, support for distribution to arbitrarily shaped trees of rendering nodes, a scene management system that is decoupled from the disribution system, use of no third-party libraries, and the ability to run on any vanilla JVM with a version of 1.6 or higher.

For a more detailed description and a full list of features, please see the project wiki page: http://ivl.calit2.net/wiki/index.php/Parallel_Raytracing

##Todo List
*Note: This is not comprehensive.

* Improve Camera decomposition when distributing to compute nodes of varying load capabilities.
* Dynamic Load Balancing
* Improve distributed rendering stability over long renders (renders that take more than several hours)
