package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Haodong Li
 * @date 2022年10月24日 11:18
 */
public class SaveUtils {
    public static void TextToFile(final String strFilename, final String strBuffer)
    {
        try
        {
            // 创建文件对象
            File fileText = new File(strFilename);
            // 向文件写入对象写入信息
            FileWriter fileWriter = new FileWriter(fileText);

            // 写文件
            fileWriter.write(strBuffer);
            // 关闭
            fileWriter.close();
        }
        catch (IOException e)
        {
            //
            e.printStackTrace();
        }
    }
}
