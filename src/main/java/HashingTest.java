import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;

public class HashingTest
{
	private Multiset<Long> occ = HashMultiset.create();
	private int NEW_LINE;

	@Test
	public void testHash() throws IOException
	{
		long startTime = System.currentTimeMillis();
		for( int i = 0; i < 448; i++ )
		{
			checksumForFile( "D:\\d2.htm" );
			checksumForFile( "D:\\d.htm" );
		}
		System.out.println( System.currentTimeMillis() - startTime );

		for( HashMultiset.Entry e : occ.entrySet() )
			System.out.println( e );
	}

	private void checksumForFile( String filePath ) throws IOException
	{
		FileInputStream fis = new FileInputStream( filePath );

		FileChannel channel = fis.getChannel();
		int length = (int) channel.size();
		MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, length );

		Adler32 checksum = new Adler32();

		//CheckedInputStream c = new CheckedInputStream( fis, new Adler32() );

		boolean eof = false;
		int p=0;
		while(!eof)
		{
			checksum.reset();
			int i = 0;
			int b = buffer.get(p);
			NEW_LINE = 10;
			while( p<length && b != -1 && !( i > 10240 && b == NEW_LINE ) )
			{
				checksum.update( b );
				b = buffer.get(p);
				i++;
				p++;
			}
			if( i < 10240 )
				eof = true;
			occ.add( checksum.getValue() );
		}
	}
}
