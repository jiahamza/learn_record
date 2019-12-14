package devtools.classloader;

import devtools.Application;
import devtools.FileListener;
import devtools.Test;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyClassLoader extends ClassLoader {
    private String rootPath;
    //由自己的类加载器去加载的类
    private List<String> classes;

    //classPaths 需要热部署的由自己的类加载器去加载的目录
    public MyClassLoader(String rootPath, String... classPaths) throws Exception {
        this.rootPath = rootPath;
        this.classes = new ArrayList<String>();
        for (String classPath : classPaths) {
            //扫描包并将类用自己的类加载器加载
            scanClassPath(new File(classPath));
        }
    }

    private void scanClassPath(File file) throws Exception {
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                scanClassPath(listFile);
            }
        } else {
            String fileName = file.getName();
            String filePath = file.getPath();
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (suffix.equals("class")) {
                //这里加载的是一个class文件,如何将class文件,加载成一个class对象?
                InputStream in = new FileInputStream(file);
                byte[] bytes = new byte[(int) file.length()];
                in.read(bytes);
                String className = fileNameToClassName(filePath);
                //把class文件转换成class对象,并缓存下来
                defineClass(className, bytes, 0, bytes.length);
                classes.add(className);
            }
        }
    }

    private String fileNameToClassName(String filePath) {
        // d://xxx//com//yy/zz
        String className = filePath.replace(rootPath, "").replaceAll("\\\\", ".");
        className = className.substring(1, className.lastIndexOf("."));
        // com.yy.zz
        return className;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        //看缓存中是否有这个class
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass == null) {
            //没有,只有2种情况:
            // 1.这个类不需要我们加载,那就用系统类加载器加载
            // 2.需要我们加载,但是没加载到,抛异常
            if (!classes.contains(name)) {
                loadedClass = getSystemClassLoader().loadClass(name);
            } else {
                throw new ClassNotFoundException("没有加载到类!");
            }
        }
        return loadedClass;
    }

    public static void main(String[] args) throws Exception {
        String rootPath = MyClassLoader.class.getResource("/").getPath();
        rootPath = new File(rootPath).getPath();
        // 热替换 new Test().test();
        // new 出来的对象依然是系统类加载器加载的.因为main方法时系统类加载器加载的.
        // 此即全盘委托机制 当有new关键字时,jvm会判断调用new关键字的类的加载器是神马
        // 如何打破这种机制?
        /*while (true) {
            MyClassLoader myClassLoader = new MyClassLoader(rootPath, rootPath + "/devtools");
            Class<?> aClass = myClassLoader.loadClass("devtools.Test");
            aClass.getMethod("test").invoke(aClass.newInstance());
            new Test().test();
            Thread.sleep(1000l);
        }*/

        // 给一个程序入口
        Application.run(MyClassLoader.class);
    }
}
