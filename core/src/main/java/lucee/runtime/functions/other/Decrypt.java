/**
 *
 * Copyright (c) 2014, the Railo Company Ltd. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either 
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 **/
/**
 * Implements the CFML Function decrypt
 */
package lucee.runtime.functions.other;

import lucee.runtime.PageContext;
import lucee.runtime.coder.Coder;
import lucee.runtime.crypt.CFMXCompat;
import lucee.runtime.crypt.Cryptor;
import lucee.runtime.exp.PageException;
import lucee.runtime.ext.function.Function;
import lucee.runtime.op.Caster;


public final class Decrypt implements Function {


    public synchronized static String call( PageContext pc, String input, String key ) throws PageException {

        return invoke(input, key, CFMXCompat.ALGORITHM_NAME, Cryptor.DEFAULT_ENCODING, null, 0);
    }


    public synchronized static String call( PageContext pc, String input, String key, String algorithm ) throws PageException {

        return invoke(input, key, algorithm, Cryptor.DEFAULT_ENCODING, null, 0);
    }


    public synchronized static String call( PageContext pc, String input, String key, String algorithm, String encoding ) throws PageException {

        return invoke( input, key, algorithm, encoding, null, 0 );
    }


    public synchronized static String call( PageContext pc, String input, String key, String algorithm, String encoding, Object ivOrSalt ) throws PageException {

        return invoke(input, key, algorithm, encoding, ivOrSalt, 0);
    }


    /**
     * call with all optional args
     */
    public synchronized static String call( PageContext pc, String input, String key, String algorithm, String encoding, Object ivOrSalt, double iterations ) throws PageException {

        return invoke( input, key, algorithm, encoding, ivOrSalt, Caster.toInteger( iterations ) );
    }


    public synchronized static String invoke( String input, String key, String algorithm, String encoding, Object ivOrSalt, int iterations ) throws PageException {

        try {

            if ( CFMXCompat.isCfmxCompat( algorithm ) )
                return new String( invoke( Coder.decode( encoding, input ), key, algorithm, null, 0 ), Cryptor.DEFAULT_CHARSET );

            byte[] baIVS = null;
            if ( ivOrSalt instanceof String )
                baIVS = ((String)ivOrSalt).getBytes( Cryptor.DEFAULT_CHARSET );
            else if ( ivOrSalt != null )
                baIVS = Caster.toBinary( ivOrSalt );

            return Cryptor.decrypt( input, key, algorithm, baIVS, iterations, encoding, Cryptor.DEFAULT_CHARSET  );
        }
        catch(Throwable t) {

            throw Caster.toPageException( t );
        }
    }


    public synchronized static byte[] invoke( byte[] input, String key, String algorithm, byte[] ivOrSalt, int iterations ) throws PageException {

        if ( CFMXCompat.isCfmxCompat( algorithm ) )
            return new CFMXCompat().transformString( key, input );

        return Cryptor.decrypt( input, key, algorithm, ivOrSalt, iterations );
    }
}