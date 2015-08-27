package com.hand.create_hibernate;
import java.util.List; 
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException; 
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ManageEmployee {
	//定义一个session工厂
   private static SessionFactory factory; 
   public static void main(String[] args) {
      try{
    	  //通过session工厂获取JDBC配置信息
//         factory = new Configuration().configure().buildSessionFactory();
    	  factory = new AnnotationConfiguration().configure().addPackage("com.hand.create_hibernate")
    			  .addAnnotatedClass(Employee.class).buildSessionFactory();
         
      }catch (Throwable ex) { 
         System.err.println("Failed to create sessionFactory object." + ex);
         throw new ExceptionInInitializerError(ex); 
      }
      ManageEmployee ME = new ManageEmployee();

//      添加数据
      Integer empID1 = ME.addEmployee("jky", "Ali", 1000);
      Integer empID2 = ME.addEmployee("Daisy", "Das", 5000);
      Integer empID3 = ME.addEmployee("John", "Paul", 10000);

//      调用列出所有的信息的方法
      ME.listEmployees();

//      调用更新数据的方法
      ME.updateEmployee(empID1, 5000);

//      调用删除数据方法
      ME.deleteEmployee(empID2);

//      列出所有用户
      ME.listEmployees();
      
//      查询工资超过5000的员工
      ME.Employees_for_salary(5000);
      
      ME.totalSalary();
      
      ME.countEmployee();
      
   }
//   实现增加数据
   public Integer addEmployee(String fname, String lname, int salary){
      Session session = factory.openSession();
      Transaction tx = null;
      Integer employeeID = null;
      try{
         tx = session.beginTransaction();
//         Employee employee = new Employee(fname, lname, salary);
//         employeeID = (Integer) session.save(employee); 
         Employee employee = new Employee();
         employee.setFirstName(fname);
         employee.setLastName(lname);
         employee.setSalary(salary);
         employeeID = (Integer) session.save(employee);
         System.out.println("添加了数据："+fname+","+lname+", "+salary);
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
      return employeeID;
   }
//   实现读取数据
   public void listEmployees( ){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
//         分步使用hql查询数据
//         String hql = "FROM Employee";//这里的Empolyee并不是指表名，而是指持久化类中的文件名称。
//         如果要限定包名的持久化类，则要使用 "FROM com.hibernatebook.criteria.Employee"
//         Query query = session.createQuery(hql);
//         List results = query.list();
//         一步完成hql查询数据
         List employees = session.createQuery("FROM Employee").list(); 
         for (Iterator iterator = 
                           employees.iterator(); iterator.hasNext();){
            Employee employee = (Employee) iterator.next(); 
            System.out.print("First Name: " + employee.getFirstName()); 
            System.out.print("  Last Name: " + employee.getLastName()); 
            System.out.println("  Salary: " + employee.getSalary()); 
         }
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
   }
//   实现更新数据
   public void updateEmployee(Integer EmployeeID, int salary ){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         Employee employee = 
                    (Employee)session.get(Employee.class, EmployeeID); 
         employee.setSalary( salary );
         session.update(employee); 
         System.out.println("将ID为："+EmployeeID+"的工资改为了："+salary);
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
   }
//   实现删除数据
   public void deleteEmployee(Integer EmployeeID){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         Employee employee = 
                   (Employee)session.get(Employee.class, EmployeeID); 
         session.delete(employee); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
   }
//   标准查询
//   查找超过指定工资的员工
   public void Employees_for_salary(int salary){
	   Session session = factory.openSession();
	   Transaction tx = null;
	   try {
		   tx = session.beginTransaction();
		   Criteria cr = session.createCriteria(Employee.class);
		   cr.add(Restrictions.gt("salary", salary));
		   List employees = cr.list();
		   System.out.println("以下是工资为："+salary+"的员工。");
		   for (Iterator iterator = employees.iterator();iterator.hasNext();) {
			   Employee employee = (Employee) iterator.next();
			   System.out.print("First Name: " + employee.getFirstName()); 
	            System.out.print("  Last Name: " + employee.getLastName()); 
	            System.out.println("  Salary: " + employee.getSalary()); 
		}
		   tx.commit();
		
	} catch (HibernateException e) {
		
		e.printStackTrace();
	}finally {
		session.close();
	}
   }
   
   public void countEmployee(){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Criteria cr = session.createCriteria(Employee.class);

	         // To get total row count.
	         cr.setProjection(Projections.rowCount());
	         List rowCount = cr.list();

	         System.out.println("工资总数为: " + rowCount.get(0) );
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
	  /* Method to print sum of salaries */
	   public void totalSalary(){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Criteria cr = session.createCriteria(Employee.class);

	         // To get total salary.
	         cr.setProjection(Projections.sum("salary"));
	         List totalSalary = cr.list();

	         System.out.println("员工人数为: " + totalSalary.get(0) );
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }
   
   
   
   
}