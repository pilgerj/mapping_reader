package data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

	private String local;
	private String user;
	private String senha;
	private Connection c;
	private Statement statment;
	private String strConexao;
	private String driverjdbc;

	public Conexao(String strConexao, String user, String senha, String driver) {
			setStrConexao(strConexao);
			setSenha(senha);
			setUser(user);
			setDriverjdbc(driver);
	}

	public void configUser(String user, String senha) {
		setUser(user);
		setSenha(senha);
	}

	public void configLocal(String banco) {
		setLocal(banco);
	}

	// Conexão com o Banco de Dados
	public void connect() {
		try {
			Class.forName(getDriverjdbc());
			setC(DriverManager.getConnection(getStrConexao(), getUser(), getSenha()));
			setStatment(getC().createStatement());
			System.out.println("Conectado com sucesso: " + getStrConexao() );
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			getC().close();
			System.out.println("Desconectado.");
		} catch (SQLException ex) {
			System.err.println(ex);
			ex.printStackTrace();
		}
	}

	public ResultSet query(String query) {
		try {
			return getStatment().executeQuery(query);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// GETs AND SETs
	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Connection getC() {
		return c;
	}

	public void setC(Connection c) {
		this.c = c;
	}

	public Statement getStatment() {
		return statment;
	}

	public void setStatment(Statement statment) {
		this.statment = statment;
	}

	public String getStrConexao() {
		return strConexao;
	}

	public void setStrConexao(String strConexao) {
		this.strConexao = strConexao;
	}

	public String getDriverjdbc() {
		return driverjdbc;
	}

	public void setDriverjdbc(String driverjdbc) {
		this.driverjdbc = driverjdbc;
	}

}
