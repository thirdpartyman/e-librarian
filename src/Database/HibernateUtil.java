package Database;


import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;

import javax.persistence.FetchType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;



//соединение с базой данных Oracle через интерфейс Hibernate

public class HibernateUtil {
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				LogManager.getLogManager().reset();// убираем логирование при создании соединения (слишком уж его много)

				Configuration configuration = new Configuration();

				// Hibernate settings equivalent to hibernate.cfg.xml's properties
				Properties settings = new Properties();
//				settings.put(Environment.DRIVER, "oracle.jdbc.OracleDriver");
//				settings.put(Environment.URL, "jdbc:oracle:thin:@localhost:1521:test");
//				settings.put(Environment.USER, "system");
//				settings.put(Environment.PASS, "password");
//				settings.put(Environment.DIALECT, "org.hibernate.dialect.Oracle12cDialect");
				
				settings.put(Environment.DRIVER, "org.h2.Driver");
				settings.put(Environment.URL, "jdbc:h2:./test");
				settings.put(Environment.USER, "sa");
				settings.put(Environment.PASS, "");
				settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
				
				
				settings.put(Environment.SHOW_SQL, "true");// вывод генерируемых sql запросов

				settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
				settings.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, true);//to avoid error "Hibernate could not initialize proxy – no Session" due to FetchType.LAZY

				settings.put(Environment.HBM2DDL_AUTO, "update");
//				settings.put(Environment.HBM2DDL_AUTO, "create-drop");

				configuration.setProperties(settings);

				configuration.addAnnotatedClass(Librarian.class);
				configuration.addAnnotatedClass(Reader.class);
				configuration.addAnnotatedClass(Book.class);
				configuration.addAnnotatedClass(Formular.class);
				configuration.addAnnotatedClass(BBK.class);
				configuration.addAnnotatedClass(Author.class);
				configuration.addAnnotatedClass(PublishHouse.class);

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();

				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}

	public static <T> List<T> loadAllData(Class<T> type) {
		var session = getSessionFactory().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(type);
		Root<T> root = criteria.from(type);
		criteria.select(root);
		List<T> data = session.createQuery(criteria).getResultList();
		session.close();
		return data;
	}

	public static <T> void insert(T object) {
		var session = getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		session.save(object);
		transaction.commit();
		session.close();
	}

	public static <T> void update(T object) {
		var session = getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		session.update(object);
		transaction.commit();
		session.close();
	}
	
	public static <T> void update(List<T> list) {
		var session = getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		for (T item : list)
		{
			item = (T) session.merge(item);
			session.update(item);
		}
		transaction.commit();
		session.close();
	}
}