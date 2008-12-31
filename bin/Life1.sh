#!/bin/sh
INSTALL_PATH=/home/rssh/work/TermWare.2
TERMWARE_HOME="$INSTALL_PATH"
if test -z "$TERMWARE_HOME"
then
  echo Variable TERMWARE_HOME is not set.
  echo use default: /usr/local
  TERMWARE_HOME=/usr/local/TermWare
fi
java -Xrunhprof -Dtermware.path=$TERMWARE_HOME/systems -cp $TERMWARE_HOME/lib/TermWare2.jar:$TERMWARE_HOME/lib/TermWareDemos.jar  ua.gradsoft.termwaredemos.life.Life1Main
