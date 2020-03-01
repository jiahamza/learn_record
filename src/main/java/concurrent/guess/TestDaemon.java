package concurrent.guess;

import java.util.concurrent.TimeUnit;

/**
 * 验证2个问题
 * 1.守护线程中穿件的线程,还是守护线程
 * 2.守护线程不止守护创建它的线程.当创建它的线程已死,还有其他线程时(非守护线程)没死,该守护线程还不会死.
 * 3.最后一个非守护线程死了,其他所有守护线程立即结束
 */
public class TestDaemon {
    public static void main(String[] args) {
        Thread noDaemon = new Thread(new NormalThread(false, 15), "noDaemon");
        Thread hasDaemon = new Thread(new NormalThread(true, 5), "hasDaemon");
        noDaemon.start();
        hasDaemon.start();
        NormalThread t = new NormalThread(false, 5);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main end...");

    }

    static class NormalThread implements Runnable {
        private boolean hasDaemon;
        private long sleepTime;

        public NormalThread(boolean hasDaemon, long sleepTime) {
            this.hasDaemon = hasDaemon;
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            System.out.println("current thread :" + Thread.currentThread().getName() + " start");
            if (hasDaemon) {
                Thread daemon1 = new Thread(new DaemonThread(), "daemon1");
                daemon1.setDaemon(true);
                daemon1.start();
                Thread daemon2 = new Thread(new DaemonThread(), "daemon2");
                daemon2.setDaemon(true);
                daemon2.start();
            }
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("current thread :" + Thread.currentThread().getName() + " end");
        }

    }

    static class DaemonThread implements Runnable {
        @Override
        public void run() {
            System.out.println("daemon start ");
            Thread innerThread = new Thread(() -> {
                System.out.println("inner thread start");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            innerThread.start();
            System.out.println("inner thread is daemon? " + innerThread.isDaemon());
            System.out.println(Thread.currentThread().getName() + " thread is daemon? " + Thread.currentThread().isDaemon());

            try {
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName() + " daemon thread is alive !!");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
