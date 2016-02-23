import static org.junit.Assert.*;
import java.util.ArrayList;
import java.io.IOException;


import org.junit.Test;


public class TextBuddyCE2Test {
	private static final String MESSAGE_CHECK= 
			"\"Test\" has been found in index 1, 2, 3, 4 of TestFile.txt";

	@SuppressWarnings("serial")
	public static final ArrayList<String> LIST_OF_WORDS_SEARCH = 
		    new ArrayList<String>() {{
		    	add(TextBuddy.TOP_OF_LIST);
		        add("Test 1");
		        add("Test 2");
		        add("Test 3");
		        add("This is a test case.");
		    }};
		    
/*	@SuppressWarnings("serial")
	public static final ArrayList<String> LIST_OF_WORDS_SORTED = 
		    new ArrayList<String>() {{
		        add("Apple");
		        add("Banana");
		        add("Crunchy");
		        add("Durian");
		        add("Elephant");
		        add("Fruits");
		    }};
		    */
	@SuppressWarnings("serial")
	public static final ArrayList<String> LIST_OF_WORDS_TO_SORT =
			new ArrayList<String>() {{
		        add("Banana");
		        add("Fruits");
		        add("Durian");
		        add("Apple");
		        add("Crunchy");
		        add("Elephant");
		    }};

		@Test
		public void testSearchMethod() throws IOException {
			ArrayList<String> toSearchFrom = LIST_OF_WORDS_SEARCH;
			String actual = TextBuddy.searchText("search Test", "TestFile.txt", toSearchFrom);	
				assertEquals(MESSAGE_CHECK, actual);
		}

		@Test
		public void testSortMethod() throws IOException {
			String expected = TextBuddy.MESSAGE_SORTED;
			ArrayList<String> toSort= LIST_OF_WORDS_TO_SORT;
			String actual = TextBuddy.sortText("TestFile.txt", toSort);	
				assertEquals(expected, actual);
		}
}
