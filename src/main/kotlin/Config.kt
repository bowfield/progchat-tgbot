import types.User
import java.io.File
import java.sql.*


class Config() {
    var conn: Connection? = null

    init {
        conn = DriverManager.getConnection("jdbc:sqlite:" + Const.SQLite_FILENAME)
        conn!!.autoCommit = false
    }

    fun loadUsers() : ArrayList<User> {
        var arr = arrayListOf<User>()

        val result: ResultSet = conn!!.prepareStatement("select * from users").executeQuery()
        while(result.next()) {
            arr.add(
                User(
                    result.getInt("id"),
                    result.getString("username"),
                    result.getString("name"),
                    result.getInt("score"),
                    result.getInt("money"),
                    result.getInt("is_owner"),
                    result.getInt("is_banned"),
                    result.getString("reg_date")
                )
            )
        }

        println("Загружено пользователей: ${arr.size}")
        return arr
    }

    fun saveUsers(arr: ArrayList<User>) {
        conn!!.prepareStatement("delete from users").execute() // удалить все записи
        //conn!!.prepareStatement("vacuum").execute() // сжать пустое пространство

        arr.forEach {
            conn!!.prepareStatement(
                "INSERT INTO users (id, username, name, score, money, is_owner, is_banned, reg_date)" +
                    "VALUES('${it.id}', '${it.username}', '${it.name}', '${it.score}', '${it.money}', '${it.is_owner}', '${it.is_banned}', '${it.reg_date}')").execute()
        }

        conn!!.commit()
        println("Сохранено пользователей: ${arr.size}")
    }
}