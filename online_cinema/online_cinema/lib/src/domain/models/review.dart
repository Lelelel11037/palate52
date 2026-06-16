class Review {
  final int? id;
  final int userId;
  final int movieId;
  final int score;

  Review({this.id, required this.userId, required this.movieId, required this.score});

  Map<String, dynamic> toMap() {
    return {
      if (id != null) 'id': id,
      'user_id': userId,
      'movie_id': movieId,
      'score': score,
    };
  }

  factory Review.fromMap(Map<String, dynamic> map) {
    return Review(
      id: map['id'] as int?,
      userId: map['user_id'] as int,
      movieId: map['movie_id'] as int,
      score: map['score'] as int,
    );
  }
}
