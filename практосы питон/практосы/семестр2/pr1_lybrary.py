import json
from abc import ABC, abstractmethod
import os

class SystemUser(ABC):
    def __init__(self, name, role):
        self._name = name
        self._role = role

    @abstractmethod
    def get_menu_options(self):
        pass

    def get_name(self):
        return self._name

    def get_role(self):
        return self._role


class User(SystemUser):
    def __init__(self, name, borrowed_books=None):
        super().__init__(name, "user")
        self._borrowed_books = []
        if borrowed_books is not None:
            self._borrowed_books.extend(borrowed_books)

    def borrow_book(self, book_title):
        self._borrowed_books.append(book_title)

    def return_book(self, book_title):
        if book_title in self._borrowed_books:
            self._borrowed_books.remove(book_title)
            return True
        return False

    def get_borrowed_books(self):
        return list(self._borrowed_books)

    def get_menu_options(self):
        return [
            "Просмотреть доступные книги",
            "Взять книгу",
            "Вернуть книгу",
            "Просмотреть список взятых книг",
            "Выйти"
        ]

    def to_dict(self):
        return {
            "name": self._name,
            "role": self._role,
            "borrowed_books": self._borrowed_books
        }

    @classmethod
    def from_dict(cls, data):
        return cls(data["name"], data.get("borrowed_books", []))


class Librarian(SystemUser):
    def __init__(self, name):
        super().__init__(name, "librarian")

    def get_menu_options(self):
        return [
            "Добавить новую книгу",
            "Удалить книгу из системы",
            "Зарегистрировать нового пользователя",
            "Просмотреть список всех пользователей",
            "Просмотреть список всех книг",
            "Выйти"
        ]

    def to_dict(self):
        return {
            "name": self._name,
            "role": self._role
        }

    @classmethod
    def from_dict(cls, data):
        return cls(data["name"])


class Book:
    def __init__(self, title, author, is_available=True):
        self._title = title
        self._author = author
        self._is_available = is_available

    def get_title(self):
        return self._title

    def get_author(self):
        return self._author

    def get_is_available(self):
        return self._is_available

    def set_is_available(self, value):
        self._is_available = value

    def __str__(self):
        status = "Доступна" if self._is_available else "Выдана"
        return f"'{self._title}' - {self._author} [{status}]"

    def to_dict(self):
        return {
            "title": self._title,
            "author": self._author,
            "is_available": self._is_available
        }

    @classmethod
    def from_dict(cls, data):
        return cls(data["title"], data["author"], data.get("is_available", True))


class Library:
    def __init__(self):
        self._books = []
        self._users = []
        self._current_user = None
        self._load_data()

    def add_book(self, title, author):
        for book in self._books:
            if book.get_title().lower() == title.lower() and book.get_author().lower() == author.lower():
                print(f"Книга '{title}' уже существует в библиотеке.")
                return False

        book = Book(title, author)
        self._books.append(book)
        print(f"Книга '{title}' добавлена.")
        self._save_data()
        return True

    def remove_book(self, title):
        for book in self._books:
            if book.get_title().lower() == title.lower():
                self._books.remove(book)
                for user in self._users:
                    if title in user.get_borrowed_books():
                        user.return_book(title)
                print(f"Книга '{title}' удалена.")
                self._save_data()
                return True
        print(f"Книга '{title}' не найдена.")
        return False

    def get_available_books(self):
        return [book for book in self._books if book.get_is_available()]

    def get_all_books(self):
        return list(self._books)

    def find_book(self, title):
        for book in self._books:
            if book.get_title().lower() == title.lower():
                return book
        return None

    def register_user(self, name):
        if not any(user.get_name().lower() == name.lower() for user in self._users):
            user = User(name)
            self._users.append(user)
            print(f"Пользователь '{name}' зарегистрирован.")
            self._save_data()
            return user
        print(f"Пользователь '{name}' уже существует.")
        return None

    def find_user(self, name):
        for user in self._users:
            if user.get_name().lower() == name.lower():
                return user
        return None

    def get_all_users(self):
        return list(self._users)

    def borrow_book_for_user(self, user_name, book_title):
        user = self.find_user(user_name)
        book = self.find_book(book_title)

        if not user:
            print(f"Пользователь '{user_name}' не найден.")
            return False

        if not book:
            print(f"Книга '{book_title}' не найдена.")
            return False

        if not book.get_is_available():
            print(f"Книга '{book_title}' уже выдана.")
            return False

        book.set_is_available(False)
        user.borrow_book(book_title)
        print(f"Книга '{book_title}' выдана пользователю {user_name}.")
        self._save_data()
        return True

    def return_book_from_user(self, user_name, book_title):
        user = self.find_user(user_name)
        book = self.find_book(book_title)

        if not user:
            print(f"Пользователь '{user_name}' не найден.")
            return False

        if not book:
            print(f"Книга '{book_title}' не найдена.")
            return False

        if user.return_book(book_title):
            book.set_is_available(True)
            print(f"Книга '{book_title}' возвращена.")
            self._save_data()
            return True
        print(f"У пользователя {user_name} нет книги '{book_title}'.")
        return False

    def login(self, name, role):
        if role == "librarian":
            self._current_user = Librarian(name)
            return True

        user = self.find_user(name)
        if user:
            self._current_user = user
            return True
        return False

    def logout(self):
        if self._current_user:
            print(f"{self._current_user.get_name()} вышел из системы.")
        self._current_user = None

    def get_current_user(self):
        return self._current_user

    def _load_data(self):
        try:
            with open("library.json", "r", encoding="utf-8") as f:
                data = json.load(f)

                self._books = []
                for book_data in data.get("books", []):
                    self._books.append(Book.from_dict(book_data))

                self._users = []
                for user_data in data.get("users", []):
                    if user_data.get("role") == "user":
                        self._users.append(User.from_dict(user_data))

        except FileNotFoundError:
            pass
        except json.JSONDecodeError:
            print("Ошибка: файл library.json повреждён. Создаём новые данные.")
        except Exception as e:
            print(f"Ошибка загрузки данных: {e}")

    def _save_data(self):
        try:
            data = {
                "books": [book.to_dict() for book in self._books],
                "users": [user.to_dict() for user in self._users]
            }

            with open("library.json", "w", encoding="utf-8") as f:
                json.dump(data, f, ensure_ascii=False, indent=4)

        except Exception as e:
            print(f"Ошибка сохранения данных: {e}")

    def show_json_file(self):
        try:
            with open("library.json", "r", encoding="utf-8") as f:
                content = f.read()
                if content:
                    print("\n=== Содержимое library.json ===")
                    data = json.loads(content)
                    print(json.dumps(data, ensure_ascii=False, indent=4))
                else:
                    print("Файл library.json пуст.")
        except FileNotFoundError:
            print("Файл library.json не найден.")
        except json.JSONDecodeError:
            print("Ошибка: файл library.json повреждён.")
        except Exception as e:
            print(f"Ошибка чтения файла: {e}")


library = Library()

while True:
    print(" вы в системе")
    print("1. Войти как библиотекарь")
    print("2. Войти как пользователь")
    print("3. Показать содержимое library.json")
    print("4. Выход")

    choice = input("Выберите действие: ").strip()

    if choice == "1":
        print("\nВход для библиотекаря")
        name = input("Введите имя библиотекаря: ").strip()
        if not name:
            print("Имя не может быть пустым.")
            continue

        if library.login(name, "librarian"):
            current_user = library.get_current_user()

            while True:
                print(f"  Библиотекарь: {current_user.get_name()}")

                menu = current_user.get_menu_options()
                for i, option in enumerate(menu, 1):
                    print(f"{i}. {option}")
                action = input("Выберите действие: ").strip()

                if action == "1":
                    print("\nДобавление книги")
                    title = input("Название книги: ").strip()
                    author = input("Автор: ").strip()
                    if title and author:
                        library.add_book(title, author)
                    else:
                        print("Название и автор не могут быть пустыми.")

                elif action == "2":
                    print("\nУдаление книги")
                    title = input("Название книги для удаления: ").strip()
                    if title:
                        library.remove_book(title)

                elif action == "3":
                    print("\nРегистрация пользователя")
                    name_user = input("Имя нового пользователя: ").strip()
                    if name_user:
                        library.register_user(name_user)

                elif action == "4":
                    print("\n Список пользователей")
                    users = library.get_all_users()
                    if users:
                        print(f"{'Имя':<20} {'Книг взято':<12} {'Список книг'}")
                        print("-" * 50)
                        for user in users:
                            books = user.get_borrowed_books()
                            books_str = ", ".join(books) if books else "нет книг"
                            print(f"{user.get_name():<20} {len(books):<12} {books_str}")
                    else:
                        print("Нет зарегистрированных пользователей.")

                elif action == "5":
                    print("\n Список всех книг")
                    books = library.get_all_books()
                    if books:
                        print(f"{'Название':<30} {'Автор':<20} {'Статус'}")
                        for book in books:
                            status = "Доступна" if book.get_is_available() else "Выдана"
                            print(f"{book.get_title():<30} {book.get_author():<20} {status}")
                    else:
                        print("В библиотеке нет книг.")

                elif action == "6":
                    library.logout()
                    break

                else:
                    print("Неверный выбор. Попробуйте снова.")
        else:
            print("Ошибка входа. Библиотекарь не найден.")

    elif choice == "2":
        print("\n Вход для пользователя")
        name = input("Введите ваше имя: ").strip()
        if not name:
            print("Имя не может быть пустым.")
            continue

        if library.login(name, "user"):
            current_user = library.get_current_user()

            while True:
                print(f"  Пользователь: {current_user.get_name()}")

                menu = current_user.get_menu_options()
                for i, option in enumerate(menu, 1):
                    print(f"{i}. {option}")

                action = input("Выберите действие: ").strip()

                if action == "1":
                    print("\n Доступные книги")
                    books = library.get_available_books()
                    if books:
                        print(f"{'Название':<30} {'Автор'}")
                        for book in books:
                            print(f"{book.get_title():<30} {book.get_author()}")
                    else:
                        print("Нет доступных книг.")

                elif action == "2":
                    print("\n Взятие книги")
                    title = input("Название книги: ").strip()
                    if title:
                        library.borrow_book_for_user(name, title)

                elif action == "3":
                    print("\n Возврат книги")
                    title = input("Название книги для возврата: ").strip()
                    if title:
                        library.return_book_from_user(name, title)

                elif action == "4":
                    print("\n Мои книги")
                    books = current_user.get_borrowed_books()
                    if books:
                        print("Ваши книги:")
                        for i, book_title in enumerate(books, 1):
                            print(f"  {i}. {book_title}")
                    else:
                        print("У вас нет взятых книг.")

                elif action == "5":
                    library.logout()
                    break

                else:
                    print("Неверный выбор. Попробуйте снова.")
        else:
            print("Пользователь не найден. Зарегистрируйтесь у библиотекаря.")

    elif choice == "3":
        library.show_json_file()

    elif choice == "4":
        print("\nСохранение данных...")
        library._save_data()
        print("Данные сохранены. До свидания!")
        break

    else:
        print("Неверный выбор. Пожалуйста, выберите 1, 2, 3 или 4.")