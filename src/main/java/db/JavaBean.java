
	package db;

	import java.sql.*;

	public class JavaBean {

		String error;
		Connection con;

		public JavaBean() { 
		} 
		public void connect() throws ClassNotFoundException, SQLException, Exception { 
			try { 
				Class.forName("com.mysql.cj.jdbc.Driver"); 
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pibd_andreea_bun?useSSL=false", "root", "student1234"); 
			} catch (ClassNotFoundException cnfe) { 
				error = "ClassNotFoundException: Nu s-a gasit driverul bazei de date."; 
				throw new ClassNotFoundException(error); 
			} catch (SQLException cnfe) { 
				error = "SQLException: Nu se poate conecta la baza de date."; 
				throw new SQLException(error); 
			} catch (Exception e) { 
				error = "Exception: A aparut o exceptie neprevazuta in timp ce se stabilea legatura la baza de date."; 
				throw new Exception(error); 
			} 
		} // connect() 

		public void connect(String bd) throws ClassNotFoundException, SQLException, Exception { 
			try { 
				Class.forName("com.mysql.cj.jdbc.Driver"); 
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + bd, "root", "student1234"); 
			} catch (ClassNotFoundException cnfe) { 
				error = "ClassNotFoundException: Nu s-a gasit driverul bazei de date."; 
				throw new ClassNotFoundException(error); 
			} catch (SQLException cnfe) { 
				error = "SQLException: Nu se poate conecta la baza de date."; 
				throw new SQLException(error); 
			} catch (Exception e) { 
				error = "Exception: A aparut o exceptie neprevazuta in timp ce se stabilea legatura la baza de date."; 
				throw new Exception(error); 
			} 
		} // connect(String bd) 

		public void connect(String bd, String ip) throws ClassNotFoundException, SQLException, 
		Exception { 
			try { 
				Class.forName("com.mysql.cj.jdbc.Driver"); 
				con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + bd, "root", "admin1"); 
			} catch (ClassNotFoundException cnfe) { 
				error = "ClassNotFoundException: Nu s-a gasit driverul bazei de date."; 
				throw new ClassNotFoundException(error); 
			} catch (SQLException cnfe) { 
				error = "SQLException: Nu se poate conecta la baza de date."; 
				throw new SQLException(error); 


			} catch (Exception e) { 
				error = "Exception: A aparut o exceptie neprevazuta in timp ce se stabilea legatura la baza de date."; 
				throw new Exception(error); 
			} 
		} // connect(String bd, String ip) 

		public void disconnect() throws SQLException {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException sqle) {
				error = ("SQLException: Nu se poate inchide conexiunea la baza de date.");
				throw new SQLException(error);
			}
		} // disconnect()

		public void adaugaUtilizatori(String Nume, String Prenume, String Email, String Telefon, String Data_Nasterii, String Adresa) 
		        throws SQLException, Exception { 
		    if (con != null) { 
		        try { 
		            Statement stmt; 
		            stmt = con.createStatement(); 
		            stmt.executeUpdate("INSERT INTO utilizatori (nume, prenume, email, telefon, data_nasterii, adresa) VALUES('" + Nume + "', '"
		            + Prenume + "', '" + Email + "', '" + Telefon + "', '" + Data_Nasterii + "', '" + Adresa + "')");
		        } catch (SQLException sqle) { 
		            error = "ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate."; 
		            throw new SQLException(error); 
		        } 
		    } else { 
		        error = "Exceptie: Conexiunea cu baza de date a fost pierduta."; 
		        throw new Exception(error); 
		    } 
		}


		public void adaugaSistemedeOperare(String Nume, String Versiune, String Producator, String Data_Lansare, String Tip_Sistem, String Compatibilitate) 
				throws SQLException, Exception { 
			if (con != null) { 
				try { 
					
					Statement stmt; 
					stmt = con.createStatement(); 
					stmt.executeUpdate("insert into sistemedeoperare (nume_sistem, versiune_sistem, producator, data_lansare, tip_sistem, compatibilitate) values('" + Nume + "'  ,'" + Versiune + "' ,'" + Producator + "','" + Data_Lansare + "','" + Tip_Sistem + "', '" + Compatibilitate + "');"); 

				} catch (SQLException sqle) { 
					error = "ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate."; 
					throw new SQLException(error); 
				} 
			} else { 
				error = "Exceptie: Conexiunea cu baza de date a fost pierduta."; 
				throw new Exception(error); 
			} 
		} // end of adaugaMedic() 

		public void adaugaUtilizatoriDeSisteme(long idUtilizatori, long idSistemeDeOperare, String Data_Actualizare, String Status, String Rol, String Prioritate) 
		        throws SQLException, Exception { 
		    if (con != null) { 
		        try { 
		            String query = "INSERT INTO utilizatorisisteme (idUtilizatori, idSistemeDeOperare, data_actualizare, status, rol, prioritate) VALUES (?, ?, ?, ?, ?, ?)";
		            PreparedStatement stmt = con.prepareStatement(query);
		            stmt.setLong(1, idUtilizatori);
		            stmt.setLong(2, idSistemeDeOperare);
		            stmt.setString(3, Data_Actualizare);
		            stmt.setString(4, Status);
		            stmt.setString(5, Rol);
		            stmt.setString(6, Prioritate);
		            stmt.executeUpdate();
		            stmt.close();
		        } catch (SQLException sqle) { 
		            throw new SQLException("ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate.", sqle); 
		        } 
		    } else { 
		        throw new Exception("Exceptie: Conexiunea cu baza de date a fost pierduta."); 
		    } 
		}


		public ResultSet vedeTabela(String tabel) throws SQLException, Exception {
			ResultSet rs = null;
			try {
				String queryString = ("SELECT * from `pibd_andreea_bun`.`" + tabel + "`;");
				Statement stmt = con.createStatement(/*ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY*/);
				rs = stmt.executeQuery(queryString);
			} catch (SQLException sqle) {
				error = "SQLException: Interogarea nu a fost posibila.";
				throw new SQLException(error);
			} catch (Exception e) {
				error = "A aparut o exceptie in timp ce se extrageau datele.";
				throw new Exception(error);
			}
			return rs;
		} 

		public ResultSet vedeUtilizatori() throws SQLException {
		    ResultSet rs = null;
		    if (con != null) {
		        try {
		        	String queryString = "SELECT idUtilizatori, nume, prenume, email, telefon, data_nasterii, adresa, data_inregistrare FROM utilizatori";
		            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		            rs = stmt.executeQuery(queryString);
		        } catch (SQLException sqle) {
		            String error = "SQLException: Interogarea nu a fost posibila.";
		            throw new SQLException(error, sqle);
		        }
		    } else {
		        String error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
		        throw new SQLException(error);
		    }
		    return rs;
		}

		public ResultSet vedeSistemeDeOperare() throws SQLException, Exception {
		    ResultSet rs = null;
		    try {

		        String queryString = "SELECT idSistemeDeOperare, nume_sistem, versiune_sistem, producator, data_lansare, tip_sistem, compatibilitate FROM sistemedeoperare";
		        Statement stmt = con.createStatement();
		        rs = stmt.executeQuery(queryString);
		    } catch (SQLException sqle) {
		        throw new SQLException("SQLException: Interogarea nu a fost posibila.", sqle);
		    } catch (Exception e) {
		        throw new Exception("A aparut o exceptie in timp ce se extrageau datele.", e);
		    }
		    return rs;
		}

		public ResultSet vedeSistemeUtilizatori(String tabel) throws SQLException, Exception {
		    ResultSet rs = null;
		    try {
		        String queryString = "SELECT us.idUtilizatoriSisteme AS idUtilizatoriSisteme, "
		                            + "u.nume AS Nume_Utilizator, "
		                            + "u.email AS Email, "
		                            + "us.status AS Status, "
		                            + "us.rol AS Rol, "
		                            + "so.nume_sistem AS Sistem, "
		                            + "so.versiune_sistem AS Versiune "
		                            + "FROM utilizatorisisteme us "
		                            + "INNER JOIN utilizatori u ON us.idUtilizatori = u.idUtilizatori "
		                            + "INNER JOIN sistemedeoperare so ON us.idSistemeDeOperare = so.idSistemeDeOperare";
		        Statement stmt = con.createStatement();
		        rs = stmt.executeQuery(queryString);
		    } catch (SQLException sqle) {
		        throw new SQLException("SQLException: Interogarea nu a fost posibila.", sqle);
		    } catch (Exception e) {
		        throw new Exception("A apărut o excepție în timp ce se extrăgeau datele.", e);
		    }
		    return rs;
		}

		
		
		public ResultSet vedeUtilizatoriSisteme() throws SQLException, Exception {
		    ResultSet rs = null;
		    try {
		        String queryString = "SELECT us.idUtilizatoriSisteme, "
		                            + "u.idUtilizatori, "
		                            + "u.nume AS nume, " 
		                            + "u.prenume AS prenume, " 
		                            + "u.email AS email, " 
		                            + "so.nume_sistem AS nume_sistem, " 
		                            + "so.versiune_sistem AS versiune_sistem, "
		                            + "us.data_actualizare AS data_actualizare, "
		                            + "us.status AS status, "
		                            + "us.rol AS rol, "
		                            + "us.prioritate AS prioritate "
		                            + "FROM utilizatorisisteme us "
		                            + "INNER JOIN utilizatori u ON us.idUtilizatori = u.idUtilizatori "
		                            + "INNER JOIN sistemedeoperare so ON us.idSistemeDeOperare = so.idSistemeDeOperare";

		        Statement stmt = con.createStatement();
		        rs = stmt.executeQuery(queryString);
		    } catch (SQLException sqle) {
		        throw new SQLException("SQLException: Interogarea nu a fost posibila.", sqle);
		    } catch (Exception e) {
		        throw new Exception("A apărut o excepție în timp ce se extrăgeau datele.", e);
		    }
		    return rs;
		}

		public void stergeDateTabela(String[] primaryKeys, String tabela, String dupaID) throws SQLException, Exception {
			if (con != null) {
				try {
					long aux;
					PreparedStatement delete;
					delete = con.prepareStatement("DELETE FROM " + tabela + " WHERE " + dupaID + "=?;");
					for (int i = 0; i < primaryKeys.length; i++) {
						aux = java.lang.Long.parseLong(primaryKeys[i]);
						delete.setLong(1, aux);
						delete.execute();
					}
				} catch (SQLException sqle) {
					error = "ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate.";
					throw new SQLException(error);
				} catch (Exception e) {
					error = "A aparut o exceptie in timp ce erau sterse inregistrarile.";
					throw new Exception(error);
				}
			} else {
				error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
				throw new Exception(error);
			}
		} 

		


		public void stergeTabela(String tabela) throws SQLException, Exception {
			if (con != null) {
				try {
					Statement stmt;
					stmt = con.createStatement();
					stmt.executeUpdate("TRUNCATE TABLE " + tabela + ";");
				} catch (SQLException sqle) {
					error = "ExceptieSQL: Stergere nereusita; este posibil sa existe duplicate.";
					throw new SQLException(error);
				}
			} else {
				error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
				throw new Exception(error);
			}
		}

		public void modificaTabela(String tabela, String IDTabela, long ID, String[] campuri, String[] valori) throws SQLException, Exception {
			String update = "update " + tabela + " set ";
			String temp = "";
			if (con != null) {
				try {
					for (int i = 0; i < campuri.length; i++) {
						if (i != (campuri.length - 1)) {
							temp = temp + campuri[i] + "='" + valori[i] + "', ";
						} else {
							temp = temp + campuri[i] + "='" + valori[i] + "' where " + IDTabela + " = '" + ID + "';";
						}
					}
					update = update + temp;
					Statement stmt;
					stmt = con.createStatement();
					stmt.executeUpdate(update);
				} catch (SQLException sqle) {
					error = "ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate.";
					throw new SQLException(error);
				}
			} else {
				error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
				throw new Exception(error);
			}
		}

		public ResultSet intoarceLinie(String tabela, int ID) throws SQLException, Exception {
			ResultSet rs = null;
			try {
				String queryString = ("SELECT * FROM " + tabela + " where idUtilizatori=" + ID + ";");
				Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(queryString); //sql exception
			} catch (SQLException sqle) {
				error = "SQLException: Interogarea nu a fost posibila.";
				throw new SQLException(error);
			} catch (Exception e) {
				error = "A aparut o exceptie in timp ce se extrageau datele.";
				throw new Exception(error);
			}
			return rs;
		} // end of intoarceLinie()

		public ResultSet intoarceLinieDupaId(String tabela, String denumireId, long ID) throws SQLException, Exception {
			ResultSet rs = null;
			try {
				// Executa interogarea
				String queryString = ("SELECT * FROM " + tabela + " where " + denumireId + "='" + ID + "';");
				Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(queryString); //sql exception
			} catch (SQLException sqle) {
				error = "SQLException: Interogarea nu a fost posibila.";
				throw new SQLException(error);
			} catch (Exception e) {
				error = "A aparut o exceptie in timp ce se extrageau datele.";
				throw new Exception(error);
			}
			return rs;
		} // end of intoarceLinieDupaId()

		public ResultSet intoarceObservatieId(long ID) throws SQLException, Exception {
			ResultSet rs = null;
			try {
				// Executa interogarea
				String queryString = "SELECT "
						 + "utilizatori.nume, " 
						 + "utilizatori.email, "
						 + "utilizatori.data_nasterii, "
						 + "utilizatori.data_inregistrare, "
						 + "utilizatorisisteme.data_actualizare, "
						 + "utilizatorisisteme.prioritate, "
						 + "utilizatorisisteme.rol "
						 + "FROM utilizatori "
						 + "INNER JOIN utilizatorisisteme ON utilizatori.idUtilizatori = utilizatorisisteme.idUtilizatori";
				Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = stmt.executeQuery(queryString); //sql exception
			} catch (SQLException sqle) {
				error = "SQLException: Interogarea nu a fost posibila.";
				throw new SQLException(error);
			} catch (Exception e) {
				error = "A aparut o exceptie in timp ce se extrageau datele.";
				throw new Exception(error);
			}
			return rs;
		} // end of intoarceLinieDupaId()
	}
