import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Adler32;

public class HashingTest5
{
	public static final int MIN_BLOCK_SIZE = 1024;
	public static final int MAX_BLOCK_SIZE = 10240;
	private Multiset<Long> occ = HashMultiset.create();
	private BufferedOutputStream blockContent;
	private Adler32 checksum = new Adler32();

	@Test
	public void testHash() throws IOException
	{
		File file = new File( "output.txt" );
		blockContent = new BufferedOutputStream( new FileOutputStream( file ));

		byte[] f1 = FileUtils.readFileToByteArray( new File( "D:/soapui.org.htm" ) );
		byte[] f2 = FileUtils.readFileToByteArray( new File( "D:/soapui.org2.htm" ) );
		byte[] f3 = FileUtils.readFileToByteArray( new File( "D:/atlassian.com.html" ) );
		byte[] f4 = FileUtils.readFileToByteArray( new File( "D:/atlassian.com2.html" ) );
		byte[] f5 = FileUtils.readFileToByteArray( new File( "D:/reddit.com.html" ) );
		byte[] f6 = FileUtils.readFileToByteArray( new File( "D:/reddit.com2.html" ) );

		long startTime = System.currentTimeMillis();
		for( int i = 0; i < 1; i++ )
		{
			checksumForFile( f5 );
			checksumForFile( f6 );
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
			while( p<length )
			{
				if( b=='<' && i > MIN_BLOCK_SIZE && (data[p+1] == 'h' || data[p+1] == 'H') )
						break;
				if( i > MAX_BLOCK_SIZE )
				{
					if( b=='<' && (data[p+1] == 'p' || data[p+1] == 'P') )
						break;
				}
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
