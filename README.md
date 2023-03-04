# School
Проект 3-го курса по Java-разработке.
Это веб-сервис — аналог международного учебного заведения волшебников Хогвартс.
Проект дополняется разными функциональностями по мере прохождения тем курса.
# Description
<b>1-й этап</b>. Создано Spring Boot приложение, используя модель MVC. Созданы две модели Student, Faculty.
Созданы два сервиса StudentService и FacultyService. В них реализованы CRUD-методы.
Созданы два контроллера с CRUD-эндпоинтами.
Созданы два эндпоинта в контроллерах StudentController и FacultyController.
В StudentController добавлен эндпоинт, который принимает число (возраст — поле age) и возвращает список студентов, у которых совпал возраст с переданным числом.
В FacultyController добавлен эндпоинт, который принимает строку (цвет — поле color) и возвращает список факультетов, у которых совпал цвет с переданной строкой.
Добавлен Swagger к проекту. Для этого добавлена зависимость к проекту.

<b>2-й этап.</b> Подключена БД PostgreSQL hogwarts.
Изменены модели Student и Faculty. К каждому классу добавлена аннотация @Entity. А к полю id добавлено две аннотации: @Id и @GeneratedValue.
Создан пакет repository. В нем находятся два интерфейса: StudentRepository и FacultyRepository.
В сервисы добавлены репозитории. Переделана логика работы с данными. Теперь все данные хранятся в БД, а сервисы пользуются репозиториями для их получения.

<b>3-й этап.</b> Добавлен эндпоинт для получения всех студентов, возраст которых находится в промежутке, пришедшем в запросе.  
Добавлен эндпоинт для поиска факультета по имени или цвету, игнорируя регистр.  
Добавлено 5 SQL-запросов. Составленные запросы расположены в файле scripts.sql.  
Настроена связь ManyToOne между студентами и факультетом. Добавлены два эндпоинта, которые позволяют получить студентов факультета и факультет студента.

<b>4-й этап.</b>
# Технологии, использованные в проекте.
Java-17, SpringBoot-2.7.7, Swagger
