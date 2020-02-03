package src;
import java.io.*;
import java.util.ArrayList;

/**
 * Class that parses the data in the text file
 */
public class Data {
	// Store displacement of triggers
	static int LEFT_TRIGGER_50_PERCENT_DISPLACEMENT = 192; //MAX: 256, MIN:128
	static int RIGHT_TRIGGER_50_PERCENT_DISPLACEMENT = 172; //MAX: 216, MIN: 128
	static int LEFT_JOYSTICK_50_PERCENT_DISPLACEMENT = 192; //MAX: 256, MIN: 128
	static int RIGHT_JOYSTICK_50_PERCENT_DISPLACEMNET = 192; //MAX: 256, MIN: 128

	/**
	 * Finds the substring that contains the value portion of a line in the text file.
	 * 
	 * @param line_file A line from a text file
	 * @return The substring that contains the value portion of a line in the text file
	 */
	public static String findValuesString(String line_file) {
	    int values_position = line_file.indexOf("code");
	    return line_file.substring(values_position);
	}
	
	/**
	 * Finds the code value in the values portion of a line in the text file.
	 * 
	 * @param values The substring that contains the value portion of a line in the text file
	 * @return The code value 
	 */
	public static int findCodeValue(String values) {
	    return Integer.parseInt(values.substring(values.indexOf("code ") + 5, values.indexOf(", type")));
	}

	/**
	 * Finds the type value in the values portion of a line in the text file.
	 * 
	 * @param values The substring that contains the value portion of a line in the text file
	 * @return The type value
	 */
	public static int findTypeValue(String values) {
	    return Integer.parseInt(values.substring(values.indexOf("type ") + 5, values.indexOf(", val")));
	}

	/**
	 * Finds the value value in the values portion of a line in the text file.
	 * 
	 * @param values The substring that contains the value portion of a line in the text file
	 * @return The value value
	 */
	public static int findValValue(String values) {
	    return Integer.parseInt(values.substring((values.indexOf("val ") + 4)));
	}

	/**
	 * Prints out the data from all the counted events.
	 * 
	 * @param events The Events data type that stores all the event counts
	 * @return void
	 */
	public static void printData(Events events) {
		System.out.println("BUTTON PRESS DATA");
		System.out.println("-------------------------------------------");
		System.out.println("Trigger and Joystick Data");
		System.out.println("Number of instances of L2 displacements: " + events.event02);
		System.out.println("Number of instances of R2 displacements: " + events.event05);
		System.out.println("Number of instances of Left Joystick displacements: " + events.event01);
		System.out.println("Number of instances of Right Joystick displacements: " + events.event03);
		System.out.println("-------------------------------------------");
		System.out.println("D-Pad Data");
		System.out.println("Left button presses: " + events.event16l);
		System.out.println("Right button presses: " + events.event16r);
		System.out.println("Up button presses: " + events.event17u);
		System.out.println("Down button presses: " + events.event17d);
		System.out.println("-------------------------------------------");
		System.out.println("Other Data");
		System.out.println("L1 button presses: " + events.event310);
		System.out.println("R1 button presses: " + events.event311);
		System.out.println("Share button presses: " + events.event314);
		System.out.println("Options button presses: " + events.event315);
		System.out.println("PS4 button presses: " + events.event316);
		System.out.println("Left Joystick button presses: " + events.event317);
		System.out.println("Right Joystick button presses: " + events.event318);
		System.out.println("Square button presses: " + events.event308);
		System.out.println("Triangle button presses: " + events.event307);
		System.out.println("Circle button presses: " + events.event305);
		System.out.println("X button presses: " + events.event304);
	}
	
	/**
	 * Counts the number of displacement events for two events of the same type.
	 * 
	 * @param events The Events data type that stores all the event counts
	 * @return the number of displacement events for a specific event type 
	 */
	public static int countRealDisplacementEvents(ArrayList<Integer> events) {
		int size = events.size();
		int displacement_event_counter = 0;
		
		for(int i = 0; i < size - 1; i++)
			if(Math.abs(events.get(i) - events.get(i + 1)) > 8)
				displacement_event_counter++;
		
		return displacement_event_counter;
	}

	/**
	 * Function to read all button events
	 * 
	 * @param br The buffered reader data type to read a file
	 * @param events The Events data type that stores all the event counts
	 * @return void
	 */
	public static void readBtnEvents(BufferedReader br, Events events) {
		String line_file;
		@SuppressWarnings
		int event_numbers = 0;
		int code = 0, type = 0, val = 0;
		
		//Array list to store trigger and joystick events
	    ArrayList<Integer> lt = new ArrayList<Integer>();
	    ArrayList<Integer> rt = new ArrayList<Integer>();
	    ArrayList<Integer> lj = new ArrayList<Integer>();
	    ArrayList<Integer> rj = new ArrayList<Integer>();
		
		try {
			while ((line_file = br.readLine()) != null) {
			    //Work with an event
			    //Get the code, type, and val values
			    //And count the number of events
			    if(line_file.contains("event at")) {
					//Update event number count
					event_numbers++;
					
					//Find values string
					String values = findValuesString(line_file);

			        if(values.contains("val 00")) { /*Ignore*/ }
			        else {		        
			            //Get code value
			            code = findCodeValue(values);
			            //Get type value
			            type = findTypeValue(values);
			            //Get val value
						val = findValValue(values);						
			        }
			    }

			    //Count button event using the code, type, and val
			    /*L2*/      if(code == 2 && val >= LEFT_TRIGGER_50_PERCENT_DISPLACEMENT)  { lt.add(val); } 
			    /*L1*/      if(code == 310) { events.event310++; }
			    /*R2*/      if(code == 5 && val >= RIGHT_TRIGGER_50_PERCENT_DISPLACEMENT) { rt.add(val); }
			    /*R1*/      if(code == 311) { events.event311++; }
			    /*Share*/   if(code == 314) { events.event314++; }
			    /*Options*/ if(code == 315) { events.event315++; }
			    /*/Home Button*/            if(code == 316) { events.event316++; }
			    /*Left Joystick Button*/    if(code == 317) { events.event317++; }
			    /*Right Joystick Button*/   if(code == 318) { events.event318++; }
			    /*Left Joystick*/   if(code == 1 && val >= LEFT_JOYSTICK_50_PERCENT_DISPLACEMENT)  { lj.add(val); }
			    /*Right Joystick*/  if(code == 3 && val >= RIGHT_JOYSTICK_50_PERCENT_DISPLACEMNET) { rj.add(val); }
			    /*D-Pad Left*/      if(code == 16 && val == -1) { events.event16l++; }
			    /*D-Pad Right*/     if(code == 16 && val == 1)  { events.event16r++; }
			    /*D-Pad Down*/      if(code == 17 && val == 1)  { events.event17d++; }
			    /*D-Pad Up*/        if(code == 17 && val == -1) { events.event17u++; }
			    /*Square*/  if(code == 308) { events.event308++; }
			    /*Triangle*/if(code == 307) { events.event307++; }
			    /*Circle*/  if(code == 305) { events.event305++; }
			    /*X*/       if(code == 304) { events.event304++; }
				
			}
		} catch (IOException e) { e.printStackTrace(); } 
		
		//Check for actual displacements
		events.event02 = countRealDisplacementEvents(lt);
		events.event05 = countRealDisplacementEvents(rt);
		events.event01 = countRealDisplacementEvents(lj);
		events.event03 = countRealDisplacementEvents(rj);
		
		//Output button event counts
		printData(events);
	}
	
	/**
	 * Outputs events counts to a text file.
	 * 
	 * @param events The Events data type that stores all the event counts
	 * @return void
	 */
	public static void outputToTextFile(Events events) {
		try {
			PrintStream output = new PrintStream(new FileOutputStream("/Users/Anuj/Desktop/DataBatchTest.txt"));
			System.setOut(output);
			printData(events);
		} catch (FileNotFoundException e) { e.printStackTrace(); }
	}

	// Main function
	public static void main(String[] args) throws FileNotFoundException {
		// Read file
		File file = new File("/Users/Anuj/Documents/Workspace/GE1501/milestone/27_minutes_FIFA18_data_6.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		Events events = new Events();
		
		// Parse file
		readBtnEvents(br, events);
		outputToTextFile(events);		
	}
}