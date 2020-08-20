package types

import java.awt.Color

class User(_id: Int, username: String, name: String, _score: Int, _money: Int, _is_owner: Int, _is_banned: Int, _reg_date: String) {
    var id = _id

    var is_owner = _is_owner
    var is_banned = _is_banned

    var score = _score
    var money = _money

    var name = name
    var username = username

    var reg_date = _reg_date

    /* Получит дамп пользователя для топа */
    fun getDumpForTop() : String {
        return "<a href=\"t.me/${username}\">${name} </a> : ${getRang().name} (${score})"
        //return "<code>Ранг: ${getRang().name}, Счет: ${score} >> ${name} (${username})</code>"
        //return "<code>${name} (${username}) >> Ранг: ${getLevel()}, Счет: ${score}</code>"
    }

    /* Получить дамп пользователя */
    fun getDump() : String {
        return  "<b>${name} (${username.toString()})</b>\n\n" +
                "Ранг: ${getRang().name}\n" +
                "Карма: ${money}\n" +
                "Сообщений отправлено: ${score}\n" +
                "Зарегистрирован: ${reg_date}"
    }

    val rangs = listOf(
            Rang(100, "Ламер", Color.WHITE),
            Rang(250, "Чайник", Color.RED),
            Rang(500, "Юзер", Color.GREEN),
            Rang(1000, "Кодер", Color.BLUE),
            Rang(2000, "Стажер", Color.RED),
            Rang(4000, "Джуниор", Color.RED),
            Rang(8000, "Мидл", Color.RED),
            Rang(10000, "Сеньер", Color.RED),
            Rang(15000, "Тимлид", Color.RED),
            Rang(20000, "Тех. Деректор", Color.RED)
    )

    /* Получить название ранга по количеству сообщений */
    fun getRang() : Rang {
        rangs.forEach {
            if(score < it.maxscore) return it
        }

        return Rang(0, "unknown", Color.WHITE)
    }
}