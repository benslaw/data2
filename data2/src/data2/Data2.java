

package data2;

import java.util.*;

class Testeez {

    //thanks Jay!
    public static void check(String label, Object x, Object y) throws Exception {
        if (x != y) {
            throw new Exception("\n" + label + ": " + x + " should equal " + y + " but it don't :(");
        }
    }
    
    public static void check_unequal(String label, Object x, Object y) throws Exception {
        if (x == y) {
            throw new Exception("\n" + label + ": " + x + " should not equal " + y + " but it does");
        }
    }
    
    public static void check_ints(String label, int x, int y) throws Exception {
        if (x != y) {
            throw new Exception("\n" + label + ": " + x + " should equal " + y + " but it don't :(");
        }
    }
    
    public static void check_unequalInt(String label, int x, int y) throws Exception {
        if (x == y) {
            throw new Exception("\n" + label + ": " + x + " should not equal " + y + " but it does");
        }
    }
}

public class Data2 {
    
    interface MSequenced<X extends Comparable<X>> {
        public MSequence<X> seq();
    }
    
    interface MSequence<X extends Comparable<X>> {
        public X here();
        public boolean notEmpty();
        public MSequence<X> next();
    }

    interface AVL_BST<X extends Comparable<X>> extends MSequence<X>, MSequenced<X> {
        public int cardinality();
        public boolean isEmptyHuh();
        public boolean member( X thing );
        public AVL_BST add( X thing );
        public AVL_BST remove( X thing );
        public AVL_BST union( AVL_BST set );
        public AVL_BST inter( AVL_BST set );
        public AVL_BST diff( AVL_BST set );
        public boolean equal( AVL_BST set );
        public boolean subset ( AVL_BST set );
        public int depth();
        public AVL_BST rebalance();
        public int balance_factor();
    }
    
    public static class Empty<X extends Comparable<X>> implements AVL_BST<X> {
        
        public Empty() {}
        
        public X here() {
            throw new RuntimeException("The set is empty, there is no X here");
        }
        
        public MSequence next() {
            return this;
        }
        
        public boolean notEmpty() {
            return false;
        }
        
        public MSequence seq() {
            return this;
        }
        
//        public boolean leq ( Comparable X, Comparable Y) {
//            return false;
//        }
        
        public int cardinality() {
            return 0;
        }
        
        public boolean isEmptyHuh() {
            return true;
        }
        
        public boolean member( X thing ) {
            return false;
        }
        
        public AVL_BST add( X thing ) {
            return new notEmpty(thing, new Empty(), new Empty());
        }
        
        public AVL_BST remove( X thing ) {
            return new Empty();
        }
        
        public AVL_BST union( AVL_BST set ) {
            return set;
        }
        
        public AVL_BST inter( AVL_BST set ) {
            return new Empty();
        }
        
        public AVL_BST diff( AVL_BST set ) {
            return set;
        }
        
        public boolean equal( AVL_BST set ) {
            return set.isEmptyHuh();
        }
        
        public int depth() {
            return 0;
        }
        
        public AVL_BST rebalance() {
            return this;
        }
        
        public boolean subset (AVL_BST set) {
            return true;
        }
        
        public int balance_factor() {
            return 0;
        }
        
    }
    
    public static class notEmpty<X extends Comparable<X>> implements AVL_BST<X> {
        
        X here;
        int count;
        AVL_BST lefty;
        AVL_BST righty;
        
        public notEmpty( X here, AVL_BST lefty, AVL_BST righty ) {
            this.here = here;
            this.count = 1;
            this.lefty = lefty;
            this.righty = righty;
        }
        
        public notEmpty( X here, int count, AVL_BST lefty, AVL_BST righty ) {
            this.here = here;
            this.count = count;
            this.lefty = lefty;
            this.righty = righty;
        }
        
        public MSequence next() {
            return lefty.union(righty);
        }
        
        public X here() {
            return here;
        }
        
        public boolean notEmpty() {
            return true;
        }
        
        public MSequence seq() {
            return this;
        }
        
        public int cardinality() {
            return count + lefty.cardinality() + righty.cardinality();
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
        
        public void setRighty( AVL_BST in ) {
            this.righty = in;
        }
        
        public void setLefty( AVL_BST in ) {
            this.lefty = in;
        }
        
        public AVL_BST rebalance() {
            if ((this.balance_factor() >= -1) && (this.balance_factor() <= 1)) {
                return this;
            } else if(this.balance_factor() < -1) {
                //left is larger
                if (lefty.balance_factor() < 0) {
                        // right rotation
                        AVL_BST temp = ((notEmpty) lefty).righty;
                        ((notEmpty) lefty).setRighty(this);
                        ((notEmpty) ((notEmpty) lefty).righty).setLefty(temp);
                        return this.rebalance();
                } else /* if(this.lefty.balance_factor() > 0)*/ {
                    // left rotation
//                    lefty = lefty.rebalance();
                    return new notEmpty(here, lefty.rebalance(), righty).rebalance();
                }
            } else {
                if(righty.balance_factor() < 0) {
                    // right rotation
//                    righty = righty.rebalance();
                    return new notEmpty(here, lefty, righty.rebalance()).rebalance();
                } else /* if (righty.balance_factor() > 0)*/ {
                    // left rotation
                    AVL_BST temp = ((notEmpty) righty).lefty;
                    ((notEmpty) righty).setLefty(this);
                    ((notEmpty) ((notEmpty) righty).lefty).setRighty(temp);
                    return this.rebalance();
                }
            }
        }
        
        public AVL_BST add ( X thing ) {
            if(thing.compareTo(here) < 0) {
                return new notEmpty (here, lefty.add(thing), righty).rebalance();
            } else if (thing.compareTo(here) == 0) {
                return new notEmpty (here, count + 1, lefty, righty).rebalance();
            } else {
                return new notEmpty (here, lefty, righty.add(thing)).rebalance();
            }
        }
        
        public AVL_BST remove ( X thing ) {
            if(thing.compareTo(here) == 0) {
                return new notEmpty(here, count - 1, lefty, righty).rebalance();
            } else if(thing.compareTo(here) < 0) {
                return new notEmpty(here, lefty.remove(thing), righty).rebalance();
            } else {
                return new notEmpty(here, lefty, righty.remove(thing)).rebalance();
            }
        }
        
        public AVL_BST union ( AVL_BST set ) {
            return lefty.union(set.union(righty).add(here)).rebalance();
        }
        
        public AVL_BST inter ( AVL_BST set ) {
            if(set.member(here)) {
                //if here is a member of set, it must be non-empty and therefore
                ///can be cast as a notEmpty<X>
                return new notEmpty(here, (((notEmpty) set).count - count), 
                        lefty.inter(set), righty.inter(set)).rebalance();
            } else {
                return lefty.inter(set).union(righty.inter(set)).rebalance();
            }
        }
        
        public AVL_BST diff ( AVL_BST set ) {
            if(set.member(here)) {
                return lefty.union(righty).diff(set.remove(here)).rebalance();
            } else {
                return new notEmpty(here, lefty.diff(set), righty.diff(set)).rebalance();
            }
        }
        
        public boolean equal( AVL_BST set ) {
            return (this.subset(set) && set.subset(this));
        }
        
        public boolean subset(AVL_BST set) {
            return (set.member(here) && lefty.subset(set)
                    && righty.subset(set));
        }
        
    }
    
    public static Random rand = new Random();

    public static int randomInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public static AVL_BST randomIntTree(AVL_BST temp, int lengthy) {
        if (lengthy == 0) {
            return temp;
        } else {
            int inty = randomInt(0, 100);
            return randomIntTree(temp.add(inty), lengthy - 1);
        }
    }

    public static String randomString(int min, int max) {
        StringBuffer temp = new StringBuffer();
        int lengthy = randomInt(min, max);
        for (int i = 0; i < lengthy; i++) {
            temp.append(Character.toChars(randomInt(65, 90)));
        }
        return temp.toString();
    }

    public static AVL_BST randomStrTree(AVL_BST temp, int lengthy) {
        if (lengthy == 0) {
            return temp;
        } else {
            String stry = randomString(0, 100);
            return randomIntTree(temp.add(stry), lengthy - 1);
        }
    }
    
    public static void main(String[] args) throws Exception {
        
        Empty mt = new Empty();
        AVL_BST l1 =  mt.add(1);
        AVL_BST l2 = l1.add(5);
        AVL_BST l3 = l2.add(2);
        AVL_BST l4 = l3.add(8);
        AVL_BST l5 = l4.add(5);
        AVL_BST l1a =  mt.add(5);
        AVL_BST l2b = l1a.add(5);
        AVL_BST l3c = l2b.add(1);
        AVL_BST l4d = l3c.add(2);
        AVL_BST l5e = l4d.add(8);
        AVL_BST la =  mt.add("abc");
        AVL_BST lb = la.add("deoiref");
        AVL_BST lc = lb.add("gehi");
        AVL_BST ld = lc.add("byalre");
        AVL_BST le = ld.add("abc");
        AVL_BST la1 =  mt.add("deoiref");
        AVL_BST lb2 = la1.add("gehi");
        AVL_BST lc3 = lb2.add("abc");
        AVL_BST ld4 = lc3.add("abc");
        AVL_BST le5 = ld4.add("byalre");
        AVL_BST intTree = randomIntTree(mt, 10);
        AVL_BST intTree2 = randomIntTree(mt, 10);
        AVL_BST intTree3 = randomIntTree(mt, 15);
        AVL_BST intTree4 = randomIntTree(mt, 20);
        AVL_BST strTree = randomStrTree(mt, 10);
        AVL_BST strTree2 = randomStrTree(mt, 10);
        AVL_BST strTree3 = randomStrTree(mt, 15);
        AVL_BST strTree4 = randomStrTree(mt, 20);
        
        //Property 1: two trees containing the same number of elements should have
        //the same depth if they're both balanced
        Testeez.check_ints("depth is equal", intTree.depth(), intTree2.depth());
        Testeez.check_unequalInt("depth is unequal", intTree.depth(), intTree3.depth());
        Testeez.check_ints("depth is equal", strTree.depth(), strTree2.depth());
        Testeez.check_unequalInt("depth is unequal", strTree.depth(), strTree3.depth());
        
        //Property 2: if there are multiple occurrences of an element in a tree, and
        //one is removed, the cardinality should be one less but member should still
        //return true for that element
        Testeez.check_ints("remove and cardinality", l5.cardinality() - 1, l5.remove(5).cardinality());
        Testeez.check("remove and member", l5.remove(5).member(5), true);
        Testeez.check_ints("remove and cardinality", le.remove("abc").cardinality(), le.cardinality() - 1);
        Testeez.check("remove and member", le.remove("abc").member("abc"), true);
        
        //Property 3: the difference between two sets that are equal will be empty
        Testeez.check("empty difference", intTree.diff(intTree), mt);
        Testeez.check_unequal("unempty difference", intTree2.diff(intTree), mt);
        Testeez.check("empty difference", strTree.diff(strTree), mt);
        Testeez.check_unequal("unempty difference", strTree2.diff(strTree), mt);
        
        //Property 4: if the difference between two sets 1 and 2 is null in both 
        //directions, the two sets will be equal
        Testeez.check("sets equal", (l5.diff(l5e).isEmptyHuh() && l5e.diff(l5).isEmptyHuh()), true);
        
    }
}
