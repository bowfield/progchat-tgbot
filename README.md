# Proggrammer.exe Bot
Бот для чата Proggrammer.exe

## Reproduction steps
### Using make
1. [Clone](https://docs.github.com/en/free-pro-team@latest/github/creating-cloning-and-archiving-repositories/cloning-a-repository) repository.
2. Install [make](https://packages.ubuntu.com/focal/make)
3. Rum `make init` for minimal install or `make init-full` for full install.
4. Check if your specific IDE folders were added to gitexclude<sup>[HowTo](#)(ToDo: Add link)</sup>.
5. Fill `config/config.yml` with valid settings.
6. If you ran `init-full` or you have docker-compose installed run `docker-compose up -d` to run db.

### Manually
1. [Clone](https://docs.github.com/en/free-pro-team@latest/github/creating-cloning-and-archiving-repositories/cloning-a-repository) repository.
2. Install [pipenv](https://github.com/pypa/pipenv).
3. Install all dependincies with `pipenv install --dev`
4. Add your IDE specific folder to gitexclude and Makefile<sup>[HowTo](#)(ToDo: Add link)</sup>.
5. Copy `config/config.example.yml` to `config/config.yml` and fill it with valid settings.
6. Install [Docker](https://docs.docker.com/engine/install/) and [Docker Compose](https://docs.docker.com/compose/install/)
7. Run `docker-compose up -d` to run db.

# Todo List
- [ ] Основной бот
	- [ ] Определение новых юзеров и добавление в базу
	- [ ] Обновление счетчика score (сообщений)
	- [ ] Если is_banned == 1, игнорить пользователя
	- [ ] /me - для вывода инфы о юзере
		- [ ] Своей
		- [ ] Других
	- [ ] /top - для вывода топа юзеров (10)
	- [ ] Автосохранение базы
	
- [ ] Автоудаление всех сообщений
- [ ] Администрирование
	- [ ] /ignore - Заблокировать юзера в боте (is_banned = 1), если уже == 1 - то сделать = 0
	- [ ] /own - Сделать админов в боте (is_owner = 1), если уже == 1 - то сделать 0
	- [ ] /save - Сохранить базу
