bool validateRequiredText(String? text) {
  if (text == null) return false;
  return text.trim().isNotEmpty;
}
