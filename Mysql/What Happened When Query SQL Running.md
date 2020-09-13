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
MySQL在查询缓存保存查询返回的完整结果。当查询命中该缓存，MySQL会立刻返回结果，跳过解析、优化和执行阶段。缓存存放在一个引用表，通过一个哈希值引用，包含了查询本身、当前需要查询的数据库、客户端协议的版本等一些可能会影响返回结果的信息。当查询的表发生变化，那个和这个表相关的所有缓存数据都会失效。（有点坑啊有木有...）

MySQL8.0开始直接把查询缓存的功能卸掉了。。。
```
mysql> SHOW GLOBAL VARIABLES LIKE 'query_cache%';
+------------------------------+---------+
| Variable_name                | Value   |
+------------------------------+---------+
| query_cache_limit            | 1048576 |
| query_cache_min_res_unit     | 4096    |
| query_cache_size             | 1048576 |
| query_cache_type             | OFF     |
| query_cache_wlock_invalidate | OFF     |
+------------------------------+---------+
5 rows in set (0.00 sec)
```
```
query_cache_limit :  MySQL能够缓存的最大查询结果；如果某查询的结果大小大于此值，则不会被缓存；
query_cache_min_res_unit : 查询缓存中分配内存的最小单位；(注意：此值通常是需要调整的，此值被调整为接近所有查询结果的平均值是最好的)
                           计算单个查询的平均缓存大小：（query_cache_size-Qcache_free_memory）/Qcache_queries_in_cache
query_cache_size : 查询缓存的总体可用空间，单位为字节；其必须为1024的倍数；
query_cache_type: 查询缓存类型；是否开启缓存功能，开启方式有三种{ON|OFF|DEMAND}；
query_cache_wlock_invalidate : 当其它会话锁定此次查询用到的资源时，是否不能再从缓存中返回数据；（OFF表示可以从缓存中返回数据）
```
```

mysql> SHOW  GLOBAL STATUS  LIKE  'Qcache%';
+-------------------------+---------+
| Variable_name           | Value   |
+-------------------------+---------+
| Qcache_free_blocks      | 1       | #查询缓存中的空闲块
| Qcache_free_memory      | 1031832 | #查询缓存中尚未使用的空闲内存空间
| Qcache_hits             | 0       | #缓存命中次数
| Qcache_inserts          | 0       | #向查询缓存中添加缓存记录的条数
| Qcache_lowmem_prunes    | 0       | #表示因缓存满了而不得不清理部分缓存以存储新的缓存，这样操作的次数。若此数值过大，则表示缓存空间太小了。
| Qcache_not_cached       | 5       | #没能被缓存的次数
| Qcache_queries_in_cache | 0       | #此时仍留在查询缓存的缓存个数
| Qcache_total_blocks     | 1       | #共分配出去的块数
+-------------------------+---------+
8 rows in set (0.00 sec)

```
衡量缓存是否有效：
```
mysql> SHOW GLOBAL STATUS WHERE Variable_name='Qcache_hits' OR Variable_name='Com_select';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| Com_select    | 5     |
| Qcache_hits   | 0     |
+---------------+-------+
2 rows in set (0.00 sec)
```
缓存命中率：Qcache_hits/(Qcache_hits+Com_select)
```
mysql> SHOW GLOBAL STATUS WHERE Variable_name='Qcache_hits' OR Variable_name='Qcache_inserts';
+----------------+-------+
| Variable_name  | Value |
+----------------+-------+
| Qcache_hits    | 0     |
| Qcache_inserts | 0     |
+----------------+-------+
2 rows in set (0.00 sec)
```
“命中和写入”的比率: Qcache_hits/Qcache_inserts # 如果此比值大于3:1, 说明缓存也是有效的；如果高于10:1，相当理想；

题外话：存储引擎层-innodb buffer pool
## 分析器（词法分析、语法分析）
## 优化器（执行计划生成，索引选择）
## 执行器（操作引擎，返回结果）
