// Действия диалога при уже активной сессии.
sealed trait StartAction

object StartAction:
  case object ContinueCurrent extends StartAction
  case object FinishCurrentAndStartNew extends StartAction
  case object DiscardCurrentAndStartNew extends StartAction

object SessionMenu:
  // Текст пункта и его действие лежат рядом, поэтому рассогласовать их нельзя.
  val options: Seq[MenuOption[StartAction]] = Seq(
    MenuOption("Продолжить её", StartAction.ContinueCurrent),
    MenuOption("Завершить и сформировать отчёт", StartAction.FinishCurrentAndStartNew),
    MenuOption("Отменить незавершённую сессию", StartAction.DiscardCurrentAndStartNew)
  )
