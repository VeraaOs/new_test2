// ADT пользовательских команд.
// После разбора программа работает только с этими значениями,
// а не анализирует исходную строку повторно.

sealed trait Command

object Command:
  case class StartSession(name: Option[String]) extends Command
  case object EndSession extends Command
  case class AddStep(title: Option[String]) extends Command
  case class AddNote(text: String) extends Command
  case object SkipNextCommand extends Command
  case object ShowStatus extends Command
  case class SetRootDirectory(path: Option[String]) extends Command
  case object PreviewReport extends Command
  case object Help extends Command
  case object Exit extends Command
  case class ChangeDirectory(target: Option[String], text: String) extends Command
  case class RunShell(text: String) extends Command

object CommandParser:
  def parse(input: String): Either[CommandError, Command] =
    val trimmed = input.trim
    if trimmed.isEmpty then Left(CommandError.EmptyInput)
    else
      val (name, rest) = splitFirst(trimmed)
      name match
        case "starts"   => argument("starts", rest).map(Command.StartSession.apply)
        case "ends"     => Right(Command.EndSession)
        case "mstep"    => argument("mstep", rest).map(Command.AddStep.apply)
        case "mnote"    =>
          argument("mnote", rest).flatMap {
            case Some(text) if text.trim.nonEmpty => Right(Command.AddNote(text))
            case _ => Left(CommandError.MissingArgument("mnote"))
          }
        case "mskip"    => Right(Command.SkipNextCommand)
        case "mstatus"  => Right(Command.ShowStatus)
        case "prootd"   => argument("prootd", rest).map(Command.SetRootDirectory.apply)
        case "mpreview" => Right(Command.PreviewReport)
        case "help"     => Right(Command.Help)
        case "exit" | "quit" => Right(Command.Exit)
        case "cd"       => argument("cd", rest).map(a => Command.ChangeDirectory(a, trimmed))
        case _          => Right(Command.RunShell(trimmed))

  // Делит строку на имя команды и остаток.
  private def splitFirst(input: String): (String, String) =
    val i = input.indexWhere(_.isWhitespace)
    if i < 0 then (input, "")
    else (input.take(i), input.drop(i).trim)

  // Разбирает аргумент команды. Отсутствие аргумента — это None,
  // незакрытая кавычка — типизированная ошибка.
  private def argument(name: String, rest: String): Either[CommandError, Option[String]] =
    if rest.isEmpty then Right(None)
    else if rest.startsWith("\"") then
      if rest.length >= 2 && rest.endsWith("\"") then
        Right(Some(rest.substring(1, rest.length - 1)))
      else Left(CommandError.UnterminatedQuote(name))
    else Right(Some(rest))
