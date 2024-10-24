package lab_05;

import java.util.*;

public class CComparator<T> implements Comparator<T> {
    Comparator<? super T> cmp1;
    Comparator<? super T> cmp2;
    public CComparator(Comparator<? super T> c1,
    Comparator<? super T> c2) {
    cmp1 = c1; cmp2 = c2;
    }
    
    public int compare(T o1, T o2) {
        int x = cmp1.compare(o1, o2);
        if (x!=0) return x;
        else return cmp2.compare(o1,o2);
    }

    public CComparator<T> then(Comparator<? super T> c){
        return new CComparator<>(this, c);
        }
        
}