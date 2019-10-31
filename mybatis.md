##### mybatis?

MyBatis is a first class persistence framework with support for custom SQL, stored procedures and advanced mappings. MyBatis eliminates almost all of the JDBC code and manual setting of parameters and retrieval of results. MyBatis can use simple XML or Annotations for configuration and map primitives, Map interfaces and Java POJOs (Plain Old Java Objects) to database records.

##### 创建数据库表

```
mysql> show databases;
+--------------------+
| Database           |
+--------------------+
| information_schema |
| mysql              |
| performance_schema |
| root               |
| test               |
+--------------------+
5 rows in set (0.03 sec)

mysql> create database how2java;
Query OK, 1 row affected (0.01 sec)

mysql> USE how2java;
Database changed
mysql> create table product_ (
    -> id int(11) NOT NULL AUTO_INCREMENT,
    -> name varchar(32) DEFAULT NULL,
    -> price float(10,2) DEFAULT NULL,
    -> PRIMARY KEY(id)
    -> ) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
Query OK, 0 rows affected (0.00 sec)

mysql> INSERT INTO product_ VALUES (1,'product1',3.145);
Query OK, 1 row affected (0.11 sec)

mysql> INSERT INTO product_ VALUES (2,'product2',3.14);
Query OK, 1 row affected (0.00 sec)

```
##### project

新建java project并在根目录下创建lib目录，导入两个jar包：`mybatis-3.4.2.jar`和`mysql-connector-java-5.0.8-bin.jar`。

创建实体类Product：

```
package com.how2java.pojo;

public class Product {
	private int id;
	private String name;
	private float price;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

}

```

在src目录下创建mybatis的主配置文件mybatis-config.xml。其作用主要是提供连接数据库用的驱动，数据库名称，编码方式，账号密码以及别名，
自动扫描com.how2java.pojo下的类型，使得在后续配置文件Product.xml中使用resultType的时候，可以直接使用Product,而不必写全com.how2java.pojo.Product，映射Product.xml。

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <typeAliases>
      <package name="com.how2java.pojo"/>
    </typeAliases>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/how2java?characterEncoding=UTF-8"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="com/how2java/pojo/Category.xml"/>
        <mapper resource="com/how2java/pojo/Product.xml"/>
    </mappers>
</configuration>
```

在包com.how2java.pojo下，新建文件Product.xml。

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
 
    <mapper namespace="com.how2java.pojo">
        <select id="listProduct" resultType="Product">
            select * from  product_     
        </select>
    </mapper>
```

测试类TestMybatis：

```
package com.how2java;
 
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
 

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
 

import com.how2java.pojo.Category;
import com.how2java.pojo.Product;
 
public class TestMybatis {
 
    public static void main(String[] args) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session=sqlSessionFactory.openSession();
//         
//        List<Category> cs=session.selectList("listCategory");
//        for (Category c : cs) {
//            System.out.println(c.getName());
//        }
        
          List<Product> ps=session.selectList("listProduct");
          for (Product p : ps) {
			System.out.println(p.getId()+" "+p.getName()+" "+p.getPrice());
		}
        
         
    }
}

```

结果：

```
1 product1 3.15
2 product2 3.14
```

##### CURD

- 配置文件Category.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
 
    <mapper namespace="com.how2java.pojo">
        <insert id="addCategory" parameterType="Category" >
            insert into category_ ( name ) values (#{name})   
        </insert>
         
        <delete id="deleteCategory" parameterType="Category" >
            delete from category_ where id= #{id}  
        </delete>
         
        <select id="getCategory" parameterType="_int" resultType="Category">
            select * from   category_  where id= #{id}   
        </select>
 
        <update id="updateCategory" parameterType="Category" >
            update category_ set name=#{name} where id=#{id}   
        </update>
        <select id="listCategory" resultType="Category">
            select * from   category_     
        </select>    
    </mapper>
```

- TestMybatis

```
package com.how2java;
 
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
 

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
 

import com.how2java.pojo.Category;
import com.how2java.pojo.Product;
 
public class TestMybatis {
 
    public static void main(String[] args) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session=sqlSessionFactory.openSession();
        
//        Category c = new Category();
//        c.setName("新增的Category");
//        session.insert("addCategory",c);
//        c.setId(6);
//        session.delete("deleteCategory",c);
//        
//        listAll(session);
//        
        
        Category c= session.selectOne("getCategory",3);
        
        System.out.println(c.getName());
 
        c.setName("修改了的Category名稱");
        session.update("updateCategory",c);
         
        listAll(session);
         
         
//      listAll(session);
        session.commit();
        session.close();
//        
//          List<Product> ps=session.selectList("listProduct");
//          for (Product p : ps) {
//			System.out.println(p.getId()+" "+p.getName()+" "+p.getPrice());
//		}
//        
//         
    }

	private static void listAll(SqlSession session) {
		List<Category> cs=session.selectList("listCategory");
        for (Category c : cs) {
            System.out.println(c.getName());
        }
	}
}

   
```



