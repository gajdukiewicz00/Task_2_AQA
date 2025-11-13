# JSONPlaceholder API Tests

Проект автоматизированного тестирования API для JSONPlaceholder (https://jsonplaceholder.typicode.com).

## Описание

Проект содержит набор автоматизированных тестов для проверки функциональности API постов (Posts) сервиса JSONPlaceholder. Тесты покрывают основные сценарии работы с эндпоинтами GET и POST.

## Технологический стек

- **Java 17** - язык программирования
- **Maven** - система сборки и управления зависимостями
- **JUnit 5** - фреймворк для написания и запуска тестов
- **REST Assured 5.5.0** - библиотека для тестирования REST API
- **AssertJ 3.25.3** - библиотека для fluent assertions
- **Jackson** - для работы с JSON

## Структура проекта

```
src/
├── main/java/com/example/api/
│   ├── core/              # Базовые классы (Env, Specs)
│   ├── domain/
│   │   ├── client/        # API клиенты (PostsApi)
│   │   └── model/         # Модели данных (Post)
│   └── resources/
└── test/java/com/example/api/
    └── tests/             # Тестовые классы
        ├── TestBase.java
        ├── PostsGetTests.java
        └── PostsCreateTests.java
    └── resources/
        └── schemas/       # JSON схемы для валидации
            ├── post.json
            └── posts-array.json
```

## Тестовые сценарии

### GET /posts

- ✅ Базовая проверка получения всех постов
- ✅ Валидация JSON схемы
- ✅ Проверка фильтрации по userId
- ✅ Получение поста по ID
- ✅ Обработка несуществующих ID
- ✅ Проверка заголовков и времени ответа

### POST /posts

- ✅ Создание поста с валидными данными
- ✅ Обработка пустого title
- ✅ Обработка отсутствующего body

## Запуск тестов

### Запуск всех тестов

```bash
mvn test
```

### Запуск тестов с определенным тегом

```bash
mvn test -Dgroups=api
```

### Запуск конкретного тестового класса

```bash
mvn test -Dtest=PostsGetTests
```

### Запуск с кастомным baseUrl

```bash
mvn test -DbaseUrl=https://jsonplaceholder.typicode.com
```

### Запуск с кастомным максимальным временем ответа

```bash
mvn test -DmaxResponseTimeMs=3000
```

## Конфигурация

### Переменные окружения

- `baseUrl` - базовый URL API (по умолчанию: `https://jsonplaceholder.typicode.com`)
- `maxResponseTimeMs` - максимальное время ответа в миллисекундах (по умолчанию: `2000`)

## Зависимости

Основные зависимости проекта указаны в `pom.xml`:

- `io.rest-assured:rest-assured:5.5.0`
- `io.rest-assured:json-schema-validator:5.5.0`
- `org.junit.jupiter:junit-jupiter:5.10.2`
- `org.assertj:assertj-core:3.25.3`
- `com.fasterxml.jackson.core:jackson-databind:2.17.2`

## Особенности реализации

- Использование Request/Response спецификаций для переиспользования настроек
- JSON Schema валидация ответов API
- Параметризованные тесты для проверки различных входных данных
- Fluent API для более читаемых assertions
- Логирование запросов и ответов при падении тестов

## Требования

- Java 17 или выше
- Maven 3.6 или выше

## Автор

Проект создан в рамках задачи по автоматизации тестирования API.

