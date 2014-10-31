/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication12;

/**
 *
 * @author Ben
 */
public class JavaApplication12 {

    public static class AVL_node<X extends Comparablea<X>> {
        
        public AVL_node<X> left;
        public AVL_node<X> right;
        public X here;
        
        public AVL_node() {}

        public AVL_node(X here, AVL_node<X> left, AVL_node<X> right) {
            this.left = left;
            this.here = here;
            this.right = right;
        }

        public X getHere() {
            return this.here;
        }
        
        public void setHere( X thing ) {
            this.here = thing;
        }
        
        public void setLeft( X thing ) {
            this.left = new AVL_node(thing, left.left, left.right);
        }
        
        public void setRight( X thing ) {
            this.right = new AVL_node(thing, right.left, right.right);
        }
        
        public int depth() {
            return 1 + Math.max(left.depth(), right.depth());
        }
        
        public int balance_factor() {
            return right.depth() - left.depth();
        }
        
    }
    
    public class AVL<X extends Comparablea<X>> {
        
        public AVL_node<X> parent;
        
        public AVL() {
            parent = new AVL_node<X>();
        }
        
        public void rotate(AVL_node<X> base, AVL_node<X> parent) {
            
        }
        
    }
    
    
    
    interface Comparablea<X> {
        public boolean leq(Comparablea X, Comparablea Y);
    }
    
    public static void main(String[] args) {

    }
}
