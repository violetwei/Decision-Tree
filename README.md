# Decision-Tree

From a quick web search, you learn that decision trees are a classical AI technique for classifying objects by their properties. One typically refers to object attributes rather than object properties, and one typically refers object labels to say how an object is classified.


As a concrete example, consider a computer vision system that analyzes surveillance video in a large store and it classifies people seen in the video as being either employees or customers. An example attribute could be the location of the person in the store. Employees tend to spend their time in different places than customers. For example, only employees are supposed to be behind the cash register.


For classification problems in general, one defines object attributes with x variables and one defines the object label as a y variable. In the example that you will work with in this assignment, the attributes will be the spatial position (x1, x2), and the label y will be a color (red or green).


Let’s get back to decision trees themselves. Decision trees are rooted trees. So they have internal nodes and external nodes (leaves). To classify a data item (datum) using a decision tree, one starts at the root and follows a path to a leaf. Each internal node contains an attribute test. This test amounts to a question about the value of the attribute – for example, the location of a customer in a store. Each child of an internal node in a decision tree represents an outcome of the attribute test. For simplicity, you will only have to deal with binary decision trees, so the answers to attribute test questions will be either true or false. A test might be x1 < 5. The answer determines which child node to follow on the path to a leaf.


The labelling of the object occurs when the path reaches a leaf node. Each leaf node contains a label that is assigned to any test data object that arrives at that leaf node after traversing the tree from the root. The label might be red or green, which could be coded using an enum type, or simply 0 or 1. Note that, for any test data object, the label given is the label of the leaf node reached by that object, which depends on the outcomes of the attribute tests at the internal nodes.

## 1.1 Creating decision trees

To classify objects using a decision tree, we first need to have a decision tree! Where do decision trees come from? In machine learning, one creates decision trees from a labelled data set. Each data item (datum) in the given labelled data set has well defined attributes x and label y. We refer to the data set that is used to create a decision tree as the training set. The basic algorithm for creating a decision tree using a training set is as follows. This is the algorithm that need to implement for fillDTNode() [method in DecisionTree.java].

Data: data set (training)

Result: the root node of a decision tree

MAKE DECISION TREE NODE(data)

if the labelled data set has at least k data items (see below) then

if all the data items have the same label then

create a leaf node with that class label and return it;

else   create a “best” attribute test question; (see details later)

create a new node and store the attribute test in that node, namely attribute and
threshold;

split the set of data items into two subsets, data1 and data2, according to the answers
to the test question;

newNode.child1 = MAKE DECISION TREE NODE(data1)

newNode.child2 = MAKE DECISION TREE NODE(data2)

return newNode

end 

end

In the program, k is an argument of the decision tree construction minSizeDatalist.

## 1.2 Classification using decision trees

Once you have a decision tree, you can use it to classify new objects. This is called the testing phase. For the testing phase, one can use data items from the original data used for training (above) or one can use new data. Typically when a decision tree is used in practice, the test objects are unlabelled. In the surveillance example earlier, the system would test a new video and try to classify people as employees versus customers. Here the idea is that one does not know the correct class for each person. Let’s consider this general scenario now, that we are given a decision tree and the attributes of some new unlabelled test object. We will to use the decision tree to choose a label for the object. This is done by traversing the decision tree from the root to a leaf, as follows:

Data: A decision tree,and an unlabelled data item(datum) to be classified 

Result: (Predicted) classification label

CLASSIFY(node, datum) {

if node is a leaf then

return the label of that node i.e. classify;

else

test the data item using question stored at that (internal) node, and determine which child node to go to, based on the answer ;

return CLASSIFY(child, datum);

end

}

## 2. Instantiating the decision tree problem

For this assignment, the problem is to classify points based on their 2D position. Each datapoint has two attributes x represented by an array of size 2, and a binary label y (0 or 1).

A graphical representation of example of a data set looks like this. (For the graphs, the attribute value x[0], is represented as x1 and x[1] as x2.) For those who print out the document in color, the red symbols can be label 0 and the green symbols can be label 1. For those printing in black and white, the (red) disks are label 0 and the (green) ⇥’s are label 1.

![jietu20181119-124209](https://user-images.githubusercontent.com/31902939/48724935-a5bf6200-ebf8-11e8-8cbb-60b5b76d3ac3.jpg)

## 2.1 Finding a good split

Now that we have an idea of what the data are, let us return to the question of how to split the data into two sets when creating a node in a decision tree. What makes a ’good’ split? Intuitively, a split is good when the labels in each set are as ‘pure’ as possible, that is, each subset is dominated as much as possible by a single label (and the dominant label is differs between subsets). For example, suppose this is our data:

### Datum.java

This class holds the information of a single datapoint. It has two variables, x and y. x is an array containing the attributes and y contains the label.
The class also comes with a method toString() which returns a string representation of the attributes and label of a single datapoint.

### DataReader.java

This class deals with three things. The method read data() reads a dataset from a csv3, and splits the read dataset into the training and test set, using splitTrainTestData() file. It also has methods that deal with reading and writing of “serialized” decision tree objects.
