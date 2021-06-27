import sun.misc.Launcher;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class Play2ClassLoader_fourth {

    public static void main(String[] args) {
        URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
        System.out.println("开始输出启动类加载器jar");
        for (URL urL : urLs) {
            System.out.println("== >"+ urL.toExternalForm());
        }

        printClassLoader("扩展类加载器",Play2ClassLoader_fourth.class.getClassLoader().getParent());

        printClassLoader("应用类加载器",Play2ClassLoader_fourth.class.getClassLoader());
    }

    private static void printClassLoader(String name, ClassLoader classLoader) {
        if(classLoader != null){
            System.out.println(name + "classsLoader" + classLoader.toString());
            printURLForClassLoader(classLoader);
        }else{
            System.out.println(name + "classLoader is null");
        }

    }

    private static void printURLForClassLoader(ClassLoader classLoader) {
        Object ucp = insightField(classLoader,"ucp");
        Object path = insightField(ucp, "path");
        ArrayList list = (ArrayList) path;
        list.forEach(e->{
            System.out.println("== >" +e.toString());
        });
    }

    private static Object insightField(Object classLoader, String fieldName) {
        try {
            Field field = null;
            if(classLoader instanceof URLClassLoader){
                System.out.println("fieldName" + fieldName);
                field = URLClassLoader.class.getDeclaredField(fieldName);
            }else{
                field = classLoader.getClass().getDeclaredField(fieldName);
            }
            field.setAccessible(true);
            return field.get(classLoader);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
