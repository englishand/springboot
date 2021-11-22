#####版本介绍
    1、kafka版本命名规则：
    在1.x之前的版本，基本遵循4位版本号，例如：0.8.2.2、0.9.0.1、0.10.0.0...
    在1.x之后，kafka 全面启用了遵循 Major.Minor.Patch 的三位版本规则，其中Major表示大版本，通常是一些重大改变，因此彼此之间功能可能会不兼容；Minor表示小版本，通常是一些新功能的增加；最后Patch表示修订版，主要为修复一些重点Bug而发布的版本。例如：Kafka 2.1.1，大版本就是2，小版本是1，Patch版本为1，是为修复Bug发布的第1个版本。
    2、官网下载：
    官网下载地址：https://kafka.apache.org/downloads
    打开官网，看到如下界面：
    这里简单介绍一下，kafka_2.12中的2.12表示的scala的版本，因为Kafka服务器端代码完全由Scala语音编写。”-“后面的2.5.0表示的kafka的版本信息，遵循上面的命令规则。
    注：Kafka新版客户端代码完全由Java语言编写，当然，不是Scala不行了，而是社区找来了一批Java程序员而已，而之前的Scala程序员隐退罢了。
    3、kafka版本演进：
    Kafka总共发布了7个大版本，分别是0.7.x、0.8.x、0.9.x、0.10.x、0.11.x、1.x及2.x版本。截止目前，最新版本是Kafka 2.5.0，也是最新稳定版本。
    1）0.7版本：
    这是个“上古”版本，只提供了基础的消息队列功能，还没有提供副本机制
    2）0.8版本：
    新增了如下几个重要特性：
    Kafka 0.8.0，增加了副本机制，至此Kafka成为了一个真正意义上完备的分布式高可靠消息队列解决方案；
    Kafka 0.8.2.0，consumer 的消费偏移位置 offset 由原来的保存在 zookeeper 改为保存在 kafka 本身（afka 定义了一个系统 topic，专用用来存储偏移量的数据）；
    Kafka 0.8.2.0，引入了新版本Producer API：新版本Producer API有点不同，一是连接Kafka方式上，旧版本的生产者及消费者API连接的是Zookeeper，而新版本则连接的是Broker；二是新版Producer采用异步批量方式发送消息，比之前同步发送消息的性能有所提升。
    新旧版本Producer API如下：
    //旧版本
    Producerkafka.javaapi.producer.Producer<K,V> 
    //新版本
    Producerorg.apache.kafka.clients.producer.KafkaProducer<K,V>
    注：此版本的新版本producer api还不太稳定。
    3）0.9版本：
    Kafka 0.9 是一个重大的版本迭代，增加了非常多的新特性，主要体现在三个方面：
    新版本Consumer API：Kafka 0.9.0使用java重写了新版Consumer API，使用方式也是从连接Zookeeper切到了连接Broker；
    安全方面：在0.9.0之前，Kafka安全方面的考虑几乎为0。Kafka 0.9.0 在安全认证、授权管理、数据加密等方面都得到了支持，包括支持Kerberos等；
    Kafka Connect：Kafka 0.9.0 引入了新的组件 Kafka Connect ，用于实现Kafka与其他外部系统之间的数据抽取。
    注：此时的新版本Consumer api还不大稳定，而0.9.0版本的Producer API已经比较稳定了；
    4）0.10.x版本：
    Kafka 0.10 是一个重要的大版本，因为Kafka 0.10.0.0 引入了 Kafka Streams，使得Kafka不再仅是一个消息引擎，而是往一个分布式流处理平台方向发展。0.10 大版本包含两个小版本：0.10.1 和 0.10.2，它们的主要功能变更都是在 Kafka Streams 组件上。
    值得一提的是，自 0.10.2.2 版本起，新版本 Consumer API 已经比较稳定了，而且新版本的 Producer API 的性能也得到了提升，因此对于使用 0.10.x 大版本的用户，建议使用或升级到 Kafka 0.10.2.2 版本。
    5）0.11.x版本：
    Kafka 0.11 是一个里程碑式的大版本，主要有两个大的变更：
    从这个版本开始支持Exactly-Once 语义即精准一次语义，主要是实现了Producer端的消息幂等性，以及事务特性，这对于Kafka流式处理具有非常大的意义；
    Kafka 0.11另一个重大变更是Kafka消息格式的重构（对用户是透明的），主要为了实现Producer幂等性与事务特性，重构了投递消息的数据结构。这一点非常值得关注，因为Kafka 0.11之后的消息格式发生了变化，所以我们要特别注意Kafka不同版本间消息格式不兼容的问题。
    注：这个版本中各个大功能组件都变得非常稳定了，应该算是目前最主流的版本之一。
    6）1.x版本：
    Kafka 1.x 更多的是Kafka Streams方面的改进，以及Kafka Connect的改进与功能完善等。但仍有两个重要特性：
    一是Kafka 1.0.0实现了磁盘的故障转移，当Broker的某一块磁盘损坏时数据会自动转移到其他正常的磁盘上，Broker还会正常工作，这在之前版本中则会直接导致Broker宕机，因此Kafka的可用性与可靠性得到了提升；
    二是Kafka 1.1.0开始支持副本跨路径迁移，分区副本可以在同一Broker不同磁盘目录间进行移动，这对于磁盘的负载均衡非常有意义。
    7）2.x版本：
    Kafka 2.x 更多的也是Kafka Streams、Connect方面的性能提升与功能完善，以及安全方面的增强等。一个使用特性，Kafka 2.1.0开始支持ZStandard的压缩方式，提升了消息的压缩比，显著减少了磁盘空间与网络io消耗。
    4、关于客户端版本：
    kafka 支持多个语言的客户端api，这里只关注 java 客户端。maven 的工程我们一般这样引入 kafka 客户端
    <dependency>
    	<groupId>org.apache.kafka</groupId>
    	<artifactId>kafka_2.11</artifactId>
    	<version>0.10.2.0</version>
    </dependency>
    这种会引入两个依赖jar，分别是
    kafka-clients-0.10.2.0.jar
    kafka_2.11-0.10.2.0.jar
    前者是官方推荐的java客户端，后者是scala客户端。调用方式有所不同。如果确定不使用 scala api，也可以用下面这种方式只包含java版本的客户端。
    <dependency>
    	<groupId>org.apache.kafka</groupId>
    	<artifactId>kafka-clients</artifactId>
    	<version>0.10.2.0</version>
    </dependency>
    最后，给出一些建议：
    遵循一个基本原则，Kafka客户端版本和服务端版本应该保持一致，否则可能会遇到一些问题。
    根据是否用到了Kafka的一些新特性来选择，假如要用到Kafka生产端的消息幂等性，那么建议选择Kafka 0.11 或之后的版本。
    选择一个自己熟悉且稳定的版本，如果说没有比较熟悉的版本，建议选择一个较新且稳定、使用比较广泛的版本。
#####消息队列概述
    消息队列中间件是分布式系统中的重要组件，主要解决应用解耦、异步消息、流量销峰等问题，实现高性能、高可用、可伸缩和最终一致性加构。目前使用较多的消息
    队列有ActiveMQ,RabbitMQ，ZeroMQ，Kafka,MetaMQ,RocketMQ。Kafka目前主要作为一个分布式的发布订阅式的消息系统使用。一个producer对应一个topic。
    消息传输过程：
        Producer即生产者，向Kafka集群发送消息，在发送消息之前，会对消息进行分类，即Topic。Topic即主题，通过对消息指定主题可以将消息分类，消费者可以
        只关注自己需要的Topic中的消息。Consumer即消费者，消费者通过与Kafka集群建立长连接的方式，不断的从集群中拉消息，然后对这些消息进行处理。
    Kafka服务器消息存储策略：
        Kafka的存储，利用分区，即partitions,创建一个topic时，同时可以指定分区数目，分区数目越多，其吞吐量越大，但需要的资源也越多，同时也会导致更高
        的不可用性，Kafka在接收到生产者发送的消息之后，会根据均衡策略将消息存储到不同的分区中。在每个分区中，消息以顺序存储，最晚接收的消息会最后被消费
        在生产者发送消息时，可以直接指定分区。也可以通过指定均衡策略来将信息发送到不同的分区中，如果不指定，则采用默认的均衡策略，将消息存储到不同分区
        在消费者消费消息时，Kafka使用offset来记录当前消费者的位置，消费者分配到group中。在Kafka设计中，可以有不同的group来同时消费同一个topic下的消
        息。对于一个group而言，消费者的数量不应该多余分区的数量，因为在一个group中，每个分区至多只能绑定到一个消费者上，即一个消费者可以消费多个分区，
        一个分区只能给一个消费者消费。因此，若一个group中的消费者的数量大于分区的数量，多余的消费者将不会收到任何信息。
#####kafka的分区数和多线程消费
    broker:独立的kafka服务器称之为broker。broker接收生产者的消息，为消息设置偏移量，并提交到磁盘保存。broker为消费者提供服务，返回已经提交到磁盘的
        信息。单个broker可以轻松处理千个分区以及每秒百万级的消息。
    消费者：消费者通过检查消息的偏移量(一种元数据，不断递增的整数值，在创建消息时，Kafka会把它放到消息里面，具有唯一性，存到zk或Kafka上面，重启不会
        丢失消息的状态)来区分消息是否已读。
    保留信息：Kafka一个重要特性，Kafka broker保留信息的策略是：时间/字节数，过期后将会被删除。
    分区：一类信息通常由topic来归类成组，把一组消息分发给若干个分区（partition），每个分区的消息各不相同。
    offset:每个分区都维护着自己的偏移量(offset),记录着该分区的消息此时被消费的位置。
    消费线程：一个消费线程可以对应若干个分区，但一个分区只能被一个线程消费。
    goup.id:用于标记一个消费组，每一个消费组都会被记录 他在某一个分区的offset，即不同的consumer group针对同一个分区，都有各自的偏移量。
    注意：设置server.properties的num.partitions属性值，大于1.
#####分区策略
    1.Range strategy(范围消费)
     Range startegy是对每个主题而言的，首先对同一个主题里面的分区按照序号排序，并对消费者按照字母排序。然后用partitions总数除以消费者线程的总数来
     决定每个消费线程消费几个分区。如果有余数，那么前面的消费线程会多消费分区。
    2.RoundRobin strategy（轮询消费策略）
      使用时要满足的条件：同一个consumer group里面的所有消费者的num.streams必须相等。每个消费者订阅的主题必须相同。
      工作原理:将所有主题的分区组成TopicAndPartition列表,然后对列表按照hashcode进行排序,最后按照round-robin风格将分区分别分配给不同的消费者线程。
#####实例引用：
    1.启动zookeeper,可以使用外部的zookeeper，也可以使用kafka自带的zookeeper。这里使用自带的zookeeper。
        D:\tools\kafka_2.11-2.0.0\bin\windows> bin/windows/zookeeper-server-start.bat config/zookeeper.properties
    2.启动kafka:
        D:\tools\kafka_2.11-2.0.0\bin\windows> bin/windows/kafka-server-start.bat config/server.properties
    3.启动消息消费者和生产者：
        com.zhy.kafka.java.KafkaConsumer,com.zhy.kafka.java.KafkaProducer;
    本例中消费者在分区中的偏移量为mam.offset()；通过消费组的消费情况命令查看组信息，有三个偏移量：current-offset(表示消费组最新消费的位移值，此值
    在消费过程中是变化的),log-end-offset(代表partition的最高日志位移，对消费者不可见),lag(表示滞后进度)
    
    kafka-console-consumer.sh**********************************************
    该脚本是一个简易的消费者控制台，该脚本的功能通过调用Kafka.tools包下的ConsoleConsumer类，并将提供的命令行参数全部传给该类实现。    
#####Kafka命令操作   
    1.查看服务器中所有的topics:  bin/kafka-topic.sh --zookeeper localhost:2181 --list
    2.添加topics： bin/kafka-topics.sh --zookeeper localhost:2181 --create --replication-factor 3 --partitions 1 --topic first
      其中replication-factor为副本的意思。
    3.删除：   bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic first
    4.详情：   bin/kafka-topic.sh --describe --topic first --zookeeper localhost:2181 
    5.对已经创建的topic修改partitions和replicaiton-factor数量： 
        a:修改partitions: bin/kafka-topics.sh --zookeeper localhost:2181 --topic ${topicname} --alter --partitions 数量
        b:创建increase-replication-factor.json in config,配置各分区repication-factor位置
        c:更新replication-factor:
         kafka-reassign-partitions.sh --bootstrap-server ${kafkaAddress} --reassignment-json-file 
         config/increase-replication-factor.json --execute
    6.查看Kafka consumer group消费情况：  Kafka-consumer-groups.sh --describe --bootstrap-server ${kafkaAddress} --group ${groupname}
      该命令查看到的消费者的信息如consumer—id，只有在消费者消费时才显示，故该命令是一个监听命令
    7.删除消费组：   kafka-consumer-groups.sh --bootstrap-server localhost:9092 --delete --group groupName
    8.新建指定主题的分组：   
        kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topicName --consumer-property group.id=groupName
    9.使用消费者消费
        bin/kafka-console-consumer.bat --bootstrap-server ip:9092 --topic topicName --from-beginning --group groupName 
    10.工具 OffsetExplore2,该工具可以详细的展示Kafka内置元素的所有信息
#####kafka开启事务管理方式
    （一）配置Kafka事务管理器并使用@Transactional注解
    （二）使用KafkaTemplate的executeInTransaction方法
#####kafka使用，参考技术：https://blog.csdn.net/wangzhanzheng/article/details/79720029