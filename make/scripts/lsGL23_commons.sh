#! /bin/sh

BUILDDIR=$1
shift
if [ -z "$BUILDDIR" ] ; then
    echo "usage $0 <BUILDDIR>"
    exit 1
fi

idir=$BUILDDIR/jogl/gensrc/classes/com/jogamp/opengl

SOURCE="$idir/GL.java $idir/GL2ES2.java $idir/GL2GL3.java $idir/GL3.java"

echo GL GL2ES2 GL2GL3 GL3 defines
sort $SOURCE | uniq -d | grep GL_ | grep -v "Part of <code>"

echo GL GL2ES2 GL2GL3 GL3 functions
sort $SOURCE | uniq -d | grep "public [a-z0-9_]* gl"
