import com.google.common.collect.HashMultiset;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multiset;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;

public class HashingTest5
{
	public static final int MIN_BLOCK_SIZE = 1024;
	public static final int MAX_BLOCK_SIZE = 10240;
	private Multiset<Integer> occ = HashMultiset.create();
    private ListMultimap<Long, Integer> storedFiles = LinkedListMultimap.create(1000);
    private BufferedOutputStream blockContent;
	private Adler32 checksum = new Adler32();

	@Test
	public void testHash() throws IOException
	{
		File file = new File( "output.txt" );
		blockContent = new BufferedOutputStream( new FileOutputStream( file ));

		byte[] f1 = FileUtils.readFileToByteArray( new File( "C:\\Users\\Henrik\\Documents\\temp/d.html" ) );
		byte[] f2 = FileUtils.readFileToByteArray( new File( "C:\\Users\\Henrik\\Documents\\temp/d2.html" ) );

		long startTime = System.currentTimeMillis();
		for( int i = 0; i < 1; i++ )
		{
			checksumForFile( f1 );
			checksumForFile( f2 );
		}
		System.out.println( System.currentTimeMillis() - startTime );

		blockContent.close();

		for( HashMultiset.Entry e : occ.entrySet() )
			System.out.println( e );

        for( Long timestamp : storedFiles.keySet() )
        {
			System.out.println( System.lineSeparator() + timestamp + ":" );
            for( Integer hash : storedFiles.get(timestamp) )
            {
                System.out.println("  "+hash);
            }
        }

		System.out.println("Output: " + file.getAbsolutePath());
	}

	private void checksumForFile( byte[] data ) throws IOException
	{
		int p=0;
		int length = data.length;
        List<Integer> hashes = new ArrayList<>( data.length / MIN_BLOCK_SIZE );

		while(p<length)
		{
			checksum.reset();
			int i = 0;
			int b;
			while( p<length )
			{
                b = data[p];
				if( b=='<' && i > MIN_BLOCK_SIZE && (data[p+1] == 'h' || data[p+1] == 'H') )
						break;
				if( i > MAX_BLOCK_SIZE )
				{
					if( b=='<' && (data[p+1] == 'p' || data[p+1] == 'P') )
						break;
				}
				i++;
				p++;
			}
			checksum.update( data, p - i, i );

			int hash = (int) checksum.getValue();
			if( !occ.contains( hash ) )
			{
                blockContent.write(hash);
                blockContent.write(i);
				blockContent.write( data, p-i, i );
			}
			occ.add( hash );

            hashes.add(hash);
		}

        storedFiles.putAll(System.currentTimeMillis(), hashes );
	}
}
