package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOException;
import hr.fer.zemris.java.hw14.model.Poll;
import hr.fer.zemris.java.hw14.model.PollOption;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 * 
 * @author Bruno Iljazović
 */
public class SQLDAO implements DAO {

	@Override
	public List<Poll> getPolls() {
		List<Poll> polls = new ArrayList<>();
		Connection dbConnection = SQLConnectionProvider.getConnection();
		System.out.println(dbConnection == null);
		try (PreparedStatement pst = dbConnection.prepareStatement(
				"SELECT id, title, message FROM polls");
			ResultSet rs = pst.executeQuery();
		) {
			while (rs != null && rs.next()) {
				polls.add(new Poll(rs.getLong(1), rs.getString(2), rs.getString(3)));
			}
		} catch(Exception ex) {
			throw new DAOException("Error while getting list of polls: ", ex);
		}
		return polls;
	}

	@Override
	public List<PollOption> getOptions(long pollId) {
		List<PollOption> options = new ArrayList<>();
		Connection dbConnection = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = dbConnection.prepareStatement(
				"SELECT id, optiontitle, optionlink, pollid, votescount FROM polloptions WHERE pollid=?");
		) {
			pst.setLong(1, pollId);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs != null && rs.next()) {
					options.add(new PollOption(
							rs.getLong(1), 
							rs.getString(2), 
							rs.getString(3),
							rs.getLong(4), 
							rs.getLong(5)
					));
				}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while getting list of poll options: ", ex);
		}
		return options;
	}

	@Override
	public Poll getPoll(long pollId) {
		Poll poll = null;
		Connection dbConnection = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = dbConnection.prepareStatement(
				"SELECT id, title, message FROM polls WHERE id=?");
		) {
			pst.setLong(1, pollId);
			try (ResultSet rs = pst.executeQuery()) {
				while (rs != null && rs.next()) {
					poll = new Poll(rs.getLong(1), rs.getString(2), rs.getString(3));
				}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while getting a poll: ", ex);
		}
		return poll;
	}

	@Override
	public Long vote(long optionId) {
		Connection dbConnection = SQLConnectionProvider.getConnection();
		try (PreparedStatement pst = dbConnection.prepareStatement(
				"UPDATE PollOptions SET votesCount=votesCount+1 WHERE id=?");
		) {
			pst.setLong(1, optionId);
			pst.executeUpdate();
		} catch(Exception ex) {
			throw new DAOException("Error while processing a vote: ", ex);
		}
		
		Long pollid = null;
		try (PreparedStatement pst = dbConnection.prepareStatement(
				"SELECT pollid FROM PollOptions WHERE id=?");
		) {
			pst.setLong(1, optionId);
			try (ResultSet rs = pst.executeQuery();) {
				if  (rs != null && rs.next()) {
					pollid = rs.getLong(1);
				}
			}
		} catch(Exception ex) {
			throw new DAOException("Error while processing a vote: ", ex);
		}
		
		return pollid;
	}
}