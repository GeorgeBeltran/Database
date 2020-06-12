
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FD {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		ArrayList<String> U = getU(sc);
		ArrayList<ArrayList<String>> F = getF(sc);
		System.out.println("U " + U);
		System.out.println("F " + F);
		
		System.out.println("question 1");
		ArrayList<Character> key = getCanKey(U, F);
		System.out.println("Candidate Key " + key);

		System.out.println("question 2");
		ArrayList<Character> x = enterX(sc);
		ArrayList<Character> close = getClosure(x, F);
		System.out.println("Closure of X " + close);

		System.out.println("question 3");
		ArrayList<ArrayList<String>> minCov = minimalCover(F);
		System.out.println("Minimum cover of F " + minCov);
		sc.close();
	}

	public static ArrayList<String> getU(Scanner input) { //U set
		System.out.println("Enter U");
		String sc = input.nextLine();
		sc = sc.toUpperCase();

		ArrayList<String> fList = new ArrayList<String>();
		for (int i = 0; i < sc.length(); i++) {
			fList.add(sc.charAt(i) + "");
		}
		return fList;
	}

	public static ArrayList<ArrayList<String>> getF(Scanner input) { //F set

		ArrayList<ArrayList<String>> fList = new ArrayList<ArrayList<String>>();

		String left = "";
		String right = "";
		int index = 0;

		System.out.println("enter F");
		while (true) {
		
			left = input.next();
			right = input.next();
			left = left.toUpperCase();
			right = right.toUpperCase();

			if (left.equals("/") || right.equals("/")) {
				
				break;
			}

			fList.add(new ArrayList<String>());

			fList.get(index).add(left);

			for (int i = 0; i < right.length(); i++) {
				fList.get(index).add(right.charAt(i) + "");
			}
			index++;
		}
		return fList;
	}

	public static ArrayList<Character> enterX(Scanner input) {

		System.out.println("Enter X set");
		input.nextLine();
		String sc = input.nextLine();
		sc = sc.toUpperCase();

		ArrayList<Character> fList = new ArrayList<Character>();

		for (int i = 0; i < sc.length(); i++) {
			fList.add(sc.charAt(i));
		}
		return fList;
	}

	public static ArrayList<Character> getCanKey(ArrayList<String> u, ArrayList<ArrayList<String>> f) {

		ArrayList<Character> lhand = getLeftHand(f);
		ArrayList<Character> rhand = getRightHand(f);
		ArrayList<Character> key = removeSets(lhand, rhand);

		System.out.println("Possible key: " + key);

		ArrayList<Character> k = getClosure(key, f);

		if (!checkIfEqual(k, u)) {
			System.out.println("NOT VALID!");
			key = addLetters(key, u, f);
			Collections.sort(key);
		}
		return key;
	}

	public static ArrayList<Character> getLeftHand(ArrayList<ArrayList<String>> f) {
		
		ArrayList<Character> fList = new ArrayList<Character>();
		String leftHand = "";

		for (int a = 0; a < f.size(); a++) {
			leftHand = f.get(a).get(0);
			for (int b = 0; b < leftHand.length(); b++) {
				if (!fList.contains(leftHand.charAt(b))) {
					fList.add(leftHand.charAt(b));
				}
			}
		}
		Collections.sort(fList);
		return fList;
	}

	public static ArrayList<Character> getRightHand(ArrayList<ArrayList<String>> f) {
		ArrayList<Character> fList = new ArrayList<Character>();
		String rightHand = "";
		for (int a = 0; a < f.size(); a++) {
			for (int b = 0; b < f.get(a).size(); b++) {
				if (b > 0) {
					rightHand = f.get(a).get(b);
					for (int c = 0; c < rightHand.length(); c++) {
						if (!fList.contains(rightHand.charAt(c))) {
							fList.add(rightHand.charAt(c));
						}
					}
				}
			}
		}
		Collections.sort(fList);
		return fList;
	}

	public static ArrayList<Character> removeSets(ArrayList<Character> lhs, ArrayList<Character> rhs) {
		ArrayList<Character> fList = new ArrayList<Character>();
		for (Character var : lhs) {
			if (!rhs.contains(var)) {
				fList.add(var);
			}
		}
		Collections.sort(fList);
		return fList;
	}


	public static ArrayList<Character> getClosure(ArrayList<Character> set, ArrayList<ArrayList<String>> f) {
		ArrayList<Character> closure = (ArrayList<Character>) set.clone();

		int size = 0;
		do {
			size = closure.size();

			for (int i = 0; i < closure.size(); i++) {
				for (int j = 0; j < f.size(); j++) {
					if ((closure.get(i) + "").equals(f.get(j).get(0))) {
						for (int x = 1; x < f.get(j).size(); x++) {
							if (!closure.contains(f.get(j).get(x).charAt(0))) {
								closure.add(f.get(j).get(x).charAt(0));
							}
						}
					}
				}
			}

			for (int z = 0; z < f.size(); z++) {
				if (enterClosure(f, closure, z)) {
					for (int y = 1; y < f.get(z).size(); y++) {
						if (!closure.contains(f.get(z).get(y).charAt(0))) {
							closure.add(f.get(z).get(y).charAt(0));
						}
					}
				}
			}
		} 
		while (size != closure.size());
		Collections.sort(closure);
		return closure;
	}

	public static boolean enterClosure(ArrayList<ArrayList<String>> f, ArrayList<Character> set, int index) {
		boolean closure = false;
		for (int j = 0; j < f.get(index).get(0).length(); j++) {
			if (set.contains(f.get(index).get(0).charAt(j))) {
				closure = true;
			} else {
				return false;
			}
		}
		return closure;
	}


	public static ArrayList<Character> addLetters(ArrayList<Character> set, ArrayList<String> u,
			ArrayList<ArrayList<String>> f) {
		ArrayList<Character> newSet = (ArrayList<Character>) set.clone();

		for (int i = 0; i < u.size(); i++) {
			for (int j = 0; j < u.size(); j++) {
				if (!u.get(i).equals(u.get(j))) {
					newSet.add(u.get(i).charAt(0));
					newSet.add(u.get(j).charAt(0));
				} else {
					newSet.add(u.get(i).charAt(0));
				}
				if (checkIfEqual(getClosure(newSet, f), u)) {
					return newSet;
				}
				newSet = (ArrayList<Character>) set.clone();
			}
		}
		return set;
	}

	public static ArrayList<ArrayList<String>> minimalCover(ArrayList<ArrayList<String>> f) {
		ArrayList<ArrayList<String>> fList = new ArrayList<ArrayList<String>>();

		firstStep(f, fList);

		secondStep(fList);

		thirdStep(fList);
		
		return fList;
	}

	public static void firstStep(ArrayList<ArrayList<String>> f, ArrayList<ArrayList<String>> fList) {
		int index = 0;
		int add = 0;
		for (int i = 0; i < f.size(); i++) {
			if (f.get(i).size() > 2) {
				index = i + add;
				for (int j = 1; j < f.get(i).size(); j++) {
					for (int v = 0; v <= add; v++) {
						fList.add(new ArrayList<String>());
					}
					fList.get(index).add(f.get(i).get(0));
					fList.get(index).add(f.get(i).get(i));
					add++;
					index++;
				}
			} else {
				fList.add(f.get(i));
			}
		}

		for (int a = 0; a <= add; a++) {
			removeExtra(fList);
		}
	}

	public static void removeExtra(ArrayList<ArrayList<String>> f) {
		for (int i = 0; i < f.size(); i++) {
			if (f.get(i).isEmpty()) {
				f.remove(i);
			}
		}
	}

	public static void secondStep(ArrayList<ArrayList<String>> f) {
		String sc = "";
		ArrayList<Character> currentRight;

		for (int x = 0; x < f.size(); x++) {
			if (f.get(x).get(0).length() > 1) {
				sc = f.get(x).get(0);
				currentRight = stringList(sc);
				for (int y = 0; y < sc.length(); y++) {
					currentRight.remove(y);
					if (getClosure(currentRight, f).contains(sc.charAt(y))) {
						f.get(x).set(0, listString(currentRight));
					}
					currentRight = stringList(sc);
				}
			}
		}
	}

	public static void thirdStep(ArrayList<ArrayList<String>> f) {
		int size = 0;
		ArrayList<ArrayList<String>> fCopy;
		ArrayList<Character> check;
		do {
			fCopy = copyF(f);
			char right;
			size = fCopy.size();
			for (int a = 0; a < fCopy.size(); a++) {
				check = stringList(fCopy.get(a).get(0));
				right = fCopy.get(a).get(1).charAt(0);
				fCopy.remove(a);

				if (getClosure(check, fCopy).contains(right)) {
					f.remove(a);
				}
				fCopy.clear();
				fCopy = copyF(f);
			}
		} 
		while (size != fCopy.size());
	}

	public static ArrayList<ArrayList<String>> copyF(ArrayList<ArrayList<String>> f) {
		ArrayList<ArrayList<String>> newF = new ArrayList<ArrayList<String>>();
		for (int a = 0; a < f.size(); a++) {
			newF.add(new ArrayList<String>());
			for (int b = 0; b < f.get(a).size(); b++) {
				newF.get(a).add(f.get(a).get(b));
			}
		}
		return newF;
	}

	public static String listString(ArrayList<Character> list) {
		String string = "";
		for (int i = 0; i < list.size(); i++) {
			string += list.get(i);
		}
		return string;
	}

	public static ArrayList<Character> stringList(String list) {
		ArrayList<Character> fList = new ArrayList<Character>();

		for (int x = 0; x < list.length(); x++) {
			fList.add(list.charAt(x));
		}
		return fList;
	}

	public static boolean checkIfEqual(ArrayList<Character> set, ArrayList<String> u) {
		boolean checkEqual = false;
		for (int i = 0; i < u.size(); i++) {
			if (set.contains(u.get(i).charAt(0))) {
				checkEqual = true;
			} else {
				return false;
			}
		}
		return checkEqual;
	}
	
	public static void printF(ArrayList<ArrayList<String>> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).size(); j++) {
				if (j == 0) {
					System.out.print(list.get(i).get(j) + "->");
				} else {
					if (j + 1 == list.get(i).size()) {
						System.out.print(list.get(i).get(j));

					} else {
						System.out.print(list.get(i).get(j) + ",");
					}
				}

			}
			System.out.println();
		}
	}
}
