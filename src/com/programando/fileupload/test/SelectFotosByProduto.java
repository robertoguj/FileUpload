package com.programando.fileupload.test;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.programando.fileupload.model.Foto;
import com.programando.fileupload.util.HibernateUtil;

public class SelectFotosByProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new SelectFotosByProduto().run();
	}

	@SuppressWarnings("unchecked")
	private void run() {
		Session session = HibernateUtil.getSessionfactory().openSession();

		try {
			List<Foto> fotos = session.createCriteria(Foto.class, "f")
					.createAlias("produto", "p")
					.add(Restrictions.eq("p.id", 1L)).list();

			for (Foto f : fotos) {
				System.out.println(f.getCodigo());
				System.out.println(f.getDescricao());
				System.out.println(f.getImagem());
			}

		} catch (HibernateException ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
}
