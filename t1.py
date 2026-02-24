import tkinter as tk
from tkinter import messagebox
from typing import List, Optional, Union

# Класс для представления товара
class Product:
    def __init__(self, name: str, quantity: int, price: float):
        self.name = name
        self.quantity = quantity
        self.price = price

    def update_quantity(self, amount: int) -> None:
        """Обновляет количество товара"""
        self.quantity += amount
    
    def get_info(self) -> str:
        """Возвращает информацию о товаре"""
        return f"{self.name}, Количество: {self.quantity}, Цена: {self.price:.2f}"


# Класс для управления складом
class Warehouse:
    def __init__(self):
        self.products: List[Product] = []

    def add_product(self, name: str, quantity: int, price: float) -> None:
        """Добавляет новый товар на склад"""
        # Проверяем, существует ли уже такой товар
        existing_product = self._find_product(name)
        if existing_product:
            existing_product.update_quantity(quantity)
        else:
            product = Product(name, quantity, price)
            self.products.append(product)

    def remove_product(self, name: str) -> str:
        """Удаляет товар со склада по имени"""
        product = self._find_product(name)
        if product:
            self.products.remove(product)
            return f"Товар '{name}' удален."
        return f"Товар '{name}' не найден."

    def update_product_quantity(self, name: str, amount: int) -> str:
        """Обновляет количество товара"""
        product = self._find_product(name)
        if product:
            product.update_quantity(amount)
            return f"Количество товара '{name}' обновлено. Новое количество: {product.quantity}"
        return f"Товар '{name}' не найден."

    def list_products(self) -> str:
        """Возвращает список товаров на складе"""
        if not self.products:
            return "Нет товаров на складе."
        
        product_list = [product.get_info() for product in self.products]
        return "\n".join(product_list)

    def total_inventory_value(self) -> float:
        """Возвращает общую стоимость товаров на складе"""
        return sum(product.price * product.quantity for product in self.products)

    def _find_product(self, name: str) -> Optional[Product]:
        """Внутренний метод для поиска товара по имени"""
        for product in self.products:
            if product.name == name:
                return product
        return None


# Графический интерфейс
class WarehouseApp:
    def __init__(self, master):
        self.master = master
        self.master.title("Управление складом")
        self.master.geometry("500x500")
        self.warehouse = Warehouse()

        # Создание виджетов
        self._create_input_fields()
        self._create_buttons()
        self._create_display_area()

    def _create_input_fields(self):
        """Создает поля для ввода данных"""
        # Название товара
        self.name_entry = self._create_entry("Название товара")
        
        # Количество
        self.quantity_entry = self._create_entry("Количество")
        
        # Цена
        self.price_entry = self._create_entry("Цена")

    def _create_entry(self, placeholder: str) -> tk.Entry:
        """Создает поле ввода с placeholder"""
        entry = tk.Entry(self.master)
        entry.pack(pady=2)
        entry.insert(0, placeholder)
        
        # Очистка placeholder при фокусе
        entry.bind("<FocusIn>", lambda e: self._clear_placeholder(entry, placeholder))
        entry.bind("<FocusOut>", lambda e: self._restore_placeholder(entry, placeholder))
        
        return entry

    def _clear_placeholder(self, entry: tk.Entry, placeholder: str):
        """Очищает placeholder при фокусе"""
        if entry.get() == placeholder:
            entry.delete(0, tk.END)

    def _restore_placeholder(self, entry: tk.Entry, placeholder: str):
        """Восстанавливает placeholder если поле пустое"""
        if not entry.get():
            entry.insert(0, placeholder)

    def _create_buttons(self):
        """Создает кнопки управления"""
        button_frame = tk.Frame(self.master)
        button_frame.pack(pady=10)

        buttons = [
            ("Добавить товар", self.add_product),
            ("Показать товары", self.show_products),
            ("Удалить товар", self.remove_product),
            ("Обновить количество", self.update_product),
            ("Общая стоимость", self.show_total_value)
        ]

        for text, command in buttons:
            btn = tk.Button(button_frame, text=text, command=command, width=15)
            btn.pack(side=tk.LEFT, padx=2)

    def _create_display_area(self):
        """Создает область для отображения информации"""
        # Метка для области отображения
        tk.Label(self.master, text="Список товаров:").pack(pady=(10, 0))
        
        # Текстовое поле с прокруткой
        text_frame = tk.Frame(self.master)
        text_frame.pack(pady=5, padx=10, fill=tk.BOTH, expand=True)
        
        scrollbar = tk.Scrollbar(text_frame)
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        
        self.product_display = tk.Text(
            text_frame, 
            height=15, 
            width=60,
            yscrollcommand=scrollbar.set
        )
        self.product_display.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        scrollbar.config(command=self.product_display.yview)

    def _get_input_values(self) -> tuple:
        """Получает значения из полей ввода с проверкой"""
        name = self.name_entry.get()
        if name in ("Название товара", ""):
            raise ValueError("Введите название товара")
        
        try:
            quantity = int(self.quantity_entry.get())
            if quantity < 0:
                raise ValueError("Количество не может быть отрицательным")
        except ValueError:
            raise ValueError("Введите корректное количество (целое число)")
        
        try:
            price = float(self.price_entry.get())
            if price < 0:
                raise ValueError("Цена не может быть отрицательной")
        except ValueError:
            raise ValueError("Введите корректную цену (число)")
        
        return name, quantity, price

    def add_product(self):
        """Добавляет товар на склад через GUI"""
        try:
            name, quantity, price = self._get_input_values()
            self.warehouse.add_product(name, quantity, price)
            messagebox.showinfo("Успех", f"Товар '{name}' добавлен/обновлен на складе.")
            self.show_products()  # Обновляем отображение
        except ValueError as e:
            messagebox.showerror("Ошибка ввода", str(e))

    def show_products(self):
        """Отображает все товары на складе через GUI"""
        products = self.warehouse.list_products()
        self.product_display.delete(1.0, tk.END)
        self.product_display.insert(tk.END, products)

    def remove_product(self):
        """Удаляет товар со склада через GUI"""
        name = self.name_entry.get()
        if name in ("Название товара", ""):
            messagebox.showerror("Ошибка", "Введите название товара для удаления")
            return
        
        result = self.warehouse.remove_product(name)
        messagebox.showinfo("Результат", result)
        self.show_products()  # Обновляем отображение

    def update_product(self):
        """Обновляет количество товара на складе через GUI"""
        try:
            name = self.name_entry.get()
            if name in ("Название товара", ""):
                raise ValueError("Введите название товара")
            
            amount = int(self.quantity_entry.get())
            result = self.warehouse.update_product_quantity(name, amount)
            messagebox.showinfo("Результат", result)
            self.show_products()  # Обновляем отображение
        except ValueError as e:
            messagebox.showerror("Ошибка ввода", "Введите корректное количество (целое число)")

    def show_total_value(self):
        """Отображает общую стоимость товаров на складе"""
        total = self.warehouse.total_inventory_value()
        messagebox.showinfo("Общая стоимость", f"Общая стоимость товаров на складе: {total:.2f}")


# Главная функция для запуска приложения
def main():
    root = tk.Tk()
    app = WarehouseApp(root)
    root.mainloop()


if __name__ == "__main__":
    main()