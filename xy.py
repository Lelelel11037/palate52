import random
import os
import datetime

class TicTacToe:
    def __init__(self):
        self.board = [' ' for _ in range(9)]
        self.current_player = 'X'
        self.stats_file = 'stats/game_stats.txt'
        self.ensure_stats_directory()
    
    def ensure_stats_directory(self):
        try:
            os.makedirs('stats', exist_ok=True)
        except Exception as e:
            print(f"Ошибка создания директории: {e}")
    
    def print_board(self):
        print("\n   |   |   ")
        print(f" {self.board[0]} | {self.board[1]} | {self.board[2]} ")
        print("__|__|__")
        print("  4 | 5  |  6 ")
        print(f" {self.board[3]} | {self.board[4]} | {self.board[5]} ")
        print("__|__|__")
        print("   |   |   ")
        print(f" {self.board[6]} | {self.board[7]} | {self.board[8]} ")
        print("   |   |   \n")
    
    def make_move(self, position):
        try:
            if self.board[position] == ' ':
                self.board[position] = self.current_player
                return True
            return False
        except IndexError:
            return False
    
    def check_winner(self):
        for i in range(0, 9, 3):
            if self.board[i] == self.board[i+1] == self.board[i+2] != ' ':
                return self.board[i] 
                
        for i in range(3):
            if self.board[i] == self.board[i+3] == self.board[i+6] != ' ':
                return self.board[i]

        if self.board[0] == self.board[4] == self.board[8] != ' ':
            return self.board[0]
        if self.board[2] == self.board[4] == self.board[6] != ' ':
            return self.board[2]
        
        if ' ' not in self.board:
            return 'Draw'    
        return None
    
    def bot_move(self):
        try:
            for i in range(9):
                if self.board[i] == ' ':
                    self.board[i] = 'O'
                    if self.check_winner() == 'O':
                        return i
                    self.board[i] = ' '
            
            for i in range(9):
                if self.board[i] == ' ':
                    self.board[i] = 'X'
                    if self.check_winner() == 'X':
                        self.board[i] = 'O'
                        return i
                    self.board[i] = ' '

            if self.board[4] == ' ':
                return 4

            corners = [0, 2, 6, 8]
            empty_corners = [c for c in corners if self.board[c] == ' ']
            if empty_corners:
                return random.choice(empty_corners)

            empty_positions = [i for i in range(9) if self.board[i] == ' ']
            return random.choice(empty_positions)
            
        except Exception as e:
            print(f"Ошибка бота: {e}")
            empty_positions = [i for i in range(9) if self.board[i] == ' ']
            return random.choice(empty_positions) if empty_positions else 0
    
    def save_stats(self, winner, mode):
        try:
            timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            with open(self.stats_file, 'a', encoding='utf-8') as f:
                f.write(f"{timestamp} | Режим: {mode} | Победитель: {winner}\n")
        except Exception as e:
            print(f"Ошибка сохранения статистики: {e}")
    
    def show_stats(self):
        try:
            if os.path.exists(self.stats_file):
                with open(self.stats_file, 'r', encoding='utf-8') as f:
                    print("\n=== Статистика игр ===")
                    print(f.read())
            else:
                print("Статистика пока отсутствует")
        except Exception as e:
            print(f"Ошибка чтения статистики: {e}")
    
    def reset_game(self):
        self.board = [' ' for _ in range(9)]
        self.current_player = 'X'
    
    def play_vs_player(self):
        print("\nРежим: Игрок vs Игрок")
        
        while True:
            self.print_board()
            
            try:
                move = int(input(f"Игрок {self.current_player}, введите позицию (1-9): ")) - 1
                
                if move < 0 or move > 8:
                    print("Позиция должна быть от 1 до 9")
                    continue
                
                if self.make_move(move):
                    winner = self.check_winner()
                    
                    if winner:
                        self.print_board()
                        if winner == 'Draw':
                            print("Ничья")
                            self.save_stats("Ничья", "Игрок vs Игрок")
                        else:
                            print(f"Победил игрок {winner}")
                            self.save_stats(f"Игрок {winner}", "Игрок vs Игрок")
                        break
                    
                    self.current_player = 'O' if self.current_player == 'X' else 'X'
                else:
                    print("Эта позиция уже занята")  
            except ValueError:
                print("Введите число от 1 до 9")
            except KeyboardInterrupt:
                print("\nИгра прервана")
                break
            except Exception as e:
                print(f"Произошла ошибка: {e}")
    
    def play_vs_bot(self):
        "Режим игры против бота"
        print("\nРежим: Игрок vs Бот")
        print("Вы играете за X, бот играет за O")
        
        while True:
            self.print_board()
            
            if self.current_player == 'X':
                try:
                    move = int(input("Ваш ход (1-9): ")) - 1
                    
                    if move < 0 or move > 8:
                        print("Позиция должна быть от 1 до 9")
                        continue
                    
                    if not self.make_move(move):
                        print("Эта позиция уже занята")
                        continue
                        
                except ValueError:
                    print("Введите число от 1 до 9")
                    continue
                except KeyboardInterrupt:
                    print("\nИгра прервана")
                    break
                except Exception as e:
                    print(f"Произошла ошибка: {e}")
                    continue
            else:

                print("Бот думает...")
                bot_position = self.bot_move()
                self.make_move(bot_position)
                print(f"Бот походил на позицию {bot_position + 1}")
            
            winner = self.check_winner()
            if winner:
                self.print_board()
                if winner == 'Draw':
                    print("Ничья")
                    self.save_stats("Ничья", "Игрок vs Бот")
                elif winner == 'X':
                    print("Вы победили")
                    self.save_stats("Игрок", "Игрок vs Бот")
                else:
                    print("Бот победил")
                    self.save_stats("Бот", "Игрок vs Бот")
                break
            
            self.current_player = 'O' if self.current_player == 'X' else 'X'

def main():
    game = TicTacToe()
    while True:
        try:
            print("\nКрестики-нолики")
            print("1 - Играть против друга")
            print("2 - Играть против бота")
            print("3 - Показать статистику")
            print("4 - Выйти")
            
            choice = input("Выберите действие: ")
            if choice == '1':
                game.reset_game()
                game.play_vs_player()
            elif choice == '2':
                game.reset_game()
                game.play_vs_bot()
            elif choice == '3':
                game.show_stats()
            elif choice == '4':
                print("До свидания")
                break
            else:
                print("Неверный выбор")
                
        except KeyboardInterrupt:
            print("\nДо свидания")
            break
        except Exception as e:
            print(f"Произошла ошибка: {e}")

if __name__ == "__main__":
    main()