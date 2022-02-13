import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HelpAction {
	
	
	public static ArrayList<Item> sortBy(String str, ArrayList<Item> item) {
		String str1 = str.substring(0, str.indexOf(':'));
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		for(int i = 0;i < item.size(); i++) {
			ArrayList<String> lst = new ArrayList<String>();
			lst.add(getString(str1, item.get(i)));
			lst.add(""+i);
			list.add(lst);
		}
		String st = str.substring(str.indexOf(':')+2);
		if(st.equals("Low to High"))
			Collections.sort(list, com);
		else
			Collections.sort(list, Collections.reverseOrder(com));
		ArrayList<Item> newItems = new ArrayList<Item>();
		for(int i = 0;i < list.size(); i++) {
			int k = Integer.valueOf(list.get(i).get(1));
			newItems.add(item.get(k));
		}
		return newItems;
	}
	
	public static Comparator<ArrayList<String>> com = new Comparator<ArrayList<String>>(){

		
        public int compare(ArrayList<String> a1, ArrayList<String> a2) {
        	double d1 = Double.valueOf(a1.get(0));
        	double d2 = Double.valueOf(a2.get(0));
        	return Double.compare(d1, d2);
        }

    };
    
	public static String getString(String str, Item item) {
		String str1 = "";
		switch(str) {
		case "Price":
			str1 = item.getNewPrice().toString();
			break;
		case "Quantity":
			str1 = ""+item.getQuantity();
			break;
		}
		return str1;
	}
}
