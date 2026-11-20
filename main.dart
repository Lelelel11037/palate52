import 'package:flutter/material.dart';

void main() {
  runApp(const NotesApp());
}

class NotesApp extends StatelessWidget {
  const NotesApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Заметки',
      theme: ThemeData(
        scaffoldBackgroundColor: Colors.white,
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF5A55EE)),
        useMaterial3: true,
      ),
      home: const NotesScreen(),
    );
  }
}

class Note {
  String text;
  bool isImportant;

  Note({required this.text, this.isImportant = false});
}

class NotesScreen extends StatefulWidget {
  const NotesScreen({super.key});

  @override
  State<NotesScreen> createState() => _NotesScreenState();
}

class _NotesScreenState extends State<NotesScreen> {
  final TextEditingController _controller = TextEditingController();
  final List<Note> _notes = [];

  int? _editingIndex;

  void _saveNote() {
    final String text = _controller.text.trim();
    if (text.isEmpty) return;

    setState(() {
      if (_editingIndex == null) {
        _notes.add(Note(text: text));
      } else {
        _notes[_editingIndex!].text = text;
        _editingIndex = null;
      }
      _controller.clear();
    });
  }

  void _deleteNote(int index) {
    setState(() {
      _notes.removeAt(index);
      if (_editingIndex == index) {
        _editingIndex = null;
        _controller.clear();
      } else if (_editingIndex != null && _editingIndex! > index) {
        _editingIndex = _editingIndex! - 1;
      }
    });
  }

  void _editNote(int index) {
    setState(() {
      _controller.text = _notes[index].text;
      _editingIndex = index;
    });
  }

  void _cancelEdit() {
    setState(() {
      _editingIndex = null;
      _controller.clear();
    });
  }

  void _viewNote(int index) {
    final note = _notes[index];
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Row(
          children: [
            if (note.isImportant) ...[
              const Text('⭐️', style: TextStyle(fontSize: 20)),
              const SizedBox(width: 8),
            ],
            const Expanded(
              child: Text(
                'Заметка',
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
            ),
          ],
        ),
        content: SingleChildScrollView(
          child: Text(
            note.text,
            style: const TextStyle(fontSize: 16, height: 1.4),
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Закрыть'),
          ),
        ],
      ),
    );
  }

  void _toggleImportant(int index) {
    setState(() {
      _notes[index].isImportant = !_notes[index].isImportant;
    });
  }

  void _resetImportant(int index) {
    setState(() {
      _notes[index].isImportant = false;
    });
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'Мои заметки',
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        backgroundColor: Colors.white,
        elevation: 0,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            TextField(
              controller: _controller,
              maxLines: 3,
              minLines: 1,
              decoration: InputDecoration(
                hintText: 'Введите текст заметки...',
                filled: true,
                fillColor: const Color(0xFFF5F5F5),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(16),
                  borderSide: BorderSide.none,
                ),
                contentPadding: const EdgeInsets.all(16),
              ),
            ),
            const SizedBox(height: 12),
            Row(
              children: [
                Expanded(
                  child: ElevatedButton(
                    onPressed: _saveNote,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFF5A55EE),
                      foregroundColor: Colors.white,
                      padding: const EdgeInsets.symmetric(vertical: 16),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16),
                      ),
                      elevation: 0,
                    ),
                    child: Text(
                      _editingIndex == null ? 'Сохранить' : 'Обновить',
                      style: const TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                ),
                if (_editingIndex != null) ...[
                  const SizedBox(width: 12),
                  ElevatedButton(
                    onPressed: _cancelEdit,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color(0xFFF5F5F5),
                      foregroundColor: const Color(0xFF1D1D1D),
                      padding: const EdgeInsets.symmetric(
                        vertical: 16,
                        horizontal: 20,
                      ),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(16),
                      ),
                      elevation: 0,
                    ),
                    child: const Text('Отмена'),
                  ),
                ],
              ],
            ),
            const SizedBox(height: 20),
            Expanded(
              child: _notes.isEmpty
                  ? const Center(
                      child: Text(
                        'Пока нет заметок',
                        style: TextStyle(fontSize: 16, color: Colors.grey),
                      ),
                    )
                  : ListView.separated(
                      itemCount: _notes.length,
                      separatorBuilder: (context, index) =>
                          const SizedBox(height: 12),
                      itemBuilder: (context, index) {
                        final note = _notes[index];
                        final isEditing = _editingIndex == index;

                        return Dismissible(
                          key: ValueKey('note_$index'),
                          direction: DismissDirection.horizontal,
                          background: _buildSwipeBackground(
                            alignment: Alignment.centerLeft,
                            color: const Color(0xFF3AC47D),
                            icon: Icons.star_border,
                            label: 'Сброс',
                          ),
                          secondaryBackground: _buildSwipeBackground(
                            alignment: Alignment.centerRight,
                            color: const Color(0xFFFF5A6B),
                            icon: Icons.delete,
                            label: 'Удалить',
                          ),
                          confirmDismiss: (direction) async {
                            if (direction == DismissDirection.endToStart) {
                              return true;
                            } else {
                              return note.isImportant;
                            }
                          },
                          onDismissed: (direction) {
                            if (direction == DismissDirection.endToStart) {
                              _deleteNote(index);
                            } else {
                              _resetImportant(index);
                            }
                          },
                          child: GestureDetector(
                            onTap: () => _viewNote(index),
                            onDoubleTap: () => _toggleImportant(index),
                            onLongPress: () => _editNote(index),
                            child: Container(
                              padding: const EdgeInsets.all(16),
                              decoration: BoxDecoration(
                                color: isEditing
                                    ? const Color(0xFFEDEBFF)
                                    : note.isImportant
                                        ? const Color(0xFFFFF6E0)
                                        : Colors.white,
                                borderRadius: BorderRadius.circular(16),
                                border: Border.all(
                                  color: isEditing
                                      ? const Color(0xFF5A55EE)
                                      : note.isImportant
                                          ? const Color(0xFFFFC83D)
                                          : const Color(0xFFE5E7EB),
                                ),
                              ),
                              child: Row(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  if (note.isImportant) ...[
                                    const Text(
                                      '⭐️',
                                      style: TextStyle(fontSize: 16),
                                    ),
                                    const SizedBox(width: 8),
                                  ],
                                  Expanded(
                                    child: Text(
                                      note.text,
                                      style: const TextStyle(
                                        fontSize: 15,
                                        color: Color(0xFF1D1D1D),
                                      ),
                                    ),
                                  ),
                                  IconButton(
                                    icon: const Icon(
                                      Icons.edit,
                                      color: Color(0xFF5A55EE),
                                    ),
                                    onPressed: () => _editNote(index),
                                  ),
                                  IconButton(
                                    icon: const Icon(
                                      Icons.delete,
                                      color: Color(0xFFFF5A6B),
                                    ),
                                    onPressed: () => _deleteNote(index),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        );
                      },
                    ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildSwipeBackground({
    required Alignment alignment,
    required Color color,
    required IconData icon,
    required String label,
  }) {
    return Container(
      alignment: alignment,
      padding: const EdgeInsets.symmetric(horizontal: 24),
      decoration: BoxDecoration(
        color: color,
        borderRadius: BorderRadius.circular(16),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, color: Colors.white),
          const SizedBox(width: 8),
          Text(
            label,
            style: const TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.bold,
            ),
          ),
        ],
      ),
    );
  }
}