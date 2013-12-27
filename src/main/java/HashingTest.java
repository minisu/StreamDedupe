import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

public class HashingTest {

    private Multiset<Long> occ = HashMultiset.create();

    @Test
    public void testHash() throws IOException {
        checksumForFile("C:\\Users\\Sofia\\Desktop\\Nymapp\\d.htm");

        for(HashMultiset.Entry e : occ.entrySet())
            System.out.println(e + ", count: " + e.getCount());
    }

    private void checksumForFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);

        CheckedInputStream c = new CheckedInputStream(fis, new Adler32());

        boolean eof = false;
        while (!eof)
        {
            c.getChecksum().reset();
            int i = 0;
            while (c.read() != -1 && i++ < 10240 )
            {
            }
            if(i < 10240)
                eof = true;
            //System.out.println( c.getChecksum().getValue() );
            occ.add( c.getChecksum().getValue() );
        }
    }
}
