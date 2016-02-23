
/**
* TEXTBUDDY <FOR CE2>
*
* This application creates a text file with the given file
* name. After every user operation, the file will be updated.
* This might result in multiple copies of files with the same name.
* Therefore, it is not advisable to move the executable file around.
*
* Operations supported by this application includes:
* 1. Adding of string entries into text file
* 2. Displaying all string entries in text file
* 3. Deleting entries at specified index in text file
* 4. Clearing all entries in text file
* 5. Sorting lines alphabetically.
* 6. Searching for a word in the file and return the lines containing that word.
* 7. Exiting of application
*
* @author Lau Dawson
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.IndexOutOfBoundsException;

public class TextBuddy {
 private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use.";
 private static final String TEXT_DELETED = "deleted from %1$s: \"%2$s\"";
 private static final String TEXT_ADDED = "added to %1$s: \"%2$s\"";
 private static final String TEXT_CLEARED = "all content deleted from %1$s";
 private static final String END_OF_DISPLAY = "end_of_display";
 private static final String TEXT_EMPTY = "%1$s is empty.";
 private static final String MESSAGE_ERROR = "Unrecognized command type";
 private static final String INVALID_NUMBER = "This number is invalid. Please choose another number.";
 static final String MESSAGE_SORTED= "List has been sorted alphabetically.";
 private static final String MESSAGE_SEARCH= "\"%1$s\" has been found in index %2$s of %3$s";
 private static final String MESSAGE_NOT_FOUND="\"%1$s\" is not found in %2$s";
 private static final String ADD_COMMA= ", ";
 private static final int CHECK_IF_EMPTY= 1;
 static final String TOP_OF_LIST= "null";
 
 private static Scanner sc = new Scanner(System.in);
 private static ArrayList<String> sentences;

 enum CommandType {
  ADD_TEXT, DISPLAY_TEXT, DELETE_TEXT, CLEAR_TEXT, SORT_TEXT, SEARCH_TEXT, ERROR, EXIT
 };

 public static void main(String[] args) {
  showToUser(getWelcomeMessage(args[0]));
  createTextBuddy();
  while (true) {
   System.out.print("Command: ");
   String userFeedback = returnFeedback(args[0]);
   showToUser(userFeedback);
   saveFile(args[0]);
  }
 }

 private static void addBuffer(ArrayList<String> sentences) {
  sentences.add(TOP_OF_LIST);
 }

 private static String returnFeedback(String input) {
  String userCommand = sc.nextLine();
  String userFeedback = executeCommand(userCommand, input);
  return userFeedback;
 }

 private static String getWelcomeMessage(String text) {
  return String.format(MESSAGE_WELCOME, text);
 }

 private static void showToUser(String text) {
  if (!isEndOfDisplay(text)) {
   System.out.println(text);
  }
 }

 private static boolean isEndOfDisplay(String text) {
  return text.equals(END_OF_DISPLAY);
 }

 private static String executeCommand(String userCommand, String fileName) {
   String commandTypeString = getFirstWord(userCommand);
   CommandType commandType = determineCommandType(commandTypeString);
   
   switch (commandType) {
     case ADD_TEXT :
       return addText(userCommand, fileName);
     case DELETE_TEXT :
       return deleteText(userCommand, fileName);
     case CLEAR_TEXT :
       return clearText(fileName);
     case DISPLAY_TEXT :
       displayText(fileName);
       return END_OF_DISPLAY;
     case SEARCH_TEXT :
       return searchText(userCommand, fileName, sentences);
     case SORT_TEXT :
       return sortText(fileName, sentences);
     case EXIT :
       saveFile(fileName);
       System.exit(0);
     default :
       // throw an error if the command is not recognized
   return MESSAGE_ERROR;
   }
 }
 
 private static CommandType determineCommandType(String commandTypeString) {
  if (commandTypeString == null)
   throw new Error("command type string cannot be null!");

  if (commandTypeString.equalsIgnoreCase("add")) {
   return CommandType.ADD_TEXT;
  } else if (commandTypeString.equalsIgnoreCase("display")) {
   return CommandType.DISPLAY_TEXT;
  } else if (commandTypeString.equalsIgnoreCase("clear")) {
   return CommandType.CLEAR_TEXT;
  } else if (commandTypeString.equalsIgnoreCase("delete")) {
   return CommandType.DELETE_TEXT;
  } else if (commandTypeString.equalsIgnoreCase("exit")) {
   return CommandType.EXIT;
  } else if (commandTypeString.equalsIgnoreCase("sort")) {
   return CommandType.SORT_TEXT;
  }else if (commandTypeString.equalsIgnoreCase("search")) {
   return CommandType.SEARCH_TEXT;
   } else {
   return CommandType.ERROR;
  }
 }
 
 /**
  *Adds a given word into the list of words
  **/
 private static String addText(String userCommand, String fileName) {
  String toBeAdded = getSecondSentence(userCommand);
  addToList(toBeAdded);
  String responseMessage = String.format(TEXT_ADDED, fileName, toBeAdded);
  return responseMessage;
 }

 
 /**
  *Searches list for words and displays corresponding indices
  * Displays a not-found or empty message if list has no
  * such word or is empty respectively
  **/ 
 public static String searchText(String userCommand, String fileName,
                          ArrayList<String> sentences) { 
   String responseMessage="";
   String listIndices="";
   
   if(!isEmpty(sentences)) {
     
     String wordToSearch= getSecondSentence(userCommand);
     
     for(int i=1; i<sentences.size();i++) {
       if(sentences.get(i).toLowerCase().contains(wordToSearch.toLowerCase())) {
         
         listIndices+=i;
         listIndices+=ADD_COMMA;
       }
     }
     if(!listIndices.equals("")){//not empty
     responseMessage=String.format(MESSAGE_SEARCH, wordToSearch, listIndices, fileName);
     responseMessage=responseMessage.replace(",  ", " ");
     }
     else{
       responseMessage= String.format(MESSAGE_NOT_FOUND, wordToSearch, fileName);
     }
   } 
   else {
     responseMessage= String.format(TEXT_EMPTY, fileName);
   }
   return responseMessage;
 }
 
 /**
  *Sorts the current list of entries in alphabetical order.
  *Outputs an empty message if list is empty.
  **/
 static String sortText(String fileName, ArrayList<String> list) {
   if(isEmpty(list)){
     return String.format(TEXT_EMPTY, fileName);
   } else {
     ArrayList<String> tempArray= new ArrayList<String>();
     
     for(int i=1;i<list.size();i++) {
       tempArray.add(list.get(i));
     }
     createTextBuddy();
     Collections.sort(tempArray);
     
     for(String s: tempArray) {
       sentences.add(s);
     }
     return MESSAGE_SORTED;
   }
 }
 
 /**
  *Displays current list of entries on to the screen
  **/
 private static void displayText(String fileName) {
  if (!isEmpty(sentences)) {
   int count = 1;
   for (int i = 1; i < sentences.size(); i++) {
    System.out.print(count + ". ");
    count++;
    System.out.println(sentences.get(i));
   }
  } else {
   printEmptyMessage(fileName);
  }
 }
 
 /**
  *Takes in an index and deletes the entry corresponding
  *to the index. 
  **/
  private static String deleteText(String userCommand, String fileName) {
  int numToDelete = getSecondInt(userCommand);
  if (isNumToDeleteValid(numToDelete)) {
   String stringToDelete = sentences.get(numToDelete);
   sentences.remove(numToDelete);
   String responseMessage = String.format(TEXT_DELETED, fileName, stringToDelete);
   return responseMessage;
  } else {
   return INVALID_NUMBER;
  }
 }
  
  /**
   *Checks if the list is empty
   **/
  private static boolean isEmpty(ArrayList<String> checkIfEmpty) {
    return checkIfEmpty.size() == CHECK_IF_EMPTY;
  }
  
 private static void printEmptyMessage(String fileName) {
  String emptyMessage = String.format(TEXT_EMPTY, fileName);
  System.out.println(emptyMessage);
 }

 private static boolean isNumToDeleteValid(int numToDelete) {

  try {
    sentences.get(numToDelete);
  } catch (IndexOutOfBoundsException e) {
   return false;
  }
  return true;
 }

 private static String clearText(String fileName) {
  createTextBuddy();
  return String.format(TEXT_CLEARED, fileName);
 }

 private static void saveFile(String fileName) {
  PrintWriter pw = null;

  try {
   pw = new PrintWriter(new FileOutputStream(fileName));
  } catch (FileNotFoundException e) {
   System.out.println("Error saving");
  }
  for (int i = 1; i < sentences.size(); i++) {
   pw.print(i + ". ");
   pw.println(sentences.get(i));
  }
  pw.close();
 }

 private static void addToList(String toBeAdded) {
  sentences.add(toBeAdded);
 }

 private static String getFirstWord(String userCommand) {
  String commandTypeString = userCommand.trim().split("\\s+")[0];
  return commandTypeString;
 }

 private static int getSecondInt(String userCommand) {
  String numToDelete = userCommand.trim().split("\\s+")[1];
  return Integer.parseInt(numToDelete);
 }

 private static String getSecondSentence(String userCommand) {
  String stringToAdd = userCommand.trim().split(" ", 2)[1];
  return stringToAdd;
 }

 private static void createTextBuddy() {
  sentences = new ArrayList<String>();
  addBuffer(sentences);
 }
}