init:
	# ToDo: Add documentation!
	@echo "SETTING UP PROJECT ENVIRONMENT"
	@echo "######################################################"
	@echo "#  Adding IDE specific folders to .git/info/exclude  #"
	@echo "######################################################"
	echo ".idea/" >> .git/info/exclude # JetBrains IDEs
	@echo "######################################################"
	@echo "#   Ensuring that pipenv installed or installing it  #"
	@echo "######################################################"
	pipenv --version || (echo "Pipenv not found. Trying \`pip3\` to install pipenv"; pip3 install pipenv) || (echo "Pip3 not found. Trying \`pip\` to install pipenv"; pip install pipenv)
	@echo "######################################################"
	@echo "#         Installing dependencies from pipenv        #"
	@echo "######################################################"
	pipenv install --dev
	@echo "######################################################"
	@echo "#            Creating default config file            #"
	@echo "######################################################"
	cp ./config/config.example.yml ./config/config.yml
	@echo "Finished || DON'T FORGET TO MARK \`./src/\` FOLDER AS SOURCE"

init-full:
	@echo "WorkInProgress"
#	@echo "SETTING UP PROJECT ENVIRONMENT"
#	@echo "##############################################################"
#	@echo "#      Adding IDE specific folders to .git/info/exclude      #"
#	@echo "##############################################################"
#	echo ".idea/" >> .git/info/exclude # JetBrains IDEs
#	@echo "##############################################################"
#	@echo "#       Ensuring that pipenv installed or installing it      #"
#	@echo "##############################################################"
#	pipenv --version || (echo "Pipenv not found. Trying \`pip3\` to install pipenv"; pip3 install pipenv) || (echo "Pip3 not found. Trying \`pip\` to install pipenv"; pip install pipenv)
#	@echo "##############################################################"
#	@echo "#             Installing dependencies from pipenv            #"
#	@echo "##############################################################"
#	pipenv install --dev
#	@echo "##############################################################"
#	@echo "#                Creating default config file                #"
#	@echo "##############################################################"
#	cp ./config/config.example.yml ./config/config.yml
#	@echo "##############################################################"
#	@echo "#      Ensuring that Docker installed or installing it       #"
#	@echo "##############################################################"
#	sudo docker --version || (([ -d "$HOME/Downloads/" ] || mkdir "$HOME/Downloads/");)
#	@echo "##############################################################"
#	@echo "#  Ensuring that Docker Compose installed or installing it   #"
#	@echo "##############################################################"
#	@echo "Finished || DON'T FORGET TO MARK \`./src/\` FOLDER AS SOURCE"


reformat:
	# ToDo: add documentation!
	pipenv run black . --config "config/black.toml"

.IGNORE:check
check:
	# ToDo: add documentation!
	pipenv run flake8 "./src/" --config "config/flake8.cfg"
	pipenv run mypy src/  --config "config/mypy.ini" --html-report ./htmlcov_mypy

.IGNORE:tests
tests:
	# ToDo: add documentation!
	pipenv run pytest --cov-config=config/.coveragerc --cov-report=html --cov=src --doctest-modules src/

.IGNORE:static-all
static-all:
	# ToDo: add documentation!
	pipenv run black . --config "config/black.toml"
	pipenv run flake8 "./src/" --config "config/flake8.cfg"
	pipenv run mypy src/  --config "config/mypy.ini" --html-report ./htmlcov_mypy
	pipenv run pytest --cov-config=config/.coveragerc --cov-report=html --cov=src --doctest-modules src/
