init:
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


