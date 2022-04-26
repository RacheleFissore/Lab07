package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.Poweroutages;

public class PowerOutageDAO {
	
	public List<Nerc> getNercList() {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}
	
	public List<Poweroutages> getEventiBlackout(String nercString) {
		List<Poweroutages> poweroutages = new LinkedList<Poweroutages>();
		// Faccio la query che mi restituisce tutto ciò che devo stampare nella textArea, ossia gli attributi della classe Poweroutages, tale
		// che il valore di Nerc sia pari a quello che ho selezionato nella combobox
		// HOUR(timediff(date_event_began, date_event_finished)): mi restituisce l'ora della differenza tra le ore di blackout nelle date
		final String sql = "SELECT HOUR(timediff(date_event_began, date_event_finished)) AS diff, YEAR(p.date_event_began) AS anno, p.date_event_began, p.date_event_finished, p.customers_affected "
				+ "FROM poweroutages AS p, nerc AS n "
				+ "WHERE n.id = p.nerc_id "
				+ "AND n.value = ? "
				+ "ORDER BY p.date_event_began";

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, nercString);
			ResultSet rs = st.executeQuery();

			Poweroutages r = null;
			while (rs.next()) {
				// rs.getTimestamp("date_event_began").toLocalDateTime() --> mi permette di prendere data e ora del LocalDateTime che ho usato
				// come tipo dell'attributo data inizio e data fine, in modo che nella stampa mi venga restituita sia la data che l'ora
				r = new Poweroutages(rs.getInt("diff"), rs.getInt("anno"), rs.getTimestamp("date_event_began").toLocalDateTime(), rs.getTimestamp("date_event_finished").toLocalDateTime(), rs.getInt("customers_affected"));
				poweroutages.add(r);
			}

			rs.close();
			conn.close();
			return poweroutages;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
