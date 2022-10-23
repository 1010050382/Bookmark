package Utils;

import java.io.*;

/**
 * @author Haodong Li
 * @date 2022年10月23日 19:49
 */
public class CloneUtils {
    private CloneUtils() {
        throw new AssertionError();
    }

    public static <T extends Serializable> T clone(T obj) throws Exception {
        //创建一块内存来存放对象内容
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bout);
        //将对象转换成二进制内容存入到开辟的内存中(序列化)
        oos.writeObject(obj);

        //读取内存块中的二进制内容
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        //将二进制内容转换回对象 反序列化
        ObjectInputStream ois = new ObjectInputStream(bin);
        return (T) ois.readObject();

    }
}