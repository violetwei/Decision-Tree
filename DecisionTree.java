import java.io.Serializable;
import java.util.ArrayList;
import java.text.*;
import java.lang.Math;

public class DecisionTree implements Serializable {

	DTNode rootDTNode;
	int minSizeDatalist; //minimum number of datapoints that should be present in the dataset so as to initiate a split
	
	//Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
	public static final long serialVersionUID = 343L;
	
	//constructor
	public DecisionTree(ArrayList<Datum> datalist , int min) {
		minSizeDatalist = min;
		rootDTNode = (new DTNode()).fillDTNode(datalist);
	}

	class DTNode implements Serializable{
		//Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
		public static final long serialVersionUID = 438L;
		
		boolean leaf;
		int label = -1;      // only defined if node is a leaf
		int attribute; // only defined if node is not a leaf
		double threshold;  // only defined if node is not a leaf

		
		DTNode left, right; //the left and right child of a particular node. (null if leaf)

		DTNode() {
			leaf = true;
			threshold = Double.MAX_VALUE;
		}


		// this method takes in a datalist (ArrayList of type datum) and a minSizeInClassification (int) and returns
		// the calling DTNode object as the root of a decision tree trained using the datapoints present in the datalist variable
		// KEEP IN MIND that the left and right child of the node correspond to "less than" and "greater than or equal to" threshold
		DTNode fillDTNode(ArrayList<Datum> datalist) {
			//using recursion

			DTNode new_node = new DTNode();
			
			//if labeled data has at least k data items
			if(datalist.size() >= minSizeDatalist) {
				
				//check whether all labels are the same
				boolean sameLabel = true;
				
				for(int i=0; i<datalist.size()-1; i++) 
				{
					sameLabel= (datalist.get(i).y == datalist.get(i+1).y);
					
					if(sameLabel == false) 
					{
						break;
					}
				}
				
				//if all the data items have the same label
				if(sameLabel == true) 
				{
					//create a node with that class label and return it
					new_node.leaf = true;
					new_node.label = datalist.get(0).y;
					return new_node;
				}
				//if don't have the same label
				else 
				{ 
					//find the best split
					new_node.leaf = false;
					
					//initialize variables
					int best_attr = -1;
					double best_threshold = -1;
					double best_avg_entropy = 1;
					
					ArrayList<Datum> bestSubset_left = new ArrayList<Datum>();
					ArrayList<Datum> bestSubset_right = new ArrayList<Datum>();
					
					//for each attribute in [x1, x2,] do
					for(int i=0; i <= 1; i++) 
					{
						//for each data point in list (threshold) do
						for(int j=0; j < datalist.size(); j++) 
						{
							//split the set of data items into two subsets
							ArrayList<Datum> subset_left = new ArrayList<Datum>();
							ArrayList<Datum> subset_right = new ArrayList<Datum>();
							
							for(int k=0; k<datalist.size(); k++) {
								
								if(datalist.get(k).x[i] < datalist.get(j).x[i]) 
								{
									subset_left.add(datalist.get(k));
								}
								else {
									subset_right.add(datalist.get(k));
								}
							}
							
							double left_entropy = calcEntropy(subset_left);
							double right_entropy = calcEntropy(subset_right);
							double curr_avg_entropy = left_entropy*subset_left.size() /datalist.size() + right_entropy*subset_right.size() /datalist.size();
							
							if(best_avg_entropy > curr_avg_entropy)
							{
								best_avg_entropy = curr_avg_entropy;
								best_attr = i;
								best_threshold = datalist.get(j).x[i];
								bestSubset_left = subset_left;
								bestSubset_right = subset_right;
							}
						}	
					}
					
					new_node.attribute = best_attr;
					new_node.threshold = best_threshold;
					
					DTNode leftNode = new DTNode();
					DTNode rightNode = new DTNode();
					
					leftNode = leftNode.fillDTNode(bestSubset_left);
					rightNode = rightNode.fillDTNode(bestSubset_right);
					
					new_node.left=leftNode;
					new_node.right = rightNode;
					
					return new_node;
				}
				
			}
			else { //if labeled data doesn't have at least k data items
				
				new_node.leaf = true;
				new_node.label=findMajority(datalist);
				
				return new_node;
			}
		}



		//This is a helper method. Given a datalist, this method returns the label that has the most
		// occurences. In case of a tie it returns the label with the smallest value (numerically) involved in the tie.
		int findMajority(ArrayList<Datum> datalist)
		{
			int l = datalist.get(0).x.length;
			int [] votes = new int[l];

			//loop through the data and count the occurrences of datapoints of each label
			for (Datum data : datalist)
			{
				votes[data.y]+=1;
			}
			int max = -1;
			int max_index = -1;
			//find the label with the max occurrences
			for (int i = 0 ; i < l ;i++)
			{
				if (max<votes[i])
				{
					max = votes[i];
					max_index = i;
				}
			}
			return max_index;
		}




		// This method takes in a datapoint (excluding the label) in the form of an array of type double (Datum.x) and
		// returns its corresponding label, as determined by the decision tree
		int classifyAtNode(double[] xQuery) {
			//once have a decision tree, you can use it to classify new objects, called the testing phase.
			//We will use the decision tree to choose a label for the object
			//This is done by traversing the decision tree from the root to a leaf
			
			//label only defined if node is a leaf, attribute and threshold are only defined if node is not a leaf
			
			//if node is a leaf
			if(this.leaf) 
			{
				return this.label;
			}
			else {
				if(xQuery[this.attribute] < threshold) 
				{
					return (this.left).classifyAtNode(xQuery); //recursion
				}
				else 
				{
					return (this.right).classifyAtNode(xQuery); //recursion
				}
			}
			
			//return -1; //dummy code.  Update while completing the assignment.
		}


		//given another DTNode object, this method checks if the tree rooted at the calling DTNode is equal to the tree rooted
		//at DTNode object passed as the parameter
		public boolean equals(Object dt2)
		{
			
			//Using recursion is a simpler way to implement this method
			
			boolean rootEqual = false;
			boolean left_equal = false;
			boolean right_equal = false;
			
			if(this != null && ((DTNode)dt2) != null) 
			{
				
				// case when the two nodes are both "leaf nodes": labels should be same
				if(this.leaf && ((DTNode)dt2).leaf) 
				{	
					// left and right nodes are null if the node is leaf node
				    rootEqual = (this.label == ((DTNode)dt2).label);
				    left_equal = true;
				    right_equal = true;
				    
			    }
			    
				//case when the two nodes are both "internal nodes": thresholds and attributes should be same.
				else if((! this.leaf) && (! ((DTNode)dt2).leaf))
				{
				    rootEqual = (this.attribute == ((DTNode)dt2).attribute) && (this.threshold == ((DTNode)dt2).threshold);
				    
				    left_equal = (this.left).equals(((DTNode)dt2).left);  //recursion
				    
				    right_equal = (this.right).equals(((DTNode)dt2).right);   // recursion 
				
			    }
				
				return  (rootEqual && left_equal  && right_equal);
			}
		
			return false;
		
		}
	}



	//Given a dataset, this retuns the entropy of the dataset
	double calcEntropy(ArrayList<Datum> datalist)
	{
		double entropy = 0;
		double px = 0;
		float [] counter= new float[2];
		if (datalist.size()==0)
			return 0;
		double num0 = 0.00000001,num1 = 0.000000001;

		//calculates the number of points belonging to each of the labels
		for (Datum d : datalist)
		{
			counter[d.y]+=1;
		}
		//calculates the entropy using the formula specified in the document
		for (int i = 0 ; i< counter.length ; i++)
		{
			if (counter[i]>0)
			{
				px = counter[i]/datalist.size();
				entropy -= (px*Math.log(px)/Math.log(2));
			}
		}

		return entropy;
	}


	// given a datapoint (without the label) calls the DTNode.classifyAtNode() on the rootnode of the calling DecisionTree object
	int classify(double[] xQuery ) {
		DTNode node = this.rootDTNode;
		return node.classifyAtNode( xQuery );
	}

    // Checks the performance of a DecisionTree on a dataset
    //  This method is provided in case you would like to compare your
    //results with the reference values provided in the PDF in the Data
    //section of the PDF

    String checkPerformance( ArrayList<Datum> datalist)
	{
		DecimalFormat df = new DecimalFormat("0.000");
		float total = datalist.size();
		float count = 0;

		for (int s = 0 ; s < datalist.size() ; s++) {
			double[] x = datalist.get(s).x;
			int result = datalist.get(s).y;
			if (classify(x) != result) {
				count = count + 1;
			}
		}

		return df.format((count/total));
	}


	//Given two DecisionTree objects, this method checks if both the trees are equal by
	//calling onto the DTNode.equals() method
	public static boolean equals(DecisionTree dt1,  DecisionTree dt2)
	{
		boolean flag = true;
		flag = dt1.rootDTNode.equals(dt2.rootDTNode);
		return flag;
	}

}

