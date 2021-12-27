package com.spring.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class RadiologyRepository {
	
	@Autowired
	SessionFactory sessionFactory;
	
	public List<Any> getAllScans(long  id) {
	    Session s= sessionFactory.openSession();
	   Transaction tr=s.beginTransaction();
	   String sql = "select p.patient_id, p.mobile_number, p.first_name, p.last_name,pt.tests_test_id,t.test_name from patient p join patient_tests pt join tests t join laboratory l"+
               " "+"where l.lab_id=:lab_id AND t.lab_id=:lab_id AND pt.tests_test_id=t.test_id AND pt.patient_patient_id=p.patient_id";
	
		SQLQuery query = s.createSQLQuery(sql);
		query.setParameter("lab_id", id);
		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Any> results = query.list();
	    tr.commit();
	    s.close();
	   return results;
	    
	}
}
