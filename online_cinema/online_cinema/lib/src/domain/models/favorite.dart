class Favorite {
  final int? id;
  final int userId;
  final int movieId;

  Favorite({this.id, required this.userId, required this.movieId});

  Map<String, dynamic> toMap() {
    return {
      if (id != null) 'id': id,
      'user_id': userId,
      'movie_id': movieId,
    };
  }

  factory Favorite.fromMap(Map<String, dynamic> map) {
    return Favorite(
      id: map['id'] as int?,
      userId: map['user_id'] as int,
      movieId: map['movie_id'] as int,
    );
  }
}
