package devtools;

import devtools.classloader.MyClassLoader;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;

import java.io.File;

public class FileListener extends FileAlterationListenerAdaptor {
    @Override
    public void onFileChange(File file) {
        if (file.getName().indexOf(".class") != -1) {
            MyClassLoader myClassLoader = null;
            try {
                myClassLoader = new MyClassLoader(Application.ROOT_PATH, Application.ROOT_PATH + "/devtools");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Application.stop();
            try {
                Application.start0(myClassLoader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
