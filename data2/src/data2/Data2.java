

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

    interface AVL_BST<X extends Comparable<X>> extends MSequence<X>, MSequenced<X> {
        public int cardinality();
        public boolean isEmptyHuh();
        public boolean member( X thing );
        public AVL_BST<X> add( X thing );
        public AVL_BST<X> remove( X thing );
        public AVL_BST<X> union( AVL_BST<X> set );
        public AVL_BST<X> inter( AVL_BST<X> set );
        public AVL_BST<X> diff( AVL_BST<X> set );
        public boolean equal( AVL_BST<X> set );
        public int depth();
    }
    
    public class Empty<X extends Comparable<X>> implements AVL_BST<X> {
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
        
        public boolean leq ( Comparable X, Comparable Y) {
            return false;
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
        
        public int depth() {
            return 0;
        }
        
    }
    
    public class notEmpty<X extends Comparable<X>> implements AVL_BST<X> {
        
        X here;
        int count;
        AVL_BST<X> lefty;
        AVL_BST<X> righty;
        
        public notEmpty( X here, AVL_BST<X> lefty, AVL_BST<X> righty ) {
            this.here = here;
            this.count = 1;
            this.lefty = lefty;
            this.righty = righty;
        }
        
        public notEmpty( X here, int count, AVL_BST<X> lefty, AVL_BST<X> righty ) {
            this.here = here;
            this.count = count;
            this.lefty = lefty;
            this.righty = righty;
        }
        
        public MSequence<X> next() {
            return this;
        }
        
        public X here() {
            return here;
        }
        
        public boolean notEmpty() {
            return true;
        }
        
        public MSequence<X> seq() {
            return this;
        }
        
        public int cardinality() {
            return 1 + lefty.cardinality() + righty.cardinality();
        }
        
        public boolean isEmptyHuh() {
            return false;
        }
        
        public boolean member( X thing ) {
            return (here.equals(thing) || lefty.member(thing)
                    || righty.member(thing));
        }
        
        public int depth() {
            return 1 + Math.max(lefty.depth(), righty.depth());
        }
        
        public int balance_factor() {
            return this.righty.depth() - this.lefty.depth();
        }
        
        public AVL_BST<X> rebalance() {
            return this;
        }
        
        public AVL_BST<X> add ( X thing ) {
            if(thing.leq(here)) {
                return new notEmpty<X> (here, lefty.add(thing), righty);
            }
        }
        
    }
    
    interface Comparable<X> {
        public boolean leq ( Comparable X );
    }
    
    public static void main(String[] args) {

    }
}
