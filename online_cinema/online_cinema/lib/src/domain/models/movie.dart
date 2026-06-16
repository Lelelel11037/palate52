class Movie {
  final int? id;
  final String title;
  final int duration;
  final int genreId;

  Movie({
    this.id,
    required this.title,
    required this.duration,
    required this.genreId,
  });
}