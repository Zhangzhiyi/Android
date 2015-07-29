@echo off

if [%1] == [] (
echo error! Please input the dir of the png files!
PAUSE
)

for /f "eol=: delims=" %%i in ('dir %1 /b /s /a-d ^|findstr /c:".png" ^|findstr /live ".9.png"') do (
pngquant %%i --ext .png.opt --quality 96 --force --verbose
if exist %%~fi.opt (
call :optipng %%~fi.opt
call :advpng %%~fi.opt
call :advdef %%~fi.opt
call :filechoose %%~fi.opt %%i
)
)
GOTO:EOF

:optipng
@echo:
echo execute optipng
optipng -o7 %1
GOTO:EOF

:advpng
@echo:
echo execute advpng
advpng -z4 %1
GOTO:EOF

:advdef
@echo:
echo execute advdef
advdef -z4 %1
GOTO:EOF

:filechoose
echo filechoose
if %~z1 lss %~z2 (
	del %2
	rename %~f1 %~nx2
)else del %1 
GOTO:EOF

