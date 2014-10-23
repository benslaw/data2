

package data2;

import java.util.*;

public class Data2 {
    
    interface MSequenced<X> {
        public MSequence<X> seq();
    }
    
    interface MSequence<X> {
        public X here();
        public boolean notEmpty();
        public MSequence<X> next();
    }

    interface AVL_BST<X> extends MSequence<X>, MSequenced<X> {
        public int cardinality();
        public boolean isEmptyHuh();
        public boolean member( X thing );
        public AVL_BST<X> add( X thing );
        public AVL_BST<X> remove( X thing );
        public AVL_BST<X> union( AVL_BST<X> set );
        public AVL_BST<X> inter( AVL_BST<X> set );
        public AVL_BST<X> diff( AVL_BST<X> set );
        public boolean equal( AVL_BST<X> set );
    }
    
    public class Empty<X> implements AVL_BST<X> {
        public Empty() {}
        
        public X here() {
            throw new RuntimeException("The set is empty, there is no X here");
        }
        
        public MSequence<X> next() {
            return this;
        }
        
        public boolean notEmpty() {
            return false;
        }
        
        public MSequence<X> seq() {
            return this;
        }
        
        public int cardinality() {
            return 0;
        }
        
        public boolean isEmptyHuh() {
            return true;
        }
        
        public boolean member( X thing ) {
            return false;
        }
        
        public AVL_BST<X> add( X thing ) {
//            return new notEmpty<X>(thing);
            return this;
        }
        
        public AVL_BST<X> remove( X thing ) {
            return new Empty();
        }
        
        public AVL_BST<X> union( AVL_BST<X> set ) {
            return set;
        }
        
        public AVL_BST<X> inter( AVL_BST<X> set ) {
            return new Empty();
        }
        
        public AVL_BST<X> diff( AVL_BST<X> set ) {
            return set;
        }
        
        public boolean equal( AVL_BST<X> set ) {
            return set.isEmptyHuh();
        }
        
    }
    
    interface Comparable {
        public boolean leq ( Comparable X, Comparable Y);
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
}
