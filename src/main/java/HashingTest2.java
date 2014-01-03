import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.Adler32;

import org.apache.commons.io.FileUtils;

public class HashingTest2
{
	private Multiset<Long> occ = HashMultiset.create();
	private int NEW_LINE;

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

		//CheckedInputStream c = new CheckedInputStream( fis, new Adler32() );

		boolean eof = false;
		int p=0;
		int length = data.length;

		while(!eof)
		{
			checksum.reset();
			int i = 0;
			int b = data[p];
			NEW_LINE = 10;
			while( p<length && b != -1 && !( i > 10240 && b == NEW_LINE ) )
			{
				//checksum.update( b );
				b = data[p];
				i++;
				p++;
			}
			if( i < 10240 )
				eof = true;
			occ.add( checksum.getValue() );
		}
	}
}
