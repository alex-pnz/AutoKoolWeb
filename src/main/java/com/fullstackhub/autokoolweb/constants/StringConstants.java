package com.fullstackhub.autokoolweb.constants;

public class StringConstants {
    // Notifications
    public static final String NOTIFICATION_RED = "red";
    public static final String NOTIFICATION_GREEN = "green";
    public static final String LIMIT_TRIES_REACHED = "You reached the limit of tries";
    public static final int LIMIT_TRIES = 20;
    public static final int LIMIT_USERS = 30;

    // LoginView
    public static final String LOGIN_VIEW_URL = "login";
    public static final String REGISTER_VIEW_URL = "register";

    // RegisterView
    public static final String REGISTER_TEXT = "Register";
    public static final String REGISTER_USERNAME_TEXT = "Username";
    public static final String REGISTER_PASSWORD_TEXT = "Password";
    public static final String REGISTER_CONFIRM_PASSWORD_TEXT = "Confirm password";
    public static final String REGISTER_ENTER_USERNAME = "Enter a username";
    public static final String REGISTER_ENTER_PASSWORD = "Enter a password";
    public static final String REGISTER_MATCH_PASSWORD = "Passwords don't match";
    public static final String REGISTER_SUCCESS = "Registration succeeded";
    public static final String REGISTER_FAIL = "Registration failed. Limit of users is reached";
    public static final String REGISTER_USERNAME_EXISTS = "This username is already in use. Try again...";

    // UserView
    public static final String USER_VIEW_TITLE = "Autokool: User";
    public static final String USER_VIEW_URL = "user";
    public static final String CHOOSE_ANSWER = "Выберите ответ!";
    public static final String EXAM_INTERRUPTED = "Экзамен прерван!";
    public static final String EXAM_PASSED = "Экзамен сдан успешно!";
    public static final String EXAM_FAILED = "Вы не сдали экзамен!";
    public static final String LABEL_ID = "Студент Id: ";
    public static final String LABEL_NAME = "Имя пользователя: ";
    public static final String LABEL_STAT = "Статистика: ";
    public static final String LABEL_PASS = "Успешных попыток: ";
    public static final String LABEL_FAIL = "Не удачных попыток: ";
    public static final String LABEL_INCOMPLETE = "Не завершенных: ";
    public static final String LABEL_TOTAL = "Всего: ";
    public static final String BUTTON_START = "Начать Экзамен";
    public static final String BUTTON_EXIT = "Выйти";
    public static final String BUTTON_CHECK = "Проверить";
    public static final String BUTTON_NEXT = "Следующий Вопрос";
    public static final String BUTTON_INCOMPLETE = "Завершить";
    public static final String LABEL_CORRECT_YES = "Правильно";
    public static final String LABEL_CORRECT_NO = "Не правильно";
    public static final String LABEL_STATS_1 = "Успешно";
    public static final String LABEL_STATS_2 = "Не удачно";
    public static final String LABEL_STATS_3 = "Не завершил";
    public static final String LABEL_STATS_4 = "Всего";
    public static final String LABEL_TICKET = "Билет ";

    // AdminLayout
    public static final String ADMIN_LAYOUT_H1 = "AutoKoolWeb";
    public static final String ADMIN_LAYOUT_SIDENAV_USERS = "Пользователи";
    public static final String ADMIN_LAYOUT_SIDENAV_QUESTIONS = "Вопросы";

    // AdminQuestionEditForm
    public static final String ADMIN_QUESTION_FIELD = "Вопрос";
    public static final String ADMIN_QUESTION_OPTION1 = "Вариант ответа 1";
    public static final String ADMIN_QUESTION_OPTION2 = "Вариант ответа 2";
    public static final String ADMIN_QUESTION_OPTION3 = "Вариант ответа 3";
    public static final String ADMIN_QUESTION_BUTTON_SAVE = "Изменить";
    public static final String ADMIN_QUESTION_BUTTON_DELETE = "Удалить";
    public static final String ADMIN_QUESTION_CHOOSE_QUESTION = "Выберите вопрос из списка!";

    // AdminQuestionNewForm
    public static final String ADMIN_QUESTION_NEW_BUTTON = "Добавить";
    public static final String ADMIN_QUESTION_NEW_ENTER_INFO = "Введите вопрос и варианты ответов!";

    // AdminQuestionsView
    public static final String ADMIN_QUESTIONS_TITLE = "Autokool: Questions";
    public static final String ADMIN_QUESTIONS_URL = "admin-questions";
    public static final String ADMIN_QUESTIONS_ADD_IMAGE = "Выбрать картинку...";
    public static final String ADMIN_QUESTIONS_IMAGE_CHECKBOX = "Добавить картинку к вопросу";
    public static final String ADMIN_QUESTIONS_TAB1 = "Редактировать вопрос";
    public static final String ADMIN_QUESTIONS_TAB2 = "Добавить новый воопрос";
    public static final String IMAGE_FOLDER_PATH = "src/main/dev-bundle/webapp/assets/images/%s";

//    public static final String IMAGE_FOLDER_PATH = "https://res.cloudinary.com/diizdixw9/image/upload/v1698590885/%s";
    public static final String ADMIN_QUESTIONS_DELETE = "Вопрос удален!";
    public static final String ADMIN_QUESTIONS_CANT_SAVE = "Не получилось сохранить файл!";
    public static final String ADMIN_QUESTIONS_SAVED = "Вопрос сохранен!";
    public static final String ADMIN_QUESTIONS_CANT_SAVE_QUESTION = "Не получилось сохранить вопрос!";

    // AdminUserEditForm
    public static final String ADMIN_USER_NAME = "Имя";
    public static final String ADMIN_USER_PASSWORD = "Пароль";
    public static final String ADMIN_USER_ROLE = "Роль";
    public static final String ADMIN_USER_BUTTON_CHANGE = "Изменить";
    public static final String ADMIN_USER_BUTTON_DELETE = "Удалить";
    public static final String ADMIN_USER_CHOOSE_STUDENT = "Выберите студента из списка!";
    public static final String ADMIN_USER_NEW_BUTTON = "Добавить";
    public static final String ADMIN_USER_CANT_SAVE = "Пользователь не сохранен!";

    // AdminUsersView
    public static final String ADMIN_USERS_TITLE = "Autokool: Users";
    public static final String ADMIN_USERS_URL = "admin-users";
    public static final String ADMIN_USERS_EDIT = "Редактировать данные студента";
    public static final String ADMIN_USERS_NEW = "Добавить нового студента";
    public static final String ADMIN_USERS_SAVED = "Пользователь сохранен!";
}
