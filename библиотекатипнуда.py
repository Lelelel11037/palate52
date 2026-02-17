from abc import ABC, abstractmethod

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
    def __init__(self, name):
        super().__init__(name, "user")
        self._borrowed_books = []
    
    def borrow_book(self, book_title):
        self._borrowed_books.append(book_title)
    
    def return_book(self, book_title):
        if book_title in self._borrowed_books:
            self._borrowed_books.remove(book_title)
            return True
        return False
    
    def get_borrowed_books(self):
        return self._borrowed_books.copy()
    
    def get_menu_options(self):
        return [
            "1. Просмотреть доступные книги",
            "2. Взять книгу", 
            "3. Вернуть книгу",
            "4. Просмотреть список взятых книг",
            "5. Выйти"
        ]

class Librarian(SystemUser):
    def __init__(self, name):
        super().__init__(name, "librarian")
    
    def get_menu_options(self):
        return [
            "1. Добавить новую книгу",
            "2. Удалить книгу из системы",
            "3. Зарегистрировать нового пользователя",
            "4. Просмотреть список всех пользователей",
            "5. Просмотреть список всех книг",
            "6. Выйти"
        ]

class Book:
    def __init__(self, title, author, is_available=True):
        self._title = title
        self._author = author
        self._is_available = is_available
    
    def get_title(self):
        return self._title
    
    def get_author(self):
        return self._author
    
    def is_available(self):
        return self._is_available
    
    def set_available(self, value):
        self._is_available = value
    
    def __str__(self):
        status = "Доступна" if self._is_available else "Выдана"
        return f"{self._title} - {self._author} [{status}]"

class Library:
    def __init__(self):
        self._books = []
        self._users = []
        self._current_user = None
        self._load_data()
    
    def add_book(self, title, author):
        book = Book(title, author)
        self._books.append(book)
        print(f"Книга '{title}' добавлена.")
    
    def remove_book(self, title):
        for book in self._books[:]:
            if book.get_title().lower() == title.lower():
                self._books.remove(book)
                for user in self._users:
                    user.return_book(title)
                print(f"Книга '{title}' удалена.")
                return True
        print(f"Книга '{title}' не найдена.")
        return False
    
    def register_user(self, name):
        for user in self._users:
            if user.get_name().lower() == name.lower():
                print(f"Пользователь '{name}' уже существует.")
                return None
        user = User(name)
        self._users.append(user)
        print(f"Пользователь '{name}' зарегистрирован.")
        return user
    
    def get_available_books(self):
        available = []
        for book in self._books:
            if book.is_available():
                available.append(book)
        return available
    
    def get_all_books(self):
        return self._books.copy()
    
    def get_all_users(self):
        return self._users.copy()
    
    def find_book(self, title):
        for book in self._books:
            if book.get_title().lower() == title.lower():
                return book
        return None
    
    def find_user(self, name):
        for user in self._users:
            if user.get_name().lower() == name.lower():
                return user
        return None
    
    def borrow_book(self, user_name, book_title):
        user = self.find_user(user_name)
        book = self.find_book(book_title)
        
        if not user:
            print(f"Пользователь '{user_name}' не найден.")
            return False
        
        if not book:
            print(f"Книга '{book_title}' не найдена.")
            return False
        
        if not book.is_available():
            print(f"Книга '{book_title}' уже выдана.")
            return False
        
        book.set_available(False)
        user.borrow_book(book_title)
        print(f"Книга '{book_title}' выдана.")
        return True
    
    def return_book(self, user_name, book_title):
        user = self.find_user(user_name)
        book = self.find_book(book_title)
        
        if not user:
            print(f"Пользователь '{user_name}' не найден.")
            return False
        
        if not book:
            print(f"Книга '{book_title}' не найдена.")
            return False
        
        if user.return_book(book_title):
            book.set_available(True)
            print(f"Книга '{book_title}' возвращена.")
            return True
        print(f"У вас нет книги '{book_title}'.")
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
        self._current_user = None
    
    def get_current_user(self):
        return self._current_user
    
    def _load_data(self):
        try:
            with open("books.txt", "r", encoding="utf-8") as f:
                for line in f:
                    if line.strip():
                        parts = line.strip().split("|")
                        if len(parts) == 3:
                            title, author, available = parts
                            book = Book(title, author, available == "True")
                            self._books.append(book)
        except FileNotFoundError:
            pass
        
        try:
            with open("users.txt", "r", encoding="utf-8") as f:
                current_user = None
                for line in f:
                    line = line.strip()
                    if line and not line.startswith("  "):
                        current_user = User(line)
                        self._users.append(current_user)
                    elif line.startswith("  ") and current_user:
                        book_title = line.strip()
                        current_user.borrow_book(book_title)
        except FileNotFoundError:
            pass
    
    def _save_data(self):
        try:
            with open("books.txt", "w", encoding="utf-8") as f:
                for book in self._books:
                    f.write(f"{book.get_title()}|{book.get_author()}|{book.is_available()}\n")
            
            with open("users.txt", "w", encoding="utf-8") as f:
                for user in self._users:
                    f.write(f"{user.get_name()}\n")
                    for book in user.get_borrowed_books():
                        f.write(f"  {book}\n")
        except:
            print("Ошибка сохранения.")

library = Library()

while True:
    print("1. Библиотекарь")
    print("2. Пользователь")
    print("3. Выход")
    
    choice = input("Выберите роль: ").strip()
    
    if choice == "1":
        name = input("Имя библиотекаря: ").strip()
        if library.login(name, "librarian"):
            print(f"\n--- Библиотекарь: {name} ---")
            while True:
                print("\nМеню библиотекаря:")
                for option in library.get_current_user().get_menu_options():
                    print(option)
                
                action = input("Выберите действие: ").strip()
                
                if action == "1": 
                    title = input("Название книги: ").strip()
                    author = input("Автор: ").strip()
                    if title and author:
                        library.add_book(title, author)
                
                elif action == "2": 
                    title = input("Название книги для удаления: ").strip()
                    if title:
                        library.remove_book(title)
                
                elif action == "3":
                    name_user = input("Имя нового пользователя: ").strip()
                    if name_user:
                        library.register_user(name_user)
                
                elif action == "4":
                    users = library.get_all_users()
                    if users:
                        print("\nСписок пользователей:")
                        for user in users:
                            books = user.get_borrowed_books()
                            print(f"- {user.get_name()} (книг: {len(books)})")
                    else:
                        print("Нет пользователей.")
                
                elif action == "5":
                    books = library.get_all_books()
                    if books:
                        print("\nСписок всех книг:")
                        for book in books:
                            print(f"- {book}")
                    else:
                        print("Нет книг.")
                
                elif action == "6":
                    library.logout()
                    break
                
                else:
                    print("Неверный выбор")
        else:
            print("Ошибка входа")
    
    elif choice == "2":
        name = input("Имя пользователя: ").strip()
        if library.login(name, "user"):
            print(f"\nПользователь: {name}")
            while True:
                print("\nМеню пользователя:")
                for option in library.get_current_user().get_menu_options():
                    print(option)
                
                action = input("Выберите действие: ").strip()
                
                if action == "1":
                    books = library.get_available_books()
                    if books:
                        print("\nДоступные книги:")
                        for book in books:
                            print(f"- {book.get_title()} - {book.get_author()}")
                    else:
                        print("Нет доступных книг.")
                
                elif action == "2":
                    title = input("Название книги: ").strip()
                    if title:
                        library.borrow_book(name, title)
                
                elif action == "3": 
                    title = input("Название книги: ").strip()
                    if title:
                        library.return_book(name, title)
                
                elif action == "4":
                    user = library.get_current_user()
                    books = user.get_borrowed_books()
                    if books:
                        print("\nКниги:")
                        for book_title in books:
                            print(f"- {book_title}")
                    else:
                        print("У вас нет книг.")
                
                elif action == "5":
                    library.logout()
                    break
                
                else:
                    print("Неверный выбор.")
        else:
            print("Пользователь не найден.")
    
    elif choice == "3":
        library._save_data()
        print("Выход из системы.")
        break
    
    else:
        print("Неверный выбор.")