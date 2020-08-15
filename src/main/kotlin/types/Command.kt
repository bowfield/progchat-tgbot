package types

import ProgBot
import com.elbekD.bot.types.Message

/* Слушатель для комманды */
typealias CommandListener = ((args: List<String>, from: User, msg: Message) -> Unit)

class Command(_pbot: ProgBot, _name: String, _owner_only: Boolean) {
    private var name = _name
    private var owner_only = _owner_only
    private var pbot = _pbot

    open var handle: CommandListener? = null

    init {
        pbot.bot.onCommand(name) { msg, _ ->
            pbot.checkUser(msg)

            if(owner_only) // если комманда только для овнеров
                if(pbot.getUserById(msg.from!!.id)!!.is_owner == 0)
                    return@onCommand

            println("${msg.from!!.first_name} (@${msg.from!!.username}, ${msg.from!!.id}) >> ${msg.text}")

            if(handle != null)
                handle!!.invoke(msg.text!!.split(" "), pbot.getUserById(msg.from!!.id)!!, msg)
                // вызвать слушатель комманды
        }
    }
}