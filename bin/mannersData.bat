set INSTALL_PATH=$INSTALL_PATH
java -Dtermware.path="%INSTALL_PATH%\systems" -cp "%INSTALL_PATH%\lib\TermWare2.jar";"%INSTALL_PATH%\lib\TermWareDemos.jar" ua.gradsoft.termwaredemos.benchmarks.manners.MannersData %1 %2 %3 %4 %5 %6 %7 %8
