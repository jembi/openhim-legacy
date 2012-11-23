package org.jembi.openhim.connector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mule.transport.tcp.protocols.AbstractByteProtocol;

public class MLLPByteProtocol extends AbstractByteProtocol
{
	// header
	public static final char vt = '\013';
	
	// footer
	public static final char fs = '\034';
	public static final char cr = '\r';

    /**
     * Create a MLLPByteProtocol object.
     */
    public MLLPByteProtocol()
    {
        super(false); // This protocol does not support streaming.
    }

    /**
     * Write the message's bytes to the socket,
     * then encapsulate the message with MLLP bytes
     */
    @Override
    protected void writeByteArray(OutputStream os, byte[] data) throws IOException
    {
    	os.write(vt);
        super.writeByteArray(os, data);
        os.write(fs);
        os.write(cr);
    }

    /**
     * Read bytes and remove MLLP byte delimiters
     */
    public Object read(InputStream is) throws IOException
    {
    	byte read[] = new byte[1];
    	safeRead(is, read);
    	
    	if (read[0] == vt) {
    	
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	        while (true)
	        {
	        	if (safeRead(is, read) < 0)
	            {
	                // We've reached EOF.  Return null, so that our
	                // caller will know there are no
	                // remaining messages
	                return null;
	            }
	        	
	        	byte b = read[0];
	        	
        		if (b == fs) {
        			safeRead(is, read);
        			if (read[0] == cr) {
        				return baos.toByteArray();
        			}
        		}
        		
        		baos.write(b);
	        }
    	}
    	
    	return null;
    }
}
