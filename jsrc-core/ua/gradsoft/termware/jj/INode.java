/*
 * INode.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.termware.jj;

/**
 *Interface which emulate JavaCC JJTree node.
 * @author Ruslan Shevchenko
 */
public interface INode {
    
      public String getName();
      
    
   /** This method is called after the node has been made the current
	node.  It indicates that child nodes can now be added to it. */
      public void jjtOpen();

      /** This method is called after all the child nodes have been
	added. */
      public void jjtClose();

      /** This pair of methods are used to inform the node of its
	parent. */
      public void jjtSetParentInternal(INode n);


      /**
       */
      public INode jjtGetParentInternal();

      /** This method tells the node to add its argument to the node's
	list of children.  */
      public void jjtAddChildInternal(INode n, int i);

      /** This method returns a child node.  The children are numbered
	 from zero, left to right. */
      public INode jjtGetChildInternal(int i);

      /** Return the number of children the node has. */
      int jjtGetNumChildren();

    
    
}



