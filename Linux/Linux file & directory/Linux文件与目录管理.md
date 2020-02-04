# Linux文件与目录管理

## 目录与路径

### 目录的相关操作

* pwd
```
[root@iZbp1intcn2jwb8rezoz9gZ ~]# pwd
/root
[root@iZbp1intcn2jwb8rezoz9gZ ~]# cd /var/mail
[root@iZbp1intcn2jwb8rezoz9gZ mail]# pwd
/var/mail
[root@iZbp1intcn2jwb8rezoz9gZ mail]# pwd -P
/var/spool/mail
[root@iZbp1intcn2jwb8rezoz9gZ mail]# ls -d /var/mail
/var/mail
[root@iZbp1intcn2jwb8rezoz9gZ mail]# ls -ld /var/mail
lrwxrwxrwx. 1 root root 10 Aug 29 21:50 /var/mail -> spool/mail
```

`-P`:显示出当前的路径，而非使用连接（link）路径。

* mkdir
```
[root@iZbp1intcn2jwb8rezoz9gZ ~]# cd /tmp
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# mkdir test
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# mkdir test1/test2/test3/test4
mkdir: cannot create directory ‘test1/test2/test3/test4’: No such file or directory
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# mkdir -p test1/test2/test3/test4
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# mkdir -m 711 test2
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# ls -l
total 20
srwxr-xr-x 1 root root    0 Jan 29 13:00 Aegis-<Guid(5A2C30A2-A87D-490A-9281-6765EDAD7CBA)>
drwx------ 3 root root 4096 Jan 29 13:00 systemd-private-4880fbf450d04d4abf62692737b99e10-chronyd.service-eGpJvn
drwxr-xr-x 2 root root 4096 Feb  4 15:52 test
drwxr-xr-x 3 root root 4096 Feb  4 15:52 test1
drwx--x--x 2 root root 4096 Feb  4 15:53 test2
drwxr--r-- 2 root root 4096 Jan 30 11:20 testing
```
`-p`:直接将所需要的目录（包括上一层目录）递归创建。

`-m`:创建时配置新目录权限。

* rmdir
```
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# rmdir test
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# rmdir test1
rmdir: failed to remove ‘test1’: Directory not empty
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# rmdir -p test1/test2/test3/test4
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# ls -l
total 12
srwxr-xr-x 1 root root    0 Jan 29 13:00 Aegis-<Guid(5A2C30A2-A87D-490A-9281-6765EDAD7CBA)>
drwx------ 3 root root 4096 Jan 29 13:00 systemd-private-4880fbf450d04d4abf62692737b99e10-chronyd.service-eGpJvn
drwx--x--x 2 root root 4096 Feb  4 15:53 test2
drwxr--r-- 2 root root 4096 Jan 30 11:20 testing
```

`-p`:连同上层的空目录也一并删除。

### 文件路径的变量：$PATH
```
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# echo $PATH
/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/root/bin
```
- 不同的身份用户默认的PATH不同，默认能够随意执行的命令也不同。
- 本目录（.）最好不要放到PATH当中。

## 文件与目录管理

`ls`:
```
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# ls -alF ~
total 52
dr-xr-x---.  5 root root 4096 Jan 30 10:43 ./
dr-xr-xr-x. 18 root root 4096 Jan 29 13:00 ../
-rw-------   1 root root  904 Jan 30 13:03 .bash_history
-rw-r--r--.  1 root root   18 Nov 23  2016 .bash_logout
-rw-r--r--.  1 root root  176 Nov 23  2016 .bash_profile
-rw-rw-rw-.  1 root root  176 Nov 23  2016 .bashrc
-rw-r--r--   1 root root  176 Jan 30 10:43 .bashrc_test
drwx------   3 root root 4096 Aug 29 13:58 .cache/
-rw-r--r--.  1 root root  100 Nov 23  2016 .cshrc
drwxr-xr-x   2 root root 4096 Aug 29 13:58 .pip/
-rw-r--r--   1 root root  205 Jan 29 13:00 .pydistutils.cfg
drwx------   2 root root 4096 Aug 29 14:04 .ssh/
-rw-r--r--.  1 root root  129 Nov 23  2016 .tcshrc
```
