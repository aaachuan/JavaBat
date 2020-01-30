# Linux的文件权限与目录配置

## Linux文件权限的概念

### Linux文件属性

```
# ls -l /etc/shadow
---------- 1 root root 663 Jan 29 13:00 /etc/shadow
```

### 改变文件属性和权限

- chgrp
- chown
- chmod

```
[root@iZbp1intcn2jwb8rezoz9gZ ~]# cp .bashrc .bashrc_test
[root@iZbp1intcn2jwb8rezoz9gZ ~]# ls -al .bashrc*
-rw-r--r--. 1 root root 176 Nov 23  2016 .bashrc
-rw-r--r--  1 root root 176 Jan 30 10:43 .bashrc_test
[root@iZbp1intcn2jwb8rezoz9gZ ~]# chmod 777 .bashrc
[root@iZbp1intcn2jwb8rezoz9gZ ~]# ls -l .bashrc
-rwxrwxrwx. 1 root root 176 Nov 23  2016 .bashrc
[root@iZbp1intcn2jwb8rezoz9gZ ~]# chmod u=rwx,go=rx .bashrc
[root@iZbp1intcn2jwb8rezoz9gZ ~]# ls -l .bashrc
-rwxr-xr-x. 1 root root 176 Nov 23  2016 .bashrc
[root@iZbp1intcn2jwb8rezoz9gZ ~]# chmod a+w .bashrc
[root@iZbp1intcn2jwb8rezoz9gZ ~]# ls -l .bashrc
-rwxrwxrwx. 1 root root 176 Nov 23  2016 .bashrc
[root@iZbp1intcn2jwb8rezoz9gZ ~]# chmod a-x .bashrc
[root@iZbp1intcn2jwb8rezoz9gZ ~]# ls -l .bashrc
-rw-rw-rw-. 1 root root 176 Nov 23  2016 .bashrc
```

### 目录与文件的权限意义

`file`:
- r(read): 读文件内容
- w(write): 编辑、新增和修改文件内容（不包含删除该文件）
- x(execute): 被系统执行的权限

`directory`:
- r(read contents in directory): 读目录结构列表，查询该目录下的文件名数据（ls）
- w(modify contents of directory): 更改该目录结构列表的权限，即
  - 新建新的文件和目录
  - 删除已经存在的文件和目录（不论该文件的权限如何）
  - 将已存在的文件与目录进行重命名
  - 转移该目录内的文件、目录位置
- x(access directory): 用户能否进入该目录成为工作目录（cd）

```
[root@iZbp1intcn2jwb8rezoz9gZ ~]# cd /tmp
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# ls
Aegis-<Guid(5A2C30A2-A87D-490A-9281-6765EDAD7CBA)>  systemd-private-4880fbf450d04d4abf62692737b99e10-chronyd.service-eGpJvn
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# ls -al
total 32
drwxrwxrwt.  8 root root 4096 Jan 30 03:24 .
dr-xr-xr-x. 18 root root 4096 Jan 29 13:00 ..
srwxr-xr-x   1 root root    0 Jan 29 13:00 Aegis-<Guid(5A2C30A2-A87D-490A-9281-6765EDAD7CBA)>
drwxrwxrwt.  2 root root 4096 Aug 29 21:51 .font-unix
drwxrwxrwt.  2 root root 4096 Aug 29 21:51 .ICE-unix
drwx------   3 root root 4096 Jan 29 13:00 systemd-private-4880fbf450d04d4abf62692737b99e10-chronyd.service-eGpJvn
drwxrwxrwt.  2 root root 4096 Aug 29 21:51 .Test-unix
drwxrwxrwt.  2 root root 4096 Aug 29 21:51 .X11-unix
drwxrwxrwt.  2 root root 4096 Aug 29 21:51 .XIM-unix
```

rwt?

```
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# mkdir testing
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# chmod 744 testing
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# touch testing/testing
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# chmod 600 testing/testing
[root@iZbp1intcn2jwb8rezoz9gZ tmp]# ls -ald testing testing/testing
drwxr--r-- 2 root root 4096 Jan 30 11:20 testing
-rw------- 1 root root    0 Jan 30 11:20 testing/testing
```
## Linux目录配置

### Linux目录配置标准：FHS（Filesystem Hierarchy Standard）

[FHS](http://www.pathname.com/fhs/)

### 目录树（directory tree）


