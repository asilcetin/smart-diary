# smart-diary
Android app for HCI

# Git introduction
1- Download and install: https://gitforwindows.org/

2- Open a Command Prompt and un the following commands to configure your Git username and email using the following commands, replacing Emma's name with your own:

    git config --global user.name "Emma Paris"
    git config --global user.email "eparis@atlassian.com"

3- Go to the directory where you want to have your local version of the repository, this should be AndroidStudioProjects folder:

    cd /Users/asilcetin/AndroidStudioProjects

4- Clone the git repo:

    git clone https://github.com/asilcetin/smart-diary.git

5- Go inside the project:

    cd smart-diary

6- Check the status:

    git status

7- Work in Android Studio on the project and after you make some changes use these commands:

    git status (to see the files you changed)

    git add . (to add all changed files to stage)

    git commit -m "add your change message" (commit changes)

    git push (push to remote)
