package com.programando.fileupload.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.programando.fileupload.model.Produto;
import com.programando.fileupload.util.HibernateUtil;

public class ProdutoDAO implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Session session;

	public void save(Produto p) {
		session = HibernateUtil.getSessionfactory().openSession();

		try {
			session.getTransaction().begin();
			session.save(p);
			session.getTransaction().commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> listAll() {
		session = HibernateUtil.getSessionfactory().openSession();

		try {
			return session.createCriteria(Produto.class, "p").list();
		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}

		return null;
	}
}