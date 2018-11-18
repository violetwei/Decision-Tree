# Decision-Tree

From a quick web search, you learn that decision trees are a classical AI technique for classify- ing objects by their properties. One typically refers to object attributes rather than object properties, and one typically refers object labels to say how an object is classified.


As a concrete example, consider a computer vision system that analyzes surveillance video in a large store and it classifies people seen in the video as being either employees or customers. An example attribute could be the location of the person in the store. Employees tend to spend their time in different places than customers. For example, only employees are supposed to be behind the cash register.


For classification problems in general, one defines object attributes with x variables and one defines the object label as a y variable. In the example that you will work with in this assignment, the attributes will be the spatial position (x1, x2), and the label y will be a color (red or green).


Let’s get back to decision trees themselves. Decision trees are rooted trees. So they have internal nodes and external nodes (leaves). To classify a data item (datum) using a decision tree, one starts at the root and follows a path to a leaf. Each internal node contains an attribute test. This test amounts to a question about the value of the attribute – for example, the location of a customer in a store. Each child of an internal node in a decision tree represents an outcome of the attribute test. For simplicity, you will only have to deal with binary decision trees, so the answers to attribute test questions will be either true or false. A test might be x1 < 5. The answer determines which child node to follow on the path to a leaf.


The labelling of the object occurs when the path reaches a leaf node. Each leaf node contains a label that is assigned to any test data object that arrives at that leaf node after traversing the tree from the root. The label might be red or green, which could be coded using an enum type, or simply 0 or 1. Note that, for any test data object, the label given is the label of the leaf node reached by that object, which depends on the outcomes of the attribute tests at the internal nodes.

## 1.1 Creating decision trees

To classify objects using a decision tree, we first need to have a decision tree! Where do decision trees come from? In machine learning, one creates decision trees from a labelled data set. Each data item (datum) in the given labelled data set has well defined attributes x and label y. We refer to the data set that is used to create a decision tree as the training set. The basic algorithm for creating a decision tree using a training set is as follows. This is the algorithm that need to implement for fillDTNode()[method in DecisionTree.java].

### Datum.java

This class holds the information of a single datapoint. It has two variables, x and y. x is an array containing the attributes and y contains the label.
The class also comes with a method toString() which returns a string representation of the attributes and label of a single datapoint.

### DataReader.java

This class deals with three things. The method read data() reads a dataset from a csv3, and splits the read dataset into the training and test set, using splitTrainTestData() file. It also has methods that deal with reading and writing of “serialized” decision tree objects.
