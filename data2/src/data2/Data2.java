

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
        public boolean subset ( AVL_BST<X> set );
        public int depth();
        public AVL_BST<X> rebalance();
        public int balance_factor();
//        public int count = 0;
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
        
        public AVL_BST<X> rebalance() {
            return this;
        }
        
        public boolean subset (AVL_BST<X> set) {
            return true;
        }
        
        public int balance_factor() {
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
        
        public void setRighty( AVL_BST<X> in ) {
            this.righty = in;
        }
        
        public void setLefty( AVL_BST<X> in ) {
            this.lefty = in;
        }
        
        public AVL_BST<X> rebalance() {
            if ((this.balance_factor() >= -1) && (this.balance_factor() <= 1)) {
                return this;
            } else if(this.balance_factor() < -1) {
                //left is larger
                if(this.lefty.balance_factor() < 0) {
                    // right rotation
                    AVL_BST<X> temp = this.lefty.righty;
                    this.lefty.setRighty(this);
                    this.lefty.righty.setLefty(temp);
                    return this.rebalance();
                } else if(this.lefty.balance_factor() > 0) {
                    // left rotation
                }
            } else {
                if(this.righty.balance_factor() < 0) {
                    // right rotation
                } else if(this.righty.balance_factor() > 0) {
                    // left rotation
                }
            }
        }
        
        public AVL_BST<X> add ( X thing ) {
            if(thing.lt(here)) {
                return new notEmpty<X> (here, lefty.add(thing), righty).rebalance();
            } else if (thing.equals(here)) {
                return new notEmpty<X> (here, count + 1, lefty, righty).rebalance();
            } else {
                return new notEmpty<X> (here, lefty, righty.add(thing)).rebalance();
            }
        }
        
        public AVL_BST<X> remove ( X thing ) {
            if(thing.equals(here)) {
                return new notEmpty<X>(here, count - 1, lefty, righty).rebalance();
            } else if(thing.lt(here)) {
                return new notEmpty<X>(here, lefty.remove(thing), righty).rebalance();
            } else {
                return new notEmpty<X>(here, lefty, righty.remove(thing)).rebalance();
            }
        }
        
        public AVL_BST<X> union ( AVL_BST<X> set ) {
            return lefty.union(set.union(righty).add(here)).rebalance();
        }
        
        public AVL_BST<X> inter ( AVL_BST<X> set ) {
            if(set.member(here)) {
                return new notEmpty<X>(here, (set.count - count), 
                        lefty.inter(set), righty.inter(set)).rebalance();
            } else {
                return lefty.inter(set).union(righty.inter(set)).rebalance();
            }
        }
        
        public AVL_BST<X> diff ( AVL_BST<X> set ) {
            if(set.member(here)) {
                return lefty.union(righty).diff(set.remove(here)).rebalance();
            } else {
                return new notEmpty<X>(here, lefty.diff(set), righty.diff(set)).rebalance();
            }
        }
        
        public boolean equal ( AVL_BST<X> set ) {
            return (this.subset(set) && set.subset(this));
        }
        
        public boolean subset(AVL_BST<X> set) {
            return (set.member(here) && lefty.subset(set)
                    && righty.subset(set));
        }
        
    }
    
    interface Comparable<X> {
        public boolean leq ( Comparable X );
        public boolean lt ( Comparable X );
    }
    
    public static void main(String[] args) {

    }
}
