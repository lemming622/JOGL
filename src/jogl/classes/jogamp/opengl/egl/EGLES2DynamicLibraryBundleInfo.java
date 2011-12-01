/**
 * Copyright 2010 JogAmp Community. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 * 
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY JogAmp Community ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JogAmp Community OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of JogAmp Community.
 */
 
package jogamp.opengl.egl;

import java.util.*;

public class EGLES2DynamicLibraryBundleInfo extends EGLDynamicLibraryBundleInfo {
    protected EGLES2DynamicLibraryBundleInfo() {
        super();
    }

    public List<List<String>> getToolLibNames() {
        final List<List<String>> libsList = new ArrayList<List<String>>();
        {
            final List<String> libsGL = new ArrayList<String>();
            
            // GLESv2
            libsGL.add("GLESv2");
            libsGL.add("libGLESv2.so");
            // this is the default lib name, according to the spec 
            libsGL.add("libGLESv2.so.2");

            // alternative names
            libsGL.add("GLES20");
            libsGL.add("GLESv2_CM");

            // for windows distributions using the 'unlike' lib prefix
            // where our tool does not add it.
            libsGL.add("libGLESv2");
            libsGL.add("libGLESv2_CM");
            libsGL.add("libGLES20"); 
    
            libsList.add(libsGL);
        }
        libsList.add(getEGLLibNamesList());
        
        return libsList;
    }    
    
}

