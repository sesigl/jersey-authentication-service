package tokenBasedAuthentification.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateFactory {

    private static SessionFactory sessionFactory;

    private HibernateFactory() {
    }

    private static void initSessionFactoryIfNecessary() {
        if(sessionFactory == null) {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }

    }

    public static Session CreateNewSession() {
        initSessionFactoryIfNecessary();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        return session;
    }

    public static void CloseSession(Session session) {
        session.getTransaction().commit();
        session.close();
    }

}
