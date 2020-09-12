# What Happened When Query SQL Running

## MySQL的逻辑架构
MySQL可以分为Server层和存储引擎层两个部分。

Server层包含连接器、查询缓存、分析器、优化器和执行器等，以及所有的内置函数，包含跨存储引擎的功能（存储过程、触发器、视图等）。

而存储引擎负责存储数据和提供读写接口，架构模式为**插件式**，支持InnoDB、MyISAM、Memory等多个存储引擎，常用并且默认的为InnoDB。

## 连接器（管理连接，权限验证） 
对于MySQL的连接方式，到MySQL5.7为止，总共有五种连接方式，分别是TCP/IP，TLS/SSL，Unix Sockets，Shared Memory，Named pipes。
### TCP/IP
```
mysql --protocol=tcp -uroot -p
mysql -P3306 -h127.0.0.1 -uroot -p
```
### TLS/SSL
```
mysql --protocol=tcp -uroot --ssl=on -p
mysql -P3306 -h127.0.0.1 -uroot --ssl=on -p
```
TLS/SSL基于TCP/IP，只需再指定打开SSL配置即可。

**注**：在自己电脑MySQL 5.7测试的时候，使用TCP/IP连接的时候需要指定--ssl=off（或者使用--ssl-mode=disanled，command提示WARNING: --ssl is deprecated and will be removed in a future version. Use --ssl-mode instead.），默认都是TLS/SSL。
```
mysql> SELECT DISTINCT connection_type from performance_schema.threads where connection_type is not null;
+-----------------+
| connection_type |
+-----------------+
| SSL/TLS         |
| TCP/IP          |
+-----------------+
2 rows in set (0.01 sec)

```
### Unix Sockets
```
mysql -uroot
```
如果在MySQL本机使用这种方式连接MySQL数据库，默认使用Unix Sockets。
```
mysql> SELECT DISTINCT connection_type from performance_schema.threads where connection_type is not null;
+-----------------+
| connection_type |
+-----------------+
| Socket          |
+-----------------+
1 row in set (0.00 sec)
```

正常情况下，与服务端建立连接，在完成TCP握手后，连接器需要开始身份认证（user&password），认证过后会到权限表里面查出当前用户拥有的权限。

查看连接情况： 
```
mysql> show processlist;
+----+------+-----------------+------+---------+------+----------+------------------+
| Id | User | Host            | db   | Command | Time | State    | Info             |
+----+------+-----------------+------+---------+------+----------+------------------+
| 26 | root | localhost:49687 | NULL | Sleep   | 3524 |          | NULL             |
| 27 | root | localhost:49690 | NULL | Sleep   | 1523 |          | NULL             |
| 33 | root | localhost:50426 | NULL | Query   |    0 | starting | show processlist |
| 35 | root | localhost:50437 | NULL | Sleep   |   19 |          | NULL             |
+----+------+-----------------+------+---------+------+----------+------------------+
4 rows in set (0.00 sec)
```
根据断开连接的时机，连接分为`长连接`和`短连接`。

数据库里面，长连接是指连接成功后，如果客户端持续有请求，则一直使用同一个连接。短连接则是指每次执行完很少的几次查询就断开连接，下次查询再重新建立一个。

接下来先感受下mysql的各种超时参数(挖坑):
```
mysql> show variables like '%timeout%';
+-----------------------------+----------+
| Variable_name               | Value    |
+-----------------------------+----------+
| connect_timeout             | 10       |
| delayed_insert_timeout      | 300      |
| have_statement_timeout      | YES      |
| innodb_flush_log_at_timeout | 1        |
| innodb_lock_wait_timeout    | 50       |
| innodb_rollback_on_timeout  | OFF      |
| interactive_timeout         | 28800    |
| lock_wait_timeout           | 31536000 |
| net_read_timeout            | 30       |
| net_write_timeout           | 60       |
| rpl_stop_slave_timeout      | 31536000 |
| slave_net_timeout           | 60       |
| wait_timeout                | 28800    |
+-----------------------------+----------+
13 rows in set, 1 warning (0.01 sec)
```
## 查询缓存（命中则直接返回结果）
...
