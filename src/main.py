import os
import logging
from aiogram import Bot, Dispatcher, executor, types

logging.basicConfig(level = logging.DEBUG)

bot = Bot(token=os.environ.get('BOT_TOKEN'))
dp = Dispatcher(bot)


@dp.message_handler(content_types=types.ContentTypes.NEW_CHAT_MEMBERS)
async def new_chat_member(message: types.Message):
    await message.reply(f'Hey, {message.from_user.first_name}')


@dp.message_handler(commands=['me'])
async def me_handler(message: types.Message):
    pass


@dp.message_handler(commands=['start'])
async def start_handler(message: types.Message):
    await message.delete()


@dp.message_handler()
async def all_message_handler(message: types.Message):
    print(message)


if __name__ == '__main__':
    executor.start_polling(dp, skip_updates=True)
