######5种数据类型
    1.String 字符串（可以为整形、浮点型和字符串，统称为元素）
        常用命令：set name value,get name,自加incr,自减decr,加incrby,减decrby
    2，list 列表（实现队列，元素不唯一，先进先出原则）
        lpush:从左边推入，rpush从右边推入,lpop从左边弹出，rpop从右边弹出，llen查看某个list数据类型的长度。例：lpush list1 aaa
    3.set 集合（各个不相同的元素）
        sadd添加数据，scard查看set数据种存在的元素个数，sismember判断set数据中是否存在某个元素，srem删除某个set数据中的元素。
    4.hash hash散列值（hash的key必须是唯一的）
        hset添加hash元素，hget获取hash数据，hmget获取多个hash数据
    5.sort set：有序集合，和hash相似，也是映射形式的存储
        zadd添加，zcard查询，zrange数据排序
#####多个数据库
    Redis支持多数据库，并且每个数据库的数据是隔离的不能共享，并且基于单机才有，如果是集群就没有数据库的概念。Redis是一个字典结构的存储服务器，而实际上
    一个Redis实例提供了多个用来存储数据的字典，每个字典可以理解为一个独立的数据库。每个数据库对外都以一个从0开始的递增数字命名，默认支持16个数据库，可
    以通过配置databases来修改。可以通过select命令更换数据库，如：select 1，即选取1号数据库。Redis不支持自定义数据库的名字，每个数据库以编号命名。也
    不支持为每个数据库设置不同的密码。多个数据库之间不是完全隔离的，如flushall命令可以清空一个Redis实例中所有数据库数据，
#####持久化
    1.持久化流程：客户端向服务端发送写操作（数据在客户端内存中），数据库服务端接收到请求的数据（数据在服务端的内存中），服务端调用write系统调用将数据
        往磁盘上写（数据在系统内存的缓冲区上），操作系统将缓冲区中的数据移动到磁盘控制器上（数据在磁盘缓存中），磁盘控制器将数据写到磁盘的物理介质上
        （数据真正落到磁盘上）。
        考虑redis实现上面5个保存磁盘的步骤，Redis提供了两种策略机制，即：RDB和AOF
    2.RDB机制
        RDB就是把数据以快照的形式保存在磁盘上。RDB持久化是指在指定的时间间隔内将内存中的数据集快照写入磁盘。也是默认的持久化方式，这种方式就是就是将
        内存中数据以快照的方式写入到二进制文件中，默认文件名：dump.rdb。既然RDB机制是通过把某个时刻的所有数据生成一个快照来保存，那么就有一种触发机
        制实现这个过程。对于RDB来说，提供了三种机制：save、bgsave、自动化。
        1.save触发机制：该命令会阻塞当前Redis服务器，执行save命令期间，Redis不能处理其他命令，直到RDB过程完成。执行完毕后如果存在老的RDB文件，就把
        老的替换掉。
        2.bgsave触发方式：执行该命令时，Redis会在后台异步进行快照操作，快照同时还可以响应客户端请求。具体操作是Redis进程执行fork操作创建子进程，RDB
        持久化过程由子进程负责，完成后自动结束。阻塞只发生在fork阶段，一般时间很短。基本上Redis内部所有的RDB操作都是采用bgsave命令。
        3.自动触发：由配置文件来完成，redis.conf
        优势和劣势：（优势）RDB文件紧凑，全量备份，适用于进行备份和灾难恢复。生成RDB文件的时候，redis主进程会fork()一个子进程来处理所有保存工作，主
        进程不需要进行任何磁盘IO操作。RDB在恢复大数据集时的速度比AOF的恢复速度要快。（劣势）RDB快照是一次全量备份，存储的是内存数据的二进制序列化形
        式，存储非常紧凑。当进行快照持久化时，会开启一个子进程专门负责快照持久化，子进程会拥有父进程的内存数据，父进程修改内存子进程可能不会反应过来，
        所以在快照持久化期间修改的数据不会被保存，可能丢失数据。
    3.AOF机制
        持久化原理：一种高效机制，redis会将每一个收到的写命令都通过write函数追加到文件中。
        文件重写原理：AOF方式带来的问题，持久化文件越来越大。为了压缩aof的持久化文件，redis提供了bgwriteaof命令，将内存中的数据以命令的方式保存到
        临时文件中，同时会fork出一条新进程来将文件重写。重写aof文件的操作，并没有读取旧的aof文件，而是将整个内存中的数据库内容用命令的方式重写了一个
        新的aof文件。
    4.两种机制比较：
        RDB：启动优先级低，体积小，恢复速度块，数据安全性：丢数据
        AOF:启动优先级高，体积大，恢复速度慢，数据安全性：根据策略决定
#####基于redis实现分布式锁
    背景：原单体单机部署的系统被演化成分布式集群部署系统后，由于分布式系统多线程、多进程并且分布在不同机器上，这将使原单机部署情况下的并发控制策略失
        效，单纯的应用不能提供分布式锁的能力。为了解决这个问题就需要一种跨机器的互斥机制来控制共享资源的访问，这就是分布式锁要解决的问题。
    Redis提供setnx、getset命令，实现分布式锁机制。
    Redis命令介绍：
        SETNX命令(set if not exists)
        语法：SETNX key value
        功能：当且仅当key不存在，将key的值设为value，并返回1；若key已存在，则setnx不做任何操作，返回0；
        GETSET命令
        语法：GETSET key value
        功能：将给定key的值设为value，并返回key的旧值，当key存在但不是字符串类型时，返回一个错误，当key不存在时，返回nil;
        GET命令
        语法：GET key
        功能：返回key所关联的字符串值，如果key不存在那么返回特殊值nil
        DEL命令
        语法：DEL key [KEY ...]
        功能：删除给定的一个或多个key,不存在的key会被忽略。
    加锁实现：
        SETNX可以直接加锁操作，如对某个关键词foo加锁，客户端可以:SETNX foo.lock <current unix time>,如果返回1，表示客户端已经获取锁，通过
        DEL foo.lcok 命令来释放锁；如果返回0，说明foo已被其他客户端上锁，如果锁是非阻塞的，可以选择返回调用。如果是阻塞调用，就进入下个循环重试，
        直到成功获取锁或重试超时。
#####缓存雪崩和缓存穿透的解决方法
    1.缓存透传：当用户查询的key在redis中不存在，对应的id在数据库也不存在，此时被非法用户进行攻击，大量的请求会直接打在db上，造成宕机，从而影响整个系
    统，这种现象为缓存透传
    2.解决方案：
       （1）把空的数据也缓存起来，比如空字符串、空对象、空数组或list,如：
        if(list!=null && list.size()>0){
            redisOperator.set("subCat:"+rootCatId,JsonUtils.objectToJson(list));
        }else{
            redisOperator.set("subCat:"+rootCatId,JsonUtils.objectToJson(list),5*60);
        }//这样下次有相同的key来访问的时候，在缓存失效之前，都可以直接从缓存中获取数据。
        (2)布隆过滤器：判读一个元素是否在一个数组里面，利用二进制去做的一个存储，占用内存较小，0代表不存在，1代表存在，添加查询效率快，当保存了一个
        数值会经过一个算法将对应的值保存到布隆过滤器的集合上的某个位置，某个位置上可能会存在多个key,当传进来一个不存在的key值和集合进行匹配，如果匹
        配不上便返回一个null。
    3.缓存雪崩：缓存中的数据大批量失效，然后这时又有大量的请求进来，但由于redis中的key全部失效所有请求会到db上，造成宕机。
    4.解决方案：
        第一种：redis高可用，常见的两种方式1.主从复制（Replication-Sentinel模式）2.Redis集群（Redis-Cluster模式）
        第二种：多缓存结合，如：请求进入，可以先请求redis,当redis中不存在的时候再去请求memcache,如果都没有再去请求db
        第三种：限流组件（hystrix），可以设置每秒的请求，有多少能通过组件，剩余的未通过的请求走降级，可以返回一些默认的值，或友情提示，或空白的值。
        第四种：redis持久化，一旦重启，自动从磁盘上加载数据，快速恢复缓存数据。
        第五种：将热点数据设置为永远不过期；
#####Redis高可用方案
    1.主从复制（Replication-Sentinel模式）
      工作原理-Replication:
                        slave-1----------> master <-----------slave-2
       Redis主从结构如图所示，主节点master负责读写，从节点slave负责读。这个系统运行依靠三个主要的机制：
       a:当一个master实例和一个slave实例连接正常时，master会发送一连串的命令来保持对slave的更新，以便于将自身数据集的改变复制给slave，包括客户端的
       写入、key的过期或被逐出等等。
       b:当master和slave之间的连接断开之后，因为网络问题、或者是主从关系连接超时，slave重新连接上master并尝试进行部分重同步：这意味着它会尝试只获取
       在断开连接期间内丢失的命令流。
       c:当无法进行部分重同步时，slave会请求进行全量重同步。
      搭建步骤：
        将redis.conf文件复制两份slave.conf和slave2.conf并修改配置：主从服务器端口分别修改为7001、7002、7003，修改两个配置主服务器地址、端口号以及
        主服务器密码。分别启动这三个Redis服务：./src/redis-server redis.conf 和 ./src/redis-server slave.conf 及
         ./src/redis-server slave2.conf
        使用redis-cli工具连接redis服务查看主从节点是否搭建成功：
            $ ./src/redis-cli -h <主机名> -p <端口号>
            $ .auth <password>
            $ info replication
      
      工作原理-Sentinel：
        简单的主从集群有个问题，主节点挂了之后，无法从新选举的节点作为主节点进行操作，导致服务不可用。这就用到Sentinel(哨兵)功能。哨兵是一个独立的进
        程，它会实时监控master节点的状态，当master不可用时会从slave节点中选出一个作为新的master，并修改其他节点的配置指向新的master。
      该系统执行一下三个任务：
        1.监控：Sentinel会不断地检查主服务器和从服务器运行是否正常
        2.提醒：当监控的某个Redis服务器出现问题时，Sentinel可以通过API向管理员或者其他应用程序发送通知。
        3.自动故障迁移：当一个主服务器不能正常工作时，Sentinel会开始一次自动故障迁移操作，当客户端试图连接失效的主服务器时，集群也会向客户端返回新主
        服务器的地址，使得集群可以使用新竹服务器代替失效服务器。
      搭建步骤：
        将sentinel.conf文件复制两份为sentinel2.conf、sentinel3.conf，并修改配置
            # 三个配置文件分别配置不同的端口号
            port <端口号>
            # 设置为后台启动
            daemonize yes
            # 主节点的名称(可以自定义，与后面的配置保持一致即可)
            # 主机地址
            # 端口号
            # 数量(2代表只有两个或两个以上的哨兵认为主服务器不可用的时候，才会进行failover操作)
            sentinel monitor mymaster 127.0.0.1 6379 2
            # 多长时间没有响应认为主观下线(SDOWN)
            sentinel down-after-milliseconds mymaster 60000
            # 表示如果15秒后,mysater仍没活过来，则启动failover，从剩下从节点序曲新的主节点
            sentinel failover-timeout mymaster 15000
            # 指定了在执行故障转移时， 最多可以有多少个从服务器同时对新的主服务器进行同步， 这个数字越小， 完成故障转移所需的时间就越长
            sentinel parallel-syncs mymaster 1
        启动三个sentinel：./src/server-sentinel sentinel.conf和./src/server-sentinel sentinel2.conf及./src/server-sentinel sentinel3.conf
        手动关闭主节点的redis服务，并查看两个slave信息是否有一个变成了master.
        程序中使用（springboot连接redis主从集群配置）：
            spring:
                redis:  
                    sentinel:
                        master: mymaster
                        nodes:  192.168.110.182:26379,192.168.110.182:26380,192.168.110.182:26381
                    host: 192.168.110.180
                    port: 7003
                    database: 0
                    password: <password>
                    
    2.Redis集群（Redis-Cluster）
        工作原理：Redis集群是一个提供在多个Redis节点间共享数据的程序集。Redis集群有16384个哈希槽，每个key通过CRC16校验后对16384取模来决定放置哪个
            槽。集群的每个节点负责一部分hash槽。为了使在部分节点失效或大部分节点无法通信的情况下集群仍然可用，所以集群使用了主从复制模式，每个节点都
            会有1-n个从节点，如当master-A节点不可用了，集群便会选举slave-A节点作为新的主节点继续服务。
        搭建：Redis5.0之后版本放弃Ruby的集群方式，改用C语言编写的redis-cli的方式。
            创建多个Redis的配置文件，
            如：/usr/local/redis-5.0.4/redis-cluster-conf/7001/redis.conf,/usr/local/redis-5.0.4/redis-cluster-conf/7002/redis.conf,...
            配置文件内容：port 7001 #端口，每个配置文件不一样7001-7006
                       cluster-enabled yes #采用集群模式
                       cluster-config-file nodes.conf #节点配置文件
                       cluster-node-timeout 5000 #超时时间
                       appendonly yes #打开aof持久化
                       daemonize yes #后台运行
                       protected-mode no #非保护模式
                       pidfile /var/run/redis_7001.pid #根据端口修改
            启动各个redis节点：./src/redis-server redis-cluster-conf/7001/redis.conf,...
            此时启动的6个redis服务是相互独立运行的，可以通过以下命令配置集群：
                ./src/redis-cli --cluster create --cluster-replicas 1 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003
            添加一个主节点：复制一份配置文件，并修改配置/usr/local/redis-5.0.4/redis-cluster-conf/7007/redis.conf
            启动该Redis服务，并将该节点添加到集群中去：./src/redis-cli --cluster add-node 127.0.0.1:7007 127.0.0.1:7001
            添加一个从节点7008,然后连接上该节点并执行如下命令：cluster replicate <nodeId>，这样就可以指定该节点成为哪个节点的从节点。
            节点移除：./src/redis-cli --cluster del-node 127.0.0.1:7001 <nodeId>
        SpringBoot中连接Redis集群配置
            spring:
                redis:
                    cluster:
                        nodes: 192.168.1.110:7001,192.168.1.110:7002,192.168.1.110:7003,192.168.1.110:7004,...
                        database: 0
                        password: <password>
                
             