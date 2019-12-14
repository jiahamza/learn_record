package devtools;

import devtools.classloader.MyClassLoader;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

public class Application {

    public static String ROOT_PATH;


    public static void run(Class<?> clazz) throws Exception{
        String rootPath = MyClassLoader.class.getResource("/").getPath().replace("%20"," ");
        rootPath = new File(rootPath).getPath();
        Application.ROOT_PATH = rootPath;
        MyClassLoader myClassLoader = new MyClassLoader(rootPath, rootPath + "/devtools");
        //监听文件改动
        startFileListener(rootPath);
        start0(myClassLoader);
    }

    public static void startFileListener(String rootPath) throws Exception {
        //实现文件监听:启动一个线程,定时监听指定路径下的所有文件,如果文件有改动,就回调监听器
        FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(rootPath);
        fileAlterationObserver.addListener(new FileListener());
        FileAlterationMonitor fileAlterationMonitor = new FileAlterationMonitor(500l);
        fileAlterationMonitor.addObserver(fileAlterationObserver);
        fileAlterationMonitor.start();
    }

    public static void start(){
        System.out.println("启动自己的应用程序");
        new Test().test();
    }

    public static void start0(MyClassLoader myClassLoader) throws Exception {
        Class<?> aClass = myClassLoader.loadClass("devtools.Application");
        aClass.getMethod("start").invoke(aClass.newInstance());
    }
    public static void stop(){
        System.out.println("程序退出!");
        //告诉jvm可以gc了
        System.gc();
        //告诉jvm可以清除对象引用了
        System.runFinalization();
    }
}
