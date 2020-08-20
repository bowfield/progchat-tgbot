import com.elbekD.bot.Bot
import com.elbekD.bot.types.Message
import types.Command
import types.User
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.imageio.ImageIO
import kotlin.concurrent.thread

class ProgBot() {
    var users = arrayListOf<User>()
    open lateinit var bot: Bot

    var conf: Config? = null

    var top_lock = false

    init {
        conf = Config()
        users = conf!!.loadUsers()

        thread {
            while(true) {
                Thread.sleep(1000 * 60 * 10)
                saveAll()
            }
        }

        bot = Bot.createPolling(Const.TELEGRAM_USERNAME, Const.TELEGRAM_TOKEN)

        println("Бот запущен!")
    }

    fun start() {
        Command(this, "/everyone", true).handle =  { args, from, msg ->
            var list = ""
            users.forEach {
                list += "@${it.username} "
            }

            bot.sendMessage(msg.chat.id, "Внимание!\n\n${list}", replyTo = msg.message_id)
            autoRemoveMessage(msg.chat.id, msg.message_id, 40)
        }

        Command(this, "/me_", true).handle =  { args, from, msg ->
            var bImg = BufferedImage(360, 130, BufferedImage.TYPE_INT_RGB)
            var g = bImg.createGraphics()

            g.setFont(Font("TimesRoman", Font.PLAIN, 20))

            var d = getUserById(msg.from!!.id)!!.getDump()
                    .replace("<b>", "")
                    .replace("</b>", "")
                    .split("\n")


            var i = 15f
            d.forEach {
                g.drawString(it, 10f, 10f + i)
                i += 20f
            }

            ImageIO.write(bImg, "png",  File("./output_image.png"))

            var res = bot.sendPhoto(msg.chat.id, photo = File("./output_image.png"))

            autoRemoveMessage(msg.chat.id, res.get().message_id, 40)
            autoRemoveMessage(msg.chat.id, msg.message_id, 40)
        }

        Command(this, "/top", false).handle =  { args, from, msg ->
            if(top_lock) {
                autoRemoveMessage(msg.chat.id, msg.message_id, 5)
                sendTempMessage(msg.chat.id, msg.message_id, "Эту комманду можно выполнять только раз в 5 минут.", 5)
            } else {

                var tusers = users.clone() as ArrayList<User>

                var txt = "<b>Топ пользователей</b>\n"

                tusers.sortBy { usr -> usr.score }
                tusers.reverse()

                var i = 0
                tusers.forEach {
                    if (i > 14) return@forEach

                    txt += "\n${i+1}) " + getUserById(it.id)!!.getDumpForTop()

                    i++
                }

                tusers.clear()

                var res = sendTempMessage(msg.chat.id, null, txt, 60 * 10)
                bot.deleteMessage(msg.chat.id, msg.message_id)
                // bot.pinChatMessage(msg.chat.id, res.get().message_id)
                autoRemoveMessage(msg.chat.id, res.get().message_id, 60 * 10)

                thread {
                    top_lock = true
                    Thread.sleep(1000 * 60 * 2)
                    top_lock = false
                }
            }
        }

        Command(this, "/pin", true).handle = { args, from, msg ->
            bot.pinChatMessage(msg.chat.id, msg.reply_to_message!!.message_id)
            autoRemoveMessage(msg.chat.id, msg.message_id, 1)
        }

        Command(this, "/luck", false).handle = { args, from, msg ->
            var time = 0
            if(Random().nextInt(10) == 5) time = Random().nextInt(15)

            sendTempMessage(msg.chat.id, msg.message_id, "Ты выиграл ${time} минут мута!", 15)
            autoRemoveMessage(msg.chat.id, msg.message_id, 15)
        }

        Command(this, "/save", true).handle = { args, from, msg ->
            saveAll()
            bot.deleteMessage(msg.chat.id, msg.message_id)
        }

        Command(this, "/me", false).handle = { args, from, msg ->
            var usr = if(msg.reply_to_message != null) msg.reply_to_message!!.from else msg.from

            sendTempMessage(msg.chat.id, msg.message_id, getUserById(usr!!.id)!!.getDump(), 40)
            autoRemoveMessage(msg.chat.id, msg.message_id, 40)
        }

        Command(this, "/id", true).handle = { args, from, msg ->
            var id = 0

            if(msg.reply_to_message != null) id = msg.reply_to_message!!.from!!.id
            else id = msg.from!!.id

            sendTempMessage(msg.chat.id, msg.message_id, "${id}", 15)
            autoRemoveMessage(msg.chat.id, msg.message_id, 15)
        }

        Command(this, "/rules", true).handle = { args, from, msg ->
            sendTempMessage(msg.chat.id, msg.message_id, "<b>Правила Группы:</b>\n\n" +
                    "1) Не быть мудаком\n" +
                    "2) Не спамить\n" +
                    "3) Запрет на 18+\n" +
                    "4) Запрещена реклама\n" +
                    "5) Твинки банятся\n" +
                    "6) Никнеймы а-ля \"Хакер\" банятся", 120)
            autoRemoveMessage(msg.chat.id, msg.message_id, 120)
        }

        Command(this, "/links", true).handle = { args, from, msg ->
            sendTempMessage(msg.chat.id, msg.message_id, "<b>Ссылки группы:</b>\n\n" +
                    "@bell4enok_music\n" +
                    "https://t.me/ktxdevblog"
                    , 120)
            autoRemoveMessage(msg.chat.id, msg.message_id, 120)
        }

        Command(this, "/help", true).handle = { args, from, msg ->
            sendTempMessage(msg.chat.id, msg.message_id,
                    "\nВот комманды которые ты можешь использовать:" +
                            "\n/top, /me, /luck, /links, /rules"
                    , 20)
            autoRemoveMessage(msg.chat.id, msg.message_id, 20)
        }

        var arr = arrayListOf<Int>()
        bot.onMessage {
            //println(it.chat.id)
            if(it.from!!.is_bot || it.chat!!.id != -1001400025502) return@onMessage
            checkUser(it)

            println("${it.from!!.first_name} (@${it.from!!.username}, ${it.from!!.id}) >> ${it.text}")
            getUserById(it.from!!.id)!!.score++

            if(it!!.reply_to_message != null)
                if(it!!.from!!.id != it.reply_to_message!!.from!!.id) {
                    if(it.text == "+") { // + карма
                        if(!arr.contains(it!!.from!!.id)) {

                            var usr = getUserById(it!!.reply_to_message!!.from!!.id)

                            usr!!.money++
                           // var r = bot.sendMessage(it!!.chat!!.id, "Вы увеличили карму для ${usr!!.name} (${usr!!.username})", replyTo = it!!.message_id)
                           // autoRemoveMessage(it!!.chat!!.id, r.get().message_id, 15)
                           // autoRemoveMessage(it!!.chat!!.id, it!!.message_id, 15)

                            arr.add(it!!.from!!.id)
                            thread {
                                Thread.sleep(1000 * 60)
                                arr.remove(it!!.from!!.id)
                            }
                        } else {
                            //var r = bot.sendMessage(it!!.chat!!.id, "Менять карму можно только раз в минуту!", replyTo = it!!.message_id)
                            //autoRemoveMessage(it!!.chat!!.id, r.get().message_id, 15)
                           // autoRemoveMessage(it!!.chat!!.id, it!!.message_id, 15)
                        }
                    } else if(it.text == "-") { // - kарма
                        if(!arr.contains(it!!.from!!.id)) {
                            var usr = getUserById(it!!.reply_to_message!!.from!!.id)

                            usr!!.money--
                            //var r = bot.sendMessage(it!!.chat!!.id, "Вы уменьшили карму для ${usr!!.name} (${usr!!.username})", replyTo = it!!.message_id)
                           // autoRemoveMessage(it!!.chat!!.id, r.get().message_id, 15)
                           // autoRemoveMessage(it!!.chat!!.id, it!!.message_id, 15)

                            arr.add(it!!.from!!.id)
                            thread {
                                Thread.sleep(1000 * 60)
                                arr.remove(it!!.from!!.id)
                            }
                        } else {
                          //  var r = bot.sendMessage(it!!.chat!!.id, "Менять карму можно только раз в минуту!", replyTo = it!!.message_id)
                           // autoRemoveMessage(it!!.chat!!.id, r.get().message_id, 15)
                           // autoRemoveMessage(it!!.chat!!.id, it!!.message_id, 15)
                        }
                    }
                }
        }

        bot.start()
    }

    /* Сохарнить все */
    fun saveAll() {
        conf!!.saveUsers(users)
    }

    /* Отправить временное сообщение */
    fun sendTempMessage(chatid: Long, reply_msgid: Int?, text: String, time: Int) : CompletableFuture<Message> {
        var res = bot.sendMessage(chatid, text, replyTo = reply_msgid, parseMode = "html", disableWebPagePreview = true)

        autoRemoveMessage(chatid, res.get().message_id, time)

        return res
    }

    /* Удалить сообщение через */
    fun autoRemoveMessage(chatid: Long, message_id: Int, time: Int) {
        thread {
            var t = time
            while(t > 0) {
                Thread.sleep((1000).toLong())
                //println("timer: ${t}")

                t--
            }

            bot.deleteMessage(chatid, message_id)
            println("Удалено временное сообщение: ${message_id}")
        }
    }

    /* Проверить пользователя */
    fun checkUser(msg: Message) {
        if(!isUserExist(msg.from!!.id)) {
            newUser(msg)
        } else {
            var usr = getUserById(msg.from!!.id)

            if(msg.from!!.username != usr!!.username)
                usr.username = msg.from!!.username!!

            if(msg.from!!.first_name != usr!!.name)
                usr.name = msg.from!!.first_name!!
        }
    }

    /* Получить пользователя по ID */
    fun getUserById(id: Int) : User? {
        users.forEach { usr: User ->
            if(usr.id == id) {
                return usr
            }
        }

        return null
    }

    /* Существует ли пользователь в базе */
    fun isUserExist(id: Int) : Boolean {
        users.forEach {
            if(it.id == id) return true }
        return false
    }

    /* Добавить нового пользователя */
    fun newUser(msg: Message) {
        var usr = User(msg.from!!.id, msg.from!!.username!!, msg.from!!.first_name, 0, 0, 0, 0, SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))

        users.add(usr)
        println("New user: ${usr.id}, ${usr.username} (BD_ID: ${users.size-1})")

        sendTempMessage(msg.chat!!.id, msg.message_id,
                "Приветствую, ${msg.from!!.first_name}, добро пожаловать в чат программистов." +
                        "\nВ описании группы ты увидишь наших друзей в дискорде, узнаешь правила и о других проектах чата." +
                        "\n" +
                        "\nВот комманды которые ты можешь использовать:" +
                        "\n/top, /me, /luck, /links, /rules",
                40)

        saveAll()
    }
}