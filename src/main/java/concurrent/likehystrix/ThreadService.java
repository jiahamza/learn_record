package concurrent.likehystrix;

import java.util.concurrent.TimeUnit;

public class ThreadService {
    private Thread boss;
    private boolean finished = false;

    /**
     * 老板接活给工人
     * @param task 任务
     */
    public void execute(Runnable task) {
        // 老板接活
        boss = new Thread(() -> {
            // 执行工作
            Thread worker = new Thread(task, "worker-thread");
            worker.setDaemon(true);
            worker.start();

            try {
                worker.join();
                // 自觉下班,工作结束
                finished = true;
            } catch (InterruptedException e) {
                System.out.println("谢谢老板,工人下班");
            }
        });
        boss.start();
    }

    /**
     * 设置超时关闭
     * @param millis 超时时间
     */
    public void shutDown(long millis) {
        long startTime = System.currentTimeMillis();
        //
        while (!finished) {
            if (System.currentTimeMillis() - startTime >= millis) {
                System.out.println("工作超时,强制下班");
                // 让老板下班,工人也一起下班
                boss.interrupt();
                break;
            }
            // 没有超时,也没完成任务,短暂休眠
            try {
                boss.sleep(1000);
                System.out.println("老板睡1秒");
            } catch (InterruptedException e) {
                // 强制老板下班
                System.out.println("老板下班了,那么工人也可以下班了");
                break;
            }
        }
        finished = false;
    }
}
