import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Main {

	private static void eliminaStudente() throws SQLException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Inserisci l'anno dello studente che vuoi eliminare");
		int scelta = sc.nextInt();

		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prog1904", "root", "12345");
		PreparedStatement cancella = cn.prepareStatement("DELETE FROM tabellastudenti WHERE anno=?");
		Statement query = cn.createStatement();
		cancella.setInt(1, scelta);
		cancella.executeUpdate();
		System.out.println("Eliminato!");
	}

	private static void visualizzaStudenti() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prog1904", "root", "12345");
		Statement query = cn.createStatement();
		ResultSet listastudenti = query.executeQuery("SELECT * from tabellastudenti");
		try {
			while (listastudenti.next()) {
				System.out.println("Matricola: " + listastudenti.getString("matricola"));
				System.out.println("Nome: " + listastudenti.getString("nome"));
				System.out.println("Anno: " + listastudenti.getString("anno"));
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Non ci sono studenti nel database!");
		}
	}

	private static void cercaStudente() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		System.out
				.println("Vuoi cercare gli studenti per nome (1) o filtrare gli studenti nati dopo un certo anno (2)?");
		Scanner sc = new Scanner(System.in);
		int scelta = sc.nextInt();
		switch (scelta) {
		case 1:
			cercaperNome();
			break;
		case 2:
			filtraStudenti();
			break;
		}

	}

	private static void filtraStudenti() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		System.out.println("Inserisci anno");
		int filtro = sc.nextInt();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prog1904", "root", "12345");
//		PreparedStatement query = cn.prepareStatement("SELECT * from tabellastudenti WHERE nome LIKE '?%'");
//		query.setString(1, lettera.substring(0, 1));
		Statement st = cn.createStatement();
		ResultSet filtrostudenti = st.executeQuery("SELECT * from tabellastudenti WHERE anno>" + filtro);

		while (filtrostudenti.next()) {
			System.out.println("Matricola: " + filtrostudenti.getString("matricola"));
			System.out.println("Nome: " + filtrostudenti.getString("nome"));

		}
	}

	private static void cercaperNome() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		System.out.println("Inserisci lettera");
		String lettera = sc.next();
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prog1904", "root", "12345");
//		PreparedStatement query = cn.prepareStatement("SELECT * from tabellastudenti WHERE nome LIKE '?%'");
//		query.setString(1, lettera.substring(0, 1));
		Statement st = cn.createStatement();
		ResultSet filtrostudenti = st
				.executeQuery("SELECT * from tabellastudenti WHERE nome LIKE '" + lettera.substring(0, 1) + "%'");

		while (filtrostudenti.next()) {
			System.out.println("Matricola: " + filtrostudenti.getString("matricola"));
			System.out.println("Nome: " + filtrostudenti.getString("nome"));
		}

	}

	private static void aggiungiStudente() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Scanner ins = new Scanner(System.in);
		Studente studentenuovo = new Studente();
		System.out.println("Inserisci user");
		String user = ins.next();
		studentenuovo.setUser(user);
		System.out.println("Inserisci password");
		String password = ins.next();
		studentenuovo.setPassword(password);
		System.out.println("Inserisci matricola");
		int matricola = ins.nextInt();
		studentenuovo.setMatricola(matricola);
		System.out.println("Inserisci nome");
		String nome = ins.next();
		studentenuovo.setNome(nome);
		System.out.println("Inserisci anno di nascita");
		int anno = ins.nextInt();
		studentenuovo.setAnno(anno);
		System.out.println("Creazione studente!");

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3307/prog1904", "root", "12345");
		Statement query = cn.createStatement();
		ResultSet listastudenti = query.executeQuery("SELECT * FROM tabellastudenti");
		/*
		 * if (listastudenti.next() == false) { PreparedStatement pst =
		 * cn.prepareStatement(
		 * "INSERT INTO tabellastudenti (user, password, matricola, nome, anno) VALUES (?,?,?, ?, ?)"
		 * ); pst.setString(1, studentenuovo.getUser()); pst.setString(2,
		 * studentenuovo.getPassword()); pst.setInt(3, studentenuovo.getMatricola());
		 * pst.setString(4, studentenuovo.getNome()); pst.setInt(5,
		 * studentenuovo.getAnno()); pst.execute(); } else {
		 */
		String risultatocontrollo = controllo(listastudenti, studentenuovo);
			if (risultatocontrollo != "doppione") {
				PreparedStatement pst = cn.prepareStatement(
						"INSERT INTO tabellastudenti (user, password, matricola, nome, anno) VALUES (?,?,?, ?, ?)");
				pst.setString(1, studentenuovo.getUser());
				pst.setString(2, studentenuovo.getPassword());
				pst.setInt(3, studentenuovo.getMatricola());
				pst.setString(4, studentenuovo.getNome());
				pst.setInt(5, studentenuovo.getAnno());
				pst.execute();

			} else
				System.out.println("Studente già presente");
	}
//	}

	private static String controllo(ResultSet listastudenti, Studente studentenuovo) throws SQLException {

		List<String> userstudenti = new ArrayList<String>();
		List<String> passwordstudenti = new ArrayList<String>();
		List<Integer> matricolastudenti = new ArrayList<Integer>();

		while (listastudenti.next()) {
			userstudenti.add(listastudenti.getString("user"));
			passwordstudenti.add(listastudenti.getString("password"));
			matricolastudenti.add(listastudenti.getInt("matricola"));
		}

		String userpresente = "";
		String passwordpresente = "";
		String matricolapresente = "";
		String userstudente = studentenuovo.getUser();
		String passwordstudente = studentenuovo.getPassword();
		int matricolastudente = studentenuovo.getMatricola();

		for (String user : userstudenti) {
			System.out.print("L'utente esiste?");
			if (userstudente.equals(user)) {
				userpresente = "presente";
				System.out.println(userpresente);
				break;
			} else {
				userpresente = "assente";
				System.out.println(userpresente);
			} 
		}

		for (String password : passwordstudenti) {
			System.out.print("la password esiste?");
			if (passwordstudente.equals(password)) {
				
				passwordpresente = "presente";
				System.out.println(passwordpresente);
				break;
			} else {
				passwordpresente = "assente";
				System.out.println(passwordpresente);
			}
		}

		for (int matricola : matricolastudenti) {
			System.out.print("La matricola esiste?");
			if (matricolastudente == matricola) {
	
				matricolapresente = "presente";
				System.out.println(matricolapresente);
				break;
			} else {
				
				matricolapresente = "assente";
				System.out.println(matricolapresente);
			}
		}

		if (matricolapresente == "presente" && passwordpresente == "presente" && userpresente == "presente") {

			return "doppione";

			// TODO Auto-generated method stub
		} else {
			return "nuovo";
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			Scanner scelta = new Scanner(System.in);
			System.out.println("1 - Aggiungi Studente");
			System.out.println("2 - Cerca studente");
			System.out.println("3 - Visualizza tutti gli studenti");
			System.out.println("4 - Elimina Studente per anno");
			int risposta = scelta.nextInt();
			switch (risposta) {

			case 1:
				aggiungiStudente();
				scelta.close();
				break;
			case 2:
				cercaStudente();
				scelta.close();
				break;
			case 3:
				visualizzaStudenti();
				scelta.close();
				break;
			case 4:
				eliminaStudente();
				scelta.close();
				break;
			default:
				System.out.println("Scegli!");
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Errore: " + e);
		}

	}

}
