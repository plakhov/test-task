#Тестовое задание

##Общая информация
Проект представляет интерпретацию некоторой системы, в которой пользователи могу создавть заявки

##Технологии
Java SE 8, Spring Boot 2.3.0, Freemarker template, PostgreSQL 12.3, Liquibase

##Deployment
Чтобы развернуть проект у себя необходим сервер с бд.
При запуске jar файла следует указать следующий переменные среду:
+ DB_URL - url,  где расположена БД
+ DB_PORT - порт для соединения

Для создания схемы БД можно запустить сам сервис и при помощи Liquibase сервис сам себе создаст схему, либо воспользоваться файлом data.sql, который кроме схемы содержит в себе тестовые данные


###Роли
В проекте существуют две роли: пользователь и администратор.
Пользователь може создавать, редактировать, удалять и просматривать свои заявки.
Администратор может создавать, редактировать, удалять и просматривать всех пользователей. 
Администратор может создавать, редактировать, удалять и просматривать заявки всех пользователей.
