import json

class User:
    def __init__(self, name):
        self.name = name
        self.borrowed_books = []
    
    def get_menu(self):
        return [
            "Показать доступные книги",
            "Взять книгу",
            "Вернуть книгу",
            "Мои книги",
            "Выйти"
        ]

class Librarian:
    def __init__(self, name):
        self.name = name
    
    def get_menu(self):
        return [
            "Добавить книгу",
            "Удалить книгу",
            "Добавить пользователя",
            "Список пользователей",
            "Список всех книг",
            "Выйти"
        ]

class Book:
    def __init__(self, title, author, available=True):
        self.title = title
        self.author = author
        self.available = available

class Library:
    def __init__(self):
        self.books = []
        self.users = []
        self.current_user = None
        self.load_data()
    
    def load_data(self):
        try:
            with open("books.txt", "r", encoding="utf-8") as f:
                data = json.load(f)
                for item in data:
                    self.books.append(Book(item["title"], item["author"], item["available"]))
        except:
            pass
        
        try:
            with open("users.txt", "r", encoding="utf-8") as f:
                data = json.load(f)
                for item in data:
                    user = User(item["name"])
                    user.borrowed_books = item["borrowed_books"]
                    self.users.append(user)
        except:
            pass
    
    def save_data(self):
        books_data = [{"title": b.title, "author": b.author, "available": b.available} 
                     for b in self.books]
        users_data = [{"name": u.name, "borrowed_books": u.borrowed_books} 
                     for u in self.users]
        
        with open("books.txt", "w", encoding="utf-8") as f:
            json.dump(books_data, f, ensure_ascii=False, indent=2)
        
        with open("users.txt", "w", encoding="utf-8") as f:
            json.dump(users_data, f, ensure_ascii=False, indent=2)
    
    def find_user(self, name):
        for user in self.users:
            if user.name == name:
                return user
        return None
    
    def find_book(self, title):
        for book in self.books:
            if book.title == title:
                return book
        return None
    
    def add_book(self, title, author):
        self.books.append(Book(title, author))
        print(f"Книга '{title}' добавлена.")
    
    def remove_book(self, title):
        book = self.find_book(title)
        if book:
            self.books.remove(book)
            for user in self.users:
                if title in user.borrowed_books:
                    user.borrowed_books.remove(title)
            print(f"Книга '{title}' удалена.")
        else:
            print("Книга не найдена.")
    
    def add_user(self, name):
        if not self.find_user(name):
            self.users.append(User(name))
            print(f"Пользователь '{name}' добавлен.")
        else:
            print("Пользователь уже существует.")
    
    def borrow_book(self, user_name, book_title):
        user = self.find_user(user_name)
        book = self.find_book(book_title)
        
        if not user:
            print("Пользователь не найден.")
            return
        
        if not book:
            print("Книга не найдена.")
            return
        
        if not book.available:
            print("Книга уже выдана.")
            return
        
        book.available = False
        user.borrowed_books.append(book_title)
        print(f"Книга '{book_title}' выдана.")
    
    def return_book(self, user_name, book_title):
        user = self.find_user(user_name)
        book = self.find_book(book_title)
        
        if not user or not book:
            print("Ошибка данных.")
            return
        
        if book_title in user.borrowed_books:
            user.borrowed_books.remove(book_title)
            book.available = True
            print("Книга возвращена.")
        else:
            print("У вас нет этой книги.")
    
    def get_available_books(self):
        return [b for b in self.books if b.available]
    
    def login(self, name, role):
        if role == "librarian":
            self.current_user = Librarian(name)
            return True
        else:
            user = self.find_user(name)
            if user:
                self.current_user = user
                return True
        return False
    
    def logout(self):
        self.current_user = None

def main():
    lib = Library()
    
    while True:
        print("\n1. Библиотекарь\n2. Пользователь\n3. Выход")
        choice = input("Выберите роль: ")
        
        if choice == "1":
            name = input("Имя: ")
            if lib.login(name, "librarian"):
                while True:
                    print(f"\nБиблиотекарь: {name}")
                    for i, option in enumerate(lib.current_user.get_menu(), 1):
                        print(f"{i}. {option}")
                    
                    action = input("Выберите: ")
                    
                    if action == "1":
                        title = input("Название: ")
                        author = input("Автор: ")
                        lib.add_book(title, author)
                    
                    elif action == "2":
                        title = input("Название для удаления: ")
                        lib.remove_book(title)
                    
                    elif action == "3":
                        name = input("Имя нового пользователя: ")
                        lib.add_user(name)
                    
                    elif action == "4":
                        if lib.users:
                            for user in lib.users:
                                print(f"{user.name} (книг: {len(user.borrowed_books)})")
                        else:
                            print("Нет пользователей.")
                    
                    elif action == "5":
                        if lib.books:
                            for book in lib.books:
                                status = "✓" if book.available else "✗"
                                print(f"{book.title} - {book.author} [{status}]")
                        else:
                            print("Нет книг.")
                    
                    elif action == "6":
                        lib.logout()
                        break
            else:
                print("Ошибка входа.")
        
        elif choice == "2":
            name = input("Имя: ")
            if lib.login(name, "user"):
                while True:
                    print(f"\nПользователь: {name}")
                    for i, option in enumerate(lib.current_user.get_menu(), 1):
                        print(f"{i}. {option}")
                    
                    action = input("Выберите: ")
                    
                    if action == "1":
                        books = lib.get_available_books()
                        if books:
                            for book in books:
                                print(f"{book.title} - {book.author}")
                        else:
                            print("Нет доступных книг.")
                    
                    elif action == "2":
                        title = input("Название книги: ")
                        lib.borrow_book(name, title)
                    
                    elif action == "3":
                        title = input("Название книги: ")
                        lib.return_book(name, title)
                    
                    elif action == "4":
                        user = lib.find_user(name)
                        if user.borrowed_books:
                            for book in user.borrowed_books:
                                print(f"- {book}")
                        else:
                            print("Нет взятых книг.")
                    
                    elif action == "5":
                        lib.logout()
                        break
            else:
                print("Пользователь не найден.")
        
        elif choice == "3":
            lib.save_data()
            print("Данные сохранены. Выход.")
            break

if __name__ == "__main__":
    main()