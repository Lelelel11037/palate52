import '../../domain/models/movie.dart';

class MovieRepository {
  final List<Movie> _mockDb = [];
  int _nextId = 1;

  Movie create(Movie movie) {
    final newMovie = Movie(id: _nextId++, title: movie.title, duration: movie.duration, genreId: movie.genreId);
    _mockDb.add(newMovie);
    return newMovie;
  }

  List<Movie> readAll() => _mockDb;

  Movie? readById(int id) => _mockDb.firstWhere((m) => m.id == id);

  bool update(Movie movie) {
    final index = _mockDb.indexWhere((m) => m.id == movie.id);
    if (index != -1) {
      _mockDb[index] = movie;
      return true;
    }
    return false;
  }

  bool delete(int id) {
    final lengthBefore = _mockDb.length;
    _mockDb.removeWhere((m) => m.id == id);
    return _mockDb.length < lengthBefore;
  }
}
