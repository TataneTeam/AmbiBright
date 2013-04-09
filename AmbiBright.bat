set folder=%~dp0
start javaw -Xmx128M -Dlogback.configurationFile=file:/%folder%logback.xml -cp "%folder%AmbiBright.jar;%folder%lib\*" ambibright.Launcher
