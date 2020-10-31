init:
	echo "Setting Up Project"
	echo "Adding IDE specific folders to .git/info/exclude"
	echo ".idea/" >> .git/info/exclude
	echo "check pipenv"
	pipenv --version || echo "Pipenv not found. Trying pip3 to install pipenv" && pip3 install pipenv || echo "Pip3 not found. Trying pip to install pipenv" && pip install pipenv
	echo "installing dependencies from pipenv"
	pipenv install --dev
	echo "creating default config file"
	cp ./config/config.example.yml ./config/config.yml
	echo "Finished || DON'T FORGET TO MARK \`./src/\` FOLDER AS SOURCE"

# ToDo: add `init-full`
# ToDo: add db initialization


