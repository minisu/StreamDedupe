import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Adler32;

public class HashingTest4
{
	public static final int BLOCK_SIZE = 10240;
	public static final int TAG_OPENING = 60;
	private Multiset<Long> occ = HashMultiset.create();
	private final static int NEW_LINE = 10;
	private BufferedOutputStream blockContent;
	private Adler32 checksum = new Adler32();

	@Test
	public void testHash() throws IOException
	{
		File file = new File( "output.txt" );
		blockContent = new BufferedOutputStream( new FileOutputStream( file ));

		byte[] f1 = FileUtils.readFileToByteArray( new File( "D:/d3.html" ) );
		byte[] f2 = FileUtils.readFileToByteArray( new File( "D:/d4.html" ) );

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

		System.out.println("Output: " + file.getAbsolutePath());
	}

	private void checksumForFile( byte[] data ) throws IOException
	{
		int p=0;
		int length = data.length;

		while(p<length)
		{
			checksum.reset();
			int i = 0;
			int b = data[p];
			while( p<length && !( i > BLOCK_SIZE && (b == NEW_LINE || b == TAG_OPENING ) ) )
			{
				b = data[p];
				i++;
				p++;
			}
			checksum.update( data, p - i, i );

			long hash = checksum.getValue();
			if( !occ.contains( hash ) )
			{
				blockContent.write( data, p-i, i );
			}

			occ.add( checksum.getValue() );
		}
	}
}
