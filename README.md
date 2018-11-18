# Decision-Tree
decision tree code in Java to analyze spatial data

Datum.java
This class holds the information of a single datapoint. It has two variables, x and y. x is an array containing the attributes and y contains the label.
The class also comes with a method toString() which returns a string representation of the attributes and label of a single datapoint.

DataReader.java
This class deals with three things. The method read data() reads a dataset from a csv3, and splits the read dataset into the training and test set, using splitTrainTestData() file. It also has methods that deal with reading and writing of “serialized” decision tree objects.
