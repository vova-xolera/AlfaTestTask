# AlfaTestTask
Test task for Alfa Bank

Для запуска на URL localhost.8100 необходимо запустить sh скрипт launcher.sh 
он осуществит сборку gradle и запустит приложение в docker на указанном порте, 
плюс откроет страничку браузера. Все написано и тестировалось в Linux. 
Если запускать из под Windows могут быть вопросы с путями.

Конфигурация программы, в том числе тип валюты, указана в файлах в main/java/resources/

Если требуется сменить внешний порт, то вместо скрипта из корневой папки
проекта требуется запустить команды:
docker build . -t stock-app
docker run -d -p new_port:8100 stock-app