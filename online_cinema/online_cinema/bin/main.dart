import 'package:online_cinema/online_cinema.dart';

void main() {
  final db = CinemaDatabase();
  db.init();

  final movieRepo = MovieRepository();

  try {
    runMenu(movieRepo);
  } finally {
    db.close();
  }
}
