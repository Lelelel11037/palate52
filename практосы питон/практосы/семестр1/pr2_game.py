import os
import time
import random

DEBUG = False
TITLE_WIDTH = 100
TITLE_WIDTH = os.get_terminal_size().columns-2
WINDOW_HEIGHT = os.get_terminal_size().lines

def clear_screen():
    os.system('cls')

def colored(text='', color_code=0):
    return '\x1b[' + str(color_code) + f'm{text}\x1b[0m'

def print_colored(text='', color_code=0, end='\n', flush=True):
    print(colored(text, color_code), end=end, flush=flush)

def text_animation(text, delay=0.05, color_code=0):
    if DEBUG:
       print_colored(text, color_code)
       return

    for char in text:
        print_colored(char, color_code, end='')
        time.sleep(delay)

    print()
    time.sleep(0.3)

def print_title(title):
    title_text = f'🏥 {title.upper()} 🏥'

    clear_screen()
    print_colored(title_text.center(TITLE_WIDTH, '='), 32, end='\n\n')

    time.sleep(0.5)

class Doctor:
    def __init__(self):
        self.inventory = []

    def take_item(self, item):
        if len(self.inventory) < 3:
            for exist_item in self.inventory:
                if exist_item == item:
                    print(f'Вы {colored('уже', '91')} брали {colored(item, '1;33')}! Выберите что-то другое!')
                    return

            self.inventory.append(item)

            print(f'Вы взяли {colored(item, '1;33')}')
            return
        print(f'Вы не можете взять больше предметов.')

doctor = Doctor()

def scene_0():
    print_title('Больница 52')

    print_colored(f'{colored('1', '1;33')}. Начать новую игру')
    print_colored(f'{colored('2', '1;33')}. Выйти\n')

    input_valid = False

    while not input_valid:
        choice = input(colored('Выберите опцию: ', '3;94')).strip()

        if choice == '1':
            input_valid = True
            scene_1()
        elif choice == '2':
            input_valid = True
            text_animation('\nБай бай 🤫')
            time.sleep(2)
            break
        else:
            print_colored('\x1b[1A❌ Вы вели неверное значение. Попробуйте снова\n ', 31)

def scene_1():
    print_title('Начало смены.')
    text_animation('Вы начинаете смену в психиатрической клинике...\n', delay=0.08)
    time.sleep(1)

    items = ["карандаш", "стетоскоп", "дневник наблюдений", "шариковая ручка", "маленький камертон", "блокнот для записей", "медицинский молоточек"]

    text_animation('На вашем столе лежат: \n')
    time.sleep(0.5)

    for i, item in enumerate(items, 1):
        print_colored(f'{colored(i, '1;33')}. ', end='')
        text_animation(f'{item}\n', delay=0.02)

    text_animation('\nВы можете взять с собой только 3 предмета.\n')

    input_valid = False

    while not input_valid:
        try:
            choices = list(input(colored('Введите номера предметов: ', '3;94')).strip())
            print()

            for choice in choices:
                if choice == ' ':
                    continue

                if choice.isdigit() and 1 <= int(choice) <= len(items):
                    item = items[int(choice)-1]
                    doctor.take_item(item)
                    time.sleep(0.1)
                else:
                   text_animation(f'❌ Некорректный номер: ', 0.2)
                   print(f'\x1b[1A{colored(choice, '1;91')}')

            if len(doctor.inventory) == 0:
                text_animation(f'❌ Нужно выбрать ')
                print_colored('3', '1;91', end='')
                text_animation(' предмета. Выберите что-нибудь')
                continue

            if len(doctor.inventory) < 3:
                text_animation(f'\n✅ Выбрано предметов: ')
                print_colored(str(len(doctor.inventory)), '91', end='')
                text_animation('/3')
                text_animation('Выберите еще предметы\n')

            if len(doctor.inventory) == 3:
                input_valid = True
                time.sleep(1)
                scene_2()

        except Exception as e:
            text_animation('❌ Ошибка ввода. Попробуйте снова.')

            if DEBUG:
                print(e)

    time.sleep(1)

def scene_2():
    print_title('Пациент 1. Диагноз: Расстройство аутистического спектра')
    text_animation('Вы заходите в палату 67 и здоровайтесь с пациентом. . .\n', delay=0.08)
    text_animation('"Алексей, здравствуйте. . ."\n', delay=0.08)
    time.sleep(1)

    trust = 0

    print_colored(f'Уровень доверия: {colored(trust, 93)}\n', 36)

    actions = ['Установить зрительный контакт', 'Показать предмет', 'Заговорить спокойно']

    for i, action in enumerate(actions, 1):
        print_colored(str(i), '1;33', end='')
        text_animation(f'. {action}\n', delay=0.02)

    input_valid_count = 0

    while input_valid_count < 3:
        choice = input(colored('Выберите действие: ', '3;94')).strip()
        print()

        if choice == '1':
            input_valid_count += 1
            print('\x1b[2A', end='')
            text_animation('Вы пытаетесь установить зрительный контакт...')
            time.sleep(0.4)
            text_animation('Алексей отводит взгляд.\n', 0.06, 31)
            trust -= 1
            print_colored(f'Уровень доверия(снизился): {colored(trust, 93)}\n', 36)

        elif choice == '2':
            input_valid_count +=1

            print('\x1b[2A', end='')
            text_animation('Что вы хотите показать пациенту?\n')
            for i, item in enumerate(doctor.inventory, 1):
                print_colored(f'{i}.', '1;33', end='')
                text_animation(f'{item}\n', delay=0.02)

            input2_valid = False

            while not input2_valid:
                choice2 = input(colored('Выберите предмет: ', '3;94')).strip()
                print()

                if choice2 == '1' or choice2 == '2' or choice2 == '3':
                    input2_valid = True

                    item = doctor.inventory[int(choice2)-1]

                    if item in ['карандаш', 'шариковая ручка']:
                        print('\x1b[1A', end='')
                        text_animation('Вы протягиваете предмет Алексею...')
                        time.sleep(0.4)
                        text_animation('Алексей с интересом рассматривает предмет.', 0.06, 32)
                        trust += 2
                        print_colored(f'Уровень доверия(повысился): {colored(trust, 93)}\n', 36)

                    elif item in ["блокнот для записей", "медицинский молоточек"]:
                        print('\x1b[2A', end='')
                        text_animation('Вы протягиваете предмет Алексею...')
                        time.sleep(0.4)
                        print('\x1b[3A', end='')
                        text_animation('Алексей с недоверием смотрит на вас.', 0.06, 31)
                        trust -= 2
                        print_colored(f'Уровень доверия(понизился): {colored(trust, 93)}\n', 36)
                    else:
                        print('\x1b[2А', end='')
                        text_animation('.............        \n', 0.07)
                        text_animation('Пациент не проявил интереса к предмету.\n')
                        print_colored(f'Уровень доверия(не изменился): {colored(trust, 93)}\n', 36)

                else:
                    print_colored('\x1b[1A❌ Вы вели неверное значение. Попробуйте снова\n ', 31)

        elif choice == '3':
            input_valid_count += 1
            print('\x1b[2A', end='')
            text_animation('Вы заводите разговор')
            text_animation('................\n', 0.07)
            text_animation('Алексей немного расслабился.\n', 0.06, 32)
            trust += 2
            print_colored(f'Уровень доверия(повысился): {colored(trust, 93)}\n', 36)

        else:
            print('\x1b[1A', end='')
            print_colored('❌ Вы вели неверное значение. Попробуйте снова\n ', 31)
            print()
    if trust >= 3:
        text_animation('⭐️ Алексей установил с вами контакт!\n', 0.06)

    elif trust <= -1:
        text_animation('💔 Алексей замкнулся в себе.\n', 0.06)

    else:
        text_animation('⚖️ Контакт не установлен.\n', 0.06)
        time.sleep(0.8)

    scene_3()

def scene_3():
    print_title('Пациент 2. Диагноз: Тревожное расстройство')
    text_animation('Вы заходите в палату 34. . .\n', delay=0.08)
    text_animation('"Здравствуйте, Михаил,. . ."\n', delay=0.08)
    time.sleep(1)

    trust = 5
    text_animation('Пациент в последние полмесяца идет на поправку. Вы решили сегодня вывести его на прогулку.\n\n', 0.06)
    print_colored(f'Уровень доверия: {colored(trust, 93)}\n', 36)

    actions = ['Поговорить о прогрессе лечения', 'Показать предмет', 'Обсудить планы на будущее']

    for i, action in enumerate(actions, 1):
        print_colored(str(i), '1;33', end='')
        text_animation(f'. {action}\n', delay=0.02)

    input_valid_count = 0

    while input_valid_count < 3:
        choice = input(colored('Выберите действие: ', '3;94')).strip()
        print()

        if choice == '1':
            input_valid_count += 1
            print('\x1b[2A', end='')
            text_animation('Вы начинаете обсуждать текуй прогресс пациента...')
            time.sleep(0.4)
            text_animation('По Михаилу видно, что ему неприятно это обсуждать.\n', 0.06, 31)
            trust -= 1
            print_colored(f'Уровень доверия(снизился): {colored(trust, 93)}\n', 36)

        elif choice == '2':
            input_valid_count +=1

            print('\x1b[2A', end='')
            text_animation('Что вы хотите показать пациенту?\n')
            for i, item in enumerate(doctor.inventory, 1):
                print_colored(f'{i}.', '1;33', end='')
                text_animation(f'{item}\n', delay=0.02)

            input2_valid = False

            while not input2_valid:
                choice2 = input(colored('Выберите предмет: ', '3;94')).strip()
                print()

                if choice2 == '1' or choice2 == '2' or choice2 == '3':
                    input2_valid = True

                    item = doctor.inventory[int(choice2)-1]

                    if item in ["блокнот для записей", "шариковая ручка"]:
                        print('\x1b[2A', end='')
                        text_animation('Вы протягиваете предмет Алексею...')
                        time.sleep(0.4)
                        text_animation('Алексей с интересом рассматривает предмет.', 0.06, 32)
                        trust += 2
                        print_colored(f'Уровень доверия(повысился): {colored(trust, 93)}\n', 36)

                    elif item in ["маленький камертон"]:
                        print('\x1b[2A', end='')
                        text_animation('Вы протягиваете предмет Алексею...')
                        time.sleep(0.4)
                        print('\x1b[3A', end='')
                        text_animation('Михаил с недоверием смотрит на вас.', 0.06, 31)
                        trust -= 2
                        print_colored(f'Уровень доверия(понизился): {colored(trust, 93)}\n', 36)
                    else:
                        print('\x1b[2А', end='')
                        text_animation('.............        \n', 0.07)
                        text_animation('Пациент не проявил интереса к предмету.\n')
                        print_colored(f'Уровень доверия(не изменился): {colored(trust, 93)}\n', 36)

                else:
                    print_colored('\x1b[1A❌ Вы вели неверное значение. Попробуйте снова\n ', 31)

        elif choice == '3':
            input_valid_count += 1
            print('\x1b[2A', end='')
            text_animation('Вы заводите разговор')
            text_animation('................\n', 0.07)
            text_animation('Михаил радостно рассказывает, что мечтает стать поваром.\n', 0.06, 32)
            trust += 2
            print_colored(f'Уровень доверия(повысился): {colored(trust, 93)}\n', 36)

        else:
            print('\x1b[1A', end='')
            print_colored('❌ Вы вели неверное значение. Попробуйте снова\n ', 31)

    if trust >= 3:
        text_animation('⭐️ Алексей установил с вами контакт!\n', 0.06)

    elif trust <= -1:
        text_animation('💔 Алексей замкнулся в себе.\n', 0.06)

    else:
        text_animation('⚖️ Контакт не установлен.\n', 0.06)
        time.sleep(0.8)

    scene_4()

def scene_4():
    print_title('Пациент 3. Диагноз: Параноидальная шизофрения')
    text_animation('Вы осторожно заходите в палату 993. . .\n', delay=0.08)
    text_animation('"Приветствую, Фёдор. . ."\n', delay=0.08)
    time.sleep(1)

    trust = -5
    text_animation('С каждым днем пациенту всё хуже.\n\n', 0.06)
    print_colored(f'Уровень доверия: {colored(trust, 93)}\n', 36)

    actions = ['Сохранять дистанцию', 'Показать предмет', 'Предложить лекарство']

    for i, action in enumerate(actions, 1):
        print_colored(str(i), '1;33', end='')
        text_animation(f'. {action}\n', delay=0.02)

    input_valid_count = 0

    while input_valid_count < 3:
        choice = input(colored('Выберите действие: ', '3;94')).strip()
        print()

        if choice == '1':
            input_valid_count += 1
            print('\x1b[2A', end='')
            text_animation('Вы начинаете говорить максимально осмотрительно и осторожно с пациентом....')
            time.sleep(0.4)
            text_animation('Фёдор никак не реагрут.\n', 0.06, 31)
            print_colored(f'Уровень доверия(не изменился): {colored(trust, 93)}\n', 36)

        elif choice == '2':
            input_valid_count +=1

            print('\x1b[2A', end='')
            text_animation('Что вы хотите показать пациенту?\n')
            for i, item in enumerate(doctor.inventory, 1):
                print_colored(f'{i}.', '1;33', end='')
                text_animation(f'{item}\n', delay=0.02)

            input2_valid = False

            while not input2_valid:
                choice2 = input(colored('Выберите предмет: ', '3;94')).strip()
                print()

                if choice2 == '1' or choice2 == '2' or choice2 == '3':
                    input2_valid = True

                    item = doctor.inventory[int(choice2)-1]

                    if True:
                        print('\x1b[2A', end='')
                        text_animation('Вы протягиваете предмет Фёдору...')
                        time.sleep(0.4)
                        text_animation('Фёдор кидается на вас и начинает душить.', 0.06, 31)
                        text_animation('Охраники врываются в палату и разнимают вас', 0.08)
                        text_animation('В след вы смотрите как пациента уводят в изолятор', 0.08)
                        trust -= 10
                        print_colored(f'Уровень доверия(понизился): {colored(trust, 93)}\n', 31)
                        input_valid_count = 3
                else:
                    print_colored('\x1b[1A❌ Вы вели неверное значение. Попробуйте снова\n ', 31)

        elif choice == '3':
            input_valid_count -= 1
            print('\x1b[2A', end='')
            text_animation('Вы предлагаете выпить лекарство')
            text_animation('................\n', 0.07)
            text_animation('Фёдор продолжает вас игнорировать, но видно как он раздражен.\n', 0.06)
            trust -= 1
            print_colored(f'Уровень доверия(понизился): {colored(trust, 93)}\n', 36)

        else:
            print('\x1b[1A', end='')
            print_colored('❌ Вы вели неверное значение. Попробуйте снова\n ', 31)

    if trust >= -6:
        text_animation('Состояние Фёдора никак не изменилось.\n', 0.06)

    else:
        text_animation('Пациента увели в изолятор\n', 0.06)
        time.sleep(0.8)

    scene_5()

def scene_5():
    print_title('концовка')
    text_animation('Вот и смена подошла к концу!', 0.07)
    time.sleep(0.6)
    text_animation('Вы неспеша снимаете с себе белый халат...', 0.09)
    time.sleep(0.8)
    text_animation('. . .', 0.6)
    time.sleep(3)
    print('\x1b[31m', end='')
    text_animation('И̷̨̻̋ͤͪ́̈́̓ͥн̷̸̛͔̘̱̗͎́ͬͨ̊͐ͩͣ͒ͨͤ̋͋̕͟͜͞_̡͙̜͎̰̗̃̿̌̍̐̌̈́̌ͧ̚ͅт̶̲̯ͫ̈́̀̅ͥ̊͊̚͜͝_̛̼̦̝̼͇̦͒̔̋͋ͦ͆͋̈ͮͤ̓̾͊ͮ͘̕͢͞ͅё̸̡̛̬͍̖̹̮͓̲̜̫͍̤̻́͆̇̐͐ͧ̌͊̅ͮ̈́͆̏͝͞р̷̵̵̷̛̼̯̼̤̙̤̭̱͎̬̌̎ͭ͛́̑͂͐̃̏͌͛͊ͯ̍̈͊̍̊ͮͯͮͫ̃͝͠͞͡е̵̵̨̨̡̗̻͍̯̘͚̱̠̼̝̹̤̳ͮ̌̀͌̃͋͌̿̅̆ͤ̉̓̓̾̚͝͞ͅс̴̭͕̂̍̀ͣ͟н̴̡̢̛̟͈͔̲̥̟͖ͮ͆̂̀͒̌͜о̡̰̥̠͍̬͈͚ͨ̆̌ͪ̓̌͡͡,̵̴̶͈͇̭̰̗̳̝͇̦͕̭̄ͧ͋̂͒̀̓͋̒̇̊͆͆͂̇͋̿͛ͣͪ̕͜ͅͅ.̥͌ͤ́͝.̸̷̢̻͇̭̲̩̝̪̩̩̺́ͥ̽ͫ͗̊̒̂͛͒ͦ̓͌ͣ͡_̦̝̮̐͂̉ к̸̡̢̯̼͎͓̻̜̜̎̈́ͣ̽͒ͤͤ̋͌͐ͦ̊͌͌͒̊̄ͪ̐̚͜͜о̘̱̯ͦ̿ͩ̕г̛ͦ̕͠д̻̗̥͌̃_̸̜̰͉̭̙̯͋̀̏ͣ̔͋̿̃ͦ̌͐ͣ͞а̵͍̭̠̅̂ͨ̆͊͗͋ м̭̞͇͓̋͑ͨ͜е̸̡̧͍̼̯̙̮͈͕̣̬̯̻͙̺̐̀ͤ̃͐̌͒͛̔̀͑͑ͧ͌̏н̭̜̫̭̩̙̮̤̮̬̫͌̆̔ͤ̂͗̈̿ͬ̂͛͒̕͝я̙́ͯ_̙ͤ́͂ͩ̆ͤ̐ о̸̧̩̺̝͍̼̠́̍͛̊̎̍ͮ͒͘͟͝т̢̒ͮ̾_̜̦͖͛̑̐ͦс̞̣̀͊̐͑̕ю̡̛̗̲̗͚͍̯̫̘̠̟̫͉̲̓̾̿̄́͒̈̃̀̀͋͋̄͌̐̍̋̀̐͋͑ͪ͊̕̕̚͢͡͝͡д̸̘_̡ͮ̂ͦ_̜̺̪̀ͩ͝ӓ̜̠̘̆̃͋_͚̀͒͌ͅ_̡̧̻̮͉̞͓̰̗͋̅̒ͮ̓ͫ̌̎ͯ̐ͤ͋͆̔͢ͅ в̢͛_̵̸̵̷̡̪̦̰̪̗̖͙̗̠͕̾̒̑̅̃̓ͭ̏͋ͤͪ̔ͤ̉ͪͫ͜͢͡͝ы̵̶̡̤̱̘̯̰̲͇͈̭ͪ̅̎͐͐͛͋̓̇̅͐ͤ̒̀̓̂̅̅͢͠ͅͅп̷̷̴̧̧̰̘̪̰͚̰̳̱̐̈́͊̊ͤ͘͡_ͅ_̡̉̾у_̢̢̧̻͕̫̰͕͉̾̍ͣ̀ͧͫͬ̑̎̆̉ͨ͝_̦ͨ͛_̧̛̣̞̺̝ͭͥ̌͋͗͘͢с̡͔̬̬̇̒̉ͭͭт̢̩̮̹̹̻̰̬̮̫̞̘͗͆ͦ́͂̾̊̿ͪ̑̑͒ͧ̐͛̀́̌͌̓͐ͮ̍̄̕̕ͅя̵̗̜̝͆͌_̉ͯ_̲̤̜̪̺ͣ͊̑̏̊͝т̷̴̡̧̧̰̠̻̥̼̜͎͍̃̉̔̇̋̾͢͝_͖̀ͩ?̶̸̧̞̝̥̟̯̗̳̟͔̜͉̘͓͙̻̟̃̃̓̿ͨ̓̈́̉̓̆͐ͨ̈͆̆͆͂ͧ̔́̈͗̇ͤ͐͠.͖̕.̛͚͈͍̠͓͚͓̻͖̺͓͉̻̂ͦ̂ͦͦͤ͆͂͆ͧ̕͜͠.̷̴̧̨̛͇̰͕̘̞̯͎̳̯̪̦̀ͥ̀̅ͯ̐ͪ̔̿͐̍ͣͬ̇ͣ͌̊̄͜͡_̡̥̱͈̈́ͭ̈́͌̔͜', 0.01)
    print('\x1b[0m')
    time.sleep(8)

    scene_6()

def cursor_to(x, y):
    print(f"\x1b[{y+1};{x}H", end='')

messages = [
    colored("Вы прошли игру! 😎", '1;5;94'),
    f'',
    colored('Над игрой работали:', '4;96'),
    f'- Автор Идеи - Лера Колибаба',
    f'- Ведущий Программист1 - Лера Колибаба',
    f'- Ведущий Программист2 - Паша Подъяблонский',
    f'- Главный Тестировщик - Женя Колибаба',
    f'- Тестировщик - Паша Подъяблонский',
    f'- Главный Дизайнер - Лера Колибаба',
    f'',
    colored('🌹🌹 Спасибо за игру!🌹🌹', '3;95')
]

def scene_6():
    y = WINDOW_HEIGHT
    x = round(TITLE_WIDTH/2) - 14

    while True:
        clear_screen()

        for i, message in enumerate(messages, 1):
            message_y = y + i*2

            if message_y > 0 and message_y < WINDOW_HEIGHT:
                cursor_to(x, message_y)
                print(message, end='', flush=True)

        y-=1
        time.sleep(0.4)

        if y <= -24:
            clear_screen()
            break

os.system('title Больница №52')

scene_0()

time.sleep(1)