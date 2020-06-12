import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class algorithms {
	/**
	 * Check if a relation is in the third normal form (3NF)
	 * @param attrs the set of attributes in a relation
	 * @param fds the set of FD's on this relation
	 * @return {@code true} if this relation is in 3NF
	 */
	public static Set<FD> check3NF(Set<attribute> attrs, Set<FD> fds){
		Set<Set<attribute>> keys = keys(attrs, fds);
		Set<attribute> primes = new HashSet<>();
		for(Set<attribute> k : keys){
			primes.addAll(k);
		}
		
		Set<FD> violating = new HashSet<>();
		for(FD fd : fds){
			if(!primes.containsAll(fd.getRight())){
				boolean contains = false;
				for(Set<attribute> k : keys){
					if(fd.getLeft().containsAll(k)){
						contains = true;
						break;
					}
				}
				if(!contains){
					violating.add(fd);
				}
			}
			
		}
		return violating;
	}
	
	/**
	 * Check if a relation is in Boyce-Codd normal form (BCNF)
	 * @param attrs the set of attributes in a relation
	 * @param fds the set of FD's on this relation
	 * @return {@code true} if this relation is in BCNF
	 */
	public static Set<FD> checkBCNF(Set<attribute> attrs, Set<FD> fds){
		Set<Set<attribute>> keys = keys(attrs, fds);
		Set<FD> violating = new HashSet<>();
		for(FD fd : fds){
			boolean contains = false;
			for(Set<attribute> k : keys){
				if(fd.getLeft().containsAll(k)){
					contains = true;
					break;
				}
			}
			if(!contains){
				violating.add(fd);
			}
		}
		return violating;
	}
	
	/**
	 * Check if a certain decomposition of a relation will cause FD loss
	 * @param attrs attributes in the original relation
	 * @param fds all the FD's in the original relation
	 * @param subattrs each {@code Set<Attribute>} represents a decomposed relation from the original relation
	 * @return the set of FD's that will be lost if this decomposition is performed
	 */
	public static Set<FD> checkLossyDecomposition(Set<Attribute> attrs, Set<FuncDep> fds, Set<Set<Attribute>> subattrs){
		Set<FD> lost = new HashSet<>();
		Set<FD> decomposed = new HashSet<>();
		for(Set<attribute> sa : subattrs){
			decomposed.addAll(projection(sa, fds));
		}
		for(FD fd : fds){
			Set<attribute> left = fd.getLeft();
			Set<attribute> closure = closure(left, decomposed);
			if(!closure.containsAll(fd.getRight())){
				lost.add(fd);
			}
		}
		return lost;
	}
	
	/**
	 * Compute the closure of a set of attributes under a set of FD's
	 * @param attrs a set of attributes
	 * @param fds a set of FD's
	 * @return a set of attributes, i.e. the closure
	 */
	public static Set<attribute> closure(Set<attribute> attrs, Set<FD> fds){
		Set<attribute> result = new HashSet<>(attrs);
		
		boolean found = true;
		while(found){
			found = false;
			for(FD fd : fds){
				if(result.containsAll(fd.left) && !result.containsAll(fd.right)){
					result.addAll(fd.right);
					found = true;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * In-place combine the right side of the FD's that have the same left side
	 * @param fds a set of FD's
	 */
	public static void combineRight(Set<FD> fds){
		Map<Set<attribute>, Set<attribute>> map = new HashMap<>();
		for(FD fd : fds){
			//Set<Attribute> left = fd.getLeft();
			if(map.containsKey(fd.left)){
				map.get(fd.left).addAll(fd.right);
			}
			else{
				map.put(fd.left, fd.getRight());
			}
		}
		fds.clear();
		for(Set<attribute> left : map.keySet()){
			fds.add(new FuncDep.Builder().left(left).right(map.get(left)).build());
		}
		
	}
	
	/**
	 * Check if two sets of FD's are equivalent to each other (i.e. each can be deduced from another)
	 * @param a a set of FD's
	 * @param b another set of FD's
	 * @return {@code true} if they are equivalent
	 */
	public static boolean equivalent(Set<FD> a, Set<FD> b){
		Set<attribute> names = new HashSet<>();
		for(FD fd : a){
			names.addAll(fd.getLeft());
			names.addAll(fd.getRight());
		}
		for(FD fd : b){
			names.addAll(fd.getLeft());
			names.addAll(fd.getRight());
		}
		
		Set<Set<attribute>> powerset = reducedPowerSet(names);
		for(Set<attribute> sa : powerset){
			Set<attribute> closureInA = closure(sa, a);
			Set<attribute> closureInB = closure(sa, b);
			if(!closureInA.equals(closureInB)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Compute all the candidate keys
	 * @param attrs a set of attributes
	 * @param fds a set of FD's
	 * @return a set of candidate keys, and each "key" is itself a set of attributes
	 */
	public static Set<Set<attribute>> keys(Set<attribute> attrs, Set<FD> fds){
		Set<Set<attribute>> superkeys = superKeys(attrs, fds);
		Set<Set<attribute>> toRemove = new HashSet<>();
		for(Set<attribute> key : superkeys){
			for(attribute a : key){
				Set<attribute> remaining = new HashSet<>(key);
				remaining.remove(a);
				if(superkeys.contains(remaining)){
					toRemove.add(key);
					break;
				}
			}
		}
		superkeys.removeAll(toRemove);
		return superkeys;
	}
	
	/**
	 * Compute the minimal basis (aka. minimal cover). Notice this method only returns ONE of the possible solutions,
	 * depending on the order in which the attributes and FD's are stored in the memory.
	 * @param fds a set of FD's
	 * @return a set of FD's as the minimal basis
	 */
	public static Set<FD> minimalBasis(Set<FD> fds){
		
		Set<FD> result = new HashSet<>(fds);
		
		//Step 1: split right sides
		splitRight(result);
		
		//Step 2: remove trivial FDs
		removeTrivial(result);
		
		//Step 3: remove unnecessary FD's and left side attributes
		int count = 1;
		while(count > 0){
			count = removeUnnecessaryLeftSide(result) + removeUnnecessaryEntireFD(result);
		}
		
		return result;
	}
	
	/**
	 * Generate the power set, with the empty set.
	 * @param originalSet the original set
	 * @param <T> any class
     * @return the power set
	 */
	public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
	    Set<Set<T>> sets = new HashSet<>();
	    if (originalSet.isEmpty()) {
	    	sets.add(new HashSet<T>());
	    	return sets;
	    }
	    List<T> list = new ArrayList<>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<>(list.subList(1, list.size()));
	    for (Set<T> set : powerSet(rest)) {
	    	Set<T> newSet = new HashSet<>();
	    	newSet.add(head);
	    	newSet.addAll(set);
	    	sets.add(newSet);
	    	sets.add(set);
	    }
	    //sets.remove(new HashSet<T>());
	    return sets;
	}
	
	/**
	 * Project a set of FD's on a subset of attributes
	 * @param attrs a set of attributes
	 * @param fds a set of FD's
	 * @return a set of FD's as the projection
	 */
	public static Set<FD> projection(Set<attribute> attrs, Set<FD> fds){
		Set<attribute> appeared = new HashSet<>();
		for(FD fd : fds){
			appeared.addAll(fd.getLeft());
			appeared.addAll(fd.getRight());
		}
		if(attrs.containsAll(appeared)){
			return new HashSet<>(fds);
		}
		Set<attribute> notin = new HashSet<>(appeared);
		notin.removeAll(attrs);
		Set<Set<attribute>> powerset = reducedPowerSet(attrs);
		Set<FD> result = new HashSet<>();
		for(Set<attribute> sa : powerset){
			Set<attribute> closure = closure(sa, fds);
			closure.removeAll(notin);
			//closure.removeAll(sa);
			result.add(new FD.Builder().left(sa).right(closure).build());
		}
		//return result;
		return minimalBasis(result);
	}
	
	
	/**
	 * Generate the power set (i.e. set of all the subsets)  of a generic set, but without the empty set.
	 * @param originalSet the original set
	 * @param <T> any class
     * @return the reduced power set
	 */
	public static <T> Set<Set<T>> reducedPowerSet(Set<T> originalSet){
		Set<Set<T>> result = powerSet(originalSet);
		result.remove(new HashSet<T>());
		return result;
	}
	
	/**
	 * Remove all trivial FD's from a set of FD's
	 * @param fds a set of FD's
	 */
	public static void removeTrivial(Set<FD> fds){
		Set<FD> toRemove = new HashSet<>();
		Set<FD> toAdd = new HashSet<>();
		for(FD fd : fds){
			if(fd.left.containsAll(fd.right)){
				toRemove.add(fd);
			}
			else{
				Set<attribute> toRemoveFromRight = new HashSet<>();
				for(attribute a : fd.right){
					if(fd.left.contains(a)){
						toRemoveFromRight.add(a);
					}
				}
				if(!toRemoveFromRight.isEmpty()){
					Set<attribute> right = fd.getRight();
					right.removeAll(toRemoveFromRight);
					toRemove.add(fd);
					toAdd.add(new FD.Builder().left(fd.left).right(right).build());
				}
			}
		}
		fds.addAll(toAdd);
		fds.removeAll(toRemove);
	}
	
	/**
	 * In-place remove all the unnecessary FD's, and the result is still equivalent to the original set of FD's
	 * @param fds a set of FD's
	 * @return the total number of FD's removed
	 */
	public static int removeUnnecessaryEntireFD(Set<FD> fds){
		int count = 0;
		while(true){
			FD toRemove = null;
			boolean found = false;
			for(FD fd : fds){
				Set<FD> remaining = new HashSet<>(fds);
				remaining.remove(fd);
				if(equivalent(remaining, fds)){
					++count;
					found = true;
					toRemove = fd;
					break;
				}
			}
			if(!found){
				break;
			}
			else{
				fds.remove(toRemove);
			}
		}
		
		return count;
	}
	
	/**
	 * In-place remove all the unnecessary attributes on the left side of each FD, 
	 * and the result is still equivalent to the original set of FD's  
	 * @param fds a set of FD's 
	 * @return the total number of attributes removed 
	 */
	public static int removeUnnecessaryLeftSide(Set<FuncDep> fds){
		int count = 0;
		while(true){
			boolean found = false;
			FD toRemove = null;
			FD toAdd = null;
			int loop = 0;
			for(FD fd : fds){
				Set<attribute> left = fd.getLeft();
				Set<attribute> right = fd.getRight();
				if(left.size() > 1){
					for (attribute a : left) {
						Set<attribute> remaining = new HashSet<>(left);
						remaining.remove(a);
						Set<FD> alternative = new HashSet<>(fds);
						alternative.remove(fd);
						toAdd = new FD.Builder().left(remaining).right(right).build();
						alternative.add(toAdd);
						if (equivalent(alternative, fds)) {
							found = true;
							toRemove = fd;
							++count;
							break;
						}
					}
				}
				
				if(found){
					break;
				}
				++loop;
			}
			if(found){
				fds.remove(toRemove);
				fds.add(toAdd);
			}
			if(loop == fds.size()){
				break;
			}
		}
		return count;
	}
	
	/**
	 * In-place split the right side of each FD in this set
	 * @param fds a set of FD's
	 */
	public static void splitRight(Set<FD> fds){
		Set<FD> toRemove = new HashSet<>();
		Set<FD> toAdd = new HashSet<>();
		for(FD fd : fds){
			//Set<Attribute> right = fd.getRight();
			if(fd.right.size() > 1){
				//Set<Attribute> left = fd.getLeft();
				for(attribute a : fd.right){
					toAdd.add(new FD.Builder().left(fd.left).right(a).build());
				}
				toRemove.add(fd);
			}
		}
		fds.addAll(toAdd);
		fds.removeAll(toRemove);
	}
	
	/**
	 * Compute all the superkeys (including candidate keys)
	 * @param attrs a set of attributes 
	 * @param fds a set of FD's
	 * @return a set of superkeys, and each "superkey" is itself a set of attributes 
	 */
	public static Set<Set<attribute>> superKeys(Set<attribute> attrs, Set<FD> fds){
		Set<Set<attribute>> keys = new HashSet<>();
		if(attrs.isEmpty()){
			for(FD fd : fds){
				attrs.addAll(fd.left);
				attrs.addAll(fd.right);
			}
		}
		Set<Set<attribute>> powerset = reducedPowerSet(attrs);
		for(Set<attribute> sa : powerset){
			if(closure(sa, fds).equals(attrs)){
				keys.add(sa);
			}
		}
		return keys;
	}
	
	private algorithms(){
		
	}
	
	
	
}
