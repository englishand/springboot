#####Nginx负载均衡策略
    负载均衡用于从“upstream"模块定义的后端服务器列表中选取一台服务器接收用户的请求。
    目前Nginx服务器的upstream模块支持6种方式的分配：
    参数：fail_timeout(经过max_fails失败后，服务暂停的时间);max_fails(允许请求最大失败次数);fail_time(服务器会被认为停机的时间长度，默认为10s);
        backup(标记该服务器为备用服务器。当主服务器停止时，请求会被发送到这里)、down(标记服务器永久停机了)
        max_fails默认为1.
    1.轮询（默认方式）
       每个请求会按照时间顺序逐一分配到不同的后端服务器。
        
        upstream www_server_pools{
            server 192.168.0.122:80 weight=1;
            server 192.168.0.123:80 weight=1;
        }
        server {
            listen 8088;
            server_name:192.168.110.182;
            
            location / {
                proxy_class http://www_server_pools;
            }
            
            error_page 500 502  503 504 /50x.html;
            location = /50x.html {
            root html;
            }
        }
        
       注：在轮询种中，如果服务器down掉了，会自动剔除该服务器。
    2.weight(权重方式)
       在轮询策略的基础上指定轮询的机率。weight的默认值是1，该数值与访问比例成正比。
        upstream backserver{
            server 192.168.0.14:8080 weight=2;
            server 192.168.0.15:80 weight=1;
        }
    3.ip_hash(依据ip分配方式)
       指定负载均衡器按照基于客户端ip的hash结果分配方式，确保了相同的客户端的请求一直发送到相同的服务器，以保证session会话。
       解决session不能跨服务器的问题。
       upstream backserver{
            ip_hash;
            server localhost:8080;
            server localhost:80;
       }
       注：在nginx版本1.3.1之前，不能在ip_hash中使用权重（weight）。ip_hash不能与backup同时使用。
    4.least_conn(最少连接方式)
       把请求转发给连接数较少的后端服务器。
        upstream dynamic_zuoyu {
            least_conn;    #把请求转发给连接数较少的后端服务器
            server localhost:8080   weight=2;  #tomcat 7.0
            server localhost:8081;  #tomcat 8.0
            server localhost:8082 backup;  #tomcat 8.5
            server localhost:8083   max_fails=3 fail_timeout=20s;  #tomcat 9.0
        }
       注：此策略适合请求处理时间长短不一致造成服务器过载的情况。
    5.第三方策略
        第三方的负载均衡策略的实现需要安装第三方插件。
       （1）fair(响应时间方式)
            按照服务器端的响应时间来分配请求，响应时间短的优先分配。
          upstream backserver{
               server localshot:80;
               server localhost 8080;
               fair;
          }     
       （2）url_hash（依据url分配方式）
            按访问url的hash结果来分配请求，使每个url定向到同一个后端服务器，要配合缓存命中来使用。同一资源多次请求，可能会到达不同的服务器上，导致不
            必要的多次下载，缓存命中率不高，以及一些资源时间的浪费。而使用url_hash,可以使得同一个url（即同一个资源请求）会到达同一台服务器，一旦缓存
            住了资源，再次收到请求，就可以从缓存中读取。
            upstream dynamic_zuoyu {
                hash $request_uri;    #实现每个url定向到同一个后端服务器
                server localhost:8080 down;  #dwon 表示当前的server暂时不参与负载
                server localhost:8081;  
                server localhost:8082;  
                server localhost:8083;  
            }
#####Nginx反向代理
    判断nginx.exe是否启动成功：tasklist /fi "imagename eq nginx.exe"或直接访问localhost
    关闭：nginx.exe -s stop/quit
    重新载入：nginx.exe -s reload
    1.添加虚拟域名，Windows进入C:\Windows\System32\drivers\etc目录下找到hosts文件(Linux：/etc/hosts)，添加需要使用的虚拟域名，指向本机：
        127.0.0.1 www.zxrcl.com
        127.0.0.1 image.zxrcl.com
        (windows下不需要重启电脑，使用命令：ipconfig /displaydns   ipconfig /flushdns)
        (linux下source 文件名)
    2.修改nginx配置文件nginx.conf
        添加 include vhost/*.conf;
    3.conf目录下，创建vhost文件夹
    4.新建反向代理的conf文件:www.zxrcl.com.conf和image.zxrcl.com.conf文件
    4.1打开www.zxrcl.com.conf文件添加当前域名www.zxrcl.com的server节点
        当服务器收到www.zxrcl.com域名时，nginx会转发到该服务器的8080端口执行访问。通常8080端口监听Tomcat。（在此之前需要保证Tomcat正常运行）
        server{
        	charset utf-8;
        	listen 80;
        	autoindex on;
        	server_name www.zxrcl.com.conf;
        	access_log D:/tools/nginx-1.20.0/logs/access.log combined;
        	index login.html welcome.html error.html
        	if ( $query_string ~* ".*[\;'\<\>].*" ){
        	return 404;
        	}
        	location / {
        	proxy_pass http:127.0.0.1:8080/projectone/login/welcome;
        	add_header Access-Control-Allow-Origin *;
        	}
        }
    4.2编辑转发目录的conf文件
        打开image.zxrcl.com.conf文件添加当前域名image.zxrcl.com的server节点        
        当服务器收到image. zxrcl.com域名时，nginx会转发到F:\nginxtest\目录执行访问。（此处可在目录中上传一些文件用于测试）,如果autoindex配置为off
        时需要在转发的目的目录中新建（首页）index.html
        server {
            listen 80;
            autoindex off;
            server_name image.zxrcl.com;
            access_log F:/nginx/logs/access.log combined;
            index index.html index.htm index.jsp index.php;
            #error_page 404 /404.html;
            if ( $query_string ~* ".*[\;'\<\>].*" ){
            return 404;
            }
            location ~ /(mmall_fe|mmall_admin_fe)/dist/view/* {
            deny all;
            }
            location / {
            root F:/nginxtest;
            add_header Access-Control-Allow-Origin *;
            }
        }
    4.3验证配置文件是否正确:nginx.exe -t
    4.4重启nginx测试
        (1)重启：nginx.exe -s reload
       （2）访问www.zxrcl.com.conf
       （3）测试转发目录，访问：image.zxrcl.com
#####source filename 与 sh filename 及./filename执行脚本的区别#
     当shell脚本具有可执行权限时，用sh filename与./filename执行脚本是没有区别得。
     ./filename是因为当前目录没有在PATH中，所有”.”是用来表示当前目录的。
     sh filename 重新建立一个子shell，在子shell中执行脚本里面的语句，该子shell继承父shell的环境变量，但子shell新建的、改变的变量不会被带回父shell，
     除非使用export。
     source filename：这个命令其实只是简单地读取脚本里面的语句依次在当前shell里面执行，没有建立新的子shell。那么脚本里面所有新建、改变变量的语句
     都会保存在当前shell里面。