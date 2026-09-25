# #задание1

# import json

# initial_data = {
#     "название": "Франция",
#     "столица": "Париж",
#     "население": 67390000,
#     "площадь": 643801,
#     "валюта": "Евро"
# }

# try:
#     with open("country.json", "r", encoding="utf-8") as file:
#         data = json.load(file)
#         print("Файл country.json найден, загружаем данные...")
# except FileNotFoundError:
#     print("Файл country.json не найден, создаём новый...")
#     with open("country.json", "w", encoding="utf-8") as file:
#         json.dump(initial_data, file, ensure_ascii=False, indent=4)
#     data = initial_data
#     print("Создан файл country.json с начальными данными")

# print("\nТекущие данные в файле:")
# for key, value in data.items():
#     print(f"  {key}: {value}")

# data["язык"] = "французский"

# with open("country.json", "w", encoding="utf-8") as file:
#     json.dump(data, file, ensure_ascii=False, indent=4)

# print("\nФайл country.json успешно обновлён!")
# print("Добавлен ключ 'язык' со значением: французский")

# print("\nОбновлённые данные:")
# for key, value in data.items():
#     print(f"  {key}: {value}")


#задание2

import json
import csv
import os

test_data = {
    "Имя": "Анна Смирнова",
    "Возраст": 22,
    "Город": "Санкт-Петербург"
}

try:
    with open("test_json", "r", encoding="utf-8") as file:
        data = json.load(file)
        print("Файл test_json найден, загружаем данные...")
except FileNotFoundError:
    print("Файл test_json не найден, создаём новый...")
    with open("test_json", "w", encoding="utf-8") as file:
        json.dump(test_data, file, ensure_ascii=False, indent=4)
    data = test_data
    print("Создан файл test_json с тестовыми данными")

print("\nДанные из JSON файла:")
for key, value in data.items():
    print(f"  {key}: {value}")

name = data.get("Имя", "")
age = data.get("Возраст", "")
city = data.get("Город", "")

if not name or not age or not city:
    print("\nВнимание: В JSON файле отсутствуют некоторые поля!")
    if not name:
        print("  - Отсутствует поле 'Имя'")
    if not age:
        print("  - Отсутствует поле 'Возраст'")
    if not city:
        print("  - Отсутствует поле 'Город'")
    if not name:
        name = "Неизвестный"
    if not age:
        age = 0
    if not city:
        city = "Неизвестный"
    print("\nПоля заполнены значениями по умолчанию")

new_row = [name, age, city, "Стажёр", 50000]

csv_file = "employees_with_salary.csv"

file_exists = os.path.isfile(csv_file)

with open(csv_file, "a", encoding="utf-8", newline="") as file:
    writer = csv.writer(file)
    
    if not file_exists:
        headers = ["Имя", "Возраст", "Город", "Должность", "Зарплата"]
        writer.writerow(headers)
        print(f"\nСоздан новый файл {csv_file} с заголовками")
    
    writer.writerow(new_row)

print(f"\nДанные успешно добавлены в {csv_file}!")
print(f"Добавлена запись: {new_row}")

print(f"\nСодержимое файла {csv_file}:")
with open(csv_file, "r", encoding="utf-8") as file:
    reader = csv.reader(file)
    for row in reader:
        print(f"  {row}")