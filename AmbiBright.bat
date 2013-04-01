set folder=%~dp0
start javaw -Xmx128M -Djava.library.path=%folder%lib -cp %folder%AmbiBright.jar;%folder%lib\* ambibright.Launcher

