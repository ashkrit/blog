
import java.util.ArrayList;

public class SearchTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {

		Object l = ObjectFactory.build(args[0],"http://www.umich.edu/~umfandsf/other/ebooks/alice30.txt");

		int itr = 1000;
		String valuesToSearch = "does not exists";

		if (args[0].equals("AL")) {
			ArrayList<String> list = (ArrayList<String>) l;
			for (int i = 0; i < itr; i++) {
				long start = System.nanoTime();
				boolean f = list.contains(valuesToSearch);
				long total = System.nanoTime() - start;
				System.out.println(String.format("Type %s time taken %s(ns), result %s", 
						args[0],total , f)) ;
			}
		} else if (args[0].equals("CL")) {
			CompactStringList list = (CompactStringList) l;
			for (int i = 0; i < itr; i++) {
				long start = System.nanoTime();
				boolean f = list.contains(valuesToSearch);
				long total = System.nanoTime() - start;
				System.out.println(String.format("Type %s time taken %s(ns), result %s", 
						args[0],total , f));
			}
		}

	}

}
