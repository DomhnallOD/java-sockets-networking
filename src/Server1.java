//Imports
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.TreeSet;

//Create class Server1
public class Server1 {

	//Declare member variables
	private PrintWriter out;
	private Scanner in;
	private Socket s; 
	TreeSet tree = new TreeSet();
	//Start main method
	public static void main(String[] args) throws IOException {
		//Allow comms between TokenClient and Server1 class via 9999, create instance of Server1
		ServerSocket s = new ServerSocket(9999);
		Server1 serverInstance = new Server1();

		//Output initial connection msg to user
		System.out.println("Server running. Waiting for a client to connect...");

		//Upon client attempting to connect output accepted, or disconnected and waiting for new client depending on status
		//Run() upon connection
		while(true) { 
			serverInstance.s = s.accept(); 
			System.out.println("Client connected"); 
			serverInstance.run(); 
			System.out.println("Client disconnected. Waiting for a new client to connect...");
		}
	}

	//Create run() method to allow user input to console and output
	public void run() {
		try {
			try {
				in = new Scanner(s.getInputStream());
				out = new PrintWriter(s.getOutputStream());
				RequestHandling();
				//Finally close socket for best practice
			} finally {
				s.close();
			}
			//Output errors
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	//Exception handling, throw exception 
	public void RequestHandling() throws IOException {
		while(true) {
			//Return blank
			if(!in.hasNext()) 
				return;
			String request = in.next();
			//Output what user has entered into console
			System.out.println("Request received: " + request);
			//Call next method
			secondRequestHandle(request); 
		}
	}

	public void secondRequestHandle(String request) throws IOException {
		//Recieve user input to console
		String nextWord = in.next();

		//Output user input
		System.out.println("Received from client: " + nextWord);
		System.out.println("OK");
		//If user wishes to submit a token use following actions...
		if (request.equals("SUBMIT")) {
			//If list is already full output appropriate error message
			if (tree.size() == 10) { 
				out.println("ERROR: List is Full!");
				out.flush();
			}

			//If word is already contained output appropriate error message
			else if (tree.contains(nextWord)) {
				out.println("ERROR: Word is already here!");
				out.flush();
			}

			//If list isn't full and nextWord is original to list, output "OK"
			else if (tree.size() < 10) {
				tree.add(nextWord);
				out.println("OK");
				out.flush();
			}
		}
		
		//Is user wishes to remove an entry take following action...
		else if (request.equals("REMOVE")) { 
			//If nextWord is in list, remove it an output "OK"
			if (tree.contains(nextWord)) { 
				tree.remove(nextWord);
				out.println("OK");
				out.flush();
			}
			//Provide for error
			else
				out.println("ERROR: Word has not been removed!");
			out.flush();
		} 
		//If user wishes to disconnect from server, close socket etc.
		if (request.equals("BYE")) {
			in.close();
			out.close();
			s.close(); 

		}
		
		//Output list 
		System.out.print(tree);
	//End Main()	
	}
	
//End the class
}

