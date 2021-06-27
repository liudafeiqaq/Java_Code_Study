import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class Plat2ClassLoader extends ClassLoader {
    public static void main(String[] args) {
        try {
            String methodName = "hello";
            ClassLoader classLoader = new Plat2ClassLoader();
            Class<?> clazz = classLoader.loadClass("Hello");
            String isPublicMethod = "0";
            //获取公共方法
            Method[] methods = clazz.getMethods();
            System.out.println("show Hello.xlass public method name:");
            for (Method method : methods) {
                if(method.getName().equals(methodName)){
                    isPublicMethod = "1";
                }
                System.out.println(" == >" + method.getName());
            }
            //获取所有方法
            System.out.println("Show Hello.xlass Method Name:");
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                System.out.println(" == >" + declaredMethod.getName());
            }
            //初始化Hello对象
            Object instance = clazz.getDeclaredConstructor().newInstance();

            Method hello = null;
            System.out.println("isPublicMethod :" + isPublicMethod);
            if("1".equals(isPublicMethod)){
                hello = clazz.getMethod(methodName);
            }else{
                hello = clazz.getDeclaredMethod(methodName);
            }
            hello.invoke(instance);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("自定义classLoader初始化文件失败");
        }
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        System.out.println("classLoader loader class begin , name :" + name);
        InputStream stream = this.getClass().getResourceAsStream(name + ".xlass");
        //获取流的长度
        int available = 0;
        try {
            available = stream.available();
            byte[] sourceByte = new byte[available];
            stream.read(sourceByte);
            byte[] targetByte = new byte[available];
            for (int i = 0 ; i < sourceByte.length ; i++) {
                targetByte[i] = (byte) (255 - sourceByte[i]);
            }
            return defineClass(name,targetByte,0,targetByte.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }finally {
            if(stream != null){
                try {
                    stream.close();
                } catch (IOException e) {
                    System.out.println("关闭输入流失败");
                }
            }
        }
    }
}
