/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * - Redistribution of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN
 * MICROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR
 * ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR
 * DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE
 * DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed or intended for use
 * in the design, construction, operation or maintenance of any nuclear
 * facility.
 * 
 * Sun gratefully acknowledges that this software was originally authored
 * and developed by Kenneth Bradley Russell and Christopher John Kline.
 */

package net.java.games.jogl;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import net.java.games.jogl.impl.*;

/** <P> Provides a virtual machine- and operating system-independent
    mechanism for creating {@link net.java.games.jogl.GLCanvas} and {@link
    net.java.games.jogl.GLJPanel} objects. </P>

    <P> The {@link net.java.games.jogl.GLCapabilities} objects passed in to the
    various factory methods are used as a hint for the properties of
    the returned drawable. The default capabilities selection
    algorithm (equivalent to passing in a null {@link
    GLCapabilitiesChooser}) is described in {@link
    DefaultGLCapabilitiesChooser}. Sophisticated applications needing
    to change the selection algorithm may pass in their own {@link
    GLCapabilitiesChooser} which can select from the available pixel
    formats. </P>

    <P> Because of the multithreaded nature of the Java platform's
    window system toolkit, it is typically not possible to immediately
    reject a given {@link GLCapabilities} as being unsupportable by
    either returning <code>null</code> from the creation routines or
    raising a {@link GLException}. The semantics of the rejection
    process are (unfortunately) left unspecified for now. The current
    implementation will cause a {@link GLException} to be raised
    during the first repaint of the {@link GLCanvas} or {@link
    GLJPanel} if the capabilities can not be met. </P>
*/

public class GLDrawableFactory {
  private static GLDrawableFactory factory = new GLDrawableFactory();

  private GLDrawableFactory() {}

  /** Returns the sole GLDrawableFactory instance. */
  public static GLDrawableFactory getFactory() {
    return factory;
  }

  /**
   * Selects an AWT GraphicsConfiguration on the specified
   * GraphicsDevice compatible with the supplied GLCapabilities. This
   * method is intended to be used by applications which do not use
   * the supplied GLCanvas class but instead wrap their own Canvas
   * with a GLDrawable. Some platforms (specifically X11) require the
   * GraphicsConfiguration to be specified when the platform-specific
   * window system object, such as a Canvas, is created. This method
   * returns null on platforms on which the OpenGL pixel format
   * selection process is performed later.
   *
   * @see java.awt.Canvas(java.awt.GraphicsConfiguration)
   */
  public GraphicsConfiguration
    chooseGraphicsConfiguration(GLCapabilities capabilities,
                                GLCapabilitiesChooser chooser,
                                GraphicsDevice device) {
    return GLContextFactory.getFactory().chooseGraphicsConfiguration(capabilities, chooser, device);
  }

  /**
   * Returns a GLDrawable that wraps a platform-specific window system
   * object, such as an AWT or LCDUI Canvas. On platforms which
   * support it, selects a pixel format compatible with the supplied
   * GLCapabilities, or if the passed GLCapabilities object is null,
   * uses a default set of capabilities. On these platforms, uses
   * either the supplied GLCapabilitiesChooser object, or if the
   * passed GLCapabilitiesChooser object is null, uses a
   * DefaultGLCapabilitiesChooser instance.
   *
   * @throw IllegalArgumentException if the passed target is either
   *        null or its data type is not supported by this GLDrawableFactory.
   * @throw GLException if any window system-specific errors caused
   *        the creation of the GLDrawable to fail.
   */
  public GLDrawable getGLDrawable(Object target,
                                  GLCapabilities capabilities,
                                  GLCapabilitiesChooser chooser)
    throws IllegalArgumentException, GLException {
    return GLContextFactory.getFactory().getGLDrawable(target, capabilities, chooser);
  }
  
  //----------------------------------------------------------------------
  // Methods to create high-level objects

  /** Creates a {@link GLCanvas} on the default graphics device with
      the specified capabilities using the default capabilities
      selection algorithm. */
  public GLCanvas createGLCanvas(GLCapabilities capabilities) {
    return createGLCanvas(capabilities, null, null, null);
  }

  /** Creates a {@link GLCanvas} on the specified graphics device with
      the specified capabilities using the supplied capabilities
      selection algorithm. A null chooser is equivalent to using the
      {@link DefaultGLCapabilitiesChooser}.  The canvas will share
      textures and display lists with the specified {@link GLContext};
      the context must either be null or have been fabricated by
      classes in this package. A null context indicates no sharing. A
      null GraphicsDevice is equivalent to using that returned from
      <code>GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()</code>. */
  public GLCanvas createGLCanvas(GLCapabilities capabilities,
                                 GLCapabilitiesChooser chooser,
                                 GLContext shareWith,
                                 GraphicsDevice device) {
    if (chooser == null) {
      chooser = new DefaultGLCapabilitiesChooser();
    }
    if (device == null) {
      device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }
    // The platform-specific GLContextFactory will only provide a
    // non-null GraphicsConfiguration on platforms where this is
    // necessary (currently only X11, as Windows allows the pixel
    // format of the window to be set later and Mac OS X seems to
    // handle this very differently than all other platforms). On
    // other platforms this method returns null; it is the case (at
    // least in the Sun AWT implementation) that this will result in
    // equivalent behavior to calling the no-arg super() constructor
    // for Canvas.
    return new GLCanvas(capabilities,
                        chooser,
                        shareWith,
                        device);
  }

  /** Creates a {@link GLJPanel} with the specified capabilities using
      the default capabilities selection algorithm. */
  public GLJPanel createGLJPanel(GLCapabilities capabilities) {
    return createGLJPanel(capabilities, null, null);
  }

  /** Creates a {@link GLJPanel} with the specified capabilities using
      the default capabilities selection algorithm. The panel will
      share textures and display lists with the specified {@link
      GLContext}; the context must either be null or have been
      fabricated by classes in this package. A null context indicates
      no sharing. */
  public GLJPanel createGLJPanel(GLCapabilities capabilities, GLContext shareWith) {
    return createGLJPanel(capabilities, null, shareWith);
  }

  /** Creates a {@link GLJPanel} with the specified capabilities using
      the supplied capabilities selection algorithm. A null chooser is
      equivalent to using the {@link DefaultGLCapabilitiesChooser}. */
  public GLJPanel createGLJPanel(GLCapabilities capabilities, GLCapabilitiesChooser chooser) {
    return createGLJPanel(capabilities, chooser, null);
  }

  /** Creates a {@link GLJPanel} with the specified capabilities using
      the supplied capabilities selection algorithm. A null chooser is
      equivalent to using the {@link DefaultGLCapabilitiesChooser}.
      The panel will share textures and display lists with the
      specified {@link GLContext}; the context must either be null or
      have been fabricated by classes in this package. A null context
      indicates no sharing. */
  public GLJPanel createGLJPanel(GLCapabilities capabilities,
                                 GLCapabilitiesChooser chooser,
                                 GLContext shareWith) {
    if (chooser == null) {
      chooser = new DefaultGLCapabilitiesChooser();
    }
    return new GLJPanel(capabilities, chooser, shareWith);
  }

  /**
   * Returns true if it is possible to create a GLPbuffer with the
   * given capabilites and dimensions.
   */
  public boolean canCreateGLPbuffer(GLCapabilities capabilities,
                                    int initialWidth,
                                    int initialHeight) {
    return GLContextFactory.getFactory().canCreateGLPbuffer(capabilities,
                                                            initialWidth,
                                                            initialHeight);
  }


  /**
   * Creates a GLPbuffer with the given capabilites and dimensions.
   */
  public GLPbuffer createGLPbuffer(GLCapabilities capabilities,
                                   int initialWidth,
                                   int initialHeight,
                                   GLContext shareWith) {
    return GLContextFactory.getFactory().createGLPbuffer(capabilities,
                                                         initialWidth,
                                                         initialHeight,
                                                         shareWith);
  }
}
