

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
    
    //sequenced interface
    interface MSequenced<X extends Comparable<X>> {
        public MSequence<X> seq();
    }
    
    //sequence interface, with here notEmpty and next
    interface MSequence<X extends Comparable<X>> {
        public X here();
        public boolean notEmpty();
        public MSequence next();
    }

    //AVL_BST interface
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
        
        //constructor
        public Empty() {}
        
        //here() -> takes no inputs, throws an exception because an empty tree
        //will never have anything here
        public X here() {
            throw new RuntimeException("The set is empty, there is no X here");
        }
        
        //next() -> takes no inputs, returns the empty set as there is no next
        //field to move to in the empty set
        public MSequence next() {
            return this;
        }
        
        //notEmpty() -> takes no inputs, returns false because by definition the
        //set is empty
        public boolean notEmpty() {
            return false;
        }
        
        //seq() -> takes no inputs, returns the empty set as there is no sequence
        //of elements to return in the empty set
        public MSequence seq() {
            return this;
        }
        
        //cardinality() -> takes no inputs, returns zero as the empty set by definition
        //has no elements
        public int cardinality() {
            return 0;
        }
        
        //isEmptyHuh() -> takes no inputs, returns true as the empty set by definition
        //is empty
        public boolean isEmptyHuh() {
            return true;
        }
        
        //member(X thing) -> takes a generic X as input, always returns false because
        //the empty set by definition is empty
        public boolean member( X thing ) {
            return false;
        }
        
        //add(X thing) -> takes a generic X as input, returns a new instance of
        //the notEmpty class with two empty leaves as children
        public AVL_BST add( X thing ) {
            return new notEmpty(thing, new Empty(), new Empty());
        }
        
        //remove(X thing) -> takes a generic X as input, returns a new instance
        //of the Empty class because there is nothing to remove from the empty set
        public AVL_BST remove( X thing ) {
            return new Empty();
        }
        
        //union(AVL_BST set) -> takes an AVL_BST named set as input, returns set
        //as the union between set and the empty set is just the original set
        public AVL_BST union( AVL_BST set ) {
            return set;
        }
        
        //inter(AVL_BST set) -> takes an AVL_BST named set as input, returns a new
        //instance of the empty class as there is no intersection between set
        //and the empty set
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
        Testeez.check_unequal("sets unequal", (l5.diff(l3).isEmptyHuh() && l3.diff(l5).isEmptyHuh()), true);
        Testeez.check("sets equal string", (le.diff(le5).isEmptyHuh() && le5.diff(le).isEmptyHuh()), true);
        Testeez.check_unequal("sets unequal string", (le.diff(lc).isEmptyHuh() && lc.diff(le).isEmptyHuh()), true);
        
        //Property 5: if a given element is a member of a set, when the element is removed
        //the cardinality of the set should be one less than the original cardinality
        int x = randomInt(0,10);
        String y = randomString(0,10);
        int original_card = intTree.cardinality();
        int original_str_card = strTree.cardinality();
        if(intTree.member(x)) {
            Testeez.check("remove cardinality", intTree.remove(x).cardinality(), original_card - 1);
        }
        if(strTree.member(y)) {
            Testeez.check("remove cardinality string", strTree.remove(y).cardinality(), original_str_card - 1);
        }
        if(!intTree.member(x)) {
            Testeez.check_unequal("remove cardinality", intTree.remove(x).cardinality(), original_card - 1);
        }
        if(!strTree.member(y)) {
            Testeez.check_unequal("remove cardinality string", strTree.remove(y).cardinality(), original_str_card - 1);
        }
        
        //Property 6: for all set1, set2, set3: 
        //set1.union(set2).subset(set3) = set1.subset(set3) && set2.subset(set3)
        Testeez.check("union/subset", intTree.union(intTree2).subset(intTree3), 
                (intTree.subset(intTree3) && intTree2.subset(intTree3)));
        Testeez.check("union/subset string", strTree.union(strTree2).subset(strTree3), 
                (strTree.subset(strTree3) && strTree2.subset(strTree3)));
        
        //Property 7: if an element exists in two distinct sets set1 and set2, then
        //set1.inter(set2) should not be empty
        if(intTree.member(x) && intTree2.member(x)) {
            Testeez.check("inter", intTree.inter(intTree2).isEmptyHuh(), true);
        } else {
            Testeez.check_unequal("inter", intTree.inter(intTree2).isEmptyHuh(), true);
        }
        
        //Property 8: if an element occurs in a set more than once, then removing
        //one instance of the element should still allow member to return true
        if(l5.member(5) && l5.remove(5).member(5)) {
            Testeez.check("count > 1", l5.remove(5).member(5), true);
        } else {
            Testeez.check_unequal("count > 1", l5.remove(5).member(5), true);
        }
        if(le.member("abc") && le.remove("abc").member("abc")) {
            Testeez.check("count > 1 string", le.remove("abc").member("abc"), true);
        } else {
            Testeez.check_unequal("count > 1 string", le.remove("abc").member("abc"), true);
        }
        
        //Property 9: if a tree is balanced, the balance factor should be -1, 0, or 1
        int bal_fac = intTree.balance_factor();
        int bal_fac_str = strTree.balance_factor();
        Testeez.check("balance", (bal_fac >= -1 && bal_fac <= 1), true);
        Testeez.check("balance string", (bal_fac_str >= -1 && bal_fac_str <= 1), true);
        
        //Property 10: the MSequence notEmpty should return true on the notEmpty data
        //type, and false for the empty data type
        Testeez.check("notEmpty mt", mt.notEmpty(), false);
        Testeez.check("notEmpty !mt", l3.notEmpty(), true);
        Testeez.check("notEmpty !mt string", lc.notEmpty(), true);
        
    }
}
