package concurrent.likehystrix;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ThreadService service = new ThreadService();
        long start = System.currentTimeMillis();
        service.execute(()->{
            /*while (true) {
                // load a very heavy source
            }*/
            // load a source
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("before shutdown");
        service.shutDown(10_000);
        long end = System.currentTimeMillis();
        System.out.println("耗时:"+ (end-start));
    }
}
