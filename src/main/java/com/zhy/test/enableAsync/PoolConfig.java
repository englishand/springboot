package com.zhy.test.enableAsync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.*;

@Slf4j
@Configuration
@EnableAsync(proxyTargetClass = true)//基于类的代理被创建即cglib代理
public class PoolConfig {

    /**
     * ThreadPoolTaskExecutor和ThreadPoolEexcutor区别：
     * 1.参数不一样 ThreadPoolExecutor重要参数：corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit(时间单位),
     *  BlockingQueue（缓存队列）,ThreadFactory(线程工厂),RejectedExecutionHandler（执行拒绝策略的对象）
     * 2.ThreadPoolExecutor是JDK中的JUC,ThreadPoolTaskExecutor是spring core包中的。后者是对前者进行了封装处理。
     */
    @Bean("poolexecutor")
    public ThreadPoolTaskExecutor executor(){

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-9);
        date = calendar.getTime();
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        ThreadPoolTaskExecutor hte = new ThreadPoolTaskExecutor();
        hte.setCorePoolSize(10);//核心线程数10：线程池创建时候初始化的线程数
        hte.setMaxPoolSize(20);//最大线程数20：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        hte.setQueueCapacity(200);//缓冲队列200：用来缓冲执行任务的队列
        hte.setKeepAliveSeconds(60);//允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        hte.setThreadNamePrefix("poolexecutor-"+sdf.format(date)+"-");//线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池

        //线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，
        //线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        hte.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        hte.initialize();
        return hte;
    }

    /**
     * 可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收复用线程，则新建。
     * 优点：1.工作线程的数量无限制，可灵活的往线程池添加线程
     *      2.如果长时间没有往线程池添加任务，则该工作线程会自动终止。
     * 注：控制任务的数量，大量的线程运行，可能会导致系统瘫痪。
     * @return 一个Executor，提供管理终端的方法和生成Future的方法，用于追踪一个或多个异步任务的进度。
     */
    @Bean
    public ExecutorService cachedThreadPool(){
        ExecutorService executor = Executors.newCachedThreadPool();
        log.info("该线程池是否已经终止：{}",executor.isTerminated());
        return executor;
    }

    /**
     *  指定线程数量的线程池，如果线程数量达到最大，则将提交的任务存入池队列中
     *  线程池在空闲时，不会释放空闲线程。定长线程池的大小最好根据系统资源进行设置：Runtime.getRuntime().availableProcessors();
     *  优点：节省创建线程所耗的开销。
     * @return
     */
    @Bean
    public ExecutorService fixedThreadPool(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        return executorService;
    }

    /**
     * 定长线程池、支持定时以及周期性的任务执行,ScheduledExecutorService比Timer更安全，功能更强大
     * @return
     */
    @Bean
    public ScheduledExecutorService scheduleThreadPool(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        return executor;
    }

    /**
     * 单例化的executor,创建及使用唯一的线程执行任务。按照指定顺序：FIFO,LIFO,优先级执行
     * @return
     */
    @Bean
    public ExecutorService singleThreadExecutor(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        return executor;
    }
}
