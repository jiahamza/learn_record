package concurrent.guess;

import java.util.concurrent.TimeUnit;

/**
 * 主线程没点start前,子线程逻辑不执行
 */
public class BeforeStart {
    public static void main(String[] args) {
        Thread t = new Thread(()->{
            try {
                System.out.println("子线程开始了");
                long l = System.nanoTime();
                TimeUnit.SECONDS.sleep(5);
                System.out.println("子线程睡了"+ (System.nanoTime()-l)/1000000000 +"秒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //
        System.out.println("主线程还没点start");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.start();
    }
}
