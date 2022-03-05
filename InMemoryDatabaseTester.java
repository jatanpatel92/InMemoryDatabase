import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * InMemoryDatabaseTester to test the InMemoryDatabase implementation.
 */

/**
 * @author jatan
 *
 */
public class InMemoryDatabaseTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InMemoryDatabase database = new InMemoryDatabaseImp();
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.print("Command: ");
			String command = sc.nextLine();
			if(command!=null && performCommand(command, database))
				break;
		}
	}

	private static boolean performCommand(String command, InMemoryDatabase database) {
		String[] expression = command.split(" ");
		Set<String> dataCommands = Stream.of(DataCommands.values()).map(DataCommands::name).collect(Collectors.toSet()); 
		if(dataCommands.contains(expression[0])) {
			switch(expression[0]) {
				case "SET":
					try {
						database.insert(expression[1], expression[2]);
					}
					catch(Exception e) {
						System.out.println("Error : Cannot execute command : "+command);
						e.printStackTrace();
					}
					return false;
				case "GET":
					try {
						System.out.println(database.get(expression[1]));
					}
					catch(Exception e) {
						System.out.println("Error : Cannot execute command : "+command);
						e.printStackTrace();
					}
					return false;
				case "UNSET":
					try {
						database.remove(expression[1]);
					}
					catch(Exception e) {
						System.out.println("Error : Cannot execute command : "+command);
						e.printStackTrace();
					}
					return false;
				case "NUMEQUALTO":
					try {
						System.out.println(database.numberEqualToValue(expression[1]));
					}
					catch(Exception e) {
						System.out.println("Error : Cannot execute command : "+command);
						e.printStackTrace();
					}
					return false;
				case "END":
					return true;
			}
				
		}
		Set<String> transactionCommands = Stream.of(TransactionCommands.values()).map(TransactionCommands::name).collect(Collectors.toSet());
		if(transactionCommands.contains(expression[0])) {
			switch(expression[0]) {
				case "BEGIN":
					database.begin();
					break;
				case "COMMIT":
					database.commit();
					break;
				case "ROLLBACK":
					System.out.println(database.rollback());
					break;
			}
		}
		return false;
	}

}
