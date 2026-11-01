// Ожидаемые ошибки разных уровней.
// Ни один вариант не кодируется через null, -1, пустую строку или просто false.

// Ошибки разбора пользовательской строки.
sealed trait CommandError

object CommandError:
  case object EmptyInput extends CommandError
  case class UnterminatedQuote(command: String) extends CommandError
  case class MissingArgument(command: String) extends CommandError

// Ошибки операций над сессией.
sealed trait SessionError

object SessionError:
  case object NoActiveSession extends SessionError

// Ошибки слоя хранения (config.json, файлы отчётов).
sealed trait StorageError

object StorageError:
  case class FileNotFound(path: String) extends StorageError
  case class ReadFailed(path: String, reason: String) extends StorageError
  case class InvalidConfig(path: String, reason: String) extends StorageError
  case class WriteFailed(path: String, reason: String) extends StorageError

// Ошибки выполнения внешних команд и работы с каталогами.
sealed trait ShellError

object ShellError:
  case class DirectoryNotFound(path: String) extends ShellError
  case class ExecutionFailed(reason: String) extends ShellError

// Ошибка уровня приложения: сюда поднимается только то,
// что не может быть осмысленно исправлено на нижнем уровне.
sealed trait AppError

object AppError:
  case class Storage(error: StorageError) extends AppError
