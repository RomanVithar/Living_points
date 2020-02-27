import com.project.my.FileWork;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        FileWork fw = new FileWork("Files/best.txt");
        int[] d = fw.readValue();
        for(int i=0;i<64;i++) {
            System.out.print(d[i]+" ");
        }
    }
}
