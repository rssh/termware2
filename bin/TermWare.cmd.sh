#!/bin/sh
INSTALL_PATH=/home/rssh/work/TermWare.2
TERMWARE_HOME=$INSTALL_PATH
VERSION=2.3.0
if test -z "$TERMWARE_HOME"
then
  echo Variable TERMWARE_HOME is not set.
  echo use default: /usr/local
  TERMWARE_HOME=/usr/local/TermWare
fi
java -Xmx500M -Xss4048k -Dtermware.path=$TERMWARE_HOME/systems -jar $TERMWARE_HOME/lib/TermWare-$VERSION.jar $* || echo JVM exit with code!=0
