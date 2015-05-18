@echo off
setlocal

set PATH=%PATH%;%JAVA_HOME%\bin
set src_root=..\..\..\..\..\java

echo Processing ConfigSchema.xsd
xjc -p org.aksw.openqa.qald.schema QALDSchema.xsd -d %src_root%
echo .

pause

endlocal
