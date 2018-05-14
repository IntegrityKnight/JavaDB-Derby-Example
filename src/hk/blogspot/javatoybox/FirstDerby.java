package hk.blogspot.javatoybox;

import java.util.Scanner;

//This example use Java 7 feature, you must update your OpenJDK before run this program.
public class FirstDerby {
	public static void main(String[] args) {

		final String ServerMode = "jdbc:derby://localhost:1527/FirstDB";
		final String EmbeddedMode = "jdbc:derby:A:\\javatoybox\\Database\\FirstDB";
		ConnectDerby derbyDB;
		
		if (args[0].equalsIgnoreCase(ServerMode))
			derbyDB = new ConnectDerby(ServerMode);
		else
			derbyDB = new ConnectDerby(EmbeddedMode);
		
		// Show menu and allow input
		Scanner input = new Scanner(System.in);
		boolean exit = false;

		do {

			System.out.println(" \n Please select operation number ( 1-5 ) :\n"
			+ "1. read data \n" 
			+ "2. delete \n"
			+ "3. insert BLOB \n"
			+ "4. update BLOB \n"
			+ "5. extract BLOB\n"
			+ "Press any else key to exit program\n");

			switch (input.nextInt()) {

			case 1:
				derbyDB.read("SELECT id, actor, product FROM acg_character.profile");
				break;

			case 2:
				derbyDB.delete("DELETE FROM acg_character.profile WHERE id = ?", 3);
				break;

			case 3:
				derbyDB.writeBLOB("INSERT INTO acg_character.profile (media_1_filename,media_1,product) VALUES (?,?,\'ご注文はうさぎですか？\')", 
						"A:\\javatoybox\\test_data\\test\\insert.jpg");
				break;

			case 4:
				derbyDB.writeBLOB("UPDATE acg_character.profile SET media_1_filename = ?, media_1 = ? WHERE id = 1",
						"A:\\javatoybox\\test_data\\test\\insert.jpg");
				break;

			case 5:
				derbyDB.extractBLOB("SELECT media_1_filename, media_1 FROM acg_character.profile WHERE id = 1",
						"media_1_filename");
				break;

			default:
				derbyDB.shudown();
				input.close();
				exit = true;
				break;

			} // end case
		} while (!exit);// end do-while loop
	}
}