# hibernate_Demo
这是一个单纯的hibernate示例
createtable EMPLOYEE (
 id INTNOTNULL auto_increment,
 first_name VARCHAR(20) defaultNULL,
 last_name VARCHAR(20) defaultNULL, 
 salary INTdefaultNULL, 
PRIMARYKEY (id) );

第二次上传：添加了注释方法。
EJB 3 标准的注释包含在 javax.persistence 包，所以我们第一步需要导入这个包。
<dependency>
 <groupId>javax.persistence</groupId>
 <artifactId>persistence-api</artifactId>
 <version>1.0.2</version>
</dependency>

然后在实体类中添加注释
@Entity
@Table(name="EMPLOYEE")
public class Employee {

 @Id @GeneratedValue
 @Column(name="id")
 private int id;
 
 @Column(name="first_name")
 private String firstName; 
 
 @Column(name="last_name")
 private String lastName;
 
 @Column(name = "salary")
 private int salary;  

删除映射文件，并将配置文件hibernate.cfg.xml中的映射文件配置也删除。

main方法中session工厂获取配置也需要修改为：
  factory = new AnnotationConfiguration().configure().addPackage("com.hand.create_hibernate")
         .addAnnotatedClass(Employee.class).buildSessionFactory();

添加数据的方法不能使用之前的
Employee employee = new Employee(fname, lname, salary);
而是要先实例化Employee 
Employee employee = new Employee();
然后对每个列进行赋值：
employee.setFirstName(fname);
         employee.setLastName(lname);
         employee.setSalary(salary);
