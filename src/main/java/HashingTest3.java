import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.zip.Adler32;

public class HashingTest3
{
	private Multiset<Long> occ = HashMultiset.create();
	private final static int NEW_LINE = 10;

	@Test
	public void testHash() throws IOException
	{
			byte[] f1 = FileUtils.readFileToByteArray( new File( "D:/d.htm" ) );
			byte[] f2 = FileUtils.readFileToByteArray( new File( "D:/d2.htm" ) );
		long startTime = System.currentTimeMillis();
		for( int i = 0; i < 448; i++ )
		{
			checksumForFile( f1 );
			checksumForFile( f2 );
		}
		System.out.println( System.currentTimeMillis() - startTime );

		for( HashMultiset.Entry e : occ.entrySet() )
			System.out.println( e );
	}

	private void checksumForFile( byte[] data ) throws IOException
	{
		Adler32 checksum = new Adler32();

		boolean eof = false;
		int p=0;
		int length = data.length;

		while(!eof)
		{
			checksum.reset();
			int i = 0;
			int b = data[p];
			while( p<length && b != -1 && !( i > 10240 && b == NEW_LINE ) )
			{
				b = data[p];
				i++;
				p++;
			}
			checksum.update( data, p-i, i );
			if( i < 10240 )
				eof = true;
			occ.add( checksum.getValue() );
		}
	}
}
