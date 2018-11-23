package com.ragavan.sample.dao;

import javax.persistence.NoResultException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ragavan.sample.dao.exception.DataAccessException;
import com.ragavan.sample.models.User;

@Repository
public class UserDAO {
	@Autowired
	SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(UserDAO.class);

	public User retrieveUserByEmail(String email) throws DataAccessException {
		User level = null;
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		try{
		Query<User> query = session.createQuery("from User where emailId=:email", User.class);
		query.setParameter("email", email);
		level = query.getSingleResult();}
		catch(NoResultException e){
			logger.error(e.getMessage(),e);
			throw new DataAccessException(e.getMessage(),e);
		}
		transaction.commit();
		session.close();
		return level;
	}
}